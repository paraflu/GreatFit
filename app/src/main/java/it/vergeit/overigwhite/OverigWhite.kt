package it.vergeit.overigwhite

import it.vergeit.overigwhite.settings.LoadSettings
import it.vergeit.overigwhite.widget.*
import com.huami.watch.watchface.AbstractSlptClock
import java.lang.ref.WeakReference

/**
 * Amazfit watch faces
 */
class OverigWhite : AbstractWatchFace() {
    private val greatWidget: GreatWidget? = null
    override fun onCreate() {
        instance = WeakReference<OverigWhite?>(this)
        // Load settings
        val settings = LoadSettings(applicationContext, "white")
        clock = MainClock(settings)
        if (settings.theme?.date != null) {
            widgets.add(DateWidget(settings))
        }
        if (settings.theme.activity?.pulse != null || settings.theme.activity?.pulseGraph != null || settings.theme.activity?.pulseMeter != null) {
            widgets.add(HeartRateWidget(settings))
        }
        if (settings.theme.activity?.calories != null) {
            widgets.add(CaloriesWidget(settings))
        }
        if (settings.theme.battery != null) {
            widgets.add(BatteryWidget(settings))
        }
        if (settings.theme.activity?.steps != null || settings.theme.stepsProgress != null) {
            widgets.add(StepsWidget(settings));
        }

        if (settings.theme.weather != null) {
            widgets.add(WeatherWidget(settings));
        }

        if (settings.theme.activity?.distance != null) {
            widgets.add(SportTodayDistanceWidget(settings))
        }

        if (settings.theme.analogDialFace != null) {
            widgets.add(AnalogWidget(settings))
        }

//        if(settings.isTodayDistanceRate()) {
//            widgets.add(new SportTodayDistanceWidget(settings));
//        }
//        if(settings.isTotalDistanceRate()) {
//            widgets.add(new SportTotalDistanceWidget(settings));
//        }
//        if(settings.isCalories()) {
//            widgets.add(new CaloriesWidget(settings));
//        }
//        if(settings.isFloor()) {
//            widgets.add(new FloorWidget(settings));
//        }

//        if (settings.isMoonPhase()){
//            widgets.add(new MoonPhaseWidget(settings));
//        }
//        if(settings.isGreat()) {
//            greatWidget = new GreatWidget(settings);
//            widgets.add(greatWidget);
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
        return OverigWhiteSlpt::class.java
    }

    companion object {
        private var instance: WeakReference<OverigWhite?>? = null
        fun getGreatWidget(): GreatWidget? {
            val weakReference: WeakReference<*>? = instance
            return if (weakReference != null) {
                (weakReference.get() as OverigWhite?)!!.greatWidget
            } else null
        }
    }
}