package it.vergeit.watchface

import android.graphics.Point
import android.os.Build
import java.util.*

class Screen {

    companion object {
        val BUILD_VERGE_MODELS = arrayOf("qogir", "qogirUS")

        val isVerge: Boolean
            get() = listOf<String>(*BUILD_VERGE_MODELS).contains(Build.PRODUCT)

        val width: Int
            get() = if (isVerge) 360 else 340

        val height: Int
            get() = width


        val center: Point
            get() =
                if (isVerge) Point(180, 179) else Point(160, 159)


    }
}