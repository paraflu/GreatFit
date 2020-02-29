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

class HeartRateWidget() : CircleWidget() {

    override var ring: Paint? = null
    override var ringBmp: Bitmap? = null

    constructor(_settings: LoadSettings) : this() {
        settings = _settings
    }

    private var caloriesSweepAngle = 0f

    private val textPaint: TextPaint? = null
    private var heartRate: HeartRate? = null
    private var heartRateSweepAngle = 0f
    private val lastSlptUpdateHeart_rate = 0
    private var angleLength: Int = 0
    private val maxHeartRate = 200f

    override fun init(service: Service) {
        super.init(service)

        if (settings.theme.activity?.pulseMeter != null) {
            val circle = settings.theme.activity!!.pulseMeter!!
            angleLength = calcAngle(circle)
            if (circle.imageIndex == null) {
                ring = Paint(Paint.ANTI_ALIAS_FLAG)
                ring!!.strokeCap = Paint.Cap.ROUND
                ring!!.style = Paint.Style.STROKE
                ring!!.strokeWidth = settings.theme.activity!!.pulseMeter!!.width!!.toFloat()
            } else {
                ringBmp = getBitmap(circle.imageIndex)
            }
        }
    }

    // Register update listeners
    override fun getDataTypes(): List<DataType> {
        return listOf(DataType.HEART_RATE)
    }

    // Value updater
    override fun onDataUpdate(type: DataType, value: Any) { // Heart rate class
        heartRate = value as HeartRate
        heartRateSweepAngle = angleLength * (heartRate!!.heartRate / settings.target_calories).coerceAtMost(1f)
    }

    // Draw screen-on
    override fun draw(canvas: Canvas?, width: Float, height: Float, centerX: Float, centerY: Float) { // Draw heart rate element
        if (heartRate == null) {
            return
        }

        if (settings.theme.activity?.pulse != null) { // Draw icon
            drawText(canvas!!, heartRate!!.heartRate, settings.theme.activity!!.pulse!!)
        }
        val scale = settings.theme.activity?.pulseMeter

        if (ring != null) {
            drawRing(canvas!!, scale!!, ring!!, heartRateSweepAngle)
        }

        if (ringBmp != null) {
            drawCircle(canvas!!, scale!!, ringBmp!!, heartRateSweepAngle)
//            Log.d(TAG, "stepsWidget ${ringBmp!!.width}")
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
        return null;
        /*var betterResolution = better_resolution
        betterResolution = betterResolution && settings.better_resolution_when_raising_hand
        mService = service!!
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
         */
    }

    companion object {
        private const val TAG = "VergeIT-LOG"
    }

}