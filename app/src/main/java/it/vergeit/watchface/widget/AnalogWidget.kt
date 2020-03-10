package it.vergeit.watchface.widget

import android.app.Service
import android.graphics.*
import android.util.Log
import com.huami.watch.watchface.util.Util
import com.ingenic.iwds.slpt.view.analog.SlptAnalogMinuteView
import com.ingenic.iwds.slpt.view.analog.SlptAnalogSecondView
import com.ingenic.iwds.slpt.view.analog.SlptAnalogTimeView
import com.ingenic.iwds.slpt.view.core.SlptViewComponent
import it.vergeit.watchface.Screen
import it.vergeit.watchface.data.DataType
import it.vergeit.watchface.resource.SlptAnalogHourView
import it.vergeit.watchface.settings.LoadSettings
import it.vergeit.watchface.theme.bin.AnalogDialFace
import java.util.*
import kotlin.collections.ArrayList


class AnalogWidget() : TextWidget() {
    private var hourHand: Bitmap? = null
    private var minuteHand: Bitmap? = null
    private var secondsHand: Bitmap? = null
    private var hourCenterBmp: Bitmap? = null
    private var minCenterBmp: Bitmap? = null

    companion object {
        private const val TAG = "VergeIT-LOG"
    }

    constructor(_settings: LoadSettings) : this() {
        settings = _settings
    }

    override fun init(service: Service) {
        super.init(service)
        if (settings.theme.analogDialFace != null) {
            val cfg = settings.theme.analogDialFace!!
            Log.d(TAG, "theme ")
            if (cfg.hours != null) {
                hourHand = getBitmap(cfg.hours.image.imageIndex)
            }
            if (cfg.minutes != null) {
                minuteHand = getBitmap(cfg.minutes.image.imageIndex)
            }
            if (cfg.seconds != null) {
                secondsHand = getBitmap(cfg.seconds.image.imageIndex)
            }
            if (cfg.hourCenterImage != null) {
                hourCenterBmp = getBitmap(cfg.hourCenterImage.imageIndex)
                Log.d(TAG, "loadImage hour ${cfg.hourCenterImage.imageIndex}")
            }
            if (cfg.minCenterImage != null) {
                minCenterBmp = getBitmap(cfg.minCenterImage.imageIndex)
                Log.d(TAG, "loadImage min ${cfg.minCenterImage.imageIndex}")
            }
        }
    }

    override fun draw(canvas: Canvas?, width: Float, height: Float, centerX: Float, centerY: Float) {
        if (settings.theme.analogDialFace != null) {
            val cfg = settings.theme.analogDialFace!!
            if (minCenterBmp != null) {
                canvas!!.drawBitmap(minCenterBmp!!, cfg.minCenterImage!!.x.toFloat(), cfg.minCenterImage.y.toFloat(), settings.mGPaint)
            }
            if (hourCenterBmp != null) {
                canvas!!.drawBitmap(hourCenterBmp!!, cfg.hourCenterImage!!.x.toFloat(), cfg.hourCenterImage.y.toFloat(), settings.mGPaint)
            }
            val calendar = Calendar.getInstance()
            val hours = calendar[Calendar.HOUR_OF_DAY]
            val minutes = calendar[Calendar.MINUTE]
            val seconds = calendar[Calendar.SECOND]

            drawAnalogClock(canvas!!, cfg, hours, minutes, seconds)
        }
    }

    private fun drawAnalogClock(canvas: Canvas, analogDialFace: AnalogDialFace, hours: Int, minutes: Int, seconds: Int) {
        val centerScreen = if (settings.isVerge) Point(180, 179) else Point(160, 159)
        if (analogDialFace.hours != null) {
            val centerPoint = Point(centerScreen.x + analogDialFace.hours.centerOffset.x,
                    centerScreen.y + analogDialFace.hours.centerOffset.y)
            canvas.save()
            canvas.rotate((hours * 30).toFloat() + minutes.toFloat() / 60.0f * 30.0f, centerPoint.x.toFloat(), centerPoint.y.toFloat())
            canvas.drawBitmap(hourHand!!,
                    (centerPoint.x - analogDialFace.hours.image.x).toFloat(),
                    (centerPoint.y - analogDialFace.hours.image.y).toFloat(), null)
            canvas.restore()
        }

        if (analogDialFace.minutes != null) {
            val centerPoint = Point(centerScreen.x + analogDialFace.minutes.centerOffset.x,
                    centerScreen.y + analogDialFace.minutes.centerOffset.y)
            canvas.save()
            canvas.rotate((minutes * 6).toFloat(), centerScreen.x.toFloat(), centerScreen.y.toFloat())
            canvas.drawBitmap(minuteHand!!,
                    (centerPoint.x - analogDialFace.minutes.image.x).toFloat(),
                    (centerPoint.y - analogDialFace.minutes.image.y).toFloat(), null)
            canvas.restore()
        }
        if (analogDialFace.seconds != null) {
            val centerPoint = Point(centerScreen.x + analogDialFace.seconds.centerOffset.x,
                    centerScreen.y + analogDialFace.seconds.centerOffset.y)
            canvas.save()
            canvas.rotate((seconds * 6).toFloat(), centerScreen.x.toFloat() + analogDialFace.seconds.centerOffset.x, centerScreen.y.toFloat() + analogDialFace.seconds.centerOffset.y)
            canvas.drawBitmap(secondsHand!!,
                    (centerPoint.x - analogDialFace.seconds.image.x).toFloat(),
                    (centerPoint.y - analogDialFace.seconds.image.y).toFloat(), null)
            canvas.restore()
        }
    }

    override fun getDataTypes(): List<DataType> {
        return listOf(DataType.TIME)
    }

    override fun onDataUpdate(type: DataType, value: Any) {
        super.onDataUpdate(type, value)
    }

    fun setHand(it: SlptAnalogTimeView, imageIndex: Int, better_resolution: Boolean) {
        val image = getBitmap(imageIndex, true, better_resolution)
        val full = Bitmap.createBitmap(image.width, 320 + if (settings.isVerge) 40 else 0, Bitmap.Config.ARGB_8888)
        var canvas = Canvas(full)
//        canvas.drawColor(Color.CYAN);
        canvas.drawBitmap(image, /*analog.hours.image.x.toFloat(), analog.hours.image.y.toFloat()*/ 0f, 0f, null)
        val byteImage = Util.Bitmap2Bytes(full)
        it.setImagePicture(byteImage)
        it.alignX = 2.toByte()
        it.alignY = 2.toByte()
        it.setRect(320 + if (settings.isVerge) 40 else 0, 320 + if (settings.isVerge) 40 else 0)
    }

    override fun buildSlptViewComponent(service: Service?, better_resolution: Boolean): List<SlptViewComponent?>? {
        val slptObjects = ArrayList<SlptViewComponent?>()

        // SLPT only clock
        mService = service!!

        val analog = settings.theme.analogDialFace

        if (settings.theme.analogDialFace != null) {
            val hourView = SlptAnalogHourView().also {
                setHand(it, analog!!.hours!!.image.imageIndex, better_resolution)
            }
//            val centerPoint = Screen.center
//            val hourView = SlptAnalogHourView().also {
//
//                val image = getBitmap(analog.hours!!.image.imageIndex, true, better_resolution)
//                val full = Bitmap.createBitmap(image.width, 320 + if (settings.isVerge) 40 else 0, Bitmap.Config.ARGB_8888)
//                var canvas = Canvas(full)
//                canvas.drawColor(Color.CYAN);
//                canvas.drawBitmap(image, /*analog.hours.image.x.toFloat(), analog.hours.image.y.toFloat()*/ 0f, 0f, null)
//                val byteImage = Util.Bitmap2Bytes(full)
//                it.setImagePicture(byteImage)
//                it.alignX = 2.toByte()
//                it.alignY = 2.toByte()
//                it.setRect(320 + if (settings.isVerge) 40 else 0, 320 + if (settings.isVerge) 40 else 0)
////                val full = Bitmap.createBitmap(Screen.width, Screen.height, Bitmap.Config.ARGB_8888)
////                var canvas = Canvas(full)
////                var x = analog.hours!!.image.x + (Screen.width / 2f)
////                val y = analog.hours!!.image.y + (Screen.height / 2f)
////                val img = getBitmap(analog.hours!!.image.imageIndex, true, better_resolution)
////                canvas.drawBitmap(img, analog.hours.image.x + (Screen.width / 2f),
////                        analog.hours.image.y + (Screen.height / 2f), null)
////                val paint = Paint()
////                paint.style = Paint.Style.FILL
////                paint.color = Color.YELLOW
////                paint.isAntiAlias = true
////                canvas.drawRect(Rect(0,0,310,310), paint)
////                canvas.drawColor(Color.LTGRAY);
////                it.setRect(320, 320)
//////                it.setStart(x.toInt(), y.toInt())
//////                it.setRect(Screen.width, Screen.height)
////
////                it.setImagePicture(Util.Bitmap2Bytes(full)/*getBitmapSlpt(analog.hours!!.image.imageIndex, better_resolution)*/)
////                it.alignX = 2.toByte()
////                it.alignY = 2.toByte()
//            }
            slptObjects.add(hourView)

//            val minuteView = SlptAnalogMinuteView().also {
//                val img = getBitmap(analog.minutes!!.image.imageIndex)
//                it.setRect(img.height, img.width)
////                it.setStart(centerPoint.x + analog.minutes!!.centerOffset.x + analog.minutes.image.x,
////                        centerPoint.y + analog.minutes.centerOffset.y + analog.minutes.image.y)
////                it.setRect(Screen.width, Screen.height)
//
//                it.setImagePicture(getBitmapSlpt(analog.minutes!!.image.imageIndex, better_resolution))
//                it.alignX = 2.toByte()
//                it.alignY = 2.toByte()
//            }

            val minuteView = SlptAnalogHourView().also {
                setHand(it, analog!!.minutes!!.image.imageIndex, better_resolution)
            }

            slptObjects.add(minuteView)

            if (analog!!.seconds != null) {
//                val secondView = SlptAnalogSecondView().also {
//                    val img = getBitmap(analog.seconds!!.image.imageIndex)
//                    it.setRect(img.height, img.width)
//
//                    it.setImagePicture(getBitmapSlpt(analog.seconds!!.image.imageIndex, better_resolution))
//                    it.alignX = 2.toByte()
//                    it.alignY = 2.toByte()
//                }
                val secondView = SlptAnalogHourView().also {
                    setHand(it, analog!!.seconds!!.image.imageIndex, better_resolution)
                }
                slptObjects.add(secondView)
            }
//            if (settings.analog_clock) {
//                val slptAnalogHourView = SlptAnalogHourView()
//                slptAnalogHourView.setImagePicture(SimpleFile.readFileFromAssets(service, "timehand/8c/hour" + (if (settings.isVerge) "_verge" else "") + ".png"))
//                slptAnalogHourView.alignX = 2.toByte()
//                slptAnalogHourView.alignY = 2.toByte()
//                slptAnalogHourView.setRect(320 + if (settings.isVerge) 40 else 0, 320 + if (settings.isVerge) 40 else 0)
//                slpt_objects.add(slptAnalogHourView)
//                val slptAnalogMinuteView = SlptAnalogMinuteView()
//                slptAnalogMinuteView.setImagePicture(SimpleFile.readFileFromAssets(service, "timehand/8c/minute" + (if (settings.isVerge) "_verge" else "") + ".png"))
//                slptAnalogMinuteView.alignX = 2.toByte()
//                slptAnalogMinuteView.alignY = 2.toByte()
//                slptAnalogMinuteView.setRect(320 + if (settings.isVerge) 40 else 0, 320 + if (settings.isVerge) 40 else 0)
//                slpt_objects.add(slptAnalogMinuteView)
//                if (settings.secondsBool) {
//                    val slptAnalogSecondView = SlptAnalogSecondView()
//                    slptAnalogSecondView.setImagePicture(SimpleFile.readFileFromAssets(service, "timehand/8c/seconds" + (if (settings.isVerge) "_verge" else "") + ".png"))
//                    slptAnalogSecondView.alignX = 2.toByte()
//                    slptAnalogSecondView.alignY = 2.toByte()
//                    slptAnalogSecondView.setRect(320 + if (settings.isVerge) 40 else 0, 320 + if (settings.isVerge) 40 else 0)
//                    slpt_objects.add(slptAnalogSecondView)
//                }
//            }
        }
        return slptObjects
    }

}