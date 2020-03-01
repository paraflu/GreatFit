package com.dinodevs.greatfitwatchface.widget

import android.app.Service
import android.graphics.*
import android.util.Log
import com.dinodevs.greatfitwatchface.data.DataType
import com.dinodevs.greatfitwatchface.data.Steps
import com.dinodevs.greatfitwatchface.resource.ResourceManager.getTypeFace
import com.dinodevs.greatfitwatchface.settings.LoadSettings
import com.ingenic.iwds.slpt.view.arc.SlptTodayStepArcAnglePicView
import com.ingenic.iwds.slpt.view.core.SlptLinearLayout
import com.ingenic.iwds.slpt.view.core.SlptPictureView
import com.ingenic.iwds.slpt.view.core.SlptViewComponent
import com.ingenic.iwds.slpt.view.sport.SlptTodayStepNumView
import com.ingenic.iwds.slpt.view.utils.SimpleFile
import java.util.*


class StepsWidget() : CircleWidget() {

    constructor(_settings: LoadSettings) : this() {
        settings = _settings
    }

    override var ring: Paint? = null
    override var ringBmp: Bitmap? = null
    private var stepsData: Steps? = null
    private val stepsPaint: Paint? = null
    private val icon: Bitmap? = null
    private var stepsSweepAngle = 0f
    private var angleLength: Int = 0
    // Screen-on init (runs once)
    override fun init(service: Service) {
        mService = service
        if (settings.theme.stepsProgress != null) {
            val circle = settings.theme.stepsProgress!!.circle
            angleLength = calcAngle(circle)
            if (circle.imageIndex == null) {
                ring = Paint(Paint.ANTI_ALIAS_FLAG)
                ring!!.strokeCap = Paint.Cap.ROUND
                ring!!.style = Paint.Style.STROKE
                ring!!.strokeWidth = circle.width!!.toFloat()
                ring!!.color = Color.parseColor(String.format("#%s", circle.color!!.substring(12)))
            } else {
                ringBmp = getBitmap(circle.imageIndex)
            }
        }
    }

    override fun onDataUpdate(type: DataType, value: Any) { // Steps class
        stepsData = value as Steps
        stepsSweepAngle = 0f
        if (stepsData != null) {
            Log.d(TAG, "target ${stepsData!!.target} steps ${stepsData!!.steps}")
            val steps = stepsData!!.steps
            stepsSweepAngle = angleLength * (steps.coerceAtMost(stepsData!!.target).toFloat() / stepsData!!.target)
        }
    }

    override fun getDataTypes(): List<DataType> {
        return listOf(DataType.STEPS)
    }

    override fun draw(canvas: Canvas?, width: Float, height: Float, centerX: Float, centerY: Float) {
        if (stepsData == null) {
            return
        }
        if (settings.theme.activity?.steps != null) { // Draw icon
            drawText(canvas!!, stepsData!!.steps, settings.theme.activity!!.steps!!.step)
        }
        val scale = settings.theme.stepsProgress!!.circle

        if (ring != null) {
            drawRing(canvas!!, scale, ring!!, stepsSweepAngle)
        }

        if (ringBmp != null) {
            drawCircle(canvas!!, scale, ringBmp!!, stepsSweepAngle)
//            Log.d(TAG, "stepsWidget ${ringBmp!!.width}")
        }

    }

    override fun buildSlptViewComponent(service: Service?): List<SlptViewComponent?>? {
        return buildSlptViewComponent(service, false)
    }

    override fun buildSlptViewComponent(service: Service?, better_resolution: Boolean): List<SlptViewComponent?>? {
        var betterResolution = better_resolution
        betterResolution = betterResolution && settings.better_resolution_when_raising_hand
        val slpt_objects: MutableList<SlptViewComponent?> = ArrayList()
        var tmp_left: Int
        // Do not show in SLPT (but show on raise of hand)
        val show_all = !settings.clock_only_slpt || betterResolution
        if (!show_all) return slpt_objects
        // Show steps
        if (settings.steps > 0) { // Show or Not icon
            if (settings.stepsIcon) {
                val stepsIcon = SlptPictureView()
                stepsIcon.setImagePicture(SimpleFile.readFileFromAssets(service, (if (betterResolution) "26wc_" else "slpt_") + "icons/" + settings.is_white_bg + "steps.png"))
                stepsIcon.setStart(
                        settings.stepsIconLeft.toInt(),
                        settings.stepsIconTop.toInt()
                )
                slpt_objects.add(stepsIcon)
            }
            val steps = SlptLinearLayout()
            steps.add(SlptTodayStepNumView())
            steps.setTextAttrForAll(
                    settings.stepsFontSize,
                    settings.stepsColor,
                    getTypeFace(service!!.resources, settings.font)
            )
            // Position based on screen on
            steps.alignX = 2
            steps.alignY = 0
            tmp_left = settings.stepsLeft.toInt()
            if (!settings.stepsAlignLeft) { // If text is centered, set rectangle
                steps.setRect(
                        (2 * tmp_left + 640),
                        (settings.font_ratio.toFloat() / 100 * settings.stepsFontSize).toInt()
                )
                tmp_left = -320
            }
            steps.setStart(
                    tmp_left,
                    (settings.stepsTop - settings.font_ratio.toFloat() / 100 * settings.stepsFontSize).toInt()
            )
            slpt_objects.add(steps)
        }
        // Steps image
        if (settings.stepsProg > 0 && settings.stepsProgType == 1) { // Image
// todo
//SlptTodayStepPicGroupView
        }
        // steps bar
        if (settings.stepsProg > 0 && settings.stepsProgType == 0) { // Draw background image
            if (settings.stepsProgBgBool) {
                val ring_background = SlptPictureView()
                ring_background.setImagePicture(SimpleFile.readFileFromAssets(service, (if (settings.isVerge) "verge_" else if (betterResolution) "" else "slpt_") + "circles/ring1_bg.png"))
                ring_background.setStart((settings.stepsProgLeft - settings.stepsProgRadius).toInt(), (settings.stepsProgTop - settings.stepsProgRadius).toInt())
                slpt_objects.add(ring_background)
            }
            val stepsArcAnglePicView = SlptTodayStepArcAnglePicView()
            stepsArcAnglePicView.setImagePicture(SimpleFile.readFileFromAssets(service, (if (settings.isVerge) "verge_" else if (betterResolution) "" else "slpt_") + settings.stepsProgSlptImage))
            stepsArcAnglePicView.setStart((settings.stepsProgLeft - settings.stepsProgRadius).toInt(), (settings.stepsProgTop - settings.stepsProgRadius).toInt())
            stepsArcAnglePicView.start_angle = if (settings.stepsProgClockwise == 1) settings.stepsProgStartAngle else settings.stepsProgEndAngle
            //stepsArcAnglePicView.start_angle = settings.stepsProgStartAngle;
            stepsArcAnglePicView.len_angle = 0
            stepsArcAnglePicView.full_angle = if (settings.stepsProgClockwise == 1) angleLength else -angleLength
            stepsArcAnglePicView.draw_clockwise = settings.stepsProgClockwise
            slpt_objects.add(stepsArcAnglePicView)
        }
        return slpt_objects
    }

    companion object {
        private const val TAG = "VergeIT-LOG StepsWidget"
    }
}