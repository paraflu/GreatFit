package com.dinodevs.greatfitwatchface.theme

import com.dinodevs.greatfitwatchface.theme.bin.*

interface ITheme {
    val background: Background
    val battery: Array<String>
    val batterySpec: Text?
    val batteryScale: Scale?
    @get:Throws(Exception::class)
    val timeHand: String?

    val time: Time?
    fun getAmPm(amPm: Int): String
    fun getImagePath(imageIndex: Int): String
    val calories: Calories?
    val pulse: Pulse?
    val caloriesGraph: CaloriesGraph?
    val stepWidget: Steps?
    //    static ITheme Load(String path) {
    val stepProgress: StepsProgress?
//    };
}