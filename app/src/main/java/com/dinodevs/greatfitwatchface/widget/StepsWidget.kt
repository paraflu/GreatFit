package com.dinodevs.greatfitwatchface.widget

import android.app.Service
import android.graphics.*
import android.util.Log
import com.dinodevs.greatfitwatchface.data.DataType
import com.dinodevs.greatfitwatchface.data.Steps
import com.dinodevs.greatfitwatchface.resource.ResourceManager.getTypeFace
import com.dinodevs.greatfitwatchface.settings.LoadSettings
import com.huami.watch.watchface.util.Util
import com.ingenic.iwds.slpt.view.arc.SlptTodayStepArcAnglePicView
import com.ingenic.iwds.slpt.view.core.SlptLinearLayout
import com.ingenic.iwds.slpt.view.core.SlptPictureView
import com.ingenic.iwds.slpt.view.core.SlptViewComponent
import com.ingenic.iwds.slpt.view.sport.SlptTodayStepNumView
import com.ingenic.iwds.slpt.view.utils.SimpleFile
import java.util.*


class StepsWidget() : CircleWidget() {
    private var ringBmp: Bitmap? = null

    constructor(_settings: LoadSettings) : this() {
        settings = _settings
    }

    private var stepsData: Steps? = null
    private val stepsPaint: Paint? = null
    private val icon: Bitmap? = null
    private var stepsSweepAngle = 0f
    private val angleLength: Int
    private var ring: Paint? = null
    // Screen-on init (runs once)
    override fun init(service: Service) {
        mService = service
        //        if (settings.steps > 0) {
//            this.stepsPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//            this.stepsPaint.setTypeface(ResourceManager.getTypeFace(service.getResources(), settings.font));
//            this.stepsPaint.setTextSize(settings.stepsFontSize);
//            this.stepsPaint.setColor(settings.stepsColor);
//            this.stepsPaint.setTextAlign((settings.stepsAlignLeft) ? Paint.Align.LEFT : Paint.Align.CENTER);
//
//            if (settings.stepsIcon) {
//                this.icon = Util.decodeImage(mService.getResources(), "icons/" + settings.is_white_bg + "steps.png");
//            }
//        }
//        if (settings.stepsProg > 0 && settings.stepsProgType == 0) {
        if (settings.theme.stepsProgress != null) {
            if (settings.theme.stepsProgress!!.circle.imageIndex == null) {
                ring = Paint(Paint.ANTI_ALIAS_FLAG)
                ring!!.strokeCap = Paint.Cap.ROUND
                ring!!.style = Paint.Style.STROKE
                ring!!.strokeWidth = settings.theme.stepsProgress!!.circle.width.toFloat()
            } else {
                ringBmp = Util.decodeImage(service.resources, settings.getImagePath(settings.theme.stepsProgress!!.circle.imageIndex!!))
            }
        }
    }

    // Value updater
    override fun onDataUpdate(type: DataType, value: Any) { // Steps class
        stepsData = value as Steps
        // Bar angle
        stepsSweepAngle = if (stepsData == null) 0f else angleLength * (Math.min(stepsData!!.steps, stepsData!!.target).toFloat() / stepsData!!.target)
    }

    // Register update listeners
    override fun getDataTypes(): List<DataType> {
        return listOf(DataType.STEPS)
    }


