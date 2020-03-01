package com.dinodevs.greatfitwatchface.widget

import android.graphics.*
import android.util.Log
import com.dinodevs.greatfitwatchface.settings.LoadSettings
import com.dinodevs.greatfitwatchface.theme.bin.ICircle
import com.dinodevs.greatfitwatchface.theme.bin.Unknown4

abstract class CircleWidget() : TextWidget() {

    abstract var ring: Paint?
    abstract var ringBmp: Bitmap?

    constructor(_settings: LoadSettings) : this() {
        settings = _settings
    }

    protected fun calcAngle(circle: ICircle): Int {
        return calcAngle(circle.startAngle!!, circle.endAngle!!)
    }

    private fun calcAngle(startAngle: Int, endAngle: Int): Int {
        return if (startAngle > endAngle) (endAngle % 360) - (startAngle % 360) else (startAngle!! % 360) - (endAngle!! % 360)
    }

    protected fun drawProgress(canvas: Canvas, unknown4: Unknown4, batterySweepAngle: Float) {
        val count = canvas.save()
        val bmp = getBitmap(unknown4.image.imageIndex)
        val angle = calcAngle(unknown4.sector.startAngle, unknown4.sector.endAngle)
        canvas.rotate(angle.toFloat(), unknown4.centerOffset.x.toFloat(), unknown4.centerOffset.y.toFloat())
        canvas.drawBitmap(bmp, unknown4.image.x.toFloat(), unknown4.image.y.toFloat(), settings.mGPaint)
        canvas.restoreToCount(count)
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

    protected fun drawCircle(canvas: Canvas, circle: ICircle, ring: Bitmap, sweepAngle: Float) {
        val count = canvas.save()
        canvas.rotate(0f,
                circle.centerX!!.toFloat(), circle.centerY!!.toFloat())
//        canvas.drawBitmap(applyPieMask(ring, circle.startAngle.toFloat(), sweepAngle),
//                circle.centerX.toFloat(), circle.centerY.toFloat(), settings.mGPaint)
        canvas.drawBitmap(
                applyPieMask(ring, circle.startAngle!! - 90f, sweepAngle),
                // ring,
                circle.centerX!!.toFloat() - ring.width / 2f,
                circle.centerY!!.toFloat() - ring.height / 2f, settings.mGPaint)
        canvas.restoreToCount(count)
    }

    protected fun drawRing(canvas: Canvas, circle: ICircle, ring: Paint, sweepAngle: Float) {
        val count = canvas.save()
        // Rotate canvas to 0 degrees = 12 o'clock
        canvas.rotate(-90f, circle.centerX!!.toFloat(), circle.centerY!!.toFloat())
        // Define circle
        val radius = circle.radiusX!!.toFloat() /*- scale.getWidth()*/
        val oval = RectF(circle.centerX!! - radius, circle.centerY!! - radius,
                circle.centerX!! + radius,
                circle.centerY!! + radius)
        canvas.drawArc(oval, circle.startAngle!!.toFloat(), sweepAngle, false, ring)
        canvas.restoreToCount(count)
    }
}