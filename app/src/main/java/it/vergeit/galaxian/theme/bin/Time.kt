package it.vergeit.galaxian.theme.bin

import com.google.gson.annotations.SerializedName


data class Time(
        @SerializedName("Hours") val hours: Hours?,
        @SerializedName("Minutes") val minutes: Minutes?,
        @SerializedName("Seconds") val seconds: Seconds?,
        @SerializedName("AmPm") val amPm: AmPm?,
        @SerializedName("Delimiter") val delimiter: Image?
)