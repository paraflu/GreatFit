package it.vergeit.gtrclassic.theme.bin

import com.google.gson.annotations.SerializedName

class Calories : IText {

    @SerializedName("TopLeftX")
    override val topLeftX: Int = 0
    @SerializedName("TopLeftY")
    override val topLeftY: Int = 0
    @SerializedName("BottomRightX")
    override val bottomRightX: Int = 0
    @SerializedName("BottomRightY")
    override val bottomRightY: Int = 0
    @SerializedName("Alignment")
    override val alignment: String = "Center"
    @SerializedName("Spacing")
    override val spacing: Int = 0
    @SerializedName("ImageIndex")
    override val imageIndex: Int = 0
    @SerializedName("ImagesCount")
    override val imagesCount: Int = 0
    @SerializedName("Circle")
    val circle: Circle? = null
}