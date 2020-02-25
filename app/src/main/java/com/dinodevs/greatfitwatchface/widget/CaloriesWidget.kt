package com.dinodevs.greatfitwatchface.widget

import android.app.Service
import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.Log
import android.util.Size
import com.dinodevs.greatfitwatchface.AbstractWatchFace
import com.dinodevs.greatfitwatchface.data.Calories
import com.dinodevs.greatfitwatchface.data.DataType
import com.dinodevs.greatfitwatchface.resource.ResourceManager.getTypeFace
import com.dinodevs.greatfitwatchface.settings.LoadSettings
import com.dinodevs.greatfitwatchface.theme.bin.IText
import com.huami.watch.watchface.util.Util
import com.ingenic.iwds.slpt.view.arc.SlptArcAnglePicView
import com.ingenic.iwds.slpt.view.core.SlptLinearLayout
import com.ingenic.iwds.slpt.view.core.SlptPictureView
import com.ingenic.iwds.slpt.view.core.SlptViewComponent
import com.ingenic.iwds.slpt.view.sport.SlptTodayCaloriesView
import com.ingenic.iwds.slpt.view.utils.SimpleFile
import java.util.*

class CaloriesWidget(private val settings: LoadSettings) : AbstractWidget() {
    private val textPaint: TextPaint? = null
    private var calories: Calories? = null
    private val icon: Bitmap? = null
    private var caloriesSweepAngle = 0f
    private var lastSlptUpdateCalories = 0
    private var angleLength: Int = 0
    private var ring: Paint? = null
    private var mService: Service? = null
    // Screen-on init (runs once)
    override fun init(service: Service) {
        mService = service
        //
//        if(settings.calories>0) {
//            // Font
//            this.textPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
//            this.textPaint.setColor(settings.caloriesColor);
//            this.textPaint.setTypeface(ResourceManager.getTypeFace(service.getResources(), settings.font));
//            this.textPaint.setTextSize(settings.caloriesFontSize);
//            this.textPaint.setTextAlign((settings.caloriesAlignLeft) ? Paint.Align.LEFT : Paint.Align.CENTER);
//
//            if (settings.caloriesIcon) {
//                this.icon = Util.decodeImage(service.getResources(), "icons/"+settings.is_white_bg+"calories.png");
//            }
//        }
//
//        // Progress Bar Circle
//        if(settings.caloriesProg>0 && settings.caloriesProgType==0){
        if (settings.theme.caloriesGraph != null) {
            ring = Paint(Paint.ANTI_ALIAS_FLAG)
            ring!!.strokeCap = Paint.Cap.ROUND
            ring!!.style = Paint.Style.STROKE
            ring!!.strokeWidth = settings.theme.caloriesGraph!!.circle!!.width!!.toFloat()
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
        if (settings.theme.caloriesGraph != null) {
            caloriesSweepAngle = angleLength * Math.min(calories!!.calories / settings.target_calories, 1f)
            Log.d(TAG, String.format("caloriesSweepAngle %f %d %f", caloriesSweepAngle, calories!!.calories, settings.target_calories))
            if (Math.abs(calories!!.calories - lastSlptUpdateCalories) / settings.target_calories > 0.05) {
                lastSlptUpdateCalories = calories!!.calories
                // Save the value to get it on the new slpt service
                val sharedPreferences = mService!!.getSharedPreferences(mService!!.packageName + "_settings", Context.MODE_PRIVATE)
                sharedPreferences.edit().putInt("temp_calories", lastSlptUpdateCalories).apply()
                // Restart slpt
                (mService as AbstractWatchFace?)!!.restartSlpt()
            }
        }
    }

    private fun drawText(canvas: Canvas, value: Int, spec: IText?) {
        val width = spec!!.bottomRightX!! - spec.topLeftX!!
        val height = spec.bottomRightY!! - spec.topLeftY!!
        val spacing = Math.round(width / spec.imagesCount!!.toFloat()) + spec.spacing!!
        var x = spec.topLeftX!!
        val y = spec.topLeftY!!
        val bmp = Util.decodeImage(mService!!.resources, settings.theme.getImagePath(spec.imageIndex!!))
        val imageSize = Size(bmp.width, bmp.height)
        if (spec.alignment == "Center") {
            x += imageSize.width / 2 + width / 2 - value / 10 * spacing
        }
        val text = String.format("%d", value)
        Log.d(TAG, String.format("draw value %s", text))
        for (i in text.toCharArray().indices) {
            val charToPrint = text.toCharArray()[i]
            val va = charToPrint - '0'
            val charBmp = Util.decodeImage(mService!!.resources, settings.theme.getImagePath(spec.imageIndex!! + va))
            canvas.drawBitmap(charBmp, x.toFloat(), y.toFloat(), settings.mGPaint)
            x += charBmp.width + spacing
        }
        Log.d(TAG, "complete")
    }

    // Screen on
    override fun draw(canvas: Canvas, width: Float, height: Float, centerX: Float, centerY: Float) {
        if (settings.theme.calories != null) { //            if(settings.caloriesIcon){
//                canvas.drawBitmap(this.icon, settings.caloriesIconLeft, settings.caloriesIconTop, settings.mGPaint);
//            }
            drawText(canvas, calories!!.calories, settings.theme.calories)
        }
        // Calories bar
//        if (settings.caloriesProg > 0 && settings.caloriesProgType == 0) {
        if (settings.theme.caloriesGraph != null) {
            val count = canvas.save()
            val cal = settings.theme.caloriesGraph
            // Rotate canvas to 0 degrees = 12 o'clock
            canvas.rotate(-90f, centerX, centerY)
            //            // Define circle
//            float radius = settings.caloriesProgRadius - settings.caloriesProgThickness;
//            RectF oval = new RectF(settings.caloriesProgLeft - radius, settings.caloriesProgTop - radius, settings.caloriesProgLeft + radius, settings.caloriesProgTop + radius);
//
//            // Background
//            if (settings.caloriesProgBgBool) {
//                this.ring.setColor(Color.parseColor("#999999"));
//                canvas.drawArc(oval, settings.caloriesProgStartAngle, this.angleLength, false, ring);
//            }
//
//            this.ring.setColor(settings.colorCodes[settings.caloriesProgColorIndex]);
//            canvas.drawArc(oval, settings.caloriesProgStartAngle, this.caloriesSweepAngle, false, ring);
            val radius = cal!!.circle!!.radiusX /*- scale.getWidth()*/ !!.toFloat()
            val oval = RectF(cal.circle!!.centerX!! - radius, cal.circle!!.centerY!! - radius,
                    cal.circle!!.centerX!! + radius,
                    cal.circle!!.centerY!! + radius)
            // Background
            Log.d(TAG, String.format("getStartAngle: %d angleLength: %d", cal.circle!!.startAngle, angleLength))
            ring!!.color = Color.parseColor(String.format("#%s", cal.circle!!.color!!.substring(12)))
            //            canvas.drawArc(oval, cal.getCircle().getStartAngle(), this.angleLength, false, ring);
// this.ring.setColor(settings.colorCodes[settings.batteryProgColorIndex]); progressione colore
            canvas.drawArc(oval, cal.circle!!.startAngle!!.toFloat(), caloriesSweepAngle, false, ring)
            canvas.restoreToCount(count)
        }
    }

    // Screen-off (SLPT)
    override fun buildSlptViewComponent(service: Service): List<SlptViewComponent> {
        return buildSlptViewComponent(service, false)
    }

    // Screen-off (SLPT) - Better screen quality
    override fun buildSlptViewComponent(service: Service, better_resolution: Boolean): List<SlptViewComponent> {
        var better_resolution = better_resolution
        better_resolution = better_resolution && settings.better_resolution_when_raising_hand
        val slpt_objects: MutableList<SlptViewComponent> = ArrayList()
        // Do not show in SLPT (but show on raise of hand)
        val show_all = !settings.clock_only_slpt || better_resolution
        if (!show_all) return slpt_objects
        mService = service
        if (settings.calories > 0) { // Show or Not icon
            if (settings.caloriesIcon) {
                val caloriesIcon = SlptPictureView()
                caloriesIcon.setImagePicture(SimpleFile.readFileFromAssets(service, (if (better_resolution) "26wc_" else "slpt_") + "icons/" + settings.is_white_bg + "calories.png"))
                caloriesIcon.setStart(
                        settings.caloriesIconLeft.toInt(),
                        settings.caloriesIconTop.toInt()
                )
                slpt_objects.add(caloriesIcon)
            }
            val caloriesLayout = SlptLinearLayout()
            caloriesLayout.add(SlptTodayCaloriesView())
            // Show or Not Units
            if (settings.caloriesUnits) {
                val caloriesUnit = SlptPictureView()
                caloriesUnit.setStringPicture(" kcal")
                caloriesLayout.add(caloriesUnit)
            }
            caloriesLayout.setTextAttrForAll(
                    settings.caloriesFontSize,
                    settings.caloriesColor,
                    getTypeFace(service.resources, settings.font)
            )
            // Position based on screen on
            caloriesLayout.alignX = 2
            caloriesLayout.alignY = 0
            var tmp_left = settings.caloriesLeft.toInt()
            if (!settings.caloriesAlignLeft) { // If text is centered, set rectangle
                caloriesLayout.setRect(
                        (2 * tmp_left + 640),
                        (settings.font_ratio.toFloat() / 100 * settings.caloriesFontSize).toInt()
                )
                tmp_left = -320
            }
            caloriesLayout.setStart(
                    tmp_left,
                    (settings.caloriesTop - settings.font_ratio.toFloat() / 100 * settings.caloriesFontSize).toInt()
            )
            slpt_objects.add(caloriesLayout)
        }
        // Calories bar
        if (settings.caloriesProg > 0 && settings.caloriesProgType == 0) { // Draw background image
            if (settings.caloriesProgBgBool) {
                val ring_background = SlptPictureView()
                ring_background.setImagePicture(SimpleFile.readFileFromAssets(service, (if (settings.isVerge) "verge_" else if (better_resolution) "" else "slpt_") + "circles/ring1_bg.png"))
                ring_background.setStart((settings.caloriesProgLeft - settings.caloriesProgRadius).toInt(), (settings.caloriesProgTop - settings.caloriesProgRadius).toInt())
                slpt_objects.add(ring_background)
            }
            //if(calories==null){calories = new Calories(0);}
            val localSlptArcAnglePicView = SlptArcAnglePicView()
            localSlptArcAnglePicView.setImagePicture(SimpleFile.readFileFromAssets(service, (if (settings.isVerge) "verge_" else if (better_resolution) "" else "slpt_") + settings.caloriesProgSlptImage))
            localSlptArcAnglePicView.setStart((settings.caloriesProgLeft - settings.caloriesProgRadius).toInt(), (settings.caloriesProgTop - settings.caloriesProgRadius).toInt())
            localSlptArcAnglePicView.start_angle = if (settings.caloriesProgClockwise == 1) settings.caloriesProgStartAngle else settings.caloriesProgEndAngle
            localSlptArcAnglePicView.len_angle = (angleLength * Math.min(settings.temp_calories / settings.target_calories, 1f)).toInt()
            localSlptArcAnglePicView.full_angle = if (settings.caloriesProgClockwise == 1) angleLength else -angleLength
            localSlptArcAnglePicView.draw_clockwise = settings.caloriesProgClockwise
            slpt_objects.add(localSlptArcAnglePicView)
        }
        return slpt_objects
    }

    companion object {
        private const val TAG = "VergeIT-LOG"
    }

    // Constructor
    init {
        //        if (!(settings.caloriesProg > 0 && settings.caloriesProgType == 0)) {
        if (settings.theme.caloriesGraph != null) {
            //        if (settings.caloriesProgClockwise == 1) {
//            this.angleLength = (settings.caloriesProgEndAngle < settings.caloriesProgStartAngle) ? 360 - (settings.caloriesProgStartAngle - settings.caloriesProgEndAngle) : settings.caloriesProgEndAngle - settings.caloriesProgStartAngle;
//        } else {
//            this.angleLength = (settings.caloriesProgEndAngle > settings.caloriesProgStartAngle) ? 360 - (settings.caloriesProgStartAngle - settings.caloriesProgEndAngle) : settings.caloriesProgEndAngle - settings.caloriesProgStartAngle;
//        }
            angleLength = settings.theme.caloriesGraph!!.circle!!.startAngle!! - settings.theme.caloriesGraph!!.circle!!.endAngle!!
        }
    }
}