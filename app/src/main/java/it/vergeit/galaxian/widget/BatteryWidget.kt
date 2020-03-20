package it.vergeit.galaxian.widget

import android.app.Service
import android.graphics.*
import android.util.Log
import com.ingenic.iwds.slpt.view.core.SlptBatteryView
import com.ingenic.iwds.slpt.view.core.SlptNumView
import com.ingenic.iwds.slpt.view.core.SlptPictureGroupView
import com.ingenic.iwds.slpt.view.core.SlptViewComponent
import com.ingenic.iwds.slpt.view.sport.SlptPowerNumView
import it.vergeit.galaxian.data.Battery
import it.vergeit.galaxian.data.DataType
import it.vergeit.galaxian.settings.LoadSettings
import it.vergeit.galaxian.theme.bin.IText
import it.vergeit.galaxian.theme.bin.Images
import java.lang.Exception


class BatteryWidget() : CircleWidget() {
    constructor(_settings: LoadSettings) : this() {
        settings = _settings
    }

    private var angleUnknown: Int = 0
    private var progressBmp: Bitmap? = null
    override var ring: Paint? = null
    override var ringBmp: Bitmap? = null
    private var batteryData: Battery? = null
    private var batterySweepAngle = 0f
    private var batteryUnknown4SweepAngle = 0f
    private var angleLength: Int = 0
    private var batteryIcon: Bitmap? = null

    //    private Bitmap icon;
//
    private var tempBattery = 0

    // Screen-on init (runs once)
    override fun init(service: Service) {
        super.init(service)

        if (settings.theme.battery?.scale != null) {
            val circle = settings.theme.battery!!.scale!!
            angleLength = calcAngle(circle)

            if (settings.theme.battery!!.scale?.imageIndex == null) {
                ring = Paint(Paint.ANTI_ALIAS_FLAG)
                ring!!.strokeCap = Paint.Cap.ROUND
                ring!!.style = Paint.Style.STROKE
                ring!!.strokeWidth = circle.width!!.toFloat()
                ring!!.color = Color.parseColor(String.format("#%s", circle.color!!.substring(12)))
            } else {
                ringBmp = getBitmap(circle.imageIndex!!)
            }

        }
        if (settings.theme.battery?.unknown4 != null) {
            val unknown4 = settings.theme.battery!!.unknown4
            angleUnknown = calcAngle(unknown4.sector.startAngle, unknown4.sector.endAngle)
            progressBmp = getBitmap(settings.theme.battery!!.unknown4.image.imageIndex)
        }

    }

    // Value updater
    override fun onDataUpdate(type: DataType, value: Any) { // Battery class
        batteryData = value as Battery
        if (batteryData == null || settings.theme.battery == null) {
            return
        }

        val level: Int = batteryData!!.level

        // Bar angle
        if (settings.theme.battery?.scale != null) {
            batterySweepAngle = angleLength * (level / batteryData!!.scale.toFloat())
        }

        if (settings.theme.battery?.text != null) {
            val batterySteps: Int = settings.theme.battery!!.text!!.imagesCount
            tempBattery = level / batterySteps
        }

        if (settings.theme.battery?.unknown4 != null) {
            batteryUnknown4SweepAngle = angleUnknown * (level / batteryData!!.scale.toFloat())
        }
    }

    // Register update listeners
    override fun getDataTypes(): List<DataType> {
        return listOf(DataType.BATTERY)
    }

//    private fun drawText(canvas: Canvas, value: Int, text: it.vergeit.watchface.theme.bin.Text) {
//        val width = text.bottomRightX - text.topLeftX
//        val height = text.bottomRightY - text.topLeftY
//        val spacing = (width / text.imagesCount.toFloat()).roundToInt() + text.spacing
//        var x = text.topLeftX
//        val y = text.topLeftY
//        val bmp = getBitmap(text.imageIndex))
//        val imageSize = Size(bmp.width, bmp.height)
//
//        val textString = String.format("%d", value)
//        if (text.alignment == "Center") {
//            x += (width / 2f - textString.length * imageSize.width / 2f).roundToInt()
//        }
//        Log.d(TAG, String.format("draw value %s", textString))
//        for (i in textString.toCharArray().indices) {
//            val charToPrint = textString.toCharArray()[i]
//            val va = charToPrint - '0'
//            Log.d(TAG, String.format("draw char (x: %d, y: %d) %d - %c > bmp %s", x, y, i, charToPrint, settings.getImagePath(text.imageIndex + va)))
//            val charBmp = Util.decodeImage(mService!!.resources, settings.getImagePath(text.imageIndex + va))
//            canvas.drawBitmap(charBmp, x.toFloat(), y.toFloat(), settings.mGPaint)
//            x += charBmp.width + spacing
//        }
//        Log.d(TAG, "complete")
//    }

