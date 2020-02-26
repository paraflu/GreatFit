package com.dinodevs.greatfitwatchface.widget

import android.app.Service
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import android.util.Size
import com.dinodevs.greatfitwatchface.data.DataType
import com.dinodevs.greatfitwatchface.settings.LoadSettings
import com.dinodevs.greatfitwatchface.theme.bin.IText
import com.huami.watch.watchface.util.Util
import com.ingenic.iwds.slpt.view.core.SlptViewComponent


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

    protected fun drawText(canvas: Canvas, value: Int, spec: IText) {
        val width = spec.bottomRightX - spec.topLeftX
        val height = spec.bottomRightY - spec.topLeftY
        val spacing = Math.round(width / spec.imagesCount.toFloat()) + spec.spacing
        var x = spec.topLeftX
        val y = spec.topLeftY
        val bmp: Bitmap = Util.decodeImage(mService.resources, settings.getImagePath(spec.imageIndex))
        val imageSize = Size(bmp.width, bmp.height)
        if (spec.alignment == "Center") {
            x += imageSize.getWidth() / 2 + width / 2 - value / 10 * spacing
        }
        val text = String.format("%d", value)
        Log.d(TAG, String.format("draw value %s", text))
        for (i in text.toCharArray().indices) {
            val charToPrint = text.toCharArray()[i]
            val va = charToPrint - '0'
            val charBmp: Bitmap = Util.decodeImage(mService.resources, settings.getImagePath(spec.imageIndex + va))
            canvas.drawBitmap(charBmp, x.toFloat(), y.toFloat(), settings.mGPaint)
            x += charBmp.width + spacing
        }
        Log.d(TAG, "complete")
    }


}