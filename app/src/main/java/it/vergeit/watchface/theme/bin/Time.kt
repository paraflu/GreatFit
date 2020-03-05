package it.vergeit.watchface.theme.bin

import com.google.gson.annotations.SerializedName


data class Time(

        @SerializedName("Hours") val hours: Hours?,
        @SerializedName("Minutes") val minutes: Minutes?,
        @SerializedName("AmPm") val amPm: AmPm?
)