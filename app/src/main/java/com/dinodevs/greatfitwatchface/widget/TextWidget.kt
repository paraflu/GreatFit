package com.dinodevs.greatfitwatchface.widget

import android.app.Service
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Point
import android.util.Size
import com.dinodevs.greatfitwatchface.data.DataType
import com.dinodevs.greatfitwatchface.settings.LoadSettings
import com.dinodevs.greatfitwatchface.theme.bin.IText
import com.huami.watch.watchface.util.Util
import com.ingenic.iwds.slpt.view.core.SlptViewComponent
import kotlin.math.roundToInt


open class TextWidget() : AbstractWidget() {

    protected lateinit var settings: LoadSettings

    companion object {
        const val TAG = "VergeIT-LOG"
    }

    protected lateinit var mService: Service

    protected constructor(_settings: LoadSettings) : this() {
        settings = _settings
    }

    override fun buildSlptViewComponent(service: Service?): List<SlptViewComponent?>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun buildSlptViewComponent(service: Service?, better_resolution: Boolean): List<SlptViewComponent?>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun draw(canvas: Canvas?, width: Float, height: Float, centerX: Float, centerY: Float) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun init(service: Service) {
        mService = service
    }

    override fun onDataUpdate(type: DataType, value: Any) {
        super.onDataUpdate(type, value)
    }

    /**
     * drawText on canvas
     *
     * @param canvas Canvas to contain draw
     * @param value Digit to write
     * @param spec Coordinates
     */
    protected fun drawText(canvas: Canvas, value: Int, spec: IText) {
        val width = spec.bottomRightX - spec.topLeftX
        val height = spec.bottomRightY - spec.topLeftY
        val spacing = (width / spec.imagesCount.toFloat()).roundToInt() + spec.spacing
        var x = spec.topLeftX
        val y = spec.topLeftY
        val bmp: Bitmap = getBitmap(spec.imageIndex)
        val imageSize = Size(bmp.width, bmp.height)
        val valueToPrint = value.toString()

        // la lunghezza della stringa una volta stampata
        // numero di caratteri x dimensione dell'immagine più eventuale spaziatura
        val stringLength = valueToPrint.length * imageSize.width + (valueToPrint.length - 1) * spacing
        val startPoint: Point = when (spec.alignment) {
            "Center" -> {
                val centerX = (width / 2f).roundToInt()
                val centerY = (height / 2f).roundToInt()
                Point(x + centerX - stringLength, y + centerY - imageSize.height)
            }
            "TopRight" -> Point(x + width - stringLength, y)
            else -> Point(x, y)
        }
        for (i in valueToPrint.toCharArray().indices) {
            val charToPrint = valueToPrint.toCharArray()[i]
            val va = charToPrint - '0'
            val charBmp: Bitmap = getBitmap(spec.imageIndex + va)
            canvas.drawBitmap(charBmp, startPoint.x.toFloat(), startPoint.y.toFloat(), settings.mGPaint)
            startPoint.x += charBmp.width + spacing
        }
    }

    protected fun getBitmap(imageIdx: Int): Bitmap = Util.decodeImage(mService.resources, settings.getImagePath(imageIdx))

    protected fun drawBitmap(canvas: Canvas, imageIdx: Int, x: Int, y: Int) {
        drawBitmap(canvas, imageIdx, Point(x, y))
    }

    private fun drawBitmap(canvas: Canvas, imageIdx: Int, point: Point) {
        canvas.drawBitmap(getBitmap(imageIdx), point.x.toFloat(), point.y.toFloat(), settings.mGPaint)
    }

}