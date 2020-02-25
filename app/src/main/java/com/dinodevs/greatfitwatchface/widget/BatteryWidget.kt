package com.dinodevs.greatfitwatchface.widget

import android.app.Service
import android.graphics.*
import android.util.Log
import android.util.Size
import com.dinodevs.greatfitwatchface.data.Battery
import com.dinodevs.greatfitwatchface.data.DataType
import com.dinodevs.greatfitwatchface.settings.LoadSettings
import com.dinodevs.greatfitwatchface.theme.bin.Text
import com.huami.watch.watchface.util.Util
import com.ingenic.iwds.slpt.view.core.SlptBatteryView
import com.ingenic.iwds.slpt.view.core.SlptViewComponent
import com.ingenic.iwds.slpt.view.utils.SimpleFile
import java.util.*

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
        batteryIcon = Util.decodeImage(mService!!.resources, settings.theme.battery[0])
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
        if (settings.theme.batteryScale != null) {
            ring = Paint(Paint.ANTI_ALIAS_FLAG)
            ring!!.strokeCap = Paint.Cap.ROUND
            ring!!.style = Paint.Style.STROKE
            ring!!.strokeWidth = settings.theme.batteryScale!!.width!!.toFloat()
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
        if (settings.batteryProg > 0 && settings.batteryProgType == 0) {
            Log.d(TAG, String.format("angle %d", angleLength))
            batterySweepAngle = angleLength * (batteryData!!.level / batteryData!!.scale.toFloat())
        }
        //
//        // Battery Image
//        if( this.tempBattery == this.batteryData.getLevel()/10 || !(settings.batteryProg>0 && settings.batteryProgType==1)){
//            return;
//        }
        val batterySteps: Int = settings.theme.battery.size
        tempBattery = batteryData!!.level / batterySteps
        //        this.batteryIcon = Util.decodeImage(mService.getResources(), this.settings.theme.getBattery()[this.tempBattery]);
    }

    // Register update listeners
    override fun getDataTypes(): List<DataType> {
        return listOf(DataType.BATTERY)
    }

    private fun drawText(canvas: Canvas, value: Int, digits: Array<String>, spec: Text) {
        val width = spec.bottomRightX!! - spec.topLeftX!!
        val height = spec.bottomRightY!! - spec.topLeftY!!
        val spacing = Math.round(width / digits.size.toFloat()) + spec.spacing!!
        var x = spec.topLeftX!!
        val y = spec.topLeftY!!
        val bmp = Util.decodeImage(mService!!.resources, settings.theme.battery[0])
        val imageSize = Size(bmp.width, bmp.height)
        if (spec.alignment == "Center") {
            x += imageSize.width / 2 + width / 2 - value / 10 * spacing
        }
        val text = String.format("%d", value)
        Log.d(TAG, String.format("draw value %s", text))
        for (i in text.toCharArray().indices) {
            val charToPrint = text.toCharArray()[i]
            val va = charToPrint - '0'
            Log.d(TAG, String.format("draw char (x: %d, y: %d) %d - %c > bmp %s", x, y, i, charToPrint, settings.theme.battery[va]))
            val charBmp = Util.decodeImage(mService!!.resources, settings.theme.battery[va])
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
            drawText(canvas, batteryData!!.level, settings.theme.battery, settings.theme.batterySpec!!)
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
        if (settings.theme.batterySpec != null) {
            canvas.drawBitmap(batteryIcon!!,
                    settings.theme.batterySpec!!.topLeftX!!.toFloat(),
                    settings.theme.batterySpec!!.topLeftX!!.toFloat(),
                    settings.mGPaint)
        }
        // Battery bar
//        if (settings.batteryProg > 0 && settings.batteryProgType == 0) {
        if (settings.theme.batteryScale != null) {
            val count = canvas.save()
            // Rotate canvas to 0 degrees = 12 o'clock
            canvas.rotate(-90f, centerX, centerY)
            // Define circle
            val scale = settings.theme.batteryScale
            val radius = scale!!.radiusX!!.toFloat() /*- scale.getWidth()*/
            val oval = RectF(scale.centerX!! - radius, scale.centerY!! - radius,
                    scale.centerX!! + radius,
                    scale.centerY!! + radius)
            // Background
            Log.d(TAG, String.format("batteryProgBgBool %d", if (settings.batteryProgBgBool) 1 else 0))
            Log.d(TAG, String.format("getStartAngle: %d angleLength: %d", scale.startAngle, angleLength))
            ring!!.color = Color.parseColor(String.format("#%s", scale.color!!.substring(12)))
            //            canvas.drawArc(oval, scale.getStartAngle(), this.angleLength, false, ring);
// this.ring.setColor(settings.colorCodes[settings.batteryProgColorIndex]); progressione colore
            canvas.drawArc(oval, scale.startAngle!!.toFloat(), batterySweepAngle, false, ring)
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
                arrayOfByte[i] = SimpleFile.readFileFromAssets(service, settings.theme.battery[i])
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
        if (settings.theme.batteryScale != null) {
            //        if (settings.batteryProgClockwise == 1) {
//            this.angleLength = (settings.batteryProgEndAngle < settings.batteryProgStartAngle) ?
//                    360 - (settings.theme.getBatteryScale().getStartAngle() - settings.theme.getBatteryScale().getEndAngle()) :
//                    settings.theme.getBatteryScale().getEndAngle() - settings.theme.getBatteryScale().getStartAngle();
//        } else {
//            this.angleLength = (settings.theme.getBatteryScale().getEndAngle() > settings.theme.getBatteryScale().getStartAngle()) ?
//                    360 - (settings.theme.getBatteryScale().getStartAngle() - settings.theme.getBatteryScale().getEndAngle()) :
//                    settings.theme.getBatteryScale().getEndAngle() - settings.theme.getBatteryScale().getStartAngle();
//        }
            val batteryScale = settings.theme.batteryScale!!
            angleLength = if (batteryScale.startAngle!! > batteryScale.endAngle!!) batteryScale.startAngle!! - batteryScale.endAngle!! else batteryScale.endAngle!! - batteryScale.startAngle!!
        }
    }
}