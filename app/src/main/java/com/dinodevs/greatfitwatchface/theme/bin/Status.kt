package com.dinodevs.greatfitwatchface.theme.bin

import java.util.*

class Status {
    var bluetooth: Bluetooth? = null
    var alarm: Alarm? = null
    var doNotDisturb: DoNotDisturb? = null
    private val additionalProperties: MutableMap<String, Any> = HashMap()

    fun getAdditionalProperties(): Map<String, Any> {
        return additionalProperties
    }

    fun setAdditionalProperty(name: String, value: Any) {
        additionalProperties[name] = value
    }
}