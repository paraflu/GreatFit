package com.dinodevs.greatfitwatchface.theme.bin

import java.util.*

class Separate {
    var month: Month? = null
    var day: Day? = null
    private val additionalProperties: MutableMap<String, Any> = HashMap()

    fun getAdditionalProperties(): Map<String, Any> {
        return additionalProperties
    }

    fun setAdditionalProperty(name: String, value: Any) {
        additionalProperties[name] = value
    }
}