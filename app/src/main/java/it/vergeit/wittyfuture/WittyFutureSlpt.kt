package it.vergeit.wittyfuture

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import it.vergeit.wittyfuture.settings.LoadSettings
import it.vergeit.wittyfuture.widget.*
import com.huami.watch.watchface.util.Util
import com.ingenic.iwds.slpt.view.core.SlptAbsoluteLayout
import com.ingenic.iwds.slpt.view.core.SlptLayout
import java.lang.Exception

/**
 * Splt version of the watch.
 */
class WittyFutureSlpt : AbstractWatchFaceSlpt() {
    companion object {
        private const val TAG = "VergeIT-LOG"
    }

    var context: Context? = null
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        context = this.applicationContext
        // Load settings
        try {
            val settings = LoadSettings(context)
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
                widgets.add(StepsWidget(settings))
            }
            if (settings.theme.weather != null) {
                widgets.add(WeatherWidget(settings))
            }
            /*-----------------------
        if (settings.theme.weather != null) {
            widgets.add(WeatherWidget(settings));
        }

        if (settings.theme.activity?.distance != null) {
            widgets.add(SportTodayDistanceWidget(settings))
        }*/
            if (settings.theme.analogDialFace != null) {
                widgets.add(AnalogWidget(settings))
            }
            return super.onStartCommand(intent, flags, startId)
        } catch (e: Exception) {
            Log.e(TAG, "OnStart fail ${e.toString()}")
            throw  e
        }
    }

    private fun buildClockLayout(betterResolution: Boolean): SlptLayout {
        val result = SlptAbsoluteLayout()
        for (component in clock.buildSlptViewComponent(this, betterResolution)!!) {
            result.add(component)
        }
        for (widget in widgets) {
            for (component in widget.buildSlptViewComponent(this, betterResolution)!!) {
                result.add(component)
            }
        }
        return result
    }

    override fun createClockLayout26WC(): SlptLayout {
        return buildClockLayout(true)
    }

    override fun createClockLayout8C(): SlptLayout {
        return buildClockLayout(false)
    }

    //override fun createClockLayout26WC(): SlptLayout {
//        val result = SlptAbsoluteLayout()
//        for (component in clock.buildSlptViewComponent(this, true)!!) {
//            result.add(component)
//        }
//        widgets.forEach {
//            try {
//                Log.d(TAG, it.toString())
//                val items = it.buildSlptViewComponent(this, true)
//                items?.forEach { component -> result.add(component) }
//            } catch (e: Exception) {
//                Log.e(TAG, e.message)
//            }
//        }
////        for (widget in widgets) {
////            for (component in widget.buildSlptViewComponent(this, true)!!) {
////                result.add(component)
////            }
////        }
//        return result
    //}

//    override fun createClockLayout8C(): SlptLayout {
//        val result = SlptAbsoluteLayout()
//        for (component in clock.buildSlptViewComponent(this)!!) {
//            result.add(component)
//        }
//        widgets.forEach {
//            try {
//                Log.d(TAG, it.toString())
//                val items = it.buildSlptViewComponent(this, true)
//                items?.forEach { component -> result.add(component) }
//            } catch (e: Exception) {
//                Log.e(TAG, e.message)
//            }
//        }
////        for (widget in widgets) {
////            for (component in widget.buildSlptViewComponent(this)!!) {
////                result.add(component)
////            }
////        }
//        return result
//    }

    override fun initWatchFaceConfig() { //Log.w("VergeIT-LOG", "Initiating watchface");
    }

    override fun isClockPeriodSecond(): Boolean {
        return try {
            val context = this.applicationContext
            val needRefreshSecond = Util.needSlptRefreshSecond(context)
            if (needRefreshSecond) {
                this.isClockPeriodSecond = true
            }
            needRefreshSecond
        } catch (e: Settings.SettingNotFoundException) {
            Log.e(TAG, "isClockPeriosSecond ${e.message}")
            false
        }
    }
}