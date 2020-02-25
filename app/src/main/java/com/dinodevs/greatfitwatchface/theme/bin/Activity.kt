package com.dinodevs.greatfitwatchface.theme.bin

import java.util.*

class Activity {
    var calories: Calories? = null
    var pulse: Pulse? = null
    var distance: Distance? = null
    var steps: Steps? = null
    var caloriesGraph: CaloriesGraph? = null
    private val additionalProperties: MutableMap<String, Any> = HashMap()

    fun getAdditionalProperties(): Map<String, Any> {
        return additionalProperties
    }

    fun setAdditionalProperty(name: String, value: Any) {
        additionalProperties[name] = value
    }
}