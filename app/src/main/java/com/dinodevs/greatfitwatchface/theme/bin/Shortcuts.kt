package com.dinodevs.greatfitwatchface.theme.bin

import java.util.*

class Shortcuts {
    var state: State? = null
    var pulse: Pulse? = null
    var weather: Weather? = null
    private val additionalProperties: MutableMap<String, Any> = HashMap()

    fun getAdditionalProperties(): Map<String, Any> {
        return additionalProperties
    }

    fun setAdditionalProperty(name: String, value: Any) {
        additionalProperties[name] = value
    }
}