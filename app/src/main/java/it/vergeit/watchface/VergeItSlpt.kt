package it.vergeit.watchface

import android.content.Context
import android.content.Intent
import android.util.Log
import it.vergeit.watchface.settings.LoadSettings
import it.vergeit.watchface.widget.*
import com.huami.watch.watchface.util.Util
import com.ingenic.iwds.slpt.view.core.SlptAbsoluteLayout
import com.ingenic.iwds.slpt.view.core.SlptLayout
import java.lang.Exception

/**
 * Splt version of the watch.
 */
class VergeItSlpt : AbstractWatchFaceSlpt() {
    companion object {
        private const val TAG = "VergeIT-LOG"
    }

    var context: Context? = null
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        context = this.applicationContext
        // Load settings
        try {
            val settings = LoadSettings(context)
            Log.d(TAG, "start mainclock")
            clock = MainClock(settings)
            if (settings.theme?.date != null) {
                Log.d(TAG, "start datewidget")
                widgets.add(DateWidget(settings))
            }
            if (settings.theme.activity?.pulse != null || settings.theme.activity?.pulseGraph != null || settings.theme.activity?.pulseMeter != null) {
                Log.d(TAG, "start heartratewidget")
                widgets.add(HeartRateWidget(settings))
            }
            if (settings.theme.activity?.calories != null) {
                Log.d(TAG, "start calorieswidget")
                widgets.add(CaloriesWidget(settings))
            }
            if (settings.theme.battery != null) {
                Log.d(TAG, "start batterywidget")
                widgets.add(BatteryWidget(settings))
            }
            if (settings.theme.activity?.steps != null || settings.theme.stepsProgress != null) {
                Log.d(TAG, "start stepswidget")
                widgets.add(StepsWidget(settings))
            }
            //-----------------------
//        if (settings.theme.weather != null) {
//            widgets.add(WeatherWidget(settings));
//        }
//
//        if (settings.theme.activity?.distance != null) {
//            widgets.add(SportTodayDistanceWidget(settings))
//        }
            if (settings.theme.analogDialFace != null) {
                Log.d(TAG, "start analogwidget")
                widgets.add(AnalogWidget(settings))
            }
            Log.d(TAG, "end widgets")
            return super.onStartCommand(intent, flags, startId)
        } catch (e: Exception) {
            Log.e(TAG, "OnStart fail ${e.toString()}")
            throw  e
        }
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