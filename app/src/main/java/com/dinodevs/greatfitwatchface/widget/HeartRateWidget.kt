package com.dinodevs.greatfitwatchface.widget

import android.app.Service
import android.graphics.*
import android.text.TextPaint
import com.dinodevs.greatfitwatchface.data.DataType
import com.dinodevs.greatfitwatchface.data.HeartRate
import com.dinodevs.greatfitwatchface.resource.ResourceManager.getTypeFace
import com.dinodevs.greatfitwatchface.settings.LoadSettings
import com.dinodevs.greatfitwatchface.theme.bin.Element
import com.ingenic.iwds.slpt.view.arc.SlptArcAnglePicView
import com.ingenic.iwds.slpt.view.core.SlptLinearLayout
import com.ingenic.iwds.slpt.view.core.SlptPictureView
import com.ingenic.iwds.slpt.view.core.SlptViewComponent
import com.ingenic.iwds.slpt.view.sport.SlptLastHeartRateView
import com.ingenic.iwds.slpt.view.utils.SimpleFile
import java.util.*

class HeartRateWidget//
//        if(!(settings.heart_rateProg>0 && settings.heart_rateProgType==0)){return;}
//        if(settings.heart_rateProgClockwise==1) {
//            this.angleLength = (settings.heart_rateProgEndAngle < settings.heart_rateProgStartAngle) ? 360 - (settings.heart_rateProgStartAngle - settings.heart_rateProgEndAngle) : settings.heart_rateProgEndAngle - settings.heart_rateProgStartAngle;
//        }else{
//            this.angleLength = (settings.heart_rateProgEndAngle > settings.heart_rateProgStartAngle) ? 360 - (settings.heart_rateProgStartAngle - settings.heart_rateProgEndAngle) : settings.heart_rateProgEndAngle - settings.heart_rateProgStartAngle;
//        }
// Constructor
(private val settings: LoadSettings) : AbstractWidget() {
    private val textPaint: TextPaint? = null
    private var heartRate: HeartRate? = null
    private val heart_rateSweepAngle = 0f
    private val lastSlptUpdateHeart_rate = 0
    private val angleLength: Int? = null
    private val maxHeartRate = 200f
    private val ring: Paint? = null
    private val heart_rateIcon: Bitmap? = null
    private val heart_rate_flashingIcon: Bitmap? = null
    private var mService: Service? = null
    // Screen-on init (runs once)
    override fun init(service: Service) {
        mService = service
        //        this.textPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
//        this.textPaint.setColor(settings.heart_rateColor);
//        this.textPaint.setTypeface(ResourceManager.getTypeFace(service.getResources(), settings.font));
//        this.textPaint.setTextSize(settings.heart_rateFontSize);
//        this.textPaint.setTextAlign( (settings.heart_rateAlignLeft) ? Paint.Align.LEFT : Paint.Align.CENTER );
//
//        if(settings.heart_rateIcon){
//            this.heart_rateIcon = Util.decodeImage(service.getResources(),"icons/"+settings.is_white_bg+"heart_rate.png");
//            this.heart_rate_flashingIcon = Util.decodeImage(service.getResources(),"icons/"+settings.is_white_bg+"heart_rate_flashing.png");
//        }
//
//        // Progress Bar Circle
//        if(settings.heart_rateProg>0 && settings.heart_rateProgType==0){
//            this.ring = new Paint(Paint.ANTI_ALIAS_FLAG);
//            this.ring.setStrokeCap(Paint.Cap.ROUND);
//            this.ring.setStyle(Paint.Style.STROKE);
//            this.ring.setStrokeWidth(settings.heart_rateProgThickness);
//        }
    }

    // Register update listeners
    override fun getDataTypes(): List<DataType> {
        return listOf(DataType.HEART_RATE)
    }

    private fun drawText(canvas: Canvas, value: Int, spec: Element) {
        throw NotImplementedError("pulse DrawText")
    }

    // Value updater
    override fun onDataUpdate(type: DataType, value: Any) { // Heart rate class
        heartRate = value as HeartRate
        //        // Bar angle
//        //this.heartRateSweepAngle = (this.heartRate == null)? 0f : Math.min( this.angleLength, this.angleLength*(heartRate.getHeartRate()/this.maxHeartRate) ) ;
//        // Bar angle
//        if(settings.heart_rateProg>0 && settings.heart_rateProgType==0 && heartRate!=null && heartRate.getHeartRate()>25 ) {
//            this.heart_rateSweepAngle = this.angleLength * Math.min(heartRate.getHeartRate()/this.maxHeartRate,1f);
//
//            //Log.w("VergeIT-LOG", "Heart rate update: "+heartRate.getHeartRate()+", Sweep angle:"+ heart_rateSweepAngle+", %"+(Math.abs(heartRate.getHeartRate()-this.lastSlptUpdateHeart_rate)/this.maxHeartRate));
//
//            if(Math.abs(heartRate.getHeartRate()-this.lastSlptUpdateHeart_rate)/this.maxHeartRate>0.05){
//                this.lastSlptUpdateHeart_rate = heartRate.getHeartRate();
//                // Save the value to get it on the new slpt service
//                SharedPreferences sharedPreferences = mService.getSharedPreferences(mService.getPackageName()+"_settings", Context.MODE_PRIVATE);
//                sharedPreferences.edit().putInt( "temp_heart_rate", this.lastSlptUpdateHeart_rate).apply();
//                // Restart slpt
//                ((AbstractWatchFace) this.mService).restartSlpt();
//            }
//        }
    }

    // Draw screen-on
    override fun draw(canvas: Canvas, width: Float, height: Float, centerX: Float, centerY: Float) { // Draw heart rate element
        if (settings.heart_rate > 0) { // Draw icon
//            if(settings.heart_rateIcon){
//                // Draw flashing heart icon
//                if(settings.flashing_heart_rate_icon) {
//                    Calendar calendar = Calendar.getInstance();
//                    if (calendar.get(Calendar.SECOND) % 2 == 1) {
//                        canvas.drawBitmap(this.heart_rate_flashingIcon, settings.heart_rateIconLeft, settings.heart_rateIconTop, settings.mGPaint);
//                    }else{
//                        canvas.drawBitmap(this.heart_rateIcon, settings.heart_rateIconLeft, settings.heart_rateIconTop, settings.mGPaint);
//                    }
//                }else{
//                    canvas.drawBitmap(this.heart_rateIcon, settings.heart_rateIconLeft, settings.heart_rateIconTop, settings.mGPaint);
//                }
//            }
//
//            // if units are enabled
//            String units = (settings.heart_rateUnits) ? " bpm" : "";
//            // Draw Heart rate
//            String text = (heartRate == null || heartRate.getHeartRate() < 25) ? "--" : heartRate.getHeartRate() + units;
//            canvas.drawText(text, settings.heart_rateLeft, settings.heart_rateTop, textPaint);
            drawText(canvas, heartRate!!.heartRate, settings.theme.activity!!.pulse!!.element)
        }
        // heart_rate bar
        if (settings.heart_rateProg > 0 && settings.heart_rateProgType == 0) {
            val count = canvas.save()
            // Rotate canvas to 0 degrees = 12 o'clock
            canvas.rotate(-90f, centerX, centerY)
            // Define circle
            val radius = settings.heart_rateProgRadius - settings.heart_rateProgThickness
            val oval = RectF(settings.heart_rateProgLeft - radius, settings.heart_rateProgTop - radius, settings.heart_rateProgLeft + radius, settings.heart_rateProgTop + radius)
            // Background
            if (settings.heart_rateProgBgBool) {
                ring!!.color = Color.parseColor("#999999")
                canvas.drawArc(oval, settings.heart_rateProgStartAngle.toFloat(), angleLength!!.toFloat(), false, ring)
            }
            ring!!.color = settings.colorCodes[settings.heart_rateProgColorIndex]
            canvas.drawArc(oval, settings.heart_rateProgStartAngle.toFloat(), heart_rateSweepAngle, false, ring)
            canvas.restoreToCount(count)
        }
    }

    // This doesn't work. There is an error when getting the index
/*
    public static final Uri CONTENT_HEART_URI = Uri.parse("content://com.huami.watch.health.heartdata");
    private int getSlptHeartRate(){
        Integer system_heart_rate = heartRate.getHeartRate();
        if(system_heart_rate==0){
            try {
                Cursor cursor = mService.getContentResolver().query(CONTENT_HEART_URI, null, null, null, "utc_time DESC LIMIT 1");
                //int index = cursor.getColumnIndex( "heart_rate" );
                system_heart_rate = cursor.getInt( 0 );
                Log.w("VergeIT-LOG", "Heart rate: slpt getHertRate() = "+system_heart_rate);
            }catch(Exception e){
                // sth here
                Log.w("VergeIT-LOG", "Heart rate error: "+e.toString());
            }
        }
        return system_heart_rate;
    }
    */
// Screen-off (SLPT)
    override fun buildSlptViewComponent(service: Service?): List<SlptViewComponent?>? {
        return buildSlptViewComponent(service, false)
    }

    // Screen-off (SLPT) - Better screen quality
    override fun buildSlptViewComponent(service: Service?, better_resolution: Boolean): List<SlptViewComponent?>? {
        var betterResolution = better_resolution
        betterResolution = betterResolution && settings.better_resolution_when_raising_hand
        mService = service
        val slpt_objects: MutableList<SlptViewComponent?> = ArrayList()
        var tmp_left: Int
        // Do not show in SLPT (but show on raise of hand)
        val show_all = !settings.clock_only_slpt || betterResolution
        if (!show_all) return slpt_objects
        // Draw heart rate element
        if (settings.heart_rate > 0) { // Show or Not icon
            if (settings.heart_rateIcon) {
                val heart_rateIcon = SlptPictureView()
                heart_rateIcon.setImagePicture(SimpleFile.readFileFromAssets(service, (if (betterResolution) "26wc_" else "slpt_") + "icons/" + settings.is_white_bg + "heart_rate.png"))
                heart_rateIcon.setStart(
                        settings.heart_rateIconLeft.toInt(),
                        settings.heart_rateIconTop.toInt()
                )
                slpt_objects.add(heart_rateIcon)
            }
            val heart = SlptLinearLayout()
            heart.add(SlptLastHeartRateView())
            // Show or Not Units
            if (settings.heart_rateUnits) {
                val bpm = SlptPictureView()
                bpm.setStringPicture(" bpm")
                heart.add(bpm)
            }
            heart.setTextAttrForAll(
                    settings.heart_rateFontSize,
                    settings.heart_rateColor,
                    getTypeFace(service!!.resources, settings.font)
            )
            // Position based on screen on
            heart.alignX = 2
            heart.alignY = 0
            tmp_left = settings.heart_rateLeft.toInt()
            if (!settings.heart_rateAlignLeft) { // If text is centered, set rectangle
                heart.setRect(
                        (2 * tmp_left + 640),
                        (settings.font_ratio.toFloat() / 100 * settings.heart_rateFontSize).toInt()
                )
                tmp_left = -320
            }
            heart.setStart(
                    tmp_left,
                    (settings.heart_rateTop - settings.font_ratio.toFloat() / 100 * settings.heart_rateFontSize).toInt()
            )
            slpt_objects.add(heart)
        }
        // Draw heart rate bar
        if (settings.heart_rateProg > 0 && settings.heart_rateProgType == 0) { // Draw background image
            if (settings.heart_rateProgBgBool) {
                val ring_background = SlptPictureView()
                ring_background.setImagePicture(SimpleFile.readFileFromAssets(service, (if (betterResolution) "" else "slpt_") + "circles/ring1_bg.png"))
                ring_background.setStart((settings.heart_rateProgLeft - settings.heart_rateProgRadius).toInt(), (settings.heart_rateProgTop - settings.heart_rateProgRadius).toInt())
                slpt_objects.add(ring_background)
            }
            //if(heartRate==null){heartRate = new HeartRate(0);}
            val localSlptArcAnglePicView = SlptArcAnglePicView()
            localSlptArcAnglePicView.setImagePicture(SimpleFile.readFileFromAssets(service, (if (betterResolution) "" else "slpt_") + settings.heart_rateProgSlptImage))
            localSlptArcAnglePicView.setStart((settings.heart_rateProgLeft - settings.heart_rateProgRadius).toInt(), (settings.heart_rateProgTop - settings.heart_rateProgRadius).toInt())
            localSlptArcAnglePicView.start_angle = if (settings.heart_rateProgClockwise == 1) settings.heart_rateProgStartAngle else settings.heart_rateProgEndAngle
            localSlptArcAnglePicView.len_angle = (angleLength!! * Math.min(settings.temp_heart_rate / maxHeartRate, 1f)).toInt()
            //Log.w("VergeIT-LOG", "Heart rate: slpt "+settings.temp_heart_rate+", Sweep angle:"+ heart_rateSweepAngle+", %"+(this.angleLength * Math.min(settings.temp_heart_rate/this.maxHeartRate,1)));
            localSlptArcAnglePicView.full_angle = if (settings.heart_rateProgClockwise == 1) angleLength else -angleLength
            localSlptArcAnglePicView.draw_clockwise = settings.heart_rateProgClockwise
            slpt_objects.add(localSlptArcAnglePicView)
        }
        return slpt_objects
    }

    companion object {
        private const val TAG = "VergeIT-LOG"
    }

}