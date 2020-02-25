package com.dinodevs.greatfitwatchface.theme

import android.content.Context
import com.dinodevs.greatfitwatchface.theme.bin.*
import com.huami.watch.watchface.util.Util
import org.json.JSONException
import java.io.File
import java.io.IOException

class GtrTheme private constructor() : ITheme {
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
        get() = arrayOf("gtr/0028.png", "gtr/0029.png", "gtr/0030.png", "gtr/0031.png", "gtr/0032.png", "gtr/0033.png", "gtr/0034.png",
                "gtr/0035.png", "gtr/0036.png", "gtr/0037.png", "gtr/0038.png")

    override val batterySpec: Text
        get() {
            val res = Text()
            res.topLeftX = 138
            res.topLeftY = 36
            res.bottomRightX = 180
            res.bottomRightY = 57
            res.alignment = "Center"
            res.spacing = 0
            res.imageIndex = 28
            res.imagesCount = 10
            return res
        }

    /*"CenterX": 160,
      "CenterY": 160,
      "RadiusX": 145,
      "RadiusY": 145,
      "StartAngle": 318,
      "EndAngle": 402,
      "Width": 20,
      "Color": "0x000000000000FE00",
      "Flatness": 0*/
    override val batteryScale: Scale
        get() { /*"CenterX": 160,
      "CenterY": 160,
      "RadiusX": 145,
      "RadiusY": 145,
      "StartAngle": 318,
      "EndAngle": 402,
      "Width": 20,
      "Color": "0x000000000000FE00",
      "Flatness": 0*/
            val res = Scale()
            res.centerX = 160
            res.centerY = 160
            res.radiusX = 145
            res.radiusY = 145
            res.startAngle = 318
            res.endAngle = 402
            res.width = 20
            res.color = "0x000000000000FE00"
            res.flatness = 0
            return res
        }

    override val time: Time
        get() {
            val res = Time()
            val hours = Hours()
            var tens = Tens()
            tens.x = 109
            tens.y = 78
            tens.imageIndex = 8
            tens.imagesCount = 10
            var ones = Ones()
            ones.x = 162
            ones.y = 78
            ones.imageIndex = 8
            ones.imagesCount = 10
            hours.tens = tens
            hours.ones = ones
            tens = Tens()
            tens.x = 109
            tens.y = 169
            tens.imageIndex = 18
            tens.imagesCount = 10
            ones = Ones()
            ones.x = 162
            ones.y = 169
            ones.imageIndex = 18
            ones.imagesCount = 10
            val minutes = Minutes()
            minutes.tens = tens
            minutes.ones = ones
            res.hours = hours
            res.minutes = minutes
            val amPm = AmPm()
            amPm.x = 124
            amPm.y = 300
            amPm.imageIndexAMCN = 43
            amPm.imageIndexPMCN = 44
            amPm.imageIndexAMEN = 43
            amPm.imageIndexPMEN = 44
            res.amPm = amPm
            return res
        }

    @get:Throws(Exception::class)
    override val timeHand: String?
        get() {
            throw Exception("Not implemented")
        }

    override fun getImagePath(imageIndex: Int): String {
        return String.format("gtr/%04d.png", imageIndex)
    }

    override fun getAmPm(amPm: Int): String {
        return if (amPm == 0) {
            getImagePath(time.amPm!!.imageIndexAMEN!!)
        } else {
            getImagePath(time.amPm!!.imageIndexPMEN!!)
        }
    }

    override val calories: Calories
        get() {
            val res = Calories()
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
     * "TopLeftX": 223,
     * "TopLeftY": 171,
     * "BottomRightX": 286,
     * "BottomRightY": 191,
     * "Alignment": "Center",
     * "Spacing": 0,
     * "ImageIndex": 28,
     * "ImagesCount": 10
     */
    override val pulse: Pulse
        get() {
            /**
             *
             * "TopLeftX": 223,
             * "TopLeftY": 171,
             * "BottomRightX": 286,
             * "BottomRightY": 191,
             * "Alignment": "Center",
             * "Spacing": 0,
             * "ImageIndex": 28,
             * "ImagesCount": 10
             */
            val res = Pulse()
            res.topLeftX = 223
            res.topLeftY = 171
            res.bottomRightX = 286
            res.bottomRightY = 191
            res.alignment = "Center"
            res.spacing = 0
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
     * "Steps": {
     * "Step": {
     * "TopLeftX": 33,
     * "TopLeftY": 148,
     * "BottomRightX": 97,
     * "BottomRightY": 168,
     * "Alignment": "Center",
     * "Spacing": 0,
     * "ImageIndex": 28,
     * "ImagesCount": 10
     * }
     * },
     */
    override val stepWidget: Steps
        get() {
            val res = Steps()
            /**
             * "Steps": {
             * "Step": {
             * "TopLeftX": 33,
             * "TopLeftY": 148,
             * "BottomRightX": 97,
             * "BottomRightY": 168,
             * "Alignment": "Center",
             * "Spacing": 0,
             * "ImageIndex": 28,
             * "ImagesCount": 10
             * }
             * },
             */
            val step = Step()
            step.topLeftX = 33
            step.topLeftY = 148
            step.bottomRightX = 97
            step.bottomRightY = 168
            step.alignment = "Center"
            step.spacing = 0
            step.imageIndex = 28
            step.imagesCount = 10
            res.step = step
            return res
        }

    /***
     * "StepsProgress": {
     * "Circle": {
     * "CenterX": 160,
     * "CenterY": 160,
     * "RadiusX": 145,
     * "RadiusY": 145,
     * "StartAngle": 219,
     * "EndAngle": 296,
     * "Width": 20,
     * "Color": "0x000000000001BDF1",
     * "Flatness": 180
     * }
     * },
     */
    override val stepProgress: StepsProgress
        get() {
            val res = StepsProgress()
            val circle = Circle()
            /***
             * "StepsProgress": {
             * "Circle": {
             * "CenterX": 160,
             * "CenterY": 160,
             * "RadiusX": 145,
             * "RadiusY": 145,
             * "StartAngle": 219,
             * "EndAngle": 296,
             * "Width": 20,
             * "Color": "0x000000000001BDF1",
             * "Flatness": 180
             * }
             * },
             */
            circle.centerX = 160
            circle.centerY = 160
            circle.radiusX = 145
            circle.radiusX = 145
            circle.startAngle = 219
            circle.endAngle = 296
            circle.width = 20
            circle.color = "0x000000000001BDF1"
            circle.flatness = 180
            res.circle = circle
            return res
        }

    companion object {
        @Throws(IOException::class, JSONException::class)
        fun load(context: Context?, path: String?): ITheme {
            val json = File(path)
            val data = Util.assetToBytes(context, path)
            return GtrTheme()
        }
    }
}