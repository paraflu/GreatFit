package it.vergeit.wittyfuture.widget

import android.app.Service
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import it.vergeit.wittyfuture.data.DataType
import it.vergeit.wittyfuture.data.TodayFloor
import it.vergeit.wittyfuture.resource.ResourceManager
import it.vergeit.wittyfuture.resource.ResourceManager.getTypeFace
import it.vergeit.wittyfuture.settings.LoadSettings
import com.huami.watch.watchface.util.Util
import com.ingenic.iwds.slpt.view.core.Picture.ImagePicture
import com.ingenic.iwds.slpt.view.core.SlptLinearLayout
import com.ingenic.iwds.slpt.view.core.SlptPictureView
import com.ingenic.iwds.slpt.view.core.SlptViewComponent
import com.ingenic.iwds.slpt.view.sport.SlptTodayFloorNumView
import com.ingenic.iwds.slpt.view.utils.SimpleFile
import java.io.ByteArrayOutputStream
import java.util.*

class FloorWidget // Constructor
(private val settings: LoadSettings) : AbstractWidget() {
    private var textPaint: TextPaint? = null
    private var todayFloor: TodayFloor? = null
    private var icon: Bitmap? = null
    private val digitalNums = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
    private var mService: Service? = null
    // Screen-on init (runs once)
    override fun init(service: Service) { // Font
        textPaint = TextPaint(TextPaint.ANTI_ALIAS_FLAG)
        textPaint!!.color = settings.floorsColor
        textPaint!!.typeface = getTypeFace(service.resources, settings.font)
        textPaint!!.textSize = settings.floorsFontSize
        textPaint!!.textAlign = if (settings.floorsAlignLeft) Paint.Align.LEFT else Paint.Align.CENTER
        if (settings.floorsIcon) {
            icon = Util.decodeImage(service.resources, "icons/" + settings.is_white_bg + "floors.png")
        }
    }

    // Register floors counter
    override fun getDataTypes(): List<DataType> {
        return listOf(DataType.FLOOR)
    }

    // floors updater
    override fun onDataUpdate(type: DataType, value: Any) {
        todayFloor = value as TodayFloor
    }

    // Screen on
    override fun draw(canvas: Canvas, width: Float, height: Float, centerX: Float, centerY: Float) {
        if (settings.floors > 0) {
            if (settings.floorsIcon) {
                canvas.drawBitmap(icon, settings.floorsIconLeft, settings.floorsIconTop, settings.mGPaint)
            }
            canvas.drawText(Integer.toString(todayFloor!!.floor), settings.floorsLeft, settings.floorsTop, textPaint)
        }
    }

    // ************************************
// Create image based on text (by LFOM)
// ************************************
    private fun floorStringToImagePicture(string: String): ByteArray {
        return stringToImagePicture(
                string,
                settings.floorsFontSize,
                settings.floorsColor,
                settings.font,
                if (settings.white_bg) "#ffffff" else "#000000" // black or white background
        )
    }

    private fun stringToImagePicture(string: String, textSize: Float, textColor: Int, textFont: ResourceManager.Font, bgColor: String): ByteArray {
        val bitmap = textAsBitmap(string, textSize, textColor, textFont, bgColor)
        return decodeBitmap(bitmap)
    }

    private fun textAsBitmap(text: String, textSize: Float, textColor: Int, textFont: ResourceManager.Font, bgColor: String): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.textSize = textSize
        paint.color = textColor
        paint.typeface = getTypeFace(mService!!.resources, textFont)
        paint.textAlign = Paint.Align.LEFT
        val baseline = -paint.ascent() // ascent() is negative
        val width = (paint.measureText(text) + 0.5f).toInt() // round
        val height = (baseline + paint.descent() + 0.5f - settings.font_ratio.toFloat() / 100.0f * paint.descent()).toInt()
        println("GreatFit FloorsWidget width: " + width + " | height: " + height + " | baseline: " + baseline + " | descent: " + paint.descent() + " | text size: " + textSize + " | color: " + textColor + " | bg color: " + Color.parseColor(bgColor) + " | font: " + textFont.toString())
        val image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(image)
        val rect_paint = Paint()
        rect_paint.style = Paint.Style.FILL
        rect_paint.color = Color.parseColor(bgColor)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), rect_paint)
        canvas.drawText(text, 0f, baseline, paint)
        return image
    }

    // ************************************
// SLPT mode, screen off
    override fun buildSlptViewComponent(service: Service?): List<SlptViewComponent> {
        return buildSlptViewComponent(service, false)
    }

    override fun buildSlptViewComponent(service: Service?, better_resolution: Boolean): List<SlptViewComponent> { //better_resolution = better_resolution && settings.better_resolution_when_raising_hand;
        val slpt_objects: MutableList<SlptViewComponent> = ArrayList()
        mService = service
        // Do not show in SLPT (but show on raise of hand)
        val show_all = !settings.clock_only_slpt || better_resolution
        if (!show_all) return slpt_objects
        if (settings.floors > 0) { // Show or Not icon
            if (settings.floorsIcon) {
                val floorsIcon = SlptPictureView()
                floorsIcon.setImagePicture(SimpleFile.readFileFromAssets(service, (if (better_resolution) "26wc_" else "slpt_") + "icons/" + settings.is_white_bg + "floors.png"))
                floorsIcon.setStart(
                        settings.floorsIconLeft.toInt(),
                        settings.floorsIconTop.toInt()
                )
                slpt_objects.add(floorsIcon)
            }
            val floorsLayout = SlptLinearLayout()
            // define digits as pictures
            val SlptNumArray = arrayOf(
                    floorStringToImagePicture(digitalNums[0]),
                    floorStringToImagePicture(digitalNums[1]),
                    floorStringToImagePicture(digitalNums[2]),
                    floorStringToImagePicture(digitalNums[3]),
                    floorStringToImagePicture(digitalNums[4]),
                    floorStringToImagePicture(digitalNums[5]),
                    floorStringToImagePicture(digitalNums[6]),
                    floorStringToImagePicture(digitalNums[7]),
                    floorStringToImagePicture(digitalNums[8]),
                    floorStringToImagePicture(digitalNums[9])
            )
            // create bg image with 0 written (floors are never 0)
            floorsLayout.background.picture = ImagePicture(SlptNumArray[0])
            /*
            SlptPictureView floorStr0 = new SlptPictureView();
            floorStr0.setStringPicture( "0" );
            floorsLayout.add(floorStr0);

            SlptLinearLayout floorStr1 = new SlptLinearLayout();
            floorStr1.add(new SlptTodayFloorNumView());
            floorStr1.background.color = Color.parseColor((settings.white_bg?"#ffffff":"#000000"));
            floorStr1.setStringPictureArrayForAll(this.digitalNums);
            floorsLayout.add(floorStr1);
            */
//floorsLayout.background.color = Color.parseColor((settings.white_bg?"#ffffff":"#000000"));
            floorsLayout.add(SlptTodayFloorNumView())
            floorsLayout.setImagePictureArrayForAll(SlptNumArray)
            //floorsLayout.setStringPictureArrayForAll(this.digitalNums);
/*
            floorsLayout.setTextAttrForAll(
                    settings.floorsFontSize,
                    settings.floorsColor,
                    ResourceManager.getTypeFace(service.getResources(), settings.font)
            );
            */
// Position based on screen on
            floorsLayout.alignX = 2
            floorsLayout.alignY = 0
            var tmp_left = settings.floorsLeft.toInt()
            if (!settings.floorsAlignLeft) { // If text is centered, set rectangle
                floorsLayout.setRect(
                        (2 * tmp_left + 640),
                        (settings.font_ratio.toFloat() / 100 * settings.floorsFontSize).toInt()
                )
                tmp_left = -320
            }
            floorsLayout.setStart(
                    tmp_left,
                    (settings.floorsTop - settings.font_ratio.toFloat() / 100 * settings.floorsFontSize).toInt()
            )
            slpt_objects.add(floorsLayout)
            // Invalid floor
/* NOT TESTED
            SlptPictureView invalidFloor = new SlptPictureView();
            invalidFloor.setStringPicture( "-" );
            // Position based on screen on
            invalidFloor.alignX = 2;
            invalidFloor.alignY = 0;
            if(!settings.floorsAlignLeft) {
                // If text is centered, set rectangle
                invalidFloor.setRect(
                        (int) (2 * tmp_left + 640),
                        (int) (((float)settings.font_ratio/100)*settings.floorsFontSize)
                );
                tmp_left = -320;
            }
            invalidFloor.setStart(
                    (int) tmp_left,
                    (int) (settings.floorsTop-((float)settings.font_ratio/100)*settings.floorsFontSize)
            );
            SlptSportUtil.setTodayFloorInvalidView(invalidFloor);
             */
        }
        return slpt_objects
    }

    companion object {
        private fun decodeBitmap(bmp: Bitmap): ByteArray {
            val stream = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()
            bmp.recycle()
            return byteArray
        }
    }

}