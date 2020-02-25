package com.dinodevs.greatfitwatchface.theme.bin

import java.util.*

class MonthAndDay {
    var separate: Separate? = null
    var twoDigitsMonth: Boolean? = null
    var twoDigitsDay: Boolean? = null
    private val additionalProperties: MutableMap<String, Any> = HashMap()

    fun getAdditionalProperties(): Map<String, Any> {
        return additionalProperties
    }

    fun setAdditionalProperty(name: String, value: Any) {
        additionalProperties[name] = value
    }
}