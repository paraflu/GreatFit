package com.dinodevs.greatfitwatchface.widget

import android.app.Service
import android.graphics.*
import android.util.Log
import android.util.Size
import com.dinodevs.greatfitwatchface.data.Battery
import com.dinodevs.greatfitwatchface.data.DataType
import com.dinodevs.greatfitwatchface.settings.LoadSettings
import com.huami.watch.watchface.util.Util
import com.ingenic.iwds.slpt.view.core.SlptBatteryView
import com.ingenic.iwds.slpt.view.core.SlptViewComponent
import com.ingenic.iwds.slpt.view.utils.SimpleFile
import java.util.*
import kotlin.math.roundToInt

open class TextWidget(private val settings: LoadSettings): AbstractWidget() {
    private lateinit var mService: Service

    override fun buildSlptViewComponent(service: Service?): List<SlptViewComponent?>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun buildSlptViewComponent(service: Service?, better_resolution: Boolean): List<SlptViewComponent?>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun draw(canvas: Canvas?, width: Float, height: Float, centerX: Float, centerY: Float) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun init(service: Service) {
        mService = service
    }

    override fun onDataUpdate(type: DataType, value: Any) {
        super.onDataUpdate(type, value)
    }
}

class CircleWidget(private val settings: LoadSettings): TextWidget(settings) {

}

class BatteryWidget(private val settings: LoadSettings) : AbstractWidget() {
    private var batteryData: Battery? = null
    //    private Paint batteryPaint;
    private var ring: Paint? = null
    //
    private var batterySweepAngle = 0f
    private var angleLength: Int = 0
    //
    private var batteryIcon: Bitmap? = null
    //    private Bitmap icon;
//
    private var tempBattery = 0
    private var mService: Service? = null
    // Screen-on init (runs once)
    override fun init(service: Service) {
        mService = service
        // Battery percent element
//        if(settings.battery_percent>0){
//            if(settings.battery_percentIcon){
//                this.icon = Util.decodeImage(mService.getResources(),"icons/"+settings.is_white_bg+"battery.png");
//            }
//
//            this.batteryPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//            this.batteryPaint.setTypeface(ResourceManager.getTypeFace(service.getResources(), settings.font));
//            this.batteryPaint.setTextSize(settings.battery_percentFontSize);
//            this.batteryPaint.setColor(settings.battery_percentColor);
//            this.batteryPaint.setTextAlign( (settings.battery_percentAlignLeft) ? Paint.Align.LEFT : Paint.Align.CENTER );
//        }
//
//        // Battery as images
//        if(settings.batteryProg>0 && settings.batteryProgType==1) {
//            this.batteryIcon = Util.decodeImage(mService.getResources(),"battery/battery0.png");
//        }
//
// Progress Bar Circle
        if (settings.theme.battery.scale != null) {
            ring = Paint(Paint.ANTI_ALIAS_FLAG)
            ring!!.strokeCap = Paint.Cap.ROUND
            ring!!.style = Paint.Style.STROKE
            ring!!.strokeWidth = settings.theme.battery.scale!!.width!!.toFloat()
        }
    }

    // Value updater
    override fun onDataUpdate(type: DataType, value: Any) { // Battery class
        batteryData = value as Battery
        if (batteryData == null) {
            return
        }
        // Bar angle
        Log.d(TAG, String.format("settings.batteryProg > %d && settings.batteryProgType == %d", settings.batteryProg, settings.batteryProgType))
        if (settings.theme.battery.scale != null) {
            Log.d(TAG, String.format("angle %d", angleLength))
            batterySweepAngle = angleLength * (batteryData!!.level / batteryData!!.scale.toFloat())
        }

        if (settings.theme.battery != null) {
            val batterySteps: Int = settings.theme.battery.text!!.imagesCount
            tempBattery = batteryData!!.level / batterySteps
        }
    }

    // Register update listeners
    override fun getDataTypes(): List<DataType> {
        return listOf(DataType.BATTERY)
    }

