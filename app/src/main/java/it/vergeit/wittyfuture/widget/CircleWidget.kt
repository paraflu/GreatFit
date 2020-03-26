package it.vergeit.wittyfuture.widget

import android.graphics.*
import android.util.Log
import it.vergeit.wittyfuture.settings.LoadSettings
import it.vergeit.wittyfuture.theme.bin.ClockHand
import it.vergeit.wittyfuture.theme.bin.ICircle
import kotlin.math.abs

abstract class CircleWidget() : TextWidget() {

    abstract var ring: Paint?
    abstract var ringBmp: Bitmap?

    constructor(_settings: LoadSettings) : this() {
        settings = _settings
    }

    protected fun calcAngle(circle: ICircle): Int {
        return calcAngle(circle.startAngle!!, circle.endAngle!!)
    }

    protected fun calcAngle(startAngle: Int, endAngle: Int): Int {
        val start: Float = parseAngle(startAngle.toFloat())
        val end: Float = parseAngle(endAngle.toFloat())
//        val res = /*if (start > end) (end % 360f) - (start % 360f) else (start % 360f) - (end % 360f)*/ (start % 360) - (end % 360);
//        return res.toInt()
//        val res = if (start > end) {
//            if (end < start)
//                360 - (start - end)
//            else
//                end - start
//        } else {
//            if (end > start)
//                360 - (start - end)
//            else
//                end - start
//        }
        val res = end - start
        return res.toInt()
    }

    private fun parseAngle(angle: Float): Float {
        if (abs(angle) > 360) {
            return angle / 100f
        }
        return angle;
    }

    protected fun drawProgress(canvas: Canvas, bmp: Bitmap, clockHand: ClockHand, progressAngle: Float) {
        val centerScreen = if (settings.isVerge) Point(180, 179) else Point(160, 159)

        val centerPoint = Point(centerScreen.x + clockHand.centerOffset.x,
                centerScreen.y + clockHand.centerOffset.y)
        val count = canvas.save()
        val degree = parseAngle(clockHand.sector.startAngle.toFloat()) - progressAngle;
        canvas.rotate(degree                /*degree*/, centerPoint.x.toFloat(), centerPoint.y.toFloat())
        canvas.drawBitmap(bmp,
                (centerPoint.x - clockHand.image.x).toFloat(),
                (centerPoint.y - clockHand.image.y).toFloat(), null)
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