package it.vergeit.watchface

import android.content.Context
import android.content.Intent
import it.vergeit.watchface.settings.LoadSettings
import it.vergeit.watchface.widget.*
import com.huami.watch.watchface.util.Util
import com.ingenic.iwds.slpt.view.core.SlptAbsoluteLayout
import com.ingenic.iwds.slpt.view.core.SlptLayout

/**
 * Splt version of the watch.
 */
class VergeItSlpt : AbstractWatchFaceSlpt() {
    var context: Context? = null
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        context = this.applicationContext
        // Load settings
        val settings = LoadSettings(context)
        clock = MainClock(settings)
        if (settings.theme?.date != null) {
            widgets.add(DateWidget(settings))
        }
        if (settings.theme.battery != null) {
            widgets.add(BatteryWidget(settings))
        }


//        if (settings.theme.activity?.calories != null) {
//            widgets.add(CaloriesWidget(settings))
//        }
        // Disable all except clock in both SLPT modes
//if(settings.clock_only_slpt)
//    return super.onStartCommand(intent, flags, startId);
//        if (settings.isHeartRate) {
//            widgets.add(HeartRateWidget(settings))
//        }
//        if (settings.isStepsRate) {
//            widgets.add(StepsWidget(settings))
//        }
//        if (settings.isTodayDistanceRate) {
//            widgets.add(SportTodayDistanceWidget(settings))
//        }
//        if (settings.isTotalDistanceRate) {
//            widgets.add(SportTotalDistanceWidget(settings))
//        }
//        if (settings.isCalories) {
//            widgets.add(CaloriesWidget(settings))
//        }
//        if (settings.isFloor) {
//            widgets.add(FloorWidget(settings))
//        }
//        if (settings.isBattery) {
//            widgets.add(BatteryWidget(settings))
//        }
//        if (settings.isWeather) {
//            widgets.add(WeatherWidget(settings))
//        }
//        if (settings.isMoonPhase) {
//            widgets.add(MoonPhaseWidget(settings))
//        }
//        if (settings.isGreat) {
//            widgets.add(GreatWidget(settings))
//        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun createClockLayout26WC(): SlptLayout {
        val result = SlptAbsoluteLayout()
        for (component in clock.buildSlptViewComponent(this, true)!!) {
            result.add(component)
        }
        for (widget in widgets) {
            for (component in widget.buildSlptViewComponent(this, true)!!) {
                result.add(component)
            }
        }
        return result
    }

    override fun createClockLayout8C(): SlptLayout {
        val result = SlptAbsoluteLayout()
        for (component in clock.buildSlptViewComponent(this)!!) {
            result.add(component)
        }
        for (widget in widgets) {
            for (component in widget.buildSlptViewComponent(this)!!) {
                result.add(component)
            }
        }
        return result
    }

    override fun initWatchFaceConfig() { //Log.w("VergeIT-LOG", "Initiating watchface");
    }

    override fun isClockPeriodSecond(): Boolean {
        val context = this.applicationContext
        val needRefreshSecond = Util.needSlptRefreshSecond(context)
        if (needRefreshSecond) {
            this.isClockPeriodSecond = true
        }
        return needRefreshSecond
    }
}