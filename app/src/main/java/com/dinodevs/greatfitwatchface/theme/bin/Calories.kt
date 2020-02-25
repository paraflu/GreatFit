package com.dinodevs.greatfitwatchface.theme.bin

import java.util.*

class Calories() : IText {
    //    override val topLeftX: Int get() = field
//
//    override var topLeftY: Int? = null public get() {
//        return field
//    }
//    override var bottomRightX: Int? = null public get() {
//        return field
//    }
//    override var bottomRightY: Int? = null public get() {
//        return field
//    }
//    override var alignment: String? = null public get() {
//        return field
//    }
//    override var spacing: Int? = null public get() {
//        return field
//    }
//    override var imageIndex: Int? = null public get() {
//        return field
//    }
//    override var imagesCount: Int? = null public get() {
//        return field
//    }
    private val additionalProperties: MutableMap<String, Any> = HashMap()

    fun getAdditionalProperties(): Map<String, Any> {
        return additionalProperties
    }

    fun setAdditionalProperty(name: String, value: Any) {
        additionalProperties[name] = value
    }

    override var topLeftX: Int? = null
    override var topLeftY: Int? = null
    override var bottomRightX: Int? = null
    override var bottomRightY: Int? = null
    override var alignment: String? = null
    override var spacing: Int? = null
    override var imageIndex: Int? = null
    override var imagesCount: Int? = null
}