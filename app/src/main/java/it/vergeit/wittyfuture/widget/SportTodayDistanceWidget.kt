package it.vergeit.wittyfuture.widget

import android.app.Service
import android.graphics.*
import it.vergeit.wittyfuture.data.DataType
import it.vergeit.wittyfuture.data.TodayDistance
import it.vergeit.wittyfuture.resource.ResourceManager.getTypeFace
import it.vergeit.wittyfuture.settings.LoadSettings
import com.ingenic.iwds.slpt.view.arc.SlptTodayDistanceArcAnglePicView
import com.ingenic.iwds.slpt.view.core.SlptLinearLayout
import com.ingenic.iwds.slpt.view.core.SlptPictureView
import com.ingenic.iwds.slpt.view.core.SlptViewComponent
import com.ingenic.iwds.slpt.view.sport.SlptTodaySportDistanceFView
import com.ingenic.iwds.slpt.view.sport.SlptTodaySportDistanceLView
import com.ingenic.iwds.slpt.view.utils.SimpleFile
import java.util.*

class SportTodayDistanceWidget() : CircleWidget() {
    private var today_distanceData: TodayDistance? = null
    private var today_distancePaint: Paint? = null
    private var today_distanceIcon: Bitmap? = null
    private var today_distanceSweepAngle = 0f
    private var angleLength: Int = 0

    override var ring: Paint? = null
    override var ringBmp: Bitmap? = null

    constructor(_settings: LoadSettings) : this() {
        settings = _settings
    }

    // Screen-on init (runs once)
    override fun init(service: Service) {

        super.init(service)

        val distance = settings.theme.activity?.distance
        if (distance != null) {

        }

//        // Constructor
//        if (settings.theme.activity?.)
//            angleLength = circleCalc()
//
//            today_distancePaint = Paint(Paint.ANTI_ALIAS_FLAG)
//            today_distancePaint!!.typeface = getTypeFace(service.resources, settings.font)
//            today_distancePaint!!.textSize = settings.today_distanceFontSize
//            today_distancePaint!!.color = settings.today_distanceColor
//            today_distancePaint!!.textAlign = if (settings.today_distanceAlignLeft) Paint.Align.LEFT else Paint.Align.CENTER
//            if (settings.today_distanceIcon) {
//                today_distanceIcon = Util.decodeImage(service.resources, "icons/" + settings.is_white_bg + "today_distance.png")
//            }
//        }
//        if (settings.today_distanceProg > 0 && settings.today_distanceProgType == 0) {
//            ring = Paint(Paint.ANTI_ALIAS_FLAG)
//            ring!!.strokeCap = Paint.Cap.ROUND
//            ring!!.style = Paint.Style.STROKE
//            ring!!.strokeWidth = settings.today_distanceProgThickness
//        }
    }

    // Value updater
    override fun onDataUpdate(type: DataType, value: Any) { // Today Sport's Distance class
        today_distanceData = value as TodayDistance
        // Bar angle
        today_distanceSweepAngle = if (today_distanceData == null) 0f else Math.min(angleLength.toFloat(), angleLength * (today_distanceData!!.distance / 3.0f))
    }

    // Register update listeners
    override fun getDataTypes(): List<DataType> {
        return listOf(DataType.DISTANCE)
    }

    // Draw screen-on
    override fun draw(canvas: Canvas?, width: Float, height: Float, centerX: Float, centerY: Float) {
        if (today_distanceData == null) {
            return
        }

        drawText(canvas!!, today_distanceData!!.distance.toString(), settings.theme.activity!!.distance!!)

    }

    // Screen-off (SLPT)
    override fun buildSlptViewComponent(service: Service?): List<SlptViewComponent?>? {
        return buildSlptViewComponent(service, false)
    }

    // Screen-off (SLPT) - Better screen quality
    override fun buildSlptViewComponent(service: Service?, better_resolution: Boolean): List<SlptViewComponent?>? {
        var betterResolution = better_resolution
        betterResolution = betterResolution && settings.better_resolution_when_raising_hand
        val slpt_objects: MutableList<SlptViewComponent?> = ArrayList()
        var tmp_left: Int
        // Do not show in SLPT (but show on raise of hand)
        val show_all = !settings.clock_only_slpt || betterResolution
        if (!show_all) return slpt_objects
        // Show Today Sport's Distance
        if (settings.today_distance > 0) { // Show or Not icon
            if (settings.today_distanceIcon) {
                val today_distanceIcon = SlptPictureView()
                today_distanceIcon.setImagePicture(SimpleFile.readFileFromAssets(service, (if (betterResolution) "26wc_" else "slpt_") + "icons/" + settings.is_white_bg + "today_distance.png"))
                today_distanceIcon.setStart(
                        settings.today_distanceIconLeft.toInt(),
                        settings.today_distanceIconTop.toInt()
                )
                slpt_objects.add(today_distanceIcon)
            }
            val dot = SlptPictureView()
            dot.setStringPicture(".")
            val kilometer = SlptPictureView()
            kilometer.setStringPicture(" km")
            val distance = SlptLinearLayout()
            distance.add(SlptTodaySportDistanceFView())
            distance.add(dot)
            distance.add(SlptTodaySportDistanceLView())
            // Show or Not Units
            if (settings.today_distanceUnits) {
                distance.add(kilometer)
            }
            distance.setTextAttrForAll(
                    settings.today_distanceFontSize,
                    settings.today_distanceColor,
                    getTypeFace(service!!.resources, settings.font)
            )
            // Position based on screen on
            distance.alignX = 2
            distance.alignY = 0
            tmp_left = settings.today_distanceLeft.toInt()
            if (!settings.today_distanceAlignLeft) { // If text is centered, set rectangle
                distance.setRect(
                        (2 * tmp_left + 640),
                        (settings.font_ratio.toFloat() / 100 * settings.today_distanceFontSize).toInt()
                )
                tmp_left = -320
            }
            distance.setStart(
                    tmp_left,
                    (settings.today_distanceTop - settings.font_ratio.toFloat() / 100 * settings.today_distanceFontSize).toInt()
            )
            slpt_objects.add(distance)
        }
        // Today Sport's Distance images
        if (settings.today_distanceProg > 0 && settings.today_distanceProgType == 1) { // Image
// todo
//SlptTodayDistancePicGroupView
        }
        // Today Sport's Distance bar
        if (settings.today_distanceProg > 0 && settings.today_distanceProgType == 0) { // Draw background image
            if (settings.today_distanceProgBgBool) {
                val ring_background = SlptPictureView()
                ring_background.setImagePicture(SimpleFile.readFileFromAssets(service, (if (settings.isVerge) "verge_" else if (betterResolution) "" else "slpt_") + "circles/ring1__bg.png"))
                slpt_objects.add(ring_background)
            }
            val today_distanceArcAnglePicView = SlptTodayDistanceArcAnglePicView()
            today_distanceArcAnglePicView.setImagePicture(SimpleFile.readFileFromAssets(service, (if (settings.isVerge) "verge_" else if (betterResolution) "" else "slpt_") + settings.today_distanceProgSlptImage))
            today_distanceArcAnglePicView.setStart(settings.today_distanceProgLeft.toInt(), settings.today_distanceProgTop.toInt())
            today_distanceArcAnglePicView.start_angle = settings.today_distanceProgStartAngle
            today_distanceArcAnglePicView.len_angle = 0
            today_distanceArcAnglePicView.full_angle = settings.today_distanceProgEndAngle
            today_distanceArcAnglePicView.draw_clockwise = settings.today_distanceProgClockwise
            slpt_objects.add(today_distanceArcAnglePicView)
        }
        return slpt_objects
    }


}