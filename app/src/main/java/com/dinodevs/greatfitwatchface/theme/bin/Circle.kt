package com.dinodevs.greatfitwatchface.theme.bin

import java.util.*

class Circle {
    var centerX: Int? = null
    var centerY: Int? = null
    var radiusX: Int? = null
    var radiusY: Int? = null
    var startAngle: Int? = null
    var endAngle: Int? = null
    var width: Int? = null
    var color: String? = null
    var flatness: Int? = null
    private val additionalProperties: MutableMap<String, Any> = HashMap()

    fun getAdditionalProperties(): Map<String, Any> {
        return additionalProperties
    }

    fun setAdditionalProperty(name: String, value: Any) {
        additionalProperties[name] = value
    }
}