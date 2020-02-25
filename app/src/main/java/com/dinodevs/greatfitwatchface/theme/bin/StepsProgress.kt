package com.dinodevs.greatfitwatchface.theme.bin

import java.util.*

class StepsProgress {
    var circle: Circle? = null
    private val additionalProperties: MutableMap<String, Any> = HashMap()

    fun getAdditionalProperties(): Map<String, Any> {
        return additionalProperties
    }

    fun setAdditionalProperty(name: String, value: Any) {
        additionalProperties[name] = value
    }
}