    private fun drawText(canvas: Canvas, value: Int, text: com.dinodevs.greatfitwatchface.theme.bin.Text) {
        val width = text.bottomRightX - text.topLeftX
        val height = text.bottomRightY - text.topLeftY
        val spacing = (width / text.imagesCount.toFloat()).roundToInt() + text.spacing
        var x = text.topLeftX
        val y = text.topLeftY
        val bmp = Util.decodeImage(mService!!.resources, settings.getImagePath(settings.theme.battery.text!!.imageIndex))
        val imageSize = Size(bmp.width, bmp.height)

        val textString = String.format("%d", value)
        if (text.alignment == "Center") {
            x += (width /2f - textString.length * imageSize.width / 2f).roundToInt()
        }
        Log.d(TAG, String.format("draw value %s", textString))
        for (i in textString.toCharArray().indices) {
            val charToPrint = textString.toCharArray()[i]
            val va = charToPrint - '0'
            Log.d(TAG, String.format("draw char (x: %d, y: %d) %d - %c > bmp %s", x, y, i, charToPrint, settings.getImagePath(settings.theme.battery.text!!.imageIndex + va)))
            val charBmp = Util.decodeImage(mService!!.resources, settings.getImagePath(settings.theme.battery.text!!.imageIndex + va))
            canvas.drawBitmap(charBmp, x.toFloat(), y.toFloat(), settings.mGPaint)
            x += charBmp.width + spacing
        }
        Log.d(TAG, "complete")
    }

    // Draw screen-on
    override fun draw(canvas: Canvas, width: Float, height: Float, centerX: Float, centerY: Float) {
        if (batteryData == null) {
            return
        }
        try {
            drawText(canvas, batteryData!!.level, settings.theme.battery.text!!)
        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }
        // Battery % widget
//        if(settings.battery_percent>0){
//            if(settings.battery_percentIcon){
//                canvas.drawBitmap(this.icon, settings.battery_percentIconLeft, settings.battery_percentIconTop, settings.mGPaint);
//            }
//
//            String text = Integer.toString(this.batteryData.getLevel() * 100 / this.batteryData.getScale())+"%";
//            canvas.drawText(text, settings.battery_percentLeft, settings.battery_percentTop, batteryPaint);
//        }
// Battery Progress Image
//        if (settings.theme.battery.text != null) {
//            canvas.drawBitmap(batteryIcon!!,
//                    settings.theme.battery.text!!.topLeftX.toFloat(),
//                    settings.theme.battery.text!!.topLeftX.toFloat(),
//                    settings.mGPaint)
//        }
        // Battery bar
//        if (settings.batteryProg > 0 && settings.batteryProgType == 0) {
        if (settings.theme.battery.scale != null) {
            val count = canvas.save()
            // Rotate canvas to 0 degrees = 12 o'clock
            canvas.rotate(-90f, centerX, centerY)
            // Define circle
            val scale = settings.theme.battery.scale!!
            val radius = scale.radiusX.toFloat() /*- scale.getWidth()*/
            val oval = RectF(scale.centerX - radius, scale.centerY - radius,
                    scale.centerX + radius,
                    scale.centerY + radius)
            // Background
            ring!!.color = Color.parseColor(String.format("#%s", scale.color.substring(12)))
            //            canvas.drawArc(oval, scale.getStartAngle(), this.angleLength, false, ring);
// this.ring.setColor(settings.colorCodes[settings.batteryProgColorIndex]); progressione colore
            canvas.drawArc(oval, scale.startAngle.toFloat(), batterySweepAngle, false, ring)
            canvas.restoreToCount(count)
        }
    }

    // Screen-off (SLPT)
    override fun buildSlptViewComponent(service: Service?): List<SlptViewComponent> {
        return buildSlptViewComponent(service, false)
    }

