package it.vergeit.watchface.widget

import android.app.Service
import android.graphics.Canvas
import com.huami.watch.watchface.util.Util
import com.ingenic.iwds.slpt.view.core.SlptLinearLayout
import com.ingenic.iwds.slpt.view.core.SlptPictureView
import it.vergeit.watchface.settings.LoadSettings
import it.vergeit.watchface.theme.bin.Date
import it.vergeit.watchface.theme.bin.MonthAndDay
import it.vergeit.watchface.theme.bin.WeekDay
import com.ingenic.iwds.slpt.view.core.SlptViewComponent
import com.ingenic.iwds.slpt.view.digital.*
import java.util.*

class DateWidget() : TextWidget() {

    constructor(_settings: LoadSettings) : this() {
        settings = _settings
    }

    private var calendar: Calendar = Calendar.getInstance()
    private var date: Date? = null
    private var lastDate: String? = null

    override fun init(service: Service) {
        super.init(service)
        date = settings.theme.date
    }

    override fun buildSlptViewComponent(service: Service?): List<SlptViewComponent?>? {
        return buildSlptViewComponent(service, false)
    }

    private fun loadDigitArray(idx: Int, count: Int = 10, slpt: Boolean, slptBetter: Boolean): Array<ByteArray> {
        return (0 until count).map {
            Util.Bitmap2Bytes(getBitmap(idx + it, slpt, slptBetter))
        }.toTypedArray()
    }

    override fun buildSlptViewComponent(service: Service?, better_resolution: Boolean): List<SlptViewComponent?>? {
        mService = service!!
        val slptObjects = arrayListOf<SlptViewComponent>()
        val date = settings.theme.date
        if (date != null) {
            if (date.monthAndDay != null) {
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                slptObjects.addAll(drawMonthDaySlpt(month, day, date.monthAndDay, better_resolution))
            }
            if (date.weekDay != null) {
                val calendar = Calendar.getInstance()
                var weekDay = calendar[Calendar.DAY_OF_WEEK]
                if (weekDay == Calendar.SUNDAY) {
                    weekDay = 6
                } else {
                    weekDay -= 2
                }
                slptObjects.addAll(drawWeekdaySlpt(weekDay, date.weekDay, better_resolution))
            }
        }
        return slptObjects
    }

    private fun drawWeekdaySlpt(weekDay: Int, wdSpec: WeekDay, slptBetter: Boolean): ArrayList<SlptViewComponent> {
        val slptObjects = arrayListOf<SlptViewComponent>()
        val weekDayView = SlptWeekView()
        val weekDayImages = arrayListOf<ByteArray>(Util.Bitmap2Bytes(getBitmap(wdSpec.imageIndex + wdSpec.imagesCount, true, slptBetter)))
        weekDayImages.addAll((0 until wdSpec.imagesCount - 1).map {
            Util.Bitmap2Bytes(getBitmap(wdSpec.imageIndex + it, true, slptBetter))
        }.toTypedArray())
        weekDayView.setImagePictureArray(weekDayImages.toTypedArray())
        weekDayView.setStart(wdSpec.x, wdSpec.y)
        slptObjects.add(weekDayView)
        return slptObjects
    }

    private fun drawWeekday(canvas: Canvas, weekDay: Int, spec: WeekDay) {
        val bmp = getBitmap(spec.imageIndex + weekDay)
        canvas.drawBitmap(bmp, spec.x.toFloat(), spec.y.toFloat(), settings.mGPaint)
    }

    override fun draw(canvas: Canvas?, width: Float, height: Float, centerX: Float, centerY: Float) {
        val date = settings.theme.date
        if (date != null) {
            if (date.monthAndDay != null) {
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                drawMonthDay(canvas!!, month, day, date.monthAndDay)
            }
            if (date.weekDay != null) {
                val calendar = Calendar.getInstance()
                var weekDay = calendar[Calendar.DAY_OF_WEEK]
                if (weekDay == Calendar.SUNDAY) {
                    weekDay = 6
                } else {
                    weekDay -= 2
                }
                drawWeekday(canvas!!, weekDay, date.weekDay)
            }
        }
    }

    private fun drawMonthDaySlpt(month: Int, day: Int, monthAndDay: MonthAndDay, slptBetter: Boolean): Array<SlptViewComponent> {
        val result = ArrayList<SlptViewComponent>()
        if (monthAndDay.separate != null) {
            val monthNameSpec = monthAndDay.separate.monthName
            val monthSpec = monthAndDay.separate.month
            val daySpec = monthAndDay.separate.day
            if (monthNameSpec?.imageIndex != null) {
                var monthName = SlptPictureView()
                monthName.setImagePicture(Util.Bitmap2Bytes(getBitmap(monthNameSpec.imageIndex + month, true, slptBetter)))
                monthName.setStart(monthNameSpec.x, monthNameSpec.y)
                result.add(monthName)

            }

            if (monthSpec != null) {
                val layout = SlptLinearLayout()
                val monthHView = SlptMonthHView()
                monthHView.setImagePictureArray(loadDigitArray(monthSpec.imageIndex, monthSpec.imagesCount, true, slptBetter))
                layout.add(monthHView)

                val monthLView = SlptMonthLView()
                monthLView.setImagePictureArray(loadDigitArray(monthSpec.imageIndex, monthSpec.imagesCount, true, slptBetter))
                layout.add(monthLView)
                layout.setStart(monthSpec.topLeftX, monthSpec.topLeftY)
                result.add(layout)
            }

            if (daySpec != null) {
                //drawText(canvas, String.format("%02d", day), daySpec, monthAndDay.twoDigitsDay == false)
                val layout = SlptLinearLayout()
                val dayHView = SlptDayHView()
                dayHView.setImagePictureArray(loadDigitArray(daySpec.imageIndex, daySpec.imagesCount, true, slptBetter))
                layout.add(dayHView)

                val dayLView = SlptDayLView()
                dayLView.setImagePictureArray(loadDigitArray(daySpec.imageIndex, daySpec.imagesCount, true, slptBetter))
                layout.add(dayLView)
                layout.setStart(daySpec.topLeftX, daySpec.topLeftY)
                result.add(layout)

//                dateLayout.add(SlptDayHView())
//                dateLayout.add(SlptDayLView())
//                dateLayout.add(point) //add .
//                dateLayout.add(SlptMonthHView())
//                dateLayout.add(SlptMonthLView())
//                dateLayout.add(point2) //add .
//                dateLayout.add(SlptYear3View())
//                dateLayout.add(SlptYear2View())
//                dateLayout.add(SlptYear1View())
//                dateLayout.add(SlptYear0View())
            }
        }
        return result.toTypedArray()
    }

    private fun drawMonthDay(canvas: Canvas, month: Int, day: Int, monthAndDay: MonthAndDay) {
        if (monthAndDay.separate != null) {
            val monthNameSpec = monthAndDay.separate.monthName
            val monthSpec = monthAndDay.separate.month
            val daySpec = monthAndDay.separate.day
            if (monthNameSpec?.imageIndex != null) {
                drawBitmap(canvas, monthNameSpec.imageIndex + month, monthNameSpec.x, monthNameSpec.y)
            }

            if (monthSpec != null) {
                drawText(canvas, String.format("%02d", month), monthSpec, monthAndDay.twoDigitsDay == false)
            }

            if (daySpec != null) {
                drawText(canvas, String.format("%02d", day), daySpec, monthAndDay.twoDigitsDay == false)
            }
        }
    }
}