    // Draw screen-on
    override fun draw(canvas: Canvas?, width: Float, height: Float, centerX: Float, centerY: Float) {
        if (stepsData == null) {
            return
        }
        if (settings.theme.activity.steps != null) { // Draw icon
            drawText(canvas!!, stepsData!!.steps, settings.theme.activity.steps!!.step)
        }
        if (settings.theme.stepsProgress != null) {
            if (settings.theme.stepsProgress!!.circle.imageIndex == null) {
                val count = canvas!!.save()
                // Rotate canvas to 0 degrees = 12 o'clock
                canvas.rotate(-90f, centerX, centerY)
                val cal = settings.theme.stepsProgress
                val radius = cal!!.circle.radiusX /*- scale.getWidth()*/.toFloat()
                val oval = RectF(cal.circle.centerX - radius, cal.circle.centerY - radius,
                        cal.circle.centerX + radius,
                        cal.circle.centerY + radius)
                // Background
                Log.d(TAG, String.format("getStartAngle: %d angleLength: %d", cal.circle.startAngle, angleLength))
                ring!!.color = Color.parseColor(String.format("#%s", cal.circle.color.substring(12)))
                //            canvas.drawArc(oval, cal.getCircle().getStartAngle(), this.angleLength, false, ring);
// this.ring.setColor(settings.colorCodes[settings.batteryProgColorIndex]); progressione colore
                canvas.drawArc(oval, cal.circle.startAngle.toFloat(), stepsSweepAngle, false, ring)
                canvas.restoreToCount(count)
            } else {
//                val count = canvas!!.save()
//                val circle = settings.theme.stepsProgress!!.circle
//                // Rotate canvas to 0 degrees = 12 o'clock
//                canvas.rotate(-90f, circle.centerX.toFloat(), circle.centerY.toFloat())
//                canvas.drawBitmap(applyPieMask(ringBmp!!, 0f, stepsSweepAngle),
//                        circle.centerX.toFloat(), circle.centerY.toFloat(), settings.mGPaint)
//                canvas.restoreToCount(count)
                drawCircle(canvas!!, settings.theme.stepsProgress!!.circle, ringBmp!!, stepsSweepAngle)
            }
        }
    }

    // Screen-off (SLPT)
    override fun buildSlptViewComponent(service: Service?): List<SlptViewComponent?>? {
        return buildSlptViewComponent(service, false)
    }

    // Screen-off (SLPT) - Better screen quality
    override fun buildSlptViewComponent(service: Service?, better_resolution: Boolean): List<SlptViewComponent?>? {
        var better_resolution = better_resolution
        better_resolution = better_resolution && settings.better_resolution_when_raising_hand
        val slpt_objects: MutableList<SlptViewComponent?> = ArrayList()
        var tmp_left: Int
        // Do not show in SLPT (but show on raise of hand)
        val show_all = !settings.clock_only_slpt || better_resolution
        if (!show_all) return slpt_objects
        // Show steps
        if (settings.steps > 0) { // Show or Not icon
            if (settings.stepsIcon) {
                val stepsIcon = SlptPictureView()
                stepsIcon.setImagePicture(SimpleFile.readFileFromAssets(service, (if (better_resolution) "26wc_" else "slpt_") + "icons/" + settings.is_white_bg + "steps.png"))
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
                ring_background.setImagePicture(SimpleFile.readFileFromAssets(service, (if (settings.isVerge) "verge_" else if (better_resolution) "" else "slpt_") + "circles/ring1_bg.png"))
                ring_background.setStart((settings.stepsProgLeft - settings.stepsProgRadius).toInt(), (settings.stepsProgTop - settings.stepsProgRadius).toInt())
                slpt_objects.add(ring_background)
            }
            val stepsArcAnglePicView = SlptTodayStepArcAnglePicView()
            stepsArcAnglePicView.setImagePicture(SimpleFile.readFileFromAssets(service, (if (settings.isVerge) "verge_" else if (better_resolution) "" else "slpt_") + settings.stepsProgSlptImage))
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
        private const val TAG = "VergeIT-LOG"
    }

    // Constructor
    init {
        angleLength = settings.theme.stepsProgress!!.circle.startAngle - settings.theme.stepsProgress!!.circle.endAngle
        //        if(settings.stepsProgClockwise==1) {
//            this.angleLength = (settings.stepsProgEndAngle<settings.stepsProgStartAngle)? 360-(settings.stepsProgStartAngle-settings.stepsProgEndAngle) : settings.stepsProgEndAngle - settings.stepsProgStartAngle;
//        }else{
//            this.angleLength = (settings.stepsProgEndAngle>settings.stepsProgStartAngle)? 360-(settings.stepsProgStartAngle-settings.stepsProgEndAngle) : settings.stepsProgEndAngle - settings.stepsProgStartAngle;
//        }
    }
}