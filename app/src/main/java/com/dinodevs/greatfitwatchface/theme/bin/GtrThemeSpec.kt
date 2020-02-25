package com.dinodevs.greatfitwatchface.theme.bin

import java.util.*

class GtrThemeSpec {
    var info: Info? = null
    var background: Background? = null
    var time: Time? = null
    var activity: Activity? = null
    var date: Date? = null
    var stepsProgress: StepsProgress? = null
    var status: Status? = null
    var battery: Battery? = null
    var analogDialFace: AnalogDialFace? = null
    var shortcuts: Shortcuts? = null
    private val additionalProperties: MutableMap<String, Any> = HashMap()

    fun getAdditionalProperties(): Map<String, Any> {
        return additionalProperties
    }

    fun setAdditionalProperty(name: String, value: Any) {
        additionalProperties[name] = value
    }
}