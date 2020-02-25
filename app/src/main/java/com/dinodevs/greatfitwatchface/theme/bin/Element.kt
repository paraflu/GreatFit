package com.dinodevs.greatfitwatchface.theme.bin

import java.util.*

class Element {
    var topLeftX: Int? = null
    var topLeftY: Int? = null
    var width: Int? = null
    var height: Int? = null
    private val additionalProperties: MutableMap<String, Any> = HashMap()

    fun getAdditionalProperties(): Map<String, Any> {
        return additionalProperties
    }

    fun setAdditionalProperty(name: String, value: Any) {
        additionalProperties[name] = value
    }
}