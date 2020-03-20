package it.vergeit.galaxian.widget

import android.app.Service
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.Rect
import android.util.Size
import it.vergeit.galaxian.data.DataType
import it.vergeit.galaxian.settings.LoadSettings
import com.huami.watch.watchface.util.Util
import com.ingenic.iwds.slpt.view.core.SlptNumView
import com.ingenic.iwds.slpt.view.core.SlptPictureGroupView
import com.ingenic.iwds.slpt.view.core.SlptPictureView
import com.ingenic.iwds.slpt.view.core.SlptViewComponent
import it.vergeit.galaxian.theme.bin.*
import kotlin.math.roundToInt


open class TextWidget() : AbstractWidget() {

    protected lateinit var settings: LoadSettings

    val theme: Theme
        get() = settings.theme

    companion object {
        const val TAG = "VergeIT-LOG"
    }

    protected lateinit var mService: Service

    protected constructor(_settings: LoadSettings) : this() {
        settings = _settings
    }

    internal fun bitmapArray(imageIndex: Int, imagesCount: Int, better_resolution: Boolean): Array<ByteArray> {
        return (0 until imagesCount).map {
            Util.Bitmap2Bytes(getBitmap(imageIndex + it, true, better_resolution))
        }.toTypedArray()
    }

    internal fun bitmapArray(text: Text, better_resolution: Boolean): Array<ByteArray> {
        return bitmapArray(text.imageIndex, text.imagesCount, better_resolution)
    }

    override fun buildSlptViewComponent(service: Service?): List<SlptViewComponent?>? =
            buildSlptViewComponent(service, false)

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
        return getStartPoint(spec.topLeftX, spec.topLeftY, spec.bottomRightX, spec.bottomRightY, spec.alignment, spec.spacing, spec.imageIndex, stringLen)
    }

    private fun getStartPoint(topLeftX: Int, topLeftY: Int, bottomRightX: Int, bottomRightY: Int, alignment: String, spacing: Int, imageIndex: Int, stringLen: Int): Point {
        val width = bottomRightX - topLeftX
        val height = bottomRightY - topLeftY
        val bmp: Bitmap = getBitmap(imageIndex)
        val imageSize = Size(bmp.width, bmp.height)

        // la lunghezza della stringa una volta stampata
        // numero di caratteri x dimensione dell'immagine più eventuale spaziatura
        val stringLength = stringLen * imageSize.width + (stringLen - 1) * spacing
        val centerX = (width / 2f).roundToInt()
        val centerY = (height / 2f).roundToInt()
        return when (alignment) {
            // #TopLeft, TopCenter, TopRight, Left, Center, Right, BottomLeft, BottomCenter, BottomRight
            "TopLeft" -> Point(topLeftX, topLeftY)
            "TopCenter" -> Point((topLeftX + centerX - stringLength / 2f).roundToInt(), (topLeftY + centerY - (imageSize.height / 2f).roundToInt()))
            "TopRight" -> Point(topLeftX + width - stringLength, topLeftY)
            "Center" -> Point((topLeftX + centerX - stringLength / 2f).roundToInt(), (topLeftY + centerY - imageSize.height / 2f).roundToInt())
            "Left" -> Point(topLeftX, (topLeftY + centerY - imageSize.height / 2f).roundToInt())
            "Right" -> Point(topLeftX + width - stringLength, (topLeftY + centerY - imageSize.height / 2f).roundToInt())
            "BottomLeft" -> Point(topLeftX, topLeftY + height - imageSize.height)
            "BottomRight" -> Point(topLeftX + width - stringLength, topLeftY + height - imageSize.height)
            else -> Point(topLeftX, topLeftY)
        }
    }

    protected fun drawTextSlpt(valueToPrint: String, spec: IText, skipZero: Boolean = false): ArrayList<SlptViewComponent> {
        val arrayDigit = (0 until spec.imagesCount).map {
            Util.Bitmap2Bytes(getBitmap(spec.imageIndex + it, slpt = true, slptBetter = false))
        }.toTypedArray()

        val baseImage = getBitmap(spec.imageIndex, slpt = true, slptBetter = false)
        val result = arrayListOf<SlptViewComponent>()
        val startPoint = getStartPoint(spec, valueToPrint.length)
        for (i in valueToPrint.toCharArray().indices) {
            val charToPrint = valueToPrint.toCharArray()[i]
            val va = charToPrint - '0'
            if (skipZero && va == 0) continue

            val bytImage = arrayDigit[va]
            val digit = SlptPictureView()
            digit.setImagePicture(bytImage)
            digit.setStart(startPoint.x, startPoint.y)
            startPoint.x += baseImage.width + spec.spacing
        }
        return result
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

    internal fun getBitmapSlpt(imageIdx: Int, slptBetter: Boolean): ByteArray {
        return Util.Bitmap2Bytes(getBitmap(imageIdx, true, slptBetter))
    }

    internal fun getBitmapSlptArray(imageIdx: Int, imageCount: Int, slptBetter: Boolean): Array<ByteArray> {
        return (0.rangeTo(imageCount - 1)).map {
            Util.Bitmap2Bytes(getBitmap(it + imageIdx, true, slptBetter))
        }.toTypedArray()
    }

    internal fun getBitmapArray(imageIdx: Int, imageCount: Int): Array<Bitmap> {
        return (0.rangeTo(imageCount - 1)).map {
            getBitmap(it + imageIdx)
        }.toTypedArray()
    }

    /**
     * get an assets bitmap
     *
     * Implements cache
     */
    internal fun getBitmap(imageIdx: Int, slpt: Boolean = false, slptBetter: Boolean = false): Bitmap {
        return settings.getBitmap(mService, imageIdx, slpt, slptBetter)
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

    fun drawSlptNum(view: SlptNumView, text: IText, better_resolution: Boolean): SlptNumView {
        val arrayDigit = getBitmapSlptArray(text.imageIndex, text.imagesCount, better_resolution)
        val sample = getBitmap(text.imageIndex)

        view.setImagePictureArray(arrayDigit)
        val rect: Rect = Rect(text.topLeftX, text.topLeftY, text.bottomRightX, text.bottomRightY)
        view.setStart(rect.left, rect.top)
        view.setRect(rect.right - rect.left, rect.bottom - rect.top)
        when (text.alignment) {
            "Center" -> {
                view.alignX = 2
                view.alignY = 2
            }
//            "CenterRight" -> {
//                view.alignY = 2
//                view.alignX = 1
//            }
            "CenterLeft" -> {
                view.alignY = 2
                view.alignX = 0
            }
            else -> {
                view.alignX = 0
                view.alignY = 0
            }
        }
        return view
    }

    fun drawSlptPictureGroup(view: SlptPictureGroupView, images: Images, better_resolution: Boolean): SlptPictureGroupView {
        val sample = getBitmap(images.imageIndex)
        view.setImagePictureArray(getBitmapSlptArray(images.imageIndex, images.imagesCount, better_resolution))
        view.setStart(images.x, images.y)
        view.setRect(images.x + sample.width, images.y + sample.height)
        view.alignX = 0
        view.alignY = 0
        return view
    }
}