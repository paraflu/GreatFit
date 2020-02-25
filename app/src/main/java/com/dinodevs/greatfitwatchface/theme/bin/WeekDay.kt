package com.dinodevs.greatfitwatchface.theme.bin

import java.util.*

class WeekDay {
    var x: Int? = null
    var y: Int? = null
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