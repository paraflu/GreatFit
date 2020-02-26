package com.dinodevs.greatfitwatchface

import com.dinodevs.greatfitwatchface.settings.LoadSettings
import com.dinodevs.greatfitwatchface.widget.*
import com.huami.watch.watchface.AbstractSlptClock
import java.lang.ref.WeakReference

/**
 * Amazfit watch faces
 */
class GreatFit : AbstractWatchFace() {
    private val greatWidget: GreatWidget? = null
    override fun onCreate() {
        instance = WeakReference<GreatFit?>(this)
        // Load settings
        val settings = LoadSettings(this.applicationContext)
        clock = MainClock(settings)
        widgets.add(DateWidget(settings))
        if (settings.theme.activity.pulse != null) {
            widgets.add(HeartRateWidget(settings))
        }
        if (settings.theme.activity.steps != null) {
            widgets.add(StepsWidget(settings))
        }
        if (settings.theme.activity.calories != null) {
            widgets.add(CaloriesWidget(settings))
        }

//        if (settings.theme.battery != null) {
//            widgets.add(BatteryWidget(settings))
//        }
        //
//        if(settings.isHeartRate()) {
//            this.widgets.add(new HeartRateWidget(settings));
//        }
//        if(settings.isStepsRate()) {
//            this.widgets.add(new StepsWidget(settings));
//        }
//        if(settings.isTodayDistanceRate()) {
//            this.widgets.add(new SportTodayDistanceWidget(settings));
//        }
//        if(settings.isTotalDistanceRate()) {
//            this.widgets.add(new SportTotalDistanceWidget(settings));
//        }
//        if(settings.isCalories()) {
//            this.widgets.add(new CaloriesWidget(settings));
//        }
//        if(settings.isFloor()) {
//            this.widgets.add(new FloorWidget(settings));
//        }
//        if(settings.isBattery()) {
//            this.widgets.add(new BatteryWidget(settings));
//        }
//        if(settings.isWeather()) {
//            this.widgets.add(new WeatherWidget(settings));
//        }
//        if (settings.isMoonPhase()){
//            this.widgets.add(new MoonPhaseWidget(settings));
//        }
//        if(settings.isGreat()) {
//            this.greatWidget = new GreatWidget(settings);
//            this.widgets.add(this.greatWidget);
//        }
        status_bar(settings.status_bar, settings.status_barLeft, settings.status_barTop)
        super.onCreate()
    }

    private fun status_bar(isOn: Boolean, left: Int, top: Int) { // Show it or... show it off screen :P
        if (isOn) {
            notifyStatusBarPosition(
                    left.toFloat(),
                    top.toFloat()
            )
        } else {
            notifyStatusBarPosition(10.0f, 10.0f) // not working
        }
    }

    override fun slptClockClass(): Class<out AbstractSlptClock?> {
        return GreatFitSlpt::class.java
    }

    companion object {
        private var instance: WeakReference<GreatFit?>? = null
        fun getGreatWidget(): GreatWidget? {
            val weakReference: WeakReference<*>? = instance
            return if (weakReference != null) {
                (weakReference.get() as GreatFit?)!!.greatWidget
            } else null
        }
    }
}