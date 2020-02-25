package com.dinodevs.greatfitwatchface.theme.bin

import java.util.*

class DoNotDisturb {
    var coordinates: Coordinates? = null
    var imageIndexOn: Int? = null
    private val additionalProperties: MutableMap<String, Any> = HashMap()

    fun getAdditionalProperties(): Map<String, Any> {
        return additionalProperties
    }

    fun setAdditionalProperty(name: String, value: Any) {
        additionalProperties[name] = value
    }
}