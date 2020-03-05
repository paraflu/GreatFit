package it.vergeit.watchface.widget

import android.app.Service
import android.graphics.Canvas
import android.util.Log
import it.vergeit.watchface.settings.LoadSettings
import it.vergeit.watchface.theme.bin.Date
import it.vergeit.watchface.theme.bin.MonthAndDay
import it.vergeit.watchface.theme.bin.WeekDay
import com.ingenic.iwds.slpt.view.core.SlptViewComponent
import java.util.*

class DateWidget() : TextWidget() {

    constructor(_settings: LoadSettings) : this() {
        settings = _settings
    }

    private var calendar: Calendar = Calendar.getInstance()
    private var date: Date? = null
    private var lastDate: String? = null

    override fun init(service: Service) {
        mService = service
        date = settings.theme.date
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

    private fun drawWeekday(canvas: Canvas, weekDay: Int, spec: WeekDay) {
        val bmp = getBitmap(spec.imageIndex + weekDay)
        canvas.drawBitmap(bmp, spec.x.toFloat(), spec.y.toFloat(), settings.mGPaint)
    }

    override fun draw(canvas: Canvas?, width: Float, height: Float, centerX: Float, centerY: Float) {
        val date = settings.theme.date;
        if (date != null) {
            if (date.monthAndDay != null) {
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                drawMonthDay(canvas!!, month, day, date.monthAndDay)
            }
            if (date.weekDay != null) {
                val calendar = Calendar.getInstance()
                var weekDay = calendar[Calendar.DAY_OF_WEEK]
                if (weekDay == Calendar.SUNDAY) {
                    weekDay = 6
                } else {
                    weekDay -= 2
                }
                drawWeekday(canvas!!, weekDay, date.weekDay)
            }
        }
    }

    private fun drawMonthDay(canvas: Canvas, month: Int, day: Int, monthAndDay: MonthAndDay) {
        if (monthAndDay.separate != null) {
            val monthNameSpec = monthAndDay.separate.monthName
            val monthSpec = monthAndDay.separate.month
            val daySpec = monthAndDay.separate.day
            if (monthNameSpec?.imageIndex != null) {
                drawBitmap(canvas, monthNameSpec.imageIndex + month, monthNameSpec.x, monthNameSpec.y)
            }

            if (monthSpec != null) {
                drawText(canvas, String.format("%02d", month), monthSpec, monthAndDay.twoDigitsDay == false)
            }

            if (daySpec != null) {
                drawText(canvas, String.format("%02d", day), daySpec, monthAndDay.twoDigitsDay == false)
            }
        }
    }
}