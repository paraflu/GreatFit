package it.vergeit.watchface.widget

import android.app.Service
import android.graphics.*
import android.util.Log
import com.huami.watch.watchface.util.Util
import com.ingenic.iwds.slpt.view.analog.SlptAnalogMinuteView
import com.ingenic.iwds.slpt.view.analog.SlptAnalogTimeView
import com.ingenic.iwds.slpt.view.core.SlptViewComponent
import it.vergeit.watchface.Screen
import it.vergeit.watchface.data.DataType
import it.vergeit.watchface.resource.SlptAnalogHourView
import it.vergeit.watchface.settings.LoadSettings
import it.vergeit.watchface.theme.bin.AnalogDialFace
import it.vergeit.watchface.theme.bin.Image
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

    private fun handSlpt(it: SlptAnalogTimeView, image: Image, better_resolution: Boolean) {
        val bmp = getBitmap(image.imageIndex, true, better_resolution)
        val full = Bitmap.createBitmap(bmp.width * 2, bmp.height * 2, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(full)
        val centerY = full.height / 2f
        val centerX = full.width / 2f
        val y = centerY - image.y
        val x = centerX - image.x

        canvas.drawBitmap(bmp, /*analog.hours.image.x.toFloat(), analog.hours.image.y.toFloat()*/
                x, y, null)
        val byteImage = Util.Bitmap2Bytes(full)
        it.setImagePicture(byteImage)
        it.alignX = 2.toByte()
        it.alignY = 2.toByte()
        it.setRect(Screen.width + if (settings.isVerge) 40 else 0, Screen.height + if (settings.isVerge) 40 else 0)
    }

    override fun buildSlptViewComponent(service: Service?, better_resolution: Boolean): List<SlptViewComponent?>? {
        val slptObjects = ArrayList<SlptViewComponent?>()

        // SLPT only clock
        mService = service!!

        val analog = settings.theme.analogDialFace

        if (analog != null) {
            val hourView = SlptAnalogHourView().also {
                handSlpt(it, analog.hours!!.image, better_resolution)
            }

            slptObjects.add(hourView)

            val minuteView = SlptAnalogMinuteView().also {
                handSlpt(it, analog.minutes!!.image, better_resolution)
            }

            slptObjects.add(minuteView)

            if (settings.secondsBool && analog.seconds != null) {

                val secondView = SlptAnalogHourView().also {
                    handSlpt(it, analog.seconds.image, better_resolution)
                }
                slptObjects.add(secondView)
            }
        }
        return slptObjects
    }

}