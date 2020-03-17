package it.vergeit.gtrclassic.widget

import android.app.Service
import android.content.Context
import android.graphics.*
import android.text.TextPaint
import it.vergeit.gtrclassic.AbstractWatchFace
import it.vergeit.gtrclassic.data.Calories
import it.vergeit.gtrclassic.data.DataType
import it.vergeit.gtrclassic.settings.LoadSettings
import com.ingenic.iwds.slpt.view.core.SlptViewComponent
import com.ingenic.iwds.slpt.view.sport.SlptTodayCaloriesView
import kotlin.math.abs

class CaloriesWidget() : CircleWidget() {

    override var ring: Paint? = null
    override var ringBmp: Bitmap? = null

    constructor(_settings: LoadSettings) : this() {
        settings = _settings
    }

    private val textPaint: TextPaint? = null
    private var calories: Calories? = null
    private val icon: Bitmap? = null
    private var caloriesSweepAngle = 0f
    private var lastSlptUpdateCalories = 0
    private var angleLength: Int = 0

    // Screen-on init (runs once)
    override fun init(service: Service) {
        mService = service

        if (settings.theme.activity?.calories?.circle != null) {
            val circle = settings.theme.activity!!.calories!!.circle!!
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

    // Register calories counter
    override fun getDataTypes(): List<DataType> {
        return listOf(DataType.CALORIES)
    }

    // Calories updater
    override fun onDataUpdate(type: DataType, value: Any) {
        calories = value as Calories
        // Bar angle
        if (settings.theme.activity?.calories != null) {
            caloriesSweepAngle = angleLength * Math.min(calories!!.calories / settings.target_calories, 1f)
            if (abs(calories!!.calories - lastSlptUpdateCalories) / settings.target_calories > 0.05) {
                lastSlptUpdateCalories = calories!!.calories
                // Save the value to get it on the new slpt service
                val sharedPreferences = mService.getSharedPreferences(mService.packageName + "_settings", Context.MODE_PRIVATE)
                sharedPreferences.edit().putInt("temp_calories", lastSlptUpdateCalories).apply()
                // Restart slpt
                (mService as AbstractWatchFace?)!!.restartSlpt()
            }
        }
    }

    // Screen on
    override fun draw(canvas: Canvas?, width: Float, height: Float, centerX: Float, centerY: Float) {
        if (calories == null) {
            return
        }

        if (settings.theme.activity?.calories != null) {
            drawText(canvas!!, calories!!.calories, settings.theme.activity!!.calories!!)
        }

        val circle = settings.theme.activity!!.calories?.circle

        if (ring != null) {
            drawRing(canvas!!, circle!!, ring!!, caloriesSweepAngle)
        }

        if (ringBmp != null) {
            drawCircle(canvas!!, circle!!, ringBmp!!, caloriesSweepAngle)
        }
    }

    // Screen-off (SLPT)
    override fun buildSlptViewComponent(service: Service?): List<SlptViewComponent> {
        return buildSlptViewComponent(service, false)
    }

    // Screen-off (SLPT) - Better screen quality
    override fun buildSlptViewComponent(service: Service?, better_resolution: Boolean):
            List<SlptViewComponent> {
        val slptObjects = mutableListOf<SlptViewComponent>()
        mService = service!!
        if (settings.theme.activity?.calories != null) {
//            val calories = calories!!.calories
            val calories = settings.theme.activity!!.calories!!
            val caloriesView = SlptTodayCaloriesView()
            caloriesView.setImagePictureArray(getBitmapSlptArray(calories.imageIndex, calories.imagesCount, better_resolution))
            caloriesView.setStart(calories.topLeftX, calories.topLeftY)
            caloriesView.setRect(calories.bottomRightX, calories.bottomRightY)
            slptObjects.add(caloriesView)
        }

//        val circle = settings.theme.activity!!.calories?.circle
//
//        if (ring != null) {
//            drawRing(canvas!!, circle!!, ring!!, caloriesSweepAngle)
//        }
//
//        if (ringBmp != null) {
//            drawCircle(canvas!!, circle!!, ringBmp!!, caloriesSweepAngle)
//        }
        return slptObjects

//        Log.d(TAG, "buildSlptViewComponent Calories $mService $ring $ringBmp")
//        var betterResolution = better_resolution
//        betterResolution = betterResolution && settings.better_resolution_when_raising_hand
//        val slpt_objects: MutableList<SlptViewComponent> = ArrayList()
//        // Do not show in SLPT (but show on raise of hand)
//        val show_all = !settings.clock_only_slpt || betterResolution
//        if (!show_all) return slpt_objects
//        if (settings.calories > 0) { // Show or Not icon
//            if (settings.caloriesIcon) {
//                val caloriesIcon = SlptPictureView()
//                caloriesIcon.setImagePicture(SimpleFile.readFileFromAssets(service, (if (betterResolution) "26wc_" else "slpt_") + "icons/" + settings.is_white_bg + "calories.png"))
//                caloriesIcon.setStart(
//                        settings.caloriesIconLeft.toInt(),
//                        settings.caloriesIconTop.toInt()
//                )
//                slpt_objects.add(caloriesIcon)
//            }
//            val caloriesLayout = SlptLinearLayout()
//            caloriesLayout.add(SlptTodayCaloriesView())
//            // Show or Not Units
//            if (settings.caloriesUnits) {
//                val caloriesUnit = SlptPictureView()
//                caloriesUnit.setStringPicture(" kcal")
//                caloriesLayout.add(caloriesUnit)
//            }
//            caloriesLayout.setTextAttrForAll(
//                    settings.caloriesFontSize,
//                    settings.caloriesColor,
//                    getTypeFace(service!!.resources, settings.font)
//            )
//            // Position based on screen on
//            caloriesLayout.alignX = 2
//            caloriesLayout.alignY = 0
//            var tmp_left = settings.caloriesLeft.toInt()
//            if (!settings.caloriesAlignLeft) { // If text is centered, set rectangle
//                caloriesLayout.setRect(
//                        (2 * tmp_left + 640),
//                        (settings.font_ratio.toFloat() / 100 * settings.caloriesFontSize).toInt()
//                )
//                tmp_left = -320
//            }
//            caloriesLayout.setStart(
//                    tmp_left,
//                    (settings.caloriesTop - settings.font_ratio.toFloat() / 100 * settings.caloriesFontSize).toInt()
//            )
//            slpt_objects.add(caloriesLayout)
//        }
//        // Calories bar
//        if (settings.caloriesProg > 0 && settings.caloriesProgType == 0) { // Draw background image
//            if (settings.caloriesProgBgBool) {
//                val ring_background = SlptPictureView()
//                ring_background.setImagePicture(SimpleFile.readFileFromAssets(service, (if (settings.isVerge) "verge_" else if (betterResolution) "" else "slpt_") + "circles/ring1_bg.png"))
//                ring_background.setStart((settings.caloriesProgLeft - settings.caloriesProgRadius).toInt(), (settings.caloriesProgTop - settings.caloriesProgRadius).toInt())
//                slpt_objects.add(ring_background)
//            }
//            //if(calories==null){calories = new Calories(0);}
//            val localSlptArcAnglePicView = SlptArcAnglePicView()
//            localSlptArcAnglePicView.setImagePicture(SimpleFile.readFileFromAssets(service, (if (settings.isVerge) "verge_" else if (betterResolution) "" else "slpt_") + settings.caloriesProgSlptImage))
//            localSlptArcAnglePicView.setStart((settings.caloriesProgLeft - settings.caloriesProgRadius).toInt(), (settings.caloriesProgTop - settings.caloriesProgRadius).toInt())
//            localSlptArcAnglePicView.start_angle = if (settings.caloriesProgClockwise == 1) settings.caloriesProgStartAngle else settings.caloriesProgEndAngle
//            localSlptArcAnglePicView.len_angle = (angleLength * Math.min(settings.temp_calories / settings.target_calories, 1f)).toInt()
//            localSlptArcAnglePicView.full_angle = if (settings.caloriesProgClockwise == 1) angleLength else -angleLength
//            localSlptArcAnglePicView.draw_clockwise = settings.caloriesProgClockwise
//            slpt_objects.add(localSlptArcAnglePicView)
//        }
//        return slptObjects
    }

    companion object {
        private const val TAG = "VergeIT-LOG Calories"
    }

}