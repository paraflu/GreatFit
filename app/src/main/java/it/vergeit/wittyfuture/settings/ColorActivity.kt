package it.vergeit.wittyfuture.settings

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import it.vergeit.wittyfuture.R
import java.util.*

class ColorActivity : FragmentActivity() {
    /*
        Activity to provide a settings list for choosing days
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = RecyclerView(this)
        //Add header to a list of settings
        val settings: MutableList<IBaseSettings> = ArrayList()
        settings.add(HeaderSetting(getString(R.string.set_color)))
        //Setup items for each day
        val colors = resources.getStringArray(R.array.colors)
        val colorCodes = resources.getStringArray(R.array.color_codes)
        val sharedPreferences = getSharedPreferences(packageName + "_settings", Context.MODE_PRIVATE)
        var x = 0
        val current = sharedPreferences.getInt("color", -1)
        for (color in colors) { //Each item needs a Setting
            val finalX = x
            val drawable = if (current == x) getDrawable(R.drawable.check) else getDrawable(R.drawable.circle_icon);
            settings.add(IconSetting(drawable!!, color, "", View.OnClickListener {
                sharedPreferences.edit().putInt("color", finalX).apply()
                finish()
            }, Color.parseColor(colorCodes[x])))
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