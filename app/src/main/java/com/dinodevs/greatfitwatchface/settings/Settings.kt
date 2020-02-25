package com.dinodevs.greatfitwatchface.settings

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.dinodevs.greatfitwatchface.GreatFit
import com.dinodevs.greatfitwatchface.R
import java.util.*

class Settings : FragmentActivity() {
    /*
        Activity to provide a settings list for choosing vibration and sub-settings
        Made by KieronQuinn for AmazfitStepNotify
        Modified by GreatApo
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = RecyclerView(this)
        val sharedPreferences = getSharedPreferences(packageName + "_settings", Context.MODE_PRIVATE)
        // Load settings
        val watchface_settings = LoadSettings(applicationContext)
        if (sharedPreferences.getString("widgets", null) == null) {
            sharedPreferences.edit().putString("widgets", watchface_settings.widgets_list.toString()).apply()
        }
        if (sharedPreferences.getString("progress_bars", null) == null) {
            sharedPreferences.edit().putString("progress_bars", watchface_settings.circle_bars_list.toString()).apply()
        }
        //Add header to a list of settings
        val settings: MutableList<IBaseSettings> = ArrayList()
        // Add IconSettings for each sub-setting. They contain an icon, title and subtitle, as well as a click action to launch the sub-setting's activity
        settings.add(HeaderSetting(getString(R.string.settings)))
        // Add color selection
        settings.add(IconSetting(getDrawable(R.drawable.palette)!!, getString(R.string.main_color), getString(R.string.main_color_c), View.OnClickListener { startActivity(Intent(this@Settings, ColorActivity::class.java)) }, null))
        // Add font selection
        settings.add(IconSetting(getDrawable(R.drawable.font)!!, getString(R.string.font), getString(R.string.font_c), View.OnClickListener { startActivity(Intent(this@Settings, FontActivity::class.java)) }, null))
        // Add other features
        settings.add(IconSetting(getDrawable(R.drawable.gear)!!, getString(R.string.other_features), getString(R.string.other_features_c), View.OnClickListener { startActivity(Intent(this@Settings, OthersActivity::class.java)) }, null))
        // One for each widget
        for (i in watchface_settings.widgets_list.indices) {
            settings.add(IconSetting(getDrawable(R.drawable.widgets)!!, getString(R.string.widget) + " " + (i + 1), getString(R.string.widget_c), View.OnClickListener {
                sharedPreferences.edit().putInt("temp_widget", i).apply()
                startActivity(Intent(this@Settings, WidgetsActivity::class.java))
            }, null))
        }
        // One for each progress widget
        for (i in watchface_settings.circle_bars_list.indices) {
            settings.add(IconSetting(getDrawable(R.drawable.progress)!!, getString(R.string.progress_widget) + " " + (i + 1), getString(R.string.progress_widget_c), View.OnClickListener {
                sharedPreferences.edit().putInt("temp_progress_bars", i).apply()
                startActivity(Intent(this@Settings, ProgressWidgetsActivity::class.java))
            }, null))
        }
        // Add language
        settings.add(IconSetting(getDrawable(R.drawable.language)!!, getString(R.string.language), getString(R.string.language_c), View.OnClickListener { startActivity(Intent(this@Settings, LanguageActivity::class.java)) }, null))
        // Add about
        settings.add(IconSetting(getDrawable(R.drawable.info)!!, getString(R.string.about), getString(R.string.about_c), View.OnClickListener {
            // Get pkg info
            var version = "n/a"
            try {
                val pInfo = packageManager.getPackageInfo(packageName, 0)
                version = pInfo.versionName
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            // Please do not change the following line
            Toast.makeText(applicationContext, "GreatFit Project\nVersion: " + version + "\nAuthor: GreatApo\nStyle: " + resources.getString(R.string.author), Toast.LENGTH_LONG).show()
        }, null))
        //Add save button
        settings.add(ButtonSetting(getString(R.string.save), getDrawable(R.drawable.green_button)!!, View.OnClickListener {
            quit(this@Settings)
            /*
                // CODE BASED ON STOCK WAY
                // Restart watchface
                Settings.this.sendBroadcast(new Intent("com.huami.intent.action.WATCHFACE_CONFIG_CHANGED"));
                // Slpt some times doesn't run
                startService(new Intent(getApplicationContext(), GreatFitSlpt.class));
                // Kill this
                Settings.this.setResult(-1);
                Settings.this.finish();
                */
        }))
        //Add reset button
        settings.add(ButtonSetting(getString(R.string.reset), getDrawable(R.drawable.grey_button), View.OnClickListener { view ->
            sharedPreferences.edit().clear().apply()
            Toast.makeText(view.context, "Settings reset", Toast.LENGTH_SHORT).show()
            quit(this@Settings)
            /*
                // CODE BASED ON STOCK WAY
                // Restart watchface
                Settings.this.sendBroadcast(new Intent("com.huami.intent.action.WATCHFACE_CONFIG_CHANGED"));
                // Slpt some times doesn't run
                startService(new Intent(getApplicationContext(), GreatFitSlpt.class));
                // Kill this
                Settings.this.setResult(-1);
                Settings.this.finish();
                 */
        }))
        //Setup layout
        root.setBackgroundResource(R.drawable.settings_background)
        root.layoutManager = LinearLayoutManager(this)
        root.adapter = Adapter(this, settings)
        root.setPadding(resources.getDimension(R.dimen.padding_round_small).toInt(), 0, resources.getDimension(R.dimen.padding_round_small).toInt(), resources.getDimension(R.dimen.padding_round_large).toInt())
        root.clipToPadding = false
        setContentView(root)
    }

    companion object {
        private fun quit(settings: Settings) {
            val greatWidget = GreatFit.getGreatWidget()
            greatWidget?.refreshSlpt("Apply settings", true)
            Handler().postDelayed({
                settings.sendBroadcast(Intent("com.huami.intent.action.WATCHFACE_CONFIG_CHANGED"))
                // Kill this
                settings.setResult(-1)
                settings.finish()
            }, 5)
        }
    }
}