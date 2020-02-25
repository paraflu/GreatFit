package com.dinodevs.greatfitwatchface.theme.bin

import java.util.*

class Tens : IFont {
    override var x: Int? = null;
    override var y: Int? = null;
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