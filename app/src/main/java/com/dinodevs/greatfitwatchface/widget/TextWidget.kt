package com.dinodevs.greatfitwatchface.widget

import android.app.Service
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Point
import android.util.Log
import android.util.Size
import com.dinodevs.greatfitwatchface.data.DataType
import com.dinodevs.greatfitwatchface.settings.LoadSettings
import com.dinodevs.greatfitwatchface.theme.bin.IImage
import com.dinodevs.greatfitwatchface.theme.bin.IText
import com.huami.watch.watchface.util.Util
import com.ingenic.iwds.slpt.view.core.SlptViewComponent
import kotlin.math.roundToInt


open class TextWidget() : AbstractWidget() {

    protected lateinit var settings: LoadSettings
    private var imageCache = mutableMapOf<Int, Bitmap>()

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
        mService = service!!
        return null
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
    protected fun drawText(canvas: Canvas, value: Int, spec: IText, skipZero: Boolean = false) {
        val width = spec.bottomRightX - spec.topLeftX
        val height = spec.bottomRightY - spec.topLeftY
        val spacing = (width / spec.imagesCount.toFloat()).roundToInt() + spec.spacing
        val x = spec.topLeftX
        val y = spec.topLeftY
        val bmp: Bitmap = getBitmap(spec.imageIndex)
        val imageSize = Size(bmp.width, bmp.height)
        val valueToPrint = value.toString()

        // la lunghezza della stringa una volta stampata
        // numero di caratteri x dimensione dell'immagine più eventuale spaziatura
        val stringLength = valueToPrint.length * imageSize.width + (valueToPrint.length - 1) * spacing
        val centerX = (width / 2f).roundToInt()
        val centerY = (height / 2f).roundToInt()
        val startPoint: Point = when (spec.alignment) {
            // #TopLeft, TopCenter, TopRight, Left, Center, Right, BottomLeft, BottomCenter, BottomRight
            "TopLeft" -> Point(x, y)
            "TopCenter" -> Point(x + centerX - (stringLength / 2f).roundToInt(), y)
            "TopRight" -> Point(x + width - stringLength, y)
            "Center" -> Point(x + centerX - (stringLength / 2f).roundToInt(), y + centerY - (imageSize.height / 2f).roundToInt())
            "Left" -> Point(x, y + centerY - imageSize.height)
            "Right" -> Point(x + width - stringLength, y + centerY - imageSize.height)
            "BottomLeft" -> Point(x, y + height - imageSize.height)
            "BottomRight" -> Point(x + width - stringLength, y + height - imageSize.height)
            else -> Point(x, y)
        }
        for (i in valueToPrint.toCharArray().indices) {
            val charToPrint = valueToPrint.toCharArray()[i]
            val va = charToPrint - '0'
            if (skipZero && va == 0) continue

            val charBmp: Bitmap = getBitmap(spec.imageIndex + va)
            canvas.drawBitmap(charBmp, startPoint.x.toFloat(), startPoint.y.toFloat(), settings.mGPaint)
            startPoint.x += charBmp.width + spacing
        }
    }

    /**
     * get an assets bitmap
     *
     * Implements cache
     */
    protected fun getBitmap(imageIdx: Int): Bitmap {
        var bmp: Bitmap? = null
        if (!imageCache.containsKey(imageIdx)) {
            bmp = Util.decodeImage(mService.resources, settings.getImagePath(imageIdx))
            Log.d(TAG, "cache image $imageIdx")
            imageCache[imageIdx] = bmp
        } else {
            bmp = imageCache.get(imageIdx)
        }
        return bmp!!;
    }

    protected fun drawBitmap(canvas: Canvas, imageIdx: Int, x: Int, y: Int) {
        drawBitmap(canvas, imageIdx, Point(x, y))
    }

    protected fun drawBitmap(canvas: Canvas, imageIdx: Int, point: Point) {
        drawBitmap(canvas, getBitmap(imageIdx), point)
    }

    protected fun drawBitmap(canvas: Canvas, image: Bitmap, point: Point) {
        canvas.drawBitmap(image, point.x.toFloat(), point.y.toFloat(), settings.mGPaint)
    }

    protected fun preloadCollection(images: IImage): List<Bitmap> {
        var result: Array<Bitmap> = Array(images.imagesCount!!) {
            getBitmap(it)
        }
        return result.asList()
    }
}