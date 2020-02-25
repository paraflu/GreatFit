package com.dinodevs.greatfitwatchface.theme.bin

import java.util.*

class Pulse : IText {
    override var topLeftX: Int? = null
    override var topLeftY: Int? = null
    override var bottomRightX: Int? = null
    override var bottomRightY: Int? = null
    override var alignment: String? = null
    override var spacing: Int? = null
    override var imageIndex: Int? = null
    override var imagesCount: Int? = null

    private val additionalProperties: MutableMap<String, Any> = HashMap()

    fun getAdditionalProperties(): Map<String, Any> {
        return additionalProperties
    }

    fun setAdditionalProperty(name: String, value: Any) {
        additionalProperties[name] = value
    }
}