package it.vergeit.watchface.widget

import android.app.Service
import android.graphics.Canvas

abstract class DigitalClockWidget : ClockWidget {
    override fun init(service: Service?) {}
    abstract fun onDrawDigital(canvas: Canvas?, width: Float, height: Float, centerX: Float, centerY: Float, seconds: Int, minutes: Int, hours: Int, year: Int, month: Int, day: Int, week: Int, ampm: Int)
}