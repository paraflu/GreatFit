package com.dinodevs.greatfitwatchface.theme.bin

import java.util.*

class Distance {
    var number: Number? = null
    var suffixImageIndex: Int? = null
    var decimalPointImageIndex: Int? = null
    private val additionalProperties: MutableMap<String, Any> = HashMap()

    fun getAdditionalProperties(): Map<String, Any> {
        return additionalProperties
    }

    fun setAdditionalProperty(name: String, value: Any) {
        additionalProperties[name] = value
    }
}