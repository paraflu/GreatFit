package it.vergeit.watchface.widget

import android.app.Service
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Point
import android.util.Log
import android.util.Size
import it.vergeit.watchface.data.DataType
import it.vergeit.watchface.settings.LoadSettings
import it.vergeit.watchface.theme.bin.Distance
import it.vergeit.watchface.theme.bin.IImage
import it.vergeit.watchface.theme.bin.IText
import it.vergeit.watchface.theme.bin.Temperature
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

    protected fun drawText(canvas: Canvas, value: String, spec: IText, skipZero: Boolean = false) {
        drawText(canvas, value, spec, spec.imageIndex, spec.imagesCount, skipZero)
    }

    protected fun drawText(canvas: Canvas, value: Int, spec: IText, skipZero: Boolean = false) {
        drawText(canvas, value.toString(), spec, spec.imageIndex, spec.imagesCount, skipZero)
    }

    protected fun drawText(canvas: Canvas, value: Int, spec: IText, imageIndex: Int, imageCount: Int, skipZero: Boolean = false) {
        drawText(canvas, value.toString(), spec, imageIndex, imageCount, skipZero)
    }

    protected fun drawText(canvas: Canvas, temperatureValueCelsius: String, temperature: Temperature) {
        val degreeImage: Bitmap? = if (temperature.symbols?.degreesImageIndex != null)
            getBitmap(temperature.symbols.degreesImageIndex) else null
        val minusBmp: Bitmap? = if (temperature.symbols?.minusImageIndex != null) getBitmap(temperature.symbols.minusImageIndex) else null
        val spec = temperature.current!!
        val startPoint = getStartPoint(spec, if (degreeImage != null) temperatureValueCelsius.length + 1 else temperatureValueCelsius.length)

        if (temperatureValueCelsius == "n/a" && temperature.symbols?.noDataImageIndex != null) {
            canvas.drawBitmap(getBitmap(temperature.symbols.noDataImageIndex), startPoint.x.toFloat(), startPoint.y.toFloat(), settings.mGPaint)
            return
        }

        for (i in temperatureValueCelsius.toCharArray().indices) {
            val charToPrint = temperatureValueCelsius.toCharArray()[i]
            if (charToPrint == '-' && minusBmp != null) {
                canvas.drawBitmap(minusBmp, startPoint.x.toFloat(), startPoint.y.toFloat(), settings.mGPaint)
                startPoint.x += minusBmp.width + spec.spacing

            } else {
                val va = charToPrint - '0'
                val charBmp: Bitmap = getBitmap(temperature.current.imageIndex + va)
                canvas.drawBitmap(charBmp, startPoint.x.toFloat(), startPoint.y.toFloat(), settings.mGPaint)
                startPoint.x += charBmp.width + spec.spacing

            }
        }
        if (degreeImage != null) {
            canvas.drawBitmap(degreeImage, startPoint.x.toFloat(), startPoint.y.toFloat(), settings.mGPaint)
        }
    }

    protected fun drawText(canvas: Canvas, distanceString: String, distance: Distance) {
        val suffixImage: Bitmap? = if (distance.suffixImageIndex != null)
            getBitmap(distance.suffixImageIndex) else null
        val decimalPointImage: Bitmap? = if (distance.decimalPointImageIndex != null) getBitmap(distance.decimalPointImageIndex) else null

        val spec = distance.number
        val startPoint = getStartPoint(spec, if (suffixImage != null) distanceString.length + 1 else distanceString.length)


        for (i in distanceString.toCharArray().indices) {
            val charToPrint = distanceString.toCharArray()[i]
            if (charToPrint == '.' && decimalPointImage != null) {
                canvas.drawBitmap(decimalPointImage, startPoint.x.toFloat(), startPoint.y.toFloat(), settings.mGPaint)
                startPoint.x += decimalPointImage.width + spec.spacing

            } else {
                val va = charToPrint - '0'
                val charBmp: Bitmap = getBitmap(spec.imageIndex + va)
                canvas.drawBitmap(charBmp, startPoint.x.toFloat(), startPoint.y.toFloat(), settings.mGPaint)
                startPoint.x += charBmp.width + spec.spacing

            }
        }
        if (suffixImage != null) {
            canvas.drawBitmap(suffixImage, startPoint.x.toFloat(), startPoint.y.toFloat(), settings.mGPaint)
        }
    }

    fun getStartPoint(spec: IText, stringLen: Int): Point {
        val width = spec.bottomRightX - spec.topLeftX
        val height = spec.bottomRightY - spec.topLeftY
        val x = spec.topLeftX
        val y = spec.topLeftY
        val bmp: Bitmap = getBitmap(spec.imageIndex)
        val imageSize = Size(bmp.width, bmp.height)
//        val valueToPrint = value.toString()

        // la lunghezza della stringa una volta stampata
        // numero di caratteri x dimensione dell'immagine più eventuale spaziatura
        val stringLength = stringLen * imageSize.width + (stringLen - 1) * spec.spacing
        val centerX = (width / 2f).roundToInt()
        val centerY = (height / 2f).roundToInt()
        return when (spec.alignment) {
            // #TopLeft, TopCenter, TopRight, Left, Center, Right, BottomLeft, BottomCenter, BottomRight
            "TopLeft" -> Point(x, y)
            "TopCenter" -> Point(x + centerX - (stringLength / 2f).roundToInt(),
                    y + centerY - (imageSize.height / 2f).roundToInt())
            "TopRight" -> Point(x + width - stringLength, y)
            "Center" -> Point(x + centerX - (stringLength / 2f).roundToInt(), y + centerY - (imageSize.height / 2f).roundToInt())
            "Left" -> Point(x, y + centerY - imageSize.height)
            "Right" -> Point(x + width - stringLength, y + centerY - imageSize.height)
            "BottomLeft" -> Point(x, y + height - imageSize.height)
            "BottomRight" -> Point(x + width - stringLength, y + height - imageSize.height)
            else -> Point(x, y)
        }
    }

    /**
     * drawText on canvas
     *
     * @param canvas Canvas to contain draw
     * @param value Digit to write
     * @param spec Coordinates
     */
    protected fun drawText(canvas: Canvas, valueToPrint: String, spec: IText, imageIndex: Int, imageCount: Int, skipZero: Boolean = false) {

        val startPoint = getStartPoint(spec, valueToPrint.length)
        for (i in valueToPrint.toCharArray().indices) {
            val charToPrint = valueToPrint.toCharArray()[i]
            val va = charToPrint - '0'
            if (skipZero && va == 0) continue

            val charBmp: Bitmap = getBitmap(imageIndex + va)
            canvas.drawBitmap(charBmp, startPoint.x.toFloat(), startPoint.y.toFloat(), settings.mGPaint)
            startPoint.x += charBmp.width + spec.spacing
        }
    }

    /**
     * get an assets bitmap
     *
     * Implements cache
     */
    protected fun getBitmap(imageIdx: Int): Bitmap {
        return settings.getBitmap(mService, imageIdx)
//        var bmp: Bitmap?
//        if (!imageCache.containsKey(imageIdx)) {
//            bmp = Util.decodeImage(mService.resources, settings.getImagePath(imageIdx))
//            Log.d(TAG, "cache image $imageIdx")
//            imageCache[imageIdx] = bmp
//        } else {
//            bmp = imageCache.get(imageIdx)
//        }
//        return bmp!!;
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