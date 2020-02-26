package com.dinodevs.greatfitwatchface.widget

import android.app.Service
import android.graphics.*
import android.text.TextPaint
import android.util.Log
import com.dinodevs.greatfitwatchface.resource.ResourceManager.getTypeFace
import com.dinodevs.greatfitwatchface.resource.SlptSecondHView
import com.dinodevs.greatfitwatchface.resource.SlptSecondLView
import com.dinodevs.greatfitwatchface.settings.LoadSettings
import com.dinodevs.greatfitwatchface.theme.bin.AnalogDialFace
import com.dinodevs.greatfitwatchface.theme.bin.Hours
import com.dinodevs.greatfitwatchface.theme.bin.ITimeDigit
import com.huami.watch.watchface.util.Util
import com.ingenic.iwds.slpt.view.core.SlptLinearLayout
import com.ingenic.iwds.slpt.view.core.SlptNumView
import com.ingenic.iwds.slpt.view.core.SlptPictureView
import com.ingenic.iwds.slpt.view.core.SlptViewComponent
import com.ingenic.iwds.slpt.view.digital.SlptHourHView
import com.ingenic.iwds.slpt.view.digital.SlptHourLView
import com.ingenic.iwds.slpt.view.digital.SlptMinuteHView
import com.ingenic.iwds.slpt.view.digital.SlptMinuteLView
import com.ingenic.iwds.slpt.view.sport.SlptSportUtil
import com.ingenic.iwds.slpt.view.utils.SimpleFile
import java.io.ByteArrayOutputStream


class MainClock(private val settings: LoadSettings) : DigitalClockWidget() {
    private var hourCenterBmp: Bitmap? = null
    private var minCenterBmp: Bitmap? = null
    private var amBmp: Bitmap? = null
    private var pmBmp: Bitmap? = null
    private val hourFont: TextPaint? = null
    private val minutesFont: TextPaint? = null
    private val secondsFont: TextPaint? = null
    private val indicatorFont: TextPaint? = null
    private var dateFont: TextPaint? = null
    private var dayFont: TextPaint? = null
    private var weekdayFont: TextPaint? = null
    private var monthFont: TextPaint? = null
    private var yearFont: TextPaint? = null
    private val ampmFont: TextPaint? = null
    private var dateIcon: Bitmap? = null
    private var hourHand: Bitmap? = null
    private var minuteHand: Bitmap? = null
    private var secondsHand: Bitmap? = null
    private lateinit var background: Bitmap
    private val digitalNums = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
    private val digitalNumsNo0 = arrayOf("", "1", "2", "3", "4", "5", "6", "7", "8", "9") //no 0 on first digit
    private var mService: Service? = null

    fun getBitmap(imageIdx: Int): Bitmap = Util.decodeImage(mService!!.resources, settings.getImagePath(imageIdx))

    override fun init(service: Service?) {
        mService = service!!

        background = getBitmap(settings.theme.background.image.imageIndex)
        if (settings.date > 0) {
            dateFont = TextPaint(TextPaint.ANTI_ALIAS_FLAG)
            dateFont!!.typeface = getTypeFace(service.resources, settings.font)
            dateFont!!.textSize = settings.dateFontSize
            dateFont!!.color = settings.dateColor
            dateFont!!.textAlign = if (settings.dateAlignLeft) Paint.Align.LEFT else Paint.Align.CENTER
            if (settings.dateIcon) {
                dateIcon = Util.decodeImage(service.resources, "icons/" + settings.is_white_bg + "date.png")
            }
        }

        if (settings.theme.analogDialFace != null) {
            val cfg = settings.theme.analogDialFace!!
            Log.d(TAG, "theme ")
            if (cfg.hours != null) {
                hourHand = getBitmap(cfg.hours.image.imageIndex)
            }
            if (cfg.minutes != null) {
                minuteHand = getBitmap(cfg.minutes.image.imageIndex)
            }
            if (cfg.seconds != null) {
                secondsHand = getBitmap(cfg.seconds.image.imageIndex)
            }
            if (cfg.hourCenterImage != null) {
                hourCenterBmp = getBitmap(cfg.hourCenterImage.imageIndex)
                Log.d(TAG, "loadImage hour ${cfg.hourCenterImage.imageIndex}")
            }
            if (cfg.minCenterImage != null) {
                minCenterBmp = getBitmap(cfg.minCenterImage.imageIndex)
                Log.d(TAG, "loadImage min ${cfg.minCenterImage.imageIndex}")
            }
        }
        weekdayFont = TextPaint(TextPaint.ANTI_ALIAS_FLAG)
        weekdayFont!!.typeface = getTypeFace(service.resources, settings.font)
        weekdayFont!!.textSize = settings.weekdayFontSize
        weekdayFont!!.color = settings.weekdayColor
        weekdayFont!!.textAlign = if (settings.weekdayAlignLeft) Paint.Align.LEFT else Paint.Align.CENTER
        dayFont = TextPaint(TextPaint.ANTI_ALIAS_FLAG)
        dayFont!!.typeface = getTypeFace(service.resources, settings.font)
        dayFont!!.textSize = settings.dayFontSize
        dayFont!!.color = settings.dayColor
        dayFont!!.textAlign = if (settings.dayAlignLeft) Paint.Align.LEFT else Paint.Align.CENTER
        monthFont = TextPaint(TextPaint.ANTI_ALIAS_FLAG)
        monthFont!!.typeface = getTypeFace(service.resources, settings.font)
        monthFont!!.textSize = settings.monthFontSize
        monthFont!!.color = settings.monthColor
        monthFont!!.textAlign = if (settings.monthAlignLeft) Paint.Align.LEFT else Paint.Align.CENTER
        yearFont = TextPaint(TextPaint.ANTI_ALIAS_FLAG)
        yearFont!!.typeface = getTypeFace(service.resources, settings.font)
        yearFont!!.textSize = settings.yearFontSize
        yearFont!!.color = settings.yearColor
        yearFont!!.textAlign = if (settings.yearAlignLeft) Paint.Align.LEFT else Paint.Align.CENTER

        if (settings.theme?.time?.amPm != null) {
            val amPm = settings.theme!!.time!!.amPm!!
            amBmp = getBitmap(amPm.imageIndexAMEN)
            pmBmp = getBitmap(amPm.imageIndexPMEN)
        }
    }

    /*fun drawTime(canvas: Canvas?, value: Int, font: Ones) {
        val text = String.format("%d", value)
        var x = font.x
        val y = font.y
        Log.d(TAG, text)
        for (i in text.toCharArray().indices) {
            val charToPrint = text.toCharArray()[i]
            val va = charToPrint - '0'
            val charBmp = Util.decodeImage(mService!!.resources, settings.getImagePath(font.imageIndex!! + va))
            canvas!!.drawBitmap(charBmp, x.toFloat(), y.toFloat(), settings.mGPaint)
            x += charBmp.width
        }
    }*/


    private fun drawTime(canvas: Canvas, value: Int, font: ITimeDigit) {
        val text = String.format("%d", value)
        var x = font.x
        val y = font.y
        for (i in text.toCharArray().indices) {
            val charToPrint = text.toCharArray()[i]
            val va = charToPrint - '0'
            Log.d(TAG, "drawTime $text : $va")
            val charBmp = getBitmap(font.imageIndex + va)
            canvas.drawBitmap(charBmp, x.toFloat(), y.toFloat(), settings.mGPaint)
            x += charBmp.width
        }
    }

    // Screen open watch mode
    override fun onDrawDigital(canvas: Canvas?, width: Float, height: Float, centerX: Float, centerY: Float, seconds: Int, minutes: Int, hours: Int, year: Int, month: Int, day: Int, week: Int, ampm: Int) {

        try {
            canvas!!.drawBitmap(background, 0f, 0f, settings.mGPaint)

            if (settings.theme.time != null) {
                val time = settings.theme.time!!
                if (time.hours != null) {
                    drawTime(canvas, hours / 10, time.hours.tens)
                    drawTime(canvas, hours % 10, time.hours.ones)
                }
                if (time.minutes != null) {
                    drawTime(canvas, minutes / 10, time.minutes.tens)
                    drawTime(canvas, minutes % 10, time.minutes.ones)
                }
                if (time.amPm != null) {
                    canvas.drawBitmap(
                            if (ampm == 0) amBmp!! else pmBmp!!,
                            time.amPm.x.toFloat(),
                            time.amPm.y.toFloat(), settings.mGPaint)
                }
            }

            if (settings.theme.analogDialFace != null) {
                val cfg = settings.theme.analogDialFace!!
                if (minCenterBmp != null) {
                    canvas.drawBitmap(minCenterBmp!!, cfg.minCenterImage!!.x.toFloat(), cfg.minCenterImage.y.toFloat(), settings.mGPaint)
                }
                if (hourCenterBmp != null) {
                    canvas.drawBitmap(hourCenterBmp!!, cfg.hourCenterImage!!.x.toFloat(), cfg.hourCenterImage.y.toFloat(), settings.mGPaint)
                }

                drawAnalogClock(canvas, cfg, hours, minutes, seconds)
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }
    }

    private fun drawAnalogClock(canvas: Canvas, analogDialFace: AnalogDialFace, hours: Int, minutes: Int, seconds: Int) {
        Log.d(TAG, "drawAnalogClock $hours:$minutes:$seconds")
        val centerScreen = if (settings.isVerge) Point(180, 179) else Point(160, 159)
        if (analogDialFace.hours != null) {
            val centerPoint = Point(centerScreen.x + analogDialFace.hours.centerOffset.x,
                    centerScreen.y + analogDialFace.hours.centerOffset.y)
            canvas.save()
            canvas.rotate((hours * 30).toFloat() + minutes.toFloat() / 60.0f * 30.0f, centerPoint.x.toFloat(), centerPoint.y.toFloat())
            canvas.drawBitmap(hourHand!!,
                    (centerPoint.x - analogDialFace.hours.image.x).toFloat(),
                    (centerPoint.y - analogDialFace.hours.image.y).toFloat(), null)
            canvas.restore()
        }

        if (analogDialFace.minutes != null) {
            val centerPoint = Point(centerScreen.x + analogDialFace.minutes.centerOffset.x,
                    centerScreen.y + analogDialFace.minutes.centerOffset.y)
            canvas.save()
            canvas.rotate((minutes * 6).toFloat(), centerScreen.x.toFloat(), centerScreen.y.toFloat())
            canvas.drawBitmap(minuteHand!!,
                    (centerPoint.x - analogDialFace.minutes.image.x).toFloat(),
                    (centerPoint.y - analogDialFace.minutes.image.y).toFloat(), null)
            canvas.restore()
        }
        if (analogDialFace.seconds != null) {
            val centerPoint = Point(centerScreen.x + analogDialFace.seconds.centerOffset.x,
                    centerScreen.y + analogDialFace.seconds.centerOffset.y)
            canvas.save()
            canvas.rotate((seconds * 6).toFloat(), centerScreen.x.toFloat() + analogDialFace.seconds.centerOffset.x, centerScreen.y.toFloat() + analogDialFace.seconds.centerOffset.y)
            canvas.drawBitmap(secondsHand!!,
                    (centerPoint.x - analogDialFace.seconds.image.x).toFloat(),
                    (centerPoint.y - analogDialFace.seconds.image.y).toFloat(), null)
            canvas.restore()
        }
    }

    // Screen locked/closed watch mode (Slpt mode)
    override fun buildSlptViewComponent(service: Service?): List<SlptViewComponent?>? {
        return buildSlptViewComponent(service, false)
    }

    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        val image: ByteArray = stream.toByteArray()
        return image
    }

    override fun buildSlptViewComponent(service: Service?, better_resolution: Boolean): List<SlptViewComponent?>? {
        val slpt_objects = mutableListOf<SlptViewComponent>()

        val background = SlptPictureView()
        background.setImagePicture(bitmapToByteArray(this.background))

        if (settings.theme.time != null) {
            val time = settings.theme.time!!
            if (time.hours != null) {

                val hours: Hours = time.hours
                val digit = Util.decodeImage(mService!!.resources, settings.getImagePath(hours.tens.imageIndex))
                val hourLayout = SlptLinearLayout()
                hourLayout.add(SlptHourHView())
                hourLayout.add(SlptHourLView())
                val firstDigit = SlptNumView()
                val secondDigit = SlptNumView()
                var digits = mutableListOf<ByteArray>()
                for (i in 0..time.hours.tens.imagesCount) {
                    digits.add(i, bitmapToByteArray(getBitmap(i)))
                }
                firstDigit.setImagePictureArray(digits.toTypedArray())
                secondDigit.setImagePictureArray(digits.toTypedArray())

                hourLayout.add(firstDigit)
                hourLayout.add(secondDigit)
                hourLayout.alignX = 2
                hourLayout.alignY = 0
                hourLayout.setRect(
                        (2 * hours.tens.x + digit.width).toInt(),
                        digit.height
                )
                hourLayout.setStart(hours.tens.x, hours.tens.y)

                slpt_objects.add(hourLayout)

//                hourTen.setImagePicture(bitmapToByteArray(getBitmap(time.hours.tens.imageIndex + hours/ 10)))
//                drawTimeSlpt(canvas, hours / 10, time.hours.tens)
//                drawTime(canvas, hours % 10, time.hours.ones)
            }
            if (time.minutes != null) {
//                drawTime(canvas, minutes / 10, time.minutes.tens)
//                drawTime(canvas, minutes % 10, time.minutes.ones)
            }
        }

        if (settings.low_power) { // Draw low power icon
            val lowpower = SlptPictureView()
            lowpower.setImagePicture(SimpleFile.readFileFromAssets(service, "slpt_battery/" + settings.is_white_bg + "low_battery.png"))
            lowpower.setStart(
                    settings.low_powerLeft.toInt(),
                    settings.low_powerTop.toInt()
            )
            SlptSportUtil.setLowBatteryIconView(lowpower)
            slpt_objects.add(lowpower)
        }

        var betterResolution = better_resolution
        betterResolution = betterResolution && settings.better_resolution_when_raising_hand
        // SLPT only clock
        val show_all = !settings.clock_only_slpt || betterResolution
        // SLPT only clock white bg -> to black
        if (!show_all && settings.isVerge && settings.white_bg) {
            settings.is_white_bg = ""
            settings.hoursColor = Color.parseColor("#ffffff")
            settings.minutesColor = Color.parseColor("#ffffff")
            settings.am_pmColor = Color.parseColor("#ffffff")
        }
        mService = service
        var tmp_left: Int
        // Draw background image
//        val background = SlptPictureView()
//        background.setImagePicture(SimpleFile.readFileFromAssets(service, settings.is_white_bg + "background" + (if (betterResolution) "_better" else "") + (if (settings.isVerge) "_verge" else "") + "_slpt.png"))
//        //Alternative way
////background.setImagePicture(ResourceManager.getVergeImageFromAssets(settings.isVerge(), service, "background"+ ((better_resolution)?"_better":"") +"_slpt.png"));
//        slpt_objects.add(background)
        // Set low power icon
        if (settings.low_power) { // Draw low power icon
            val lowpower = SlptPictureView()
            lowpower.setImagePicture(SimpleFile.readFileFromAssets(service, "slpt_battery/" + settings.is_white_bg + "low_battery.png"))
            //lowpower.picture.setBackgroundColor(backgroundColor);
            lowpower.setStart(
                    settings.low_powerLeft.toInt(),
                    settings.low_powerTop.toInt()
            )
            SlptSportUtil.setLowBatteryIconView(lowpower)
            slpt_objects.add(lowpower)
        }
        // Set font
        val timeTypeFace = getTypeFace(service!!.resources, settings.font)
        if (settings.digital_clock) { // Draw hours
            if (settings.hoursBool) {
                val hourLayout = SlptLinearLayout()
                if (settings.no_0_on_hour_first_digit) { // No 0 on first digit
                    val firstDigit: SlptViewComponent = SlptHourHView()
                    (firstDigit as SlptNumView).setStringPictureArray(digitalNumsNo0)
                    hourLayout.add(firstDigit)
                    val secondDigit: SlptViewComponent = SlptHourLView()
                    (secondDigit as SlptNumView).setStringPictureArray(digitalNums)
                    hourLayout.add(secondDigit)
                } else {
                    hourLayout.add(SlptHourHView())
                    hourLayout.add(SlptHourLView())
                    hourLayout.setStringPictureArrayForAll(digitalNums)
                }
                hourLayout.setTextAttrForAll(
                        settings.hoursFontSize,
                        settings.hoursColor,
                        timeTypeFace
                )
                // Position based on screen on
                hourLayout.alignX = 2
                hourLayout.alignY = 0
                hourLayout.setRect(
                        (2 * settings.hoursLeft + 640).toInt(),
                        (settings.font_ratio.toFloat() / 100 * settings.hoursFontSize).toInt()
                )
                hourLayout.setStart(
                        -320,
                        (settings.hoursTop - settings.font_ratio.toFloat() / 100 * settings.hoursFontSize).toInt()
                )
                //Add it to the list
                slpt_objects.add(hourLayout)
            }
            // Draw minutes
            if (settings.minutesBool) {
                val minuteLayout = SlptLinearLayout()
                minuteLayout.add(SlptMinuteHView())
                minuteLayout.add(SlptMinuteLView())
                minuteLayout.setStringPictureArrayForAll(digitalNums)
                minuteLayout.setTextAttrForAll(
                        settings.minutesFontSize,
                        settings.minutesColor,
                        timeTypeFace
                )
                // Position based on screen on
                minuteLayout.alignX = 2
                minuteLayout.alignY = 0
                minuteLayout.setRect(
                        (2 * settings.minutesLeft + 640).toInt(),
                        (settings.font_ratio.toFloat() / 100 * settings.minutesFontSize).toInt()
                )
                minuteLayout.setStart(
                        -320,
                        (settings.minutesTop - settings.font_ratio.toFloat() / 100 * settings.minutesFontSize).toInt()
                )
                //Add it to the list
                slpt_objects.add(minuteLayout)
            }
            // Draw indicator
            if (settings.indicatorBool) {
                val indicatorLayout = SlptLinearLayout()
                val colon = SlptPictureView()
                colon.setStringPicture(":")
                indicatorLayout.add(colon)
                indicatorLayout.setTextAttrForAll(
                        settings.indicatorFontSize,
                        settings.indicatorColor,
                        timeTypeFace
                )
                // Position based on screen on
                indicatorLayout.alignX = 2
                indicatorLayout.alignY = 0
                indicatorLayout.setRect(
                        (2 * settings.indicatorLeft + 640).toInt(),
                        (settings.font_ratio.toFloat() / 100 * settings.indicatorFontSize).toInt()
                )
                indicatorLayout.setStart(
                        -320,
                        (settings.indicatorTop - settings.font_ratio.toFloat() / 100 * settings.indicatorFontSize).toInt()
                )
                //Add it to the list
                slpt_objects.add(indicatorLayout)
            }
            // Draw Seconds
            if (settings.secondsBool) { //&& (!settings.isVerge() || better_resolution)
                val secondsLayout = SlptLinearLayout()
                secondsLayout.add(SlptSecondHView())
                secondsLayout.add(SlptSecondLView())
                secondsLayout.setTextAttrForAll(
                        settings.secondsFontSize,
                        settings.secondsColor,
                        getTypeFace(service.resources, settings.font)
                )
                // Position based on screen on
                secondsLayout.alignX = 2
                secondsLayout.alignY = 0
                secondsLayout.setRect(
                        (2 * settings.secondsLeft + 640).toInt(),
                        (settings.font_ratio.toFloat() / 100 * settings.secondsFontSize).toInt()
                )
                secondsLayout.setStart(
                        -320,
                        (settings.secondsTop - settings.font_ratio.toFloat() / 100 * settings.secondsFontSize).toInt()
                )
                //Add it to the list
                slpt_objects.add(secondsLayout)
            }
            // AM-PM (ONLY FOR 12h format)
            val ampm = SlptLinearLayout()
            val am = SlptPictureView()
            val pm = SlptPictureView()
            am.setStringPicture("AM")
            pm.setStringPicture("PM")
            SlptSportUtil.setAmBgView(am)
            SlptSportUtil.setPmBgView(pm)
            ampm.add(am)
            ampm.add(pm)
            ampm.setTextAttrForAll(settings.am_pmFontSize, settings.am_pmColor, getTypeFace(service.resources, settings.font))
            ampm.alignX = 2
            ampm.alignY = 0
            tmp_left = settings.am_pmLeft.toInt()
            if (!settings.am_pmAlignLeft) {
                ampm.setRect(tmp_left * 2 + 640, settings.am_pmFontSize.toInt())
                tmp_left = -320
            }
            ampm.setStart(tmp_left, (settings.am_pmTop - settings.font_ratio / 100.0f * settings.am_pmFontSize).toInt())
            slpt_objects.add(ampm)
        }
        /*     if (settings.analog_clock) {
                 val slptAnalogHourView = SlptAnalogHourView()
                 slptAnalogHourView.setImagePicture(SimpleFile.readFileFromAssets(service, "timehand/8c/hour" + (if (settings.isVerge) "_verge" else "") + ".png"))
                 slptAnalogHourView.alignX = 2.toByte()
                 slptAnalogHourView.alignY = 2.toByte()
                 slptAnalogHourView.setRect(320 + if (settings.isVerge) 40 else 0, 320 + if (settings.isVerge) 40 else 0)
                 slpt_objects.add(slptAnalogHourView)
                 val slptAnalogMinuteView = SlptAnalogMinuteView()
                 slptAnalogMinuteView.setImagePicture(SimpleFile.readFileFromAssets(service, "timehand/8c/minute" + (if (settings.isVerge) "_verge" else "") + ".png"))
                 slptAnalogMinuteView.alignX = 2.toByte()
                 slptAnalogMinuteView.alignY = 2.toByte()
                 slptAnalogMinuteView.setRect(320 + if (settings.isVerge) 40 else 0, 320 + if (settings.isVerge) 40 else 0)
                 slpt_objects.add(slptAnalogMinuteView)
                 if (settings.secondsBool) {
                     val slptAnalogSecondView = SlptAnalogSecondView()
                     slptAnalogSecondView.setImagePicture(SimpleFile.readFileFromAssets(service, "timehand/8c/seconds" + (if (settings.isVerge) "_verge" else "") + ".png"))
                     slptAnalogSecondView.alignX = 2.toByte()
                     slptAnalogSecondView.alignY = 2.toByte()
                     slptAnalogSecondView.setRect(320 + if (settings.isVerge) 40 else 0, 320 + if (settings.isVerge) 40 else 0)
                     slpt_objects.add(slptAnalogSecondView)
                 }
             }
             // Only CLOCK?
             if (!show_all) return slpt_objects
             // Draw DATE (30.12.2018)
             if (settings.date > 0) { // Show or Not icon
                 if (settings.dateIcon) {
                     val dateIcon = SlptPictureView()
                     dateIcon.setImagePicture(SimpleFile.readFileFromAssets(service, (if (betterResolution) "26wc_" else "slpt_") + "icons/" + settings.is_white_bg + "date.png"))
                     dateIcon.setStart(
                             settings.dateIconLeft.toInt(),
                             settings.dateIconTop.toInt()
                     )
                     slpt_objects.add(dateIcon)
                 }
                 // Set . string
                 val point = SlptPictureView()
                 point.setStringPicture(".")
                 val point2 = SlptPictureView()
                 point2.setStringPicture(".")
                 val dateLayout = SlptLinearLayout()
                 dateLayout.add(SlptDayHView())
                 dateLayout.add(SlptDayLView())
                 dateLayout.add(point) //add .
                 dateLayout.add(SlptMonthHView())
                 dateLayout.add(SlptMonthLView())
                 dateLayout.add(point2) //add .
                 dateLayout.add(SlptYear3View())
                 dateLayout.add(SlptYear2View())
                 dateLayout.add(SlptYear1View())
                 dateLayout.add(SlptYear0View())
                 dateLayout.setTextAttrForAll(
                         settings.dateFontSize,
                         settings.dateColor,
                         timeTypeFace)
                 // Position based on screen on
                 dateLayout.alignX = 2
                 dateLayout.alignY = 0
                 tmp_left = settings.dateLeft.toInt()
                 if (!settings.dateAlignLeft) { // If text is centered, set rectangle
                     dateLayout.setRect(
                             (2 * tmp_left + 640),
                             (settings.font_ratio.toFloat() / 100 * settings.dateFontSize).toInt()
                     )
                     tmp_left = -320
                 }
                 dateLayout.setStart(
                         tmp_left,
                         (settings.dateTop - settings.font_ratio.toFloat() / 100 * settings.dateFontSize).toInt()
                 )
                 //Add it to the list
                 slpt_objects.add(dateLayout)
             }
             // Draw day of month
             if (settings.dayBool) {
                 val dayLayout = SlptLinearLayout()
                 dayLayout.add(SlptDayHView())
                 dayLayout.add(SlptDayLView())
                 dayLayout.setTextAttrForAll(
                         settings.dayFontSize,
                         settings.dayColor,
                         timeTypeFace)
                 // Position based on screen on
                 dayLayout.alignX = 2
                 dayLayout.alignY = 0
                 tmp_left = settings.dayLeft.toInt()
                 if (!settings.dayAlignLeft) { // If text is centered, set rectangle
                     dayLayout.setRect(
                             (2 * tmp_left + 640),
                             (settings.font_ratio.toFloat() / 100 * settings.dayFontSize).toInt()
                     )
                     tmp_left = -320
                 }
                 dayLayout.setStart(
                         tmp_left,
                         (settings.dayTop - settings.font_ratio.toFloat() / 100 * settings.dayFontSize).toInt()
                 )
                 //Add it to the list
                 slpt_objects.add(dayLayout)
             }
             // Draw month
             if (settings.monthBool) { // JAVA calendar get/show time library
                 val calendar = Calendar.getInstance()
                 val month = calendar[Calendar.MONTH]
                 val monthLayout = SlptLinearLayout()
                 // if as text
                 if (settings.month_as_text) {
                     monthLayout.add(SlptMonthLView())
                     // Fix 00 type of month
                     if (month >= 9) { // 9: October, 10: November, 11: December
                         months_3let[settings.language][0] = months_3let[settings.language][10]
                         months_3let[settings.language][1] = months_3let[settings.language][11]
                         months_3let[settings.language][2] = months_3let[settings.language][12]
                         months[settings.language][0] = months[settings.language][10]
                         months[settings.language][1] = months[settings.language][11]
                         months[settings.language][2] = months[settings.language][12]
                     }
                     if (settings.three_letters_month_if_text) {
                         monthLayout.setStringPictureArrayForAll(months_3let[settings.language])
                     } else {
                         monthLayout.setStringPictureArrayForAll(months[settings.language])
                     }
                     // if as number
                 } else { // show first digit
                     if (month >= 9 || !settings.no_0_on_hour_first_digit) {
                         monthLayout.add(SlptMonthHView())
                     }
                     monthLayout.add(SlptMonthLView())
                 }
                 monthLayout.setTextAttrForAll(
                         settings.monthFontSize,
                         settings.monthColor,
                         timeTypeFace)
                 // Position based on screen on
                 monthLayout.alignX = 2
                 monthLayout.alignY = 0
                 tmp_left = settings.monthLeft.toInt()
                 if (!settings.monthAlignLeft) { // If text is centered, set rectangle
                     monthLayout.setRect(
                             (2 * tmp_left + 640),
                             (settings.font_ratio.toFloat() / 100 * settings.monthFontSize).toInt()
                     )
                     tmp_left = -320
                 }
                 monthLayout.setStart(
                         tmp_left,
                         (settings.monthTop - settings.font_ratio.toFloat() / 100 * settings.monthFontSize).toInt()
                 )
                 //Add it to the list
                 slpt_objects.add(monthLayout)
             }
             // Draw year number
             if (settings.yearBool) {
                 val yearLayout = SlptLinearLayout()
                 yearLayout.add(SlptYear3View())
                 yearLayout.add(SlptYear2View())
                 yearLayout.add(SlptYear1View())
                 yearLayout.add(SlptYear0View())
                 yearLayout.setTextAttrForAll(
                         settings.yearFontSize,
                         settings.yearColor,
                         timeTypeFace
                 )
                 // Position based on screen on
                 yearLayout.alignX = 2
                 yearLayout.alignY = 0
                 tmp_left = settings.yearLeft.toInt()
                 if (!settings.yearAlignLeft) { // If text is centered, set rectangle
                     yearLayout.setRect(
                             (2 * tmp_left + 640),
                             (settings.font_ratio.toFloat() / 100 * settings.yearFontSize).toInt()
                     )
                     tmp_left = -320
                 }
                 yearLayout.setStart(
                         tmp_left,
                         (settings.yearTop - settings.font_ratio.toFloat() / 100 * settings.yearFontSize).toInt()
                 )
                 //Add it to the list
                 slpt_objects.add(yearLayout)
             }
             // Set day name font
             val weekfont = getTypeFace(service.resources, settings.font)
             // Draw day name
             if (settings.weekdayBool) {
                 val WeekdayLayout = SlptLinearLayout()
                 WeekdayLayout.add(SlptWeekView())
                 if (settings.three_letters_day_if_text) {
                     WeekdayLayout.setStringPictureArrayForAll(days_3let[settings.language])
                 } else {
                     WeekdayLayout.setStringPictureArrayForAll(days[settings.language])
                 }
                 WeekdayLayout.setTextAttrForAll(
                         settings.weekdayFontSize,
                         settings.weekdayColor,
                         weekfont
                 )
                 // Position based on screen on
                 WeekdayLayout.alignX = 2
                 WeekdayLayout.alignY = 0
                 tmp_left = settings.weekdayLeft.toInt()
                 if (!settings.weekdayAlignLeft) { // If text is centered, set rectangle
                     WeekdayLayout.setRect(
                             (2 * tmp_left + 640),
                             (settings.font_ratio.toFloat() / 100 * settings.weekdayFontSize).toInt()
                     )
                     tmp_left = -320
                 }
                 WeekdayLayout.setStart(
                         tmp_left,
                         (settings.weekdayTop - settings.font_ratio.toFloat() / 100 * settings.weekdayFontSize).toInt()
                 )
                 //Add it to the list
                 slpt_objects.add(WeekdayLayout)
             }*/
        return slpt_objects
    }

    companion object {
        private const val TAG = "VergeIT-LOG"
        // Languages
        var codes = arrayOf(
                "English", "Български", "中文", "Hrvatski", "Czech", "Dansk", "Nederlands", "Français", "Deutsch", "Ελληνικά", "עברית", "Magyar", "Italiano", "日本語", "한국어", "Polski", "Português", "Română", "Русский", "Slovenčina", "Español", "ไทย", "Türkçe", "Tiếng Việt"
        )
        private val days = arrayOf(arrayOf("SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"), arrayOf("НЕДЕЛЯ", "ПОНЕДЕЛНИК", "ВТОРНИК", "СРЯДА", "ЧЕТВЪРТЪК", "ПЕТЪК", "СЪБОТА"), arrayOf("星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"), arrayOf("NEDJELJA", "PONEDJELJAK", "UTORAK", "SRIJEDA", "ČETVRTAK", "PETAK", "SUBOTA"), arrayOf("NEDĚLE", "PONDĚLÍ", "ÚTERÝ", "STŘEDA", "ČTVRTEK", "PÁTEK", "SOBOTA"), arrayOf("SØNDAG", "MANDAG", "TIRSDAG", "ONSDAG", "TORSDAG", "FREDAG", "LØRDAG"), arrayOf("ZONDAG", "MAANDAG", "DINSDAG", "WOENSDAG", "DONDERDAG", "VRIJDAG", "ZATERDAG"), arrayOf("DIMANCHE", "LUNDI", "MARDI", "MERCREDI", "JEUDI", "VENDREDI", "SAMEDI"), arrayOf("SONNTAG", "MONTAG", "DIENSTAG", "MITTWOCH", "DONNERSTAG", "FREITAG", "SAMSTAG"), arrayOf("ΚΥΡΙΑΚΉ", "ΔΕΥΤΈΡΑ", "ΤΡΊΤΗ", "ΤΕΤΆΡΤΗ", "ΠΈΜΠΤΗ", "ΠΑΡΑΣΚΕΥΉ", "ΣΆΒΒΑΤΟ"), arrayOf("ש'", "ו'", "ה'", "ד'", "ג'", "ב'", "א'"), arrayOf("VASÁRNAP", "HÉTFŐ", "KEDD", "SZERDA", "CSÜTÖRTÖK", "PÉNTEK", "SZOMBAT"), arrayOf("DOMENICA", "LUNEDÌ", "MARTEDÌ", "MERCOLEDÌ", "GIOVEDÌ", "VENERDÌ", "SABATO"), arrayOf("日曜日", "月曜日", "火曜日", "水曜日", "木曜日", "金曜日", "土曜日"), arrayOf("일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"), arrayOf("NIEDZIELA", "PONIEDZIAŁEK", "WTOREK", "ŚRODA", "CZWARTEK", "PIĄTEK", "SOBOTA"), arrayOf("DOMINGO", "SEGUNDA", "TERÇA", "QUARTA", "QUINTA", "SEXTA", "SÁBADO"), arrayOf("DUMINICĂ", "LUNI", "MARȚI", "MIERCURI", "JOI", "VINERI", "SÂMBĂTĂ"), arrayOf("ВОСКРЕСЕНЬЕ", "ПОНЕДЕЛЬНИК", "ВТОРНИК", "СРЕДА", "ЧЕТВЕРГ", "ПЯТНИЦА", "СУББОТА"), arrayOf("NEDEĽA", "PONDELOK", "UTOROK", "STREDA", "ŠTVRTOK", "PIATOK", "SOBOTA"), arrayOf("DOMINGO", "LUNES", "MARTES", "MIÉRCOLES", "JUEVES", "VIERNES", "SÁBADO"), arrayOf("อาทิตย์", "จันทร์", "อังคาร", "พุธ", "พฤหัสบดี", "ุกร์", "สาร์"), arrayOf("PAZAR", "PAZARTESI", "SALı", "ÇARŞAMBA", "PERŞEMBE", "CUMA", "CUMARTESI"), arrayOf("CHỦ NHẬT", "THỨ 2", "THỨ 3", "THỨ 4", "THỨ 5", "THỨ 6", "THỨ 7"))
        @JvmField
        var days_3let = arrayOf(arrayOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"), arrayOf("НЕД", "ПОН", "ВТО", "СРЯ", "ЧЕТ", "ПЕТ", "СЪБ"), arrayOf("星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"), arrayOf("NED", "PON", "UTO", "SRI", "ČET", "PET", "SUB"), arrayOf("NE", "PO", "ÚT", "ST", "ČT", "PÁ", "SO"), arrayOf("SØN", "MAN", "TIR", "ONS", "TOR", "FRE", "LØR"), arrayOf("ZON", "MAA", "DIN", "WOE", "DON", "VRI", "ZAT"), arrayOf("DIM", "LUN", "MAR", "MER", "JEU", "VEN", "SAM"), arrayOf("SO", "MO", "DI", "MI", "DO", "FR", "SA"), arrayOf("ΚΥΡ", "ΔΕΥ", "ΤΡΙ", "ΤΕΤ", "ΠΕΜ", "ΠΑΡ", "ΣΑΒ"), arrayOf("א'", "ב'", "ג'", "ד'", "ה'", "ו'", "ש'"), arrayOf("VAS", "HÉT", "KED", "SZE", "CSÜ", "PÉN", "SZO"), arrayOf("DOM", "LUN", "MAR", "MER", "GIO", "VEN", "SAB"), arrayOf("日曜日", "月曜日", "火曜日", "水曜日", "木曜日", "金曜日", "土曜日"), arrayOf("일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"), arrayOf("NIE", "PON", "WTO", "ŚRO", "CZW", "PIĄ", "SOB"), arrayOf("DOM", "SEG", "TER", "QUA", "QUI", "SEX", "SÁB"), arrayOf("DUM", "LUN", "MAR", "MIE", "JOI", "VIN", "SÂM"), arrayOf("ВСК", "ПНД", "ВТР", "СРД", "ЧТВ", "ПТН", "СБТ"), arrayOf("NED", "PON", "UTO", "STR", "ŠTV", "PIA", "SOB"), arrayOf("DOM", "LUN", "MAR", "MIÉ", "JUE", "VIE", "SÁB"), arrayOf("อา.", "จ.", "อ.", "พ.", "พฤ.", "ศ.", "ส."), arrayOf("PAZ", "PZT", "SAL", "ÇAR", "PER", "CUM", "CMT"), arrayOf("CN", "T2", "T3", "T4", "T5", "T6", "T7"))
        private val months = arrayOf(arrayOf("DECEMBER", "JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"), arrayOf("ДЕКЕМВРИ", "ЯНУАРИ", "ФЕВРУАРИ", "МАРТ", "АПРИЛ", "МАЙ", "ЮНИ", "ЮЛИ", "АВГУСТ", "СЕПТЕМВРИ", "ОКТОМВРИ", "НОЕМВРИ", "ДЕКЕМВРИ"), arrayOf("十二月", "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"), arrayOf("PROSINAC", "SIJEČANJ", "VELJAČA", "OŽUJAK", "TRAVANJ", "SVIBANJ", "LIPANJ", "SRPANJ", "KOLOVOZ", "RUJAN", "LISTOPAD", "STUDENI", "PROSINAC"), arrayOf("PROSINEC", "LEDEN", "ÚNOR", "BŘEZEN", "DUBEN", "KVĚTEN", "ČERVEN", "ČERVENEC", "SRPEN", "ZÁŘÍ", "ŘÍJEN", "LISTOPAD", "PROSINEC"), arrayOf("DECEMBER", "JANUAR", "FEBRUAR", "MARTS", "APRIL", "MAJ", "JUNI", "JULI", "AUGUST", "SEPTEMBER", "OKTOBER", "NOVEMBER", "DECEMBER"), arrayOf("DECEMBER", "JANUARI", "FEBRUARI", "MAART", "APRIL", "MEI", "JUNI", "JULI", "AUGUSTUS", "SEPTEMBER", "OKTOBER", "NOVEMBER", "DECEMBER"), arrayOf("DÉCEMBRE", "JANVIER", "FÉVRIER", "MARS", "AVRIL", "MAI", "JUIN", "JUILLET", "AOÛT", "SEPTEMBRE", "OCTOBRE", "NOVEMBRE", "DÉCEMBRE"), arrayOf("DEZEMBER", "JANUAR", "FEBRUAR", "MÄRZ", "APRIL", "MAI", "JUNI", "JULI", "AUGUST", "SEPTEMBER", "OKTOBER", "NOVEMBER", "DEZEMBER"), arrayOf("ΔΕΚΈΜΒΡΙΟΣ", "ΙΑΝΟΥΆΡΙΟΣ", "ΦΕΒΡΟΥΆΡΙΟΣ", "ΜΆΡΤΙΟΣ", "ΑΠΡΊΛΙΟΣ", "ΜΆΙΟΣ", "ΙΟΎΝΙΟΣ", "ΙΟΎΛΙΟΣ", "ΑΎΓΟΥΣΤΟΣ", "ΣΕΠΤΈΜΒΡΙΟΣ", "ΟΚΤΏΒΡΙΟΣ", "ΝΟΈΜΒΡΙΟΣ", "ΔΕΚΈΜΒΡΙΟΣ"), arrayOf("דצמבר", "ינואר", "פברואר", "מרץ", "אפריל", "מאי", "יוני", "יולי", "אוגוסט", "ספטמבר", "אוקטובר", "נובמבר", "דצמבר"), arrayOf("DECEMBER", "JANUÁR", "FEBRUÁR", "MÁRCIUS", "ÁPRILIS", "MÁJUS", "JÚNIUS", "JÚLIUS", "AUGUSZTUS", "SZEPTEMBER", "OKTÓBER", "NOVEMBER", "DECEMBER"), arrayOf("DICEMBRE", "GENNAIO", "FEBBRAIO", "MARZO", "APRILE", "MAGGIO", "GIUGNO", "LUGLIO", "AGOSTO", "SETTEMBRE", "OTTOBRE", "NOVEMBRE", "DICEMBRE"), arrayOf("12月", "1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"), arrayOf("12월", "1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"), arrayOf("GRUDZIEŃ", "STYCZEŃ", "LUTY", "MARZEC", "KWIECIEŃ", "MAJ", "CZERWIEC", "LIPIEC", "SIERPIEŃ", "WRZESIEŃ", "PAŹDZIERNIK", "LISTOPAD", "GRUDZIEŃ"), arrayOf("DEZEMBRO", "JANEIRO", "FEVEREIRO", "MARÇO", "ABRIL", "MAIO", "JUNHO", "JULHO", "AGOSTO", "SETEMBRO", "OUTUBRO", "NOVEMBRO", "DEZEMBRO"), arrayOf("DECEMBRIE", "IANUARIE", "FEBRUARIE", "MARTIE", "APRILIE", "MAI", "IUNIE", "IULIE", "AUGUST", "SEPTEMBRIE", "OCTOMBRIE", "NOIEMBRIE", "DECEMBRIE"), arrayOf("ДЕКАБРЬ", "ЯНВАРЬ", "ФЕВРАЛЬ", "МАРТ", "АПРЕЛЬ", "МАЙ", "ИЮНЬ", "ИЮЛЬ", "АВГУСТ", "СЕНТЯБРЬ", "ОКТЯБРЬ", "НОЯБРЬ", "ДЕКАБРЬ"), arrayOf("DECEMBER", "JANUÁR", "FEBRUÁR", "MAREC", "APRÍL", "MÁJ", "JÚN", "JÚL", "AUGUST", "SEPTEMBER", "OKTÓBER", "NOVEMBER", "DECEMBER"), arrayOf("DICIEMBRE", "ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"), arrayOf("ันวาคม", "มกราคม", "กุมภาพันธ์", "ีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายน", "กรกฎาคม", "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน"), arrayOf("ARALıK", "OCAK", "ŞUBAT", "MART", "NISAN", "MAYıS", "HAZIRAN", "TEMMUZ", "AĞUSTOS", "EYLÜL", "EKIM", "KASıM", "ARALıK"), arrayOf("THÁNG 12", "THÁNG 1", "THÁNG 2", "THÁNG 3", "THÁNG 4", "THÁNG 5", "THÁNG 6", "THÁNG 7", "THÁNG 8", "THÁNG 9", "THÁNG 10", "THÁNG 11", "THÁNG 12"))
        private val months_3let = arrayOf(arrayOf("DEC", "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"), arrayOf("ДЕК", "ЯНУ", "ФЕВ", "МАР", "АПР", "МАЙ", "ЮНИ", "ЮЛИ", "АВГ", "СЕП", "ОКТ", "НОЕ", "ДЕК"), arrayOf("十二月", "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"), arrayOf("PRO", "SIJ", "VE", "OŽU", "TRA", "SVI", "LIP", "SRP", "KOL", "RUJ", "LIS", "STU", "PRO"), arrayOf("PRO", "LED", "ÚNO", "BŘE", "DUB", "KVĚ", "ČER", "ČER", "SRP", "ZÁŘ", "ŘÍJ", "LIS", "PRO"), arrayOf("DEC", "JAN", "FEB", "MAR", "APR", "MAJ", "JUN", "JUL", "AUG", "SEP", "OKT", "NOV", "DEC"), arrayOf("DEC", "JAN", "FEB", "MAA", "APR", "MEI", "JUN", "JUL", "AUG", "SEP", "OKT", "NOV", "DEC"), arrayOf("DÉC", "JAN", "FÉV", "MAR", "AVR", "MAI", "JUI", "JUI", "AOÛ", "SEP", "OCT", "NOV", "DÉC"), arrayOf("DEZ", "JAN", "FEB", "MÄR", "APR", "MAI", "JUN", "JUL", "AUG", "SEP", "OKT", "NOV", "DEZ"), arrayOf("ΔΕΚ", "ΙΑΝ", "ΦΕΒ", "ΜΑΡ", "ΑΠΡ", "ΜΑΙ", "ΙΟΥΝ", "ΙΟΥΛ", "ΑΥΓ", "ΣΕΠ", "ΟΚΤ", "ΝΟΕ", "ΔΕΚ"), arrayOf("דצמ", "ינו", "פבר", "מרץ", "אפר", "מאי", "יונ", "יול", "אוג", "ספט", "אוק", "נוב", "דצמ"), arrayOf("DEC", "JAN", "FEB", "MÁR", "ÁPR", "MÁJ", "JÚN", "JÚL", "AUG", "SZE", "OKT", "NOV", "DEC"), arrayOf("DIC", "GEN", "FEB", "MAR", "APR", "MAG", "GIU", "LUG", "AGO", "SET", "OTT", "NOV", "DIC"), arrayOf("12月", "1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"), arrayOf("12월", "1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"), arrayOf("GRU", "STY", "LUT", "MAR", "KWI", "MAJ", "CZE", "LIP", "SIE", "WRZ", "PAŹ", "LIS", "GRU"), arrayOf("DEZ", "JAN", "FEV", "MAR", "ABR", "MAI", "JUN", "JUL", "AGO", "SET", "OUT", "NOV", "DEZ"), arrayOf("DEC", "IAN", "FEB", "MAR", "APR", "MAI", "IUN", "IUL", "AUG", "SEP", "OCT", "NOI", "DEC"), arrayOf("ДЕК", "ЯНВ", "ФЕВ", "МАР", "АПР", "МАЙ", "ИЮН", "ИЮЛ", "АВГ", "СЕН", "ОКТ", "НОЯ", "ДЕК"), arrayOf("DEC", "JAN", "FEB", "MAR", "APR", "MÁJ", "JÚN", "JÚL", "AUG", "SEP", "OKT", "NOV", "DEC"), arrayOf("DIC", "ENE", "FEB", "MAR", "ABR", "MAY", "JUN", "JUL", "AGO", "SEP", "OCT", "NOV", "DIC"), arrayOf("ธ.ค.", "ม.ค.", "ก.พ.", "มี.ค.", "เม.ย.", "พ.ค.", "มิ.ย.", "ก.ค.", "ส.ค.", "ก.ย.", "ต.ค.", "พ.ย.", "ธ.ค."), arrayOf("ARA", "OCA", "ŞUB", "MAR", "NIS", "MAY", "HAZ", "TEM", "AĞU", "EYL", "EKI", "KAS", "ARA"), arrayOf("T12", "T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12"))
    }

}