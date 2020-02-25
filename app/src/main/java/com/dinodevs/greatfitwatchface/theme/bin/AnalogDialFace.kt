package com.dinodevs.greatfitwatchface.theme.bin

import java.util.*

class AnalogDialFace {
    var hourCenterImage: HourCenterImage? = null
    var minCenterImage: MinCenterImage? = null
    private val additionalProperties: MutableMap<String, Any> = HashMap()

    fun getAdditionalProperties(): Map<String, Any> {
        return additionalProperties
    }

    fun setAdditionalProperty(name: String, value: Any) {
        additionalProperties[name] = value
    }
}