    // Screen-off (SLPT) - Better screen quality
    override fun buildSlptViewComponent(service: Service?, better_resolution: Boolean): List<SlptViewComponent> {
        var better_resolution = better_resolution
        better_resolution = better_resolution && settings.better_resolution_when_raising_hand
        val slpt_objects: MutableList<SlptViewComponent> = ArrayList()
        // Do not show in SLPT (but show on raise of hand)
        val show_all = !settings.clock_only_slpt || better_resolution
        if (!show_all) return slpt_objects
        var tmp_left: Int
        //        // Show battery
//        if(settings.battery_percent>0){
//            // Show or Not icon
//            if (settings.battery_percentIcon) {
//                SlptPictureView battery_percentIcon = new SlptPictureView();
//                battery_percentIcon.setImagePicture( SimpleFile.readFileFromAssets(service, ( (better_resolution)?"":"slpt_" )+"icons/"+settings.is_white_bg+"battery.png") );
//                battery_percentIcon.setStart(
//                        (int) settings.battery_percentIconLeft,
//                        (int) settings.battery_percentIconTop
//                );
//                slpt_objects.add(battery_percentIcon);
//            }
//
//            SlptLinearLayout power = new SlptLinearLayout();
//            SlptPictureView percentage = new SlptPictureView();
//            percentage.setStringPicture("%");
//            power.add(new SlptPowerNumView());
//            power.add(percentage);
//            power.setTextAttrForAll(
//                    settings.battery_percentFontSize,
//                    settings.battery_percentColor,
//                    ResourceManager.getTypeFace(service.getResources(), settings.font)
//            );
//            // Position based on screen on
//            power.alignX = 2;
//            power.alignY = 0;
//            tmp_left = (int) settings.battery_percentLeft;
//            if(!settings.battery_percentAlignLeft) {
//                // If text is centered, set rectangle
//                power.setRect(
//                        (int) (2 * tmp_left + 640),
//                        (int) (((float)settings.font_ratio/100)*settings.battery_percentFontSize)
//                );
//                tmp_left = -320;
//            }
//            power.setStart(
//                    tmp_left,
//                    (int) (settings.battery_percentTop-((float)settings.font_ratio/100)*settings.battery_percentFontSize)
//            );
//            slpt_objects.add(power);
//        }
// Battery as images
        if (settings.batteryProg > 0 && settings.batteryProgType == 1) {
            val battery_steps = 11
            val arrayOfByte = arrayOfNulls<ByteArray>(battery_steps)
            for (i in arrayOfByte.indices) {
                arrayOfByte[i] = SimpleFile.readFileFromAssets(service, settings.getImagePath(settings.theme.battery.text!!.imageIndex + i))
            }
            val localSlptBatteryView = SlptBatteryView(battery_steps)
            localSlptBatteryView.setImagePictureArray(arrayOfByte)
            localSlptBatteryView.setStart(settings.batteryProgLeft.toInt(), settings.batteryProgTop.toInt())
            slpt_objects.add(localSlptBatteryView)
        }
        //        // Battery bar
//        if(settings.batteryProg>0 && settings.batteryProgType==0){
//            // Draw background image
//            if(settings.batteryProgBgBool) {
//                SlptPictureView ring_background = new SlptPictureView();
//                ring_background.setImagePicture(SimpleFile.readFileFromAssets(service, ((settings.isVerge())?"verge_":( (better_resolution)?"":"slpt_" ))+"circles/ring1_bg.png"));
//                ring_background.setStart((int) (settings.batteryProgLeft-settings.batteryProgRadius), (int) (settings.batteryProgTop-settings.batteryProgRadius));
//                slpt_objects.add(ring_background);
//            }
//
//            SlptPowerArcAnglePicView localSlptPowerArcAnglePicView = new SlptPowerArcAnglePicView();
//            localSlptPowerArcAnglePicView.setImagePicture(SimpleFile.readFileFromAssets(service, ((settings.isVerge())?"verge_":( (better_resolution)?"":"slpt_" ))+settings.batteryProgSlptImage));
//            localSlptPowerArcAnglePicView.setStart((int) (settings.batteryProgLeft-settings.batteryProgRadius), (int) (settings.batteryProgTop-settings.batteryProgRadius));
//            localSlptPowerArcAnglePicView.start_angle = (settings.batteryProgClockwise==1)? settings.batteryProgStartAngle : settings.batteryProgEndAngle;
//            localSlptPowerArcAnglePicView.len_angle = 0;
//            localSlptPowerArcAnglePicView.full_angle = (settings.batteryProgClockwise==1)? this.angleLength : -this.angleLength;
//            localSlptPowerArcAnglePicView.draw_clockwise = settings.batteryProgClockwise;
//            slpt_objects.add(localSlptPowerArcAnglePicView);
//        }
        return slpt_objects
    }

    companion object {
        private const val TAG = "VergeIT-LOG"
    }

    // Constructor
    init {
        if (settings.theme.battery.scale != null) {
            //        if (settings.batteryProgClockwise == 1) {
//            this.angleLength = (settings.batteryProgEndAngle < settings.batteryProgStartAngle) ?
//                    360 - (settings.theme.getbattery.scale().getStartAngle() - settings.theme.getbattery.scale().getEndAngle()) :
//                    settings.theme.getbattery.scale().getEndAngle() - settings.theme.getbattery.scale().getStartAngle();
//        } else {
//            this.angleLength = (settings.theme.getbattery.scale().getEndAngle() > settings.theme.getbattery.scale().getStartAngle()) ?
//                    360 - (settings.theme.getbattery.scale().getStartAngle() - settings.theme.getbattery.scale().getEndAngle()) :
//                    settings.theme.getbattery.scale().getEndAngle() - settings.theme.getbattery.scale().getStartAngle();
//        }
            val scale = settings.theme.battery.scale!!
            angleLength = 360 - (if (scale.startAngle > scale.endAngle) scale.endAngle - scale.startAngle else scale.startAngle - scale.endAngle)
        }
    }
}