package it.vergeit.galaxian.settings

import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import it.vergeit.galaxian.R
import java.util.*

class ProgressWidgetsActivity : FragmentActivity() {
    /*
        Activity to provide a settings list for choosing days
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = RecyclerView(this)
        //Add header to a list of settings
        val settings: MutableList<IBaseSettings> = ArrayList()
        settings.add(HeaderSetting(getString(R.string.set_progress_bar)))
        //Setup items for each day
        val elements = resources.getStringArray(R.array.supported_progress_elements)
        val sharedPreferences = getSharedPreferences(packageName + "_settings", Context.MODE_PRIVATE)
        // Get Widgets
        val widgetsAsText = sharedPreferences.getString("progress_bars", null)
        val text = widgetsAsText.replace("(\\[|\\]| )".toRegex(), "") // Replace "[", "]" and "spaces"
        val widgets_list = Arrays.asList(*text.split(",").toTypedArray())
        val current = sharedPreferences.getInt("temp_progress_bars", 0)
        val currentAsText = widgets_list[current]
        for (element in elements) { // Go to next if already a widget
            if (widgets_list.indexOf(element) >= 0 && element != currentAsText && element != "none") {
                continue
            }
            // Create the setting
            widgets_list[current] = element
            val finalWidgets = widgets_list.toString()
            settings.add(IconSetting(if (element == currentAsText) getDrawable(R.drawable.check) else getDrawable(R.drawable.circle_icon), prepareTitle(element), prepareSubtitle(element)!!, View.OnClickListener {
                sharedPreferences.edit().putString("progress_bars", finalWidgets).apply()
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

    private fun prepareTitle(title: String): String { // Replace _ with spaces
        var title1 = title
        title1 = title1.replace("_".toRegex(), " ")
        // Capitalize first letter
        title1 = title1.substring(0, 1).toUpperCase() + title1.substring(1)
        return title1
    }

    private fun prepareSubtitle(element: String): String? {
        var subtitle: String? = null
        when (element) {
            "air_pressure" -> subtitle = getString(R.string.nextWatchAlarm)
        }
        return subtitle
    }
}