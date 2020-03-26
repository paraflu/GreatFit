package it.vergeit.wittyfuture.settings

import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import it.vergeit.wittyfuture.R

class WidgetsActivity : FragmentActivity() {
    /*
        Activity to provide a settings list for choosing days
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = RecyclerView(this)
        //Add header to a list of settings
        val settings = arrayListOf<IBaseSettings>()
        settings.add(HeaderSetting(getString(R.string.set_elements)))
        //Setup items for each day
        val elements = resources.getStringArray(R.array.supported_widgets)
        val sharedPreferences = getSharedPreferences(packageName + "_settings", Context.MODE_PRIVATE)
        // Get Widgets
        val widgetsAsText = sharedPreferences.getString("widgets", null)
        val text = widgetsAsText!!.replace("(\\[|\\]| )".toRegex(), "") // Replace "[", "]" and "spaces"
        val widgets_list = arrayOf(*text.split(",").toTypedArray())
        val current = sharedPreferences.getInt("temp_widget", 0)
        val currentAsText = widgets_list[current]
        for (element in elements) { // Go to next if already a widget
            if (widgets_list.indexOf(element) >= 0 && element != currentAsText && element != "none") {
                continue
            }
            // Create the setting
            widgets_list[current] = element
            val finalWidgets = widgets_list.toString()
            settings.add(IconSetting(if (element == currentAsText) getDrawable(R.drawable.check) else getDrawable(R.drawable.circle_icon), prepareTitle(element), prepareSubtitle(element)!!, View.OnClickListener {
                sharedPreferences.edit().putString("widgets", finalWidgets).apply()
                finish()
            }, null))
        }
        //Setup layout
        root.setBackgroundResource(R.drawable.settings_background)
        root.layoutManager = LinearLayoutManager(this)
        root.adapter = Adapter(this, settings)
        root.setPadding(resources.getDimension(R.dimen.padding_round_small).toInt(), 0, resources.getDimension(R.dimen.padding_round_small).toInt(), resources.getDimension(R.dimen.padding_round_large).toInt())
        root.clipToPadding = false
        setContentView(root)
    }

    private fun prepareTitle(title: String): String {
        var _title = title
        when (_title) {
            "altitude" -> _title = "altitude/Dive"
            "weather_img" -> _title = "weather icon"
            "min_max_temperatures" -> _title = "Max/Min temperatures"
        }
        // Replace _ with spaces
        _title = _title.replace("_".toRegex(), " ")
        // Capitalize first letter
        _title = _title.substring(0, 1).toUpperCase() + _title.substring(1)
        return _title
    }

    private fun prepareSubtitle(element: String): String? {
        var subtitle: String? = null
        when (element) {
            "air_pressure", "altitude" -> subtitle = getString(R.string.moreBattery)
            "sunset", "sunrise", "visibility", "clouds", "phone_battery", "phone_alarm", "notifications" -> subtitle = getString(R.string.needAmazmod)
            "xdrip" -> subtitle = getString(R.string.needXdrip)
            "none" -> subtitle = getString(R.string.emptyWidget)
            "total_distance" -> subtitle = getString(R.string.totalSportDistance)
            "today_distance" -> subtitle = getString(R.string.todaySportDistance)
            "walked_distance" -> subtitle = getString(R.string.basedOnHeight)
            "heart_rate" -> subtitle = getString(R.string.needsContinueHeartRate)
            "date" -> subtitle = getString(R.string.dateFormat)
            "watch_alarm" -> subtitle = getString(R.string.nextWatchAlarm)
        }
        return subtitle
    }
}