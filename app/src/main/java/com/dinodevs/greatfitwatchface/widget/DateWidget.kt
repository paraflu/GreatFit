package com.dinodevs.greatfitwatchface.widget

import android.app.Service
import android.graphics.Canvas
import android.graphics.Point
import android.util.Log
import com.dinodevs.greatfitwatchface.settings.LoadSettings
import com.dinodevs.greatfitwatchface.theme.bin.Date
import com.dinodevs.greatfitwatchface.theme.bin.MonthAndDay
import com.dinodevs.greatfitwatchface.theme.bin.WeekDay
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

    fun drawWeekday(canvas: Canvas, weekDay: Int, spec: WeekDay) {
        val bmp = getBitmap(spec.imageIndex + weekDay)
        canvas.drawBitmap(bmp, spec.x.toFloat(), spec.y.toFloat(), settings.mGPaint)
    }

    override fun draw(canvas: Canvas?, width: Float, height: Float, centerX: Float, centerY: Float) {
        val now = Date()
        val date = settings.theme.date;
        if (date != null) {
            if (date.monthAndDay != null) {
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                drawMonthDay(canvas!!, month, day, date.monthAndDay)
            }
            if (date.weekDay != null) {
                val calendar = Calendar.getInstance()
                val weekDay = calendar[Calendar.DAY_OF_WEEK]
                drawWeekday(canvas!!, weekDay, date.weekDay)
            }
        }
    }

    private fun drawMonthDay(canvas: Canvas, month: Int, day: Int, monthAndDay: MonthAndDay) {
        if (monthAndDay.separate != null) {
            val monthSpec = monthAndDay.separate.monthName
            val daySpec = monthAndDay.separate.day
            if (monthSpec != null) {
                drawBitmap(canvas, monthSpec.imageIndex + month, monthSpec.x, monthSpec.y)
            }

            if (daySpec != null) {
                drawText(canvas, day, daySpec)
            }
        }
    }
}