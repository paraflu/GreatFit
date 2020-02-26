package com.dinodevs.greatfitwatchface.widget

import android.app.Service
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import android.util.Size
import com.dinodevs.greatfitwatchface.settings.LoadSettings
import com.dinodevs.greatfitwatchface.theme.bin.Date
import com.dinodevs.greatfitwatchface.theme.bin.MonthAndDay
import com.dinodevs.greatfitwatchface.theme.bin.Separate
import com.huami.watch.watchface.util.Util
import com.ingenic.iwds.slpt.view.core.SlptViewComponent
import java.time.LocalDate
import java.util.*
import kotlin.math.roundToInt

class DateWidget(private val settings: LoadSettings) : TextWidget(settings) {

    private var calendar: Calendar? = null
    private var mService: Service? = null
    private var date: Date? = null
    private var lastDate: String? = null
    
    override fun init(service: Service) {
        mService = service
        date = settings.theme.date
        calendar= Calendar.getInstance()
    }

    override fun buildSlptViewComponent(service: Service?): List<SlptViewComponent?>? {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        Log.d(TAG, "not implemented buildSlptViewComponent")
        return null
    }

    override fun buildSlptViewComponent(service: Service?, better_resolution: Boolean): List<SlptViewComponent?>? {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return null
    }

    override fun draw(canvas: Canvas?, width: Float, height: Float, centerX: Float, centerY: Float) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        Log.d(TAG, "not implemented buildSlptViewComponent")
//        Util.formatDate()
        /*var currentDate = Util.formatTime(calendar.)
        if (lastDate == null || lastDate != currentDate){}*/
    }
//    final lateinit var mService: Service
//
//    var date: Date? = null;
//    var bitmaps: List<Bitmap>? = null
//
//    var lastWeek = -1
//    var lastDay: String? = null
//
//    override fun init(service: Service?) {
//        mService = service!!
//        date = settings.theme.date
//        if (date != null) {
//            if (date!!.weekDay != null) {
//                // load weekdays
//                bitmaps = (0..7).map {
//                    Util.decodeImage(service.resources, settings.getImagePath(date!!.weekDay!!.imageIndex + it))
//                }
//            }
//        }
//    }
//
//    private fun drawText(canvas: Canvas, value: Int, text: Month) {
//        val width = text.bottomRightX - text.topLeftX
//        val height = text.bottomRightY - text.topLeftY
//        val spacing = (width / text.imagesCount.toFloat()).roundToInt() + text.spacing
//        var x = text.topLeftX
//        val y = text.topLeftY
//        val bmp = Util.decodeImage(mService!!.resources, settings.getImagePath(settings.theme.battery.text!!.imageIndex))
//        val imageSize = Size(bmp.width, bmp.height)
//
//        val textString = String.format("%d", value)
//        if (text.alignment == "Center") {
//            x += (width /2f - textString.length * imageSize.width / 2f).roundToInt()
//        }
//        Log.d(TAG, String.format("draw value %s", textString))
//        for (i in textString.toCharArray().indices) {
//            val charToPrint = textString.toCharArray()[i]
//            val va = charToPrint - '0'
//            Log.d(TAG, String.format("draw char (x: %d, y: %d) %d - %c > bmp %s", x, y, i, charToPrint, settings.getImagePath(settings.theme.battery.text!!.imageIndex + va)))
//            val charBmp = Util.decodeImage(mService!!.resources, settings.getImagePath(settings.theme.battery.text!!.imageIndex + va))
//            canvas.drawBitmap(charBmp, x.toFloat(), y.toFloat(), settings.mGPaint)
//            x += charBmp.width + spacing
//        }
//        Log.d(TAG, "complete")
//    }
//
//    override fun onDrawDigital(canvas: Canvas?, width: Float, height: Float, centerX: Float, centerY: Float, seconds: Int, minutes: Int, hours: Int, year: Int, month: Int, day: Int, week: Int, ampm: Int) {
//        if (canvas != null) {
////            if (lastWeek != week) {
////                canvas.drawBitmap(bitmaps!![week], date!!.weekDay!!.x.toFloat(), date!!.weekDay!!.y.toFloat(), settings.mGPaint)
////                lastWeek = week
////            }
////
////            val calendar = Calendar.getInstance()
////
////            var dateStr = Util.formatTime(day) + "." + Util.formatTime(month) + "." + Integer.toString(year)
////            if (lastDay == null || lastDay != dateStr) {
////                drawText(canvas, month, )
////            }
//        }
//    }
//
//    override fun buildSlptViewComponent(service: Service?): List<SlptViewComponent?>? {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun buildSlptViewComponent(service: Service?, better_resolution: Boolean): List<SlptViewComponent?>? {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    companion object {
//        private const val TAG = "VergeIT-LOG"
//    }
}