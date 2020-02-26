package com.dinodevs.greatfitwatchface.widget

import android.graphics.*
import android.util.Log
import com.dinodevs.greatfitwatchface.settings.LoadSettings
import com.dinodevs.greatfitwatchface.theme.bin.Circle

open class CircleWidget() : TextWidget() {

    protected var ring: Paint? = null
    protected var ringBmp: Bitmap? = null

    constructor(_settings: LoadSettings) : this() {
        settings = _settings
    }

    private fun applyPieMask(src: Bitmap, startAngle: Float, sweepAngle: Float): Bitmap {
        val width = src.width
        val height = src.height
        //create bitmap mask with the same dimension of the src bitmap
        val mask = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(mask)
        canvas.drawColor(0x00000000) //fill mask bitmap with transparent black!
        //init mask paint
        val maskPaint = Paint()
        maskPaint.color = -0x1 //pick highest value for bitwise AND operation
        maskPaint.isAntiAlias = true
        //choose entire bitmap as a rect
        val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
        Log.d(TAG, "drawArc sweepAngle $sweepAngle w:$width h:$height")
        canvas.drawArc(rect, startAngle, sweepAngle, true, maskPaint) //mask the pie
        val result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        for (i in 0 until height) {
            for (j in 0 until width) { //combine src color and mask to gain the result color
                val color = mask.getPixel(i, j) and src.getPixel(i, j)
                result.setPixel(i, j, color)
            }
        }
        return result
    }

    protected fun drawCircle(canvas: Canvas, circle: Circle, ring: Bitmap, sweepAngle: Float) {
        val count = canvas.save()
        canvas.rotate(-90f, circle.centerX.toFloat(), circle.centerY.toFloat())
        canvas.drawBitmap(applyPieMask(ring, 0f, sweepAngle),
                circle.centerX.toFloat(), circle.centerY.toFloat(), settings.mGPaint)
        canvas.restoreToCount(count)
    }

    protected fun drawRing(canvas: Canvas, circle: Circle, ring: Paint, sweepAngle: Float) {
        val count = canvas.save()
        // Rotate canvas to 0 degrees = 12 o'clock
        canvas.rotate(-90f, circle.centerX.toFloat(), circle.centerY.toFloat())
        // Define circle
        val radius = circle.radiusX.toFloat() /*- scale.getWidth()*/
        val oval = RectF(circle.centerX - radius, circle.centerY - radius,
                circle.centerX + radius,
                circle.centerY + radius)
        canvas.drawArc(oval, circle.startAngle.toFloat(), sweepAngle, false, ring)
        canvas.restoreToCount(count)
    }
}