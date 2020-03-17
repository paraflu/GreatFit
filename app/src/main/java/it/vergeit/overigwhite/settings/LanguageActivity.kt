package it.vergeit.overigwhite.settings

import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import it.vergeit.overigwhite.R
import java.util.*

class LanguageActivity : FragmentActivity() {
    /*
        Activity to provide a settings list for choosing days
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = RecyclerView(this)
        //Add header to a list of settings
        val settings: MutableList<IBaseSettings> = ArrayList()
        settings.add(HeaderSetting(getString(R.string.set_language)))
        //Setup items for each day
        val languages = resources.getStringArray(R.array.language)
        val languages_EN = resources.getStringArray(R.array.languages_EN)
        val sharedPreferences = getSharedPreferences(packageName + "_settings", Context.MODE_PRIVATE)
        var x = 0
        val current = sharedPreferences.getInt("language", 0)
        for (language in languages) { //Each item needs a SwitchSetting with a value
            val finalX = x
            settings.add(IconSetting(if (current == x) getDrawable(R.drawable.check) else getDrawable(R.drawable.circle_icon), language!!, languages_EN[x], View.OnClickListener {
                sharedPreferences.edit().putInt("language", finalX).apply()
                finish()
            }, null))
            x++
        }
        //Setup layout
        root.setBackgroundResource(R.drawable.settings_background)
        root.layoutManager = LinearLayoutManager(this)
        root.adapter = Adapter(this, settings)
        root.setPadding(resources.getDimension(R.dimen.padding_round_small).toInt(), 0, resources.getDimension(R.dimen.padding_round_small).toInt(), resources.getDimension(R.dimen.padding_round_large).toInt())
        root.clipToPadding = false
        setContentView(root)
    }
}