    // Draw screen-on
    override fun draw(canvas: Canvas?, width: Float, height: Float, centerX: Float, centerY: Float) {
        if (batteryData == null) {
            return
        }

        if (settings.theme.battery?.text != null) {
            drawText(canvas!!, batteryData!!.level, settings.theme.battery!!.text!!)
        }

        val scale = settings.theme.battery?.scale

        if (ring != null) {
            drawRing(canvas!!, scale!!, ring!!, batterySweepAngle)
        }

        if (ringBmp != null) {
            drawCircle(canvas!!, scale!!, ringBmp!!, batterySweepAngle)
        }

        if (progressBmp != null) {
            drawProgress(canvas!!, progressBmp!!, settings.theme.battery?.unknown4!!, batteryUnknown4SweepAngle)
        }

        if (settings.theme.battery?.images != null) {
            val images = settings.theme.battery?.images!!
            val arrayImages = getBitmapArray(images.imageIndex, images.imagesCount)
            val currentImage: Bitmap = arrayImages[(images.imagesCount / batteryData!!.level.toFloat() * 100f).coerceAtMost(images.imagesCount.toFloat()).toInt() - 1]
            drawBitmap(canvas!!, currentImage, Point(images.x, images.y))
        }

    }

    // Screen-off (SLPT)
    override fun buildSlptViewComponent(service: Service?): List<SlptViewComponent> {
        return buildSlptViewComponent(service, false)
    }



    // Screen-off (SLPT) - Better screen quality
    override fun buildSlptViewComponent(service: Service?, better_resolution: Boolean): List<SlptViewComponent> {
        val slptObjects = arrayListOf<SlptViewComponent>()
        mService = service!!

        try {

            if (settings.theme.battery?.text != null) {
                val text = settings.theme.battery!!.text!!
                slptObjects.add(drawSlptNum(SlptPowerNumView(), text, better_resolution))
            }

            if (settings.theme.battery?.images != null) {
                val images = settings.theme.battery!!.images!!
                slptObjects.add(drawSlptPictureGroup(SlptBatteryView(images.imagesCount), images, better_resolution))
            }

        } catch (e:Exception) {
            Log.e(TAG, e.message)
        }
        return slptObjects

//        // Do not show in SLPT (but show on raise of hand)
//        val show_all = !settings.clock_only_slpt || betterResolution
//        if (!show_all) return slpt_objects
//        var tmp_left: Int
//
//        if (settings.theme.battery?.text != null) {
//            val battery_steps = 11
//            val arrayOfByte = arrayOfNulls<ByteArray>(battery_steps)
//            for (i in arrayOfByte.indices) {
//                arrayOfByte[i] = SimpleFile.readFileFromAssets(service, settings.getImagePath(settings.theme.battery!!.text!!.imageIndex + i))
//            }
//            val localSlptBatteryView = SlptBatteryView(battery_steps)
//            localSlptBatteryView.setImagePictureArray(arrayOfByte)
//            localSlptBatteryView.setStart(settings.batteryProgLeft.toInt(), settings.batteryProgTop.toInt())
//            slpt_objects.add(localSlptBatteryView)
//        }
    }

//    private fun drawBitmapSlpt(level: Int, images: Images, betterResolution: Boolean): ArrayList<SlptViewComponent> {
//        val arrayImages = (0 until images.imagesCount).map {
//            Util.Bitmap2Bytes(getBitmap(images.imageIndex + it,true, betterResolution))
//        }.toTypedArray()
//
//        val currentImage = arrayImages[(images.imagesCount / level * 100).coerceAtMost(images.imagesCount)]
//
//        val pict = SlptPictureView()
//        pict.setImagePicture(currentImage)
//        pict.setStart(images.x, images.y)
//        return arrayListOf(pict)
//    }


    companion object {
        private const val TAG = "VergeIT-LOG"
    }
}