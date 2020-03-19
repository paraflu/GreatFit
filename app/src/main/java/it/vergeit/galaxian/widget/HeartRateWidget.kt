package it.vergeit.galaxian.widget

import android.app.Service
import android.graphics.*
import android.text.TextPaint
import it.vergeit.galaxian.data.DataType
import it.vergeit.galaxian.data.HeartRate
import it.vergeit.galaxian.settings.LoadSettings
import com.ingenic.iwds.slpt.view.core.SlptViewComponent
import com.ingenic.iwds.slpt.view.sport.SlptLastHeartRateView
import java.util.ArrayList

class HeartRateWidget() : CircleWidget() {

    private var angleGraph: Int = 0
    private var heartGraphSweepAngle = 0f
    private var progressBmp: Bitmap? = null
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
                ring!!.color = Color.parseColor(String.format("#%s", circle.color!!.substring(12)))
            } else {
                ringBmp = getBitmap(circle.imageIndex)
            }
        }
        if (settings.theme.activity?.pulseGraph?.clockHand != null) {
            val clockHand = settings.theme.activity!!.pulseGraph!!.clockHand;
            angleGraph = calcAngle(clockHand.sector.startAngle, clockHand.sector.endAngle)
            progressBmp = getBitmap(clockHand.image.imageIndex);
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
        if (settings.theme.activity?.pulseGraph?.clockHand != null) {
            heartGraphSweepAngle = angleGraph * (heartRate!!.heartRate / settings.target_calories).coerceAtMost(1f)
        }
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
        val clockHand = settings.theme.activity?.pulseGraph?.clockHand

        if (ring != null) {
            drawRing(canvas!!, scale!!, ring!!, heartRateSweepAngle)
        }

        if (ringBmp != null) {
            drawCircle(canvas!!, scale!!, ringBmp!!, heartRateSweepAngle)
//            Log.d(TAG, "stepsWidget ${ringBmp!!.width}")
        }

        if (progressBmp != null) {
            drawProgress(canvas!!, progressBmp!!, clockHand!!, heartGraphSweepAngle)
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
        mService = service!!
        val slptObjects: MutableList<SlptViewComponent?> = ArrayList()

        val pulse = settings.theme.activity!!.pulse!!
        if (settings.theme.activity?.pulse != null) { // Draw icon
            var heartView = SlptLastHeartRateView()
            heartView.setImagePictureArray(getBitmapSlptArray(pulse.imageIndex, pulse.imagesCount, better_resolution))
            heartView.setStart(pulse.topLeftX, pulse.topLeftY)
            heartView.setRect(pulse.bottomRightX, pulse.bottomRightY)
            slptObjects.add(heartView)
        }

        return slptObjects;
    }

    companion object {
        private const val TAG = "VergeIT-LOG"
    }

}