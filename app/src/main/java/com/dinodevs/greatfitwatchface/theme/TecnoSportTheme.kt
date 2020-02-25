package com.dinodevs.greatfitwatchface.theme

import android.content.Context
import com.dinodevs.greatfitwatchface.theme.bin.*
import com.huami.watch.watchface.util.Util
import org.json.JSONException
import java.io.File
import java.io.IOException

class TecnoSportTheme private constructor() : ITheme {
    override val background: Background
        get() {
            val bck = Background()
            val img = Image()
            img.x = 0
            img.y = 0
            img.imageIndex = 0
            bck.image = img
            return bck
        }

    override val battery: Array<String>
        get() = arrayOf("tecno_sport/0028.png", "tecno_sport/0029.png", "tecno_sport/0030.png", "tecno_sport/0031.png", "tecno_sport/0032.png", "tecno_sport/0033.png", "tecno_sport/0034.png",
                "tecno_sport/0035.png", "tecno_sport/0036.png", "tecno_sport/0037.png", "tecno_sport/0038.png")

    /**
     * "TopLeftX": 205,
     * "TopLeftY": 74,
     * "BottomRightX": 238,
     * "BottomRightY": 85,
     * "Alignment": "TopRight",
     * "Spacing": -2,
     * "ImageIndex": 70,
     * "ImagesCount": 10
     */
    override val batterySpec: Text
        get() {
            /**
             * "TopLeftX": 205,
             * "TopLeftY": 74,
             * "BottomRightX": 238,
             * "BottomRightY": 85,
             * "Alignment": "TopRight",
             * "Spacing": -2,
             * "ImageIndex": 70,
             * "ImagesCount": 10
             */
            val res = Text()
            res.topLeftX = 205
            res.topLeftY = 74
            res.bottomRightX = 238
            res.bottomRightY = 85
            res.alignment = "TopRight"
            res.spacing = -2
            res.imageIndex = 70
            res.imagesCount = 10
            return res
        }

    /*
       "CenterX": 158,
      "CenterY": 159,
      "RadiusX": 141,
      "RadiusY": 141,
      "StartAngle": 221,
      "EndAngle": 48,
      "Width": 20,
      "Color": "0x0000000000FF0000",
      "Flatness": 0
      */
    override val batteryScale: Scale
        get() { /*
       "CenterX": 158,
      "CenterY": 159,
      "RadiusX": 141,
      "RadiusY": 141,
      "StartAngle": 221,
      "EndAngle": 48,
      "Width": 20,
      "Color": "0x0000000000FF0000",
      "Flatness": 0
      */
            val res = Scale()
            res.centerX = 158
            res.centerY = 159
            res.radiusX = 141
            res.radiusY = 141
            res.startAngle = 221
            res.endAngle = 48
            res.width = 20
            res.color = "0x0000000000FF0000"
            res.flatness = 0
            return res
        }

    //        AmPm amPm = new AmPm();
//        amPm.setX(124);
//        amPm.setY(300);
//        amPm.setImageIndexAMCN(43);
//        amPm.setImageIndexPMCN(44);
//        amPm.setImageIndexAMEN(43);
//        amPm.setImageIndexPMEN(44);
//        res.setAmPm(amPm);
    override val time: Time
        get() {
            val res = Time()
            val hours = Hours()
            var tens = Tens()
            tens.x = 98
            tens.y = 126
            tens.imageIndex = 1
            tens.imagesCount = 10
            var ones = Ones()
            ones.x = 131
            ones.y = 126
            ones.imageIndex = 1
            ones.imagesCount = 10
            hours.tens = tens
            hours.ones = ones
            tens = Tens()
            tens.x = 180
            tens.y = 126
            tens.imageIndex = 1
            tens.imagesCount = 10
            ones = Ones()
            ones.x = 213
            ones.y = 126
            ones.imageIndex = 1
            ones.imagesCount = 10
            val minutes = Minutes()
            minutes.tens = tens
            minutes.ones = ones
            res.hours = hours
            res.minutes = minutes
            //        AmPm amPm = new AmPm();
//        amPm.setX(124);
//        amPm.setY(300);
//        amPm.setImageIndexAMCN(43);
//        amPm.setImageIndexPMCN(44);
//        amPm.setImageIndexAMEN(43);
//        amPm.setImageIndexPMEN(44);
//        res.setAmPm(amPm);
            return res
        }

    @get:Throws(Exception::class)
    override val timeHand: String?
        get() {
            throw Exception("Not implemented")
        }

    override fun getImagePath(imageIndex: Int): String {
        return String.format("tecno_sport/%04d.png", imageIndex)
    }

    override fun getAmPm(amPm: Int): String {
        return if (amPm == 0) {
            getImagePath(time.amPm?.imageIndexAMEN!!)
        } else {
            getImagePath(time.amPm?.imageIndexPMEN!!)
        }
    }

    /**
     * "CenterX": 85,
     * "CenterY": 95,
     * "RadiusX": 39,
     * "RadiusY": 39,
     * "StartAngle": 277,
     * "EndAngle": 444,
     * "Width": 12,
     * "Color": "0x000000000000E3FE",
     * "Flatness": 0
     */
    override val calories: Calories
        get() {
            val res = Calories()
            /**
             * "CenterX": 85,
             * "CenterY": 95,
             * "RadiusX": 39,
             * "RadiusY": 39,
             * "StartAngle": 277,
             * "EndAngle": 444,
             * "Width": 12,
             * "Color": "0x000000000000E3FE",
             * "Flatness": 0
             */
            res.topLeftX = 223
            res.topLeftY = 148
            res.bottomRightX = 286
            res.bottomRightY = 168
            res.alignment = "Center"
            res.spacing = 0
            res.imageIndex = 28
            res.imagesCount = 10
            return res
        }

    /**
     *
     * "TopLeftX": 134,
     * "TopLeftY": 53,
     * "BottomRightX": 185,
     * "BottomRightY": 74,
     * "Alignment": "Center",
     * "Spacing": -7,
     * "ImageIndex": 28,
     * "ImagesCount": 10
     */
    override val pulse: Pulse
        get() {
            /**
             *
             * "TopLeftX": 134,
             * "TopLeftY": 53,
             * "BottomRightX": 185,
             * "BottomRightY": 74,
             * "Alignment": "Center",
             * "Spacing": -7,
             * "ImageIndex": 28,
             * "ImagesCount": 10
             */
            val res = Pulse()
            res.topLeftX = 132
            res.topLeftY = 53
            res.bottomRightX = 185
            res.bottomRightY = 74
            res.alignment = "Center"
            res.spacing = -7
            res.imageIndex = 28
            res.imagesCount = 10
            return res
        }

    /**
     * CaloriesGraph
     * "Circle": {
     * "CenterX": 160,
     * "CenterY": 162,
     * "RadiusX": 144,
     * "RadiusY": 148,
     * "StartAngle": 142,
     * "EndAngle": 64,
     * "Width": 23,
     * "Color": "0x0000000000FE3E02",
     * "Flatness": 180
     * }
     */
    override val caloriesGraph: CaloriesGraph
        get() {
            val res = CaloriesGraph()
            val circle = Circle()
            /**
             * CaloriesGraph
             * "Circle": {
             * "CenterX": 160,
             * "CenterY": 162,
             * "RadiusX": 144,
             * "RadiusY": 148,
             * "StartAngle": 142,
             * "EndAngle": 64,
             * "Width": 23,
             * "Color": "0x0000000000FE3E02",
             * "Flatness": 180
             * }
             */
            circle.centerX = 160
            circle.centerY = 162
            circle.radiusX = 144
            circle.radiusY = 148
            circle.startAngle = 142
            circle.endAngle = 64
            circle.width = 23
            circle.color = "0x0000000000FE3E02"
            circle.flatness = 180
            res.circle = circle
            return res
        }

    /**
     * "TopLeftX": 40,
     * "TopLeftY": 102,
     * "BottomRightX": 123,
     * "BottomRightY": 123,
     * "Alignment": "Center",
     * "Spacing": -7,
     * "ImageIndex": 28,
     * "ImagesCount": 10
     */
    override val stepWidget: Steps
        get() {
            val res = Steps()
            /**
             * "TopLeftX": 40,
             * "TopLeftY": 102,
             * "BottomRightX": 123,
             * "BottomRightY": 123,
             * "Alignment": "Center",
             * "Spacing": -7,
             * "ImageIndex": 28,
             * "ImagesCount": 10
             */
            val step = Step()
            step.topLeftX = 40
            step.topLeftY = 102
            step.bottomRightX = 123
            step.bottomRightY = 123
            step.alignment = "Center"
            step.spacing = -7
            step.imageIndex = 28
            step.imagesCount = 10
            res.step = step
            return res
        }

    /***
     * "CenterX": 85,
     * "CenterY": 95,
     * "RadiusX": 39,
     * "RadiusY": 39,
     * "StartAngle": 277,
     * "EndAngle": 444,
     * "Width": 12,
     * "Color": "0x000000000000E3FE",
     * "Flatness": 0
     */
    override val stepProgress: StepsProgress
        get() {
            val res = StepsProgress()
            val circle = Circle()
            /***
             * "CenterX": 85,
             * "CenterY": 95,
             * "RadiusX": 39,
             * "RadiusY": 39,
             * "StartAngle": 277,
             * "EndAngle": 444,
             * "Width": 12,
             * "Color": "0x000000000000E3FE",
             * "Flatness": 0
             */
            circle.centerX = 85
            circle.centerY = 95
            circle.radiusX = 39
            circle.radiusX = 39
            circle.startAngle = 277
            circle.endAngle = 444
            circle.width = 12
            circle.color = "0x000000000000E3FE"
            circle.flatness = 0
            res.circle = circle
            return res
        }

    companion object {
        @kotlin.jvm.JvmStatic
        @Throws(IOException::class, JSONException::class)
        fun load(context: Context?, path: String?): ITheme {
            val json = File(path)
            val data = Util.assetToBytes(context, path)
            return TecnoSportTheme()
        }
    }
}