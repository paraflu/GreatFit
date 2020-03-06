package it.vergeit.watchface.widget

import android.app.Service
import android.graphics.*
import android.util.Log
import com.huami.watch.watchface.util.Util
import com.ingenic.iwds.slpt.view.arc.SlptArcAnglePicView
import com.ingenic.iwds.slpt.view.arc.SlptPowerArcAnglePicView
import it.vergeit.watchface.data.DataType
import it.vergeit.watchface.data.Steps
import it.vergeit.watchface.settings.LoadSettings
import com.ingenic.iwds.slpt.view.arc.SlptTodayStepArcAnglePicView
import com.ingenic.iwds.slpt.view.core.SlptViewComponent
import com.ingenic.iwds.slpt.view.sport.SlptTodayStepNumView
import java.util.*


class StepsWidget() : CircleWidget() {

    constructor(_settings: LoadSettings) : this() {
        settings = _settings
    }

    private var stepProgressGraphSweepAngle = 0f
    private var progressBmp: Bitmap? = null
    private var angleGraph: Int = 0
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
        if (settings.theme.stepsProgress?.circle != null) {
            val circle = settings.theme.stepsProgress!!.circle!!
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

        if (settings.theme.stepsProgress?.clockHand != null) {
            val clockHand = settings.theme.stepsProgress!!.clockHand!!;
            angleGraph = calcAngle(clockHand.sector.startAngle, clockHand.sector.endAngle)
            progressBmp = getBitmap(clockHand.image.imageIndex);
        }
    }

    override fun onDataUpdate(type: DataType, value: Any) { // Steps class
        stepsData = value as Steps
        stepsSweepAngle = 0f

        if (stepsData != null) {
            val steps = stepsData!!.steps

            stepsSweepAngle = angleLength * (steps.coerceAtMost(stepsData!!.target).toFloat() / stepsData!!.target)
            if (settings.theme.stepsProgress?.clockHand != null) {
                stepProgressGraphSweepAngle = angleGraph * (steps.coerceAtMost(stepsData!!.target).toFloat() / stepsData!!.target)
            }
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
        val clockHand = settings.theme.stepsProgress?.clockHand

        if (ring != null) {
            drawRing(canvas!!, scale!!, ring!!, stepsSweepAngle)
        }

        if (ringBmp != null) {
            drawCircle(canvas!!, scale!!, ringBmp!!, stepsSweepAngle)
//            Log.d(TAG, "stepsWidget ${ringBmp!!.width}")
        }

        if (progressBmp != null) {
            drawProgress(canvas!!, progressBmp!!, clockHand!!, stepProgressGraphSweepAngle)
        }

    }

    override fun buildSlptViewComponent(service: Service?): List<SlptViewComponent?>? {
        return buildSlptViewComponent(service, false)
    }

    override fun buildSlptViewComponent(service: Service?, better_resolution: Boolean): List<SlptViewComponent?>? {
        mService = service!!
        val slptObjects: MutableList<SlptViewComponent?> = ArrayList()
        if (settings.theme.stepsProgress != null) {
            val stepProgress = settings.theme.stepsProgress!!
            val stepView = SlptTodayStepArcAnglePicView()
            if (stepProgress.circle!!.imageIndex != null) {
                val stepProgressImage = getBitmapSlpt(stepProgress.circle.imageIndex!!, better_resolution)
                stepView.setImagePicture(stepProgressImage)
            } else {
                val width = stepProgress.circle.radiusX!!.times(2)
                val height = stepProgress.circle.radiusY!!.times(2)
                val mask = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(mask)
                canvas.drawColor(0x00000000)
                val maskPaint = Paint()
                maskPaint.color = -0x1 //pick highest value for bitwise AND operation
                maskPaint.isAntiAlias = true
                //choose entire bitmap as a rect
                val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
                canvas.drawArc(rect, stepProgress.circle.startAngle!!.toFloat(), stepProgress.circle.endAngle!!.toFloat(), true, maskPaint) //mask the pie
//
//                ring = Paint(Paint.ANTI_ALIAS_FLAG)
//                ring!!.strokeCap = Paint.Cap.ROUND
//                ring!!.style = Paint.Style.STROKE
//                ring!!.strokeWidth = stepProgress.circle.width!!.toFloat()
                stepView.setImagePicture(Util.Bitmap2Bytes(mask))
            }
            stepView.start_angle = stepProgress.circle.startAngle!!
            stepView.len_angle = 0
            stepView.full_angle = angleLength
            slptObjects.add(stepView)
        }
        if (settings.theme.activity?.steps != null) {
            val step = settings.theme.activity!!.steps!!
            val currentStep = stepsData?.steps ?: 0
            val stepView = SlptTodayStepNumView()
//            stepView.setPadding(0, step.step.spacing, 0, 0)
            val startPoint = getStartPoint(step.step, currentStep.toString().length)
            stepView.setImagePictureArray(bitmapArray(step.step.imageIndex, step.step.imagesCount, better_resolution))
            stepView.setStart(startPoint.x, startPoint.y)
            slptObjects.add(stepView)
        }
        return slptObjects
//        var betterResolution = better_resolution
//        betterResolution = betterResolution && settings.better_resolution_when_raising_hand
//        var tmp_left: Int
//        // Do not show in SLPT (but show on raise of hand)
//        val show_all = !settings.clock_only_slpt || betterResolution
//        if (!show_all) return slpt_objects
//        // Show steps
//        if (settings.steps > 0) { // Show or Not icon
//            if (settings.stepsIcon) {
//                val stepsIcon = SlptPictureView()
//                stepsIcon.setImagePicture(SimpleFile.readFileFromAssets(service, (if (betterResolution) "26wc_" else "slpt_") + "icons/" + settings.is_white_bg + "steps.png"))
//                stepsIcon.setStart(
//                        settings.stepsIconLeft.toInt(),
//                        settings.stepsIconTop.toInt()
//                )
//                slpt_objects.add(stepsIcon)
//            }
//            val steps = SlptLinearLayout()
//            steps.add(SlptTodayStepNumView())
//            steps.setTextAttrForAll(
//                    settings.stepsFontSize,
//                    settings.stepsColor,
//                    getTypeFace(service!!.resources, settings.font)
//            )
//            // Position based on screen on
//            steps.alignX = 2
//            steps.alignY = 0
//            tmp_left = settings.stepsLeft.toInt()
//            if (!settings.stepsAlignLeft) { // If text is centered, set rectangle
//                steps.setRect(
//                        (2 * tmp_left + 640),
//                        (settings.font_ratio.toFloat() / 100 * settings.stepsFontSize).toInt()
//                )
//                tmp_left = -320
//            }
//            steps.setStart(
//                    tmp_left,
//                    (settings.stepsTop - settings.font_ratio.toFloat() / 100 * settings.stepsFontSize).toInt()
//            )
//            slpt_objects.add(steps)
//        }
//        // Steps image
//        if (settings.stepsProg > 0 && settings.stepsProgType == 1) { // Image
//// todo
////SlptTodayStepPicGroupView
//        }
//        // steps bar
//        if (settings.stepsProg > 0 && settings.stepsProgType == 0) { // Draw background image
//            if (settings.stepsProgBgBool) {
//                val ring_background = SlptPictureView()
//                ring_background.setImagePicture(SimpleFile.readFileFromAssets(service, (if (settings.isVerge) "verge_" else if (betterResolution) "" else "slpt_") + "circles/ring1_bg.png"))
//                ring_background.setStart((settings.stepsProgLeft - settings.stepsProgRadius).toInt(), (settings.stepsProgTop - settings.stepsProgRadius).toInt())
//                slpt_objects.add(ring_background)
//            }
//            val stepsArcAnglePicView = SlptTodayStepArcAnglePicView()
//            stepsArcAnglePicView.setImagePicture(SimpleFile.readFileFromAssets(service, (if (settings.isVerge) "verge_" else if (betterResolution) "" else "slpt_") + settings.stepsProgSlptImage))
//            stepsArcAnglePicView.setStart((settings.stepsProgLeft - settings.stepsProgRadius).toInt(), (settings.stepsProgTop - settings.stepsProgRadius).toInt())
//            stepsArcAnglePicView.start_angle = if (settings.stepsProgClockwise == 1) settings.stepsProgStartAngle else settings.stepsProgEndAngle
//            //stepsArcAnglePicView.start_angle = settings.stepsProgStartAngle;
//            stepsArcAnglePicView.len_angle = 0
//            stepsArcAnglePicView.full_angle = if (settings.stepsProgClockwise == 1) angleLength else -angleLength
//            stepsArcAnglePicView.draw_clockwise = settings.stepsProgClockwise
//            slpt_objects.add(stepsArcAnglePicView)
//        }
//        return slpt_objects
    }

    companion object {
        private const val TAG = "VergeIT-LOG StepsWidget"
    }
}