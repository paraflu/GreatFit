package com.dinodevs.greatfitwatchface.theme.bin

import java.util.*

class AmPm {
    var x: Int? = null
    var y: Int? = null
    var imageIndexAMCN: Int? = null
    var imageIndexPMCN: Int? = null
    var imageIndexAMEN: Int? = null
    var imageIndexPMEN: Int? = null
    private val additionalProperties: MutableMap<String, Any> = HashMap()

    fun getAdditionalProperties(): Map<String, Any> {
        return additionalProperties
    }

    fun setAdditionalProperty(name: String, value: Any) {
        additionalProperties[name] = value
    }
}