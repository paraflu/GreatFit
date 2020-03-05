package it.vergeit.watchface.widget

import android.app.Service
import android.graphics.Canvas

abstract class AnalogClockWidget : ClockWidget {
    override fun init(service: Service?) {}
    abstract fun onDrawAnalog(canvas: Canvas?, width: Float, height: Float, centerX: Float, centerY: Float, secRot: Float, minRot: Float, hrRot: Float)
}