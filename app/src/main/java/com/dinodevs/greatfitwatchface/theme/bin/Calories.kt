package com.dinodevs.greatfitwatchface.theme.bin
import com.google.gson.annotations.SerializedName

data class Calories (

		@SerializedName("TopLeftX") val topLeftX : Int,
		@SerializedName("TopLeftY") val topLeftY : Int,
		@SerializedName("BottomRightX") val bottomRightX : Int,
		@SerializedName("BottomRightY") val bottomRightY : Int,
		@SerializedName("Alignment") val alignment : String,
		@SerializedName("Spacing") val spacing : Int,
		@SerializedName("ImageIndex") val imageIndex : Int,
		@SerializedName("ImagesCount") val imagesCount : Int,
		@SerializedName("Circle") val circle : Circle?
)