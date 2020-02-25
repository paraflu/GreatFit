package com.dinodevs.greatfitwatchface.theme.bin

import java.util.*

class Month {
    var topLeftX: Int? = null
    var topLeftY: Int? = null
    var bottomRightX: Int? = null
    var bottomRightY: Int? = null
    var alignment: String? = null
    var spacing: Int? = null
    var imageIndex: Int? = null
    var imagesCount: Int? = null
    private val additionalProperties: MutableMap<String, Any> = HashMap()

    fun getAdditionalProperties(): Map<String, Any> {
        return additionalProperties
    }

    fun setAdditionalProperty(name: String, value: Any) {
        additionalProperties[name] = value
    }
}