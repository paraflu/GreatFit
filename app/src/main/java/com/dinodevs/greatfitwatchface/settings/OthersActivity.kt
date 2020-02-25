package com.dinodevs.greatfitwatchface.settings

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import com.dinodevs.greatfitwatchface.R
import java.util.*
import kotlin.math.roundToInt

class OthersActivity : FragmentActivity() {
    val BUILD_VERGE_MODELS = arrayOf("qogir", "qogirUS")
    val isVerge: Boolean
        get() = Arrays.asList(*BUILD_VERGE_MODELS).contains(Build.PRODUCT)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = RecyclerView(this)
        //Add header to a list of settings
        val settings: MutableList<IBaseSettings> = ArrayList()
        settings.add(HeaderSetting(getString(R.string.other_features)))
        //Setup items
        val sharedPreferences = getSharedPreferences(packageName + "_settings", Context.MODE_PRIVATE)
        val better_resolution_when_raising_hand = sharedPreferences.getBoolean("better_resolution_when_raising_hand", resources.getBoolean(R.bool.better_resolution_when_raising_hand))
        settings.add(SwitchSetting(null, "Better resolution", "in slpt mode when raising hand", CompoundButton.OnCheckedChangeListener { _, b -> sharedPreferences.edit().putBoolean("better_resolution_when_raising_hand", b).apply() }, better_resolution_when_raising_hand))
        val white_bg = sharedPreferences.getBoolean("white_bg", resources.getBoolean(R.bool.white_bg))
        settings.add(SwitchSetting(null, "White background", "Theme with white bg - black text/icons", CompoundButton.OnCheckedChangeListener { _, b -> sharedPreferences.edit().putBoolean("white_bg", b).apply() }, white_bg))
        val analog_clock = sharedPreferences.getBoolean("analog_clock", resources.getBoolean(R.bool.analog_clock))
        settings.add(SwitchSetting(null, "Analog clock", "Show clock time hands", CompoundButton.OnCheckedChangeListener { _, b -> sharedPreferences.edit().putBoolean("analog_clock", b).apply() }, analog_clock))
        val digital_clock = sharedPreferences.getBoolean("digital_clock", resources.getBoolean(R.bool.digital_clock))
        settings.add(SwitchSetting(null, "Digital clock", "Show digital clock", CompoundButton.OnCheckedChangeListener { _, b -> sharedPreferences.edit().putBoolean("digital_clock", b).apply() }, digital_clock))
        val clock_only_slpt = sharedPreferences.getBoolean("clock_only_slpt", isVerge || resources.getBoolean(R.bool.clock_only_slpt))
        settings.add(SwitchSetting(null, "SLPT clock only", "Show only clock when screen is off", CompoundButton.OnCheckedChangeListener { _, b -> sharedPreferences.edit().putBoolean("clock_only_slpt", b).apply() }, clock_only_slpt))
        val flashing_indicator = sharedPreferences.getBoolean("flashing_indicator", resources.getBoolean(R.bool.flashing_indicator))
        settings.add(SwitchSetting(null, "Flashing \":\"", "Make time's \":\" flashing", CompoundButton.OnCheckedChangeListener { _, b -> sharedPreferences.edit().putBoolean("flashing_indicator", b).apply() }, flashing_indicator))
        val month_as_text = sharedPreferences.getBoolean("month_as_text", resources.getBoolean(R.bool.month_as_text))
        settings.add(SwitchSetting(null, "Month as text", "Show month as text", CompoundButton.OnCheckedChangeListener { _, b -> sharedPreferences.edit().putBoolean("month_as_text", b).apply() }, month_as_text))
        val three_letters_month_if_text = sharedPreferences.getBoolean("three_letters_month_if_text", resources.getBoolean(R.bool.three_letters_month_if_text))
        settings.add(SwitchSetting(null, "3 letters month", "If text (ex.APR)", CompoundButton.OnCheckedChangeListener { _, b -> sharedPreferences.edit().putBoolean("three_letters_month_if_text", b).apply() }, three_letters_month_if_text))
        val three_letters_day_if_text = sharedPreferences.getBoolean("three_letters_day_if_text", resources.getBoolean(R.bool.three_letters_day_if_text))
        settings.add(SwitchSetting(null, "3 letters day", "(ex.MON)", CompoundButton.OnCheckedChangeListener { _, b -> sharedPreferences.edit().putBoolean("three_letters_day_if_text", b).apply() }, three_letters_day_if_text))
        val no_0_on_hour_first_digit = sharedPreferences.getBoolean("no_0_on_hour_first_digit", resources.getBoolean(R.bool.no_0_on_hour_first_digit))
        settings.add(SwitchSetting(null, "No 0 in hours/months", "Remove 0 if first digit", CompoundButton.OnCheckedChangeListener { _, b -> sharedPreferences.edit().putBoolean("no_0_on_hour_first_digit", b).apply() }, no_0_on_hour_first_digit))
        val wind_direction_as_arrows = sharedPreferences.getBoolean("wind_direction_as_arrows", resources.getBoolean(R.bool.wind_direction_as_arrows))
        settings.add(SwitchSetting(null, "Wind as arrows", "Wind direction as arrows", CompoundButton.OnCheckedChangeListener { _, b -> sharedPreferences.edit().putBoolean("wind_direction_as_arrows", b).apply() }, wind_direction_as_arrows))
        val status_bar = sharedPreferences.getBoolean("status_bar", resources.getBoolean(R.bool.status_bar))
        settings.add(SwitchSetting(null, "Show status bar", "Status bar (charging icon etc)", CompoundButton.OnCheckedChangeListener { _, b -> sharedPreferences.edit().putBoolean("status_bar", b).apply() }, status_bar))
        val flashing_heart_rate_icon = sharedPreferences.getBoolean("flashing_heart_rate_icon", resources.getBoolean(R.bool.flashing_heart_rate_icon))
        settings.add(SwitchSetting(null, "Animate heart rate", "Flashing heart rate icon", CompoundButton.OnCheckedChangeListener { _, b -> sharedPreferences.edit().putBoolean("flashing_heart_rate_icon", b).apply() }, flashing_heart_rate_icon))
        val am_pm_always = sharedPreferences.getBoolean("am_pm_always", resources.getBoolean(R.bool.am_pm_always))
        settings.add(SwitchSetting(null, "Always am/pm", "Show am/pm on 24h format", CompoundButton.OnCheckedChangeListener { _, b -> sharedPreferences.edit().putBoolean("am_pm_always", b).apply() }, am_pm_always))
        val target_calories = sharedPreferences.getInt("target_calories", 1000)
        settings.add(
                IncrementalSetting(null, "Target calories", "Current: $target_calories",
                        View.OnClickListener { view ->
                            val new_value = sharedPreferences.getInt("target_calories", 1000) - 50
                            if (new_value >= 100) {
                                sharedPreferences.edit().putInt("target_calories", new_value).apply()
                                val parent = view.parent as View
                                val value = parent.findViewById<View>(R.id.value) as TextView
                                value.text = new_value.toString() + ""
                            }
                        }, View.OnClickListener { view ->
                    val new_value = sharedPreferences.getInt("target_calories", 1000) + 50
                    if (new_value <= 10000) {
                        sharedPreferences.edit().putInt("target_calories", new_value).apply()
                        val parent = view.parent as View
                        val value = parent.findViewById<View>(R.id.value) as TextView
                        value.text = new_value.toString() + ""
                    }
                }, target_calories.toString() + "")
        )
        /*
        final int custom_refresh_rate = sharedPreferences.getInt( "custom_refresh_rate", getResources().getInteger(R.integer.custom_refresh_rate)*1000);
        settings.add(new SeekbarSetting(null, "Air pressure refresh sec", null, new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sharedPreferences.edit().putInt( "custom_refresh_rate", progress).apply();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(seekBar.getContext(), "Refresh rate: "+seekBar.getProgress()+" sec", Toast.LENGTH_SHORT).show();
            }
        }, custom_refresh_rate, 60));
         */
        val custom_refresh_rate = sharedPreferences.getInt("custom_refresh_rate", resources.getInteger(R.integer.custom_refresh_rate) * 1000)
        settings.add(
                IncrementalSetting(null, "Custom refresh",
                        "Pressure/Walked distance: " +
                                if (custom_refresh_rate / 1000 < 120)
                                    (custom_refresh_rate / 1000.toFloat()).roundToInt().toString() + " sec"
                                else
                                    ((custom_refresh_rate / 1000.toFloat()).roundToInt() / 60).toString() + " min",
                        View.OnClickListener { view ->
                            var new_value = (sharedPreferences.getInt("custom_refresh_rate", resources.getInteger(R.integer.custom_refresh_rate) * 1000) / 1000 - 5.toFloat()).roundToInt()
                            if (new_value > 115) new_value = new_value - 55
                            if (new_value >= 0) {
                                sharedPreferences.edit().putInt("custom_refresh_rate", new_value * 1000).apply()
                                val parent = view.parent as View
                                val value = parent.findViewById<View>(R.id.value) as TextView
                                value.text = if (new_value < 120) "$new_value sec" else (new_value / 60).toString() + " min"
                            }
                        }, View.OnClickListener { view ->
                    var new_value = Math.round(sharedPreferences.getInt("custom_refresh_rate", resources.getInteger(R.integer.custom_refresh_rate) * 1000) / 1000 + 5.toFloat())
                    if (new_value > 120) new_value = new_value + 55
                    sharedPreferences.edit().putInt("custom_refresh_rate", new_value * 1000).apply()
                    val parent = view.parent as View
                    val value = parent.findViewById<View>(R.id.value) as TextView
                    value.text = if (new_value < 120) "$new_value sec" else (new_value / 60).toString() + " min"
                }, if (custom_refresh_rate / 1000 < 120) Math.round(custom_refresh_rate / 1000.toFloat()).toString() + " sec" else ((custom_refresh_rate / 1000.toFloat()).roundToInt() / 60).toString() + " min")
        )
        val pressure_to_mmhg = sharedPreferences.getBoolean("pressure_to_mmhg", resources.getBoolean(R.bool.pressure_to_mmhg))
        settings.add(SwitchSetting(null, "Pressure units", "(off: hPa, on: mmHg)", CompoundButton.OnCheckedChangeListener { _, b -> sharedPreferences.edit().putBoolean("pressure_to_mmhg", b).apply() }, pressure_to_mmhg))
        val world_time_zone = sharedPreferences.getFloat("world_time_zone", 0f)
        settings.add(
                IncrementalSetting(null, "World-time zone", "Current: GMT " + if (world_time_zone > 0) "+$world_time_zone" else world_time_zone,
                        View.OnClickListener { view ->
                            val new_value = sharedPreferences.getFloat("world_time_zone", 0f) - 0.5f
                            if (new_value >= -12) {
                                sharedPreferences.edit().putFloat("world_time_zone", new_value).apply()
                                val parent = view.parent as View
                                val value = parent.findViewById<View>(R.id.value) as TextView
                                value.text = "GMT " + if (new_value > 0) "+$new_value" else new_value
                            }
                        }, View.OnClickListener { view ->
                    val new_value = sharedPreferences.getFloat("world_time_zone", 0f) + 0.5f
                    if (new_value <= 12) {
                        sharedPreferences.edit().putFloat("world_time_zone", new_value).apply()
                        val parent = view.parent as View
                        val value = parent.findViewById<View>(R.id.value) as TextView
                        value.text = "GMT " + if (new_value > 0) "+$new_value" else new_value
                    }
                }, "GMT " + if (world_time_zone > 0) "+$world_time_zone" else world_time_zone)
        )
        val height = sharedPreferences.getInt("height", 175)
        settings.add(
                IncrementalSetting(null, "Set height", "Current: $height cm",
                        View.OnClickListener { view ->
                            val new_value = sharedPreferences.getInt("height", 175) - 1
                            if (new_value >= 100) {
                                sharedPreferences.edit().putInt("height", new_value).apply()
                                val parent = view.parent as View
                                val value = parent.findViewById<View>(R.id.value) as TextView
                                value.text = "$new_value cm"
                            }
                        }, View.OnClickListener { view ->
                    val new_value = sharedPreferences.getInt("height", 175) + 1
                    if (new_value <= 250) {
                        sharedPreferences.edit().putInt("height", new_value).apply()
                        val parent = view.parent as View
                        val value = parent.findViewById<View>(R.id.value) as TextView
                        value.text = "$new_value cm"
                    }
                }, "$height cm")
        )
        //Setup layout
        root.setBackgroundResource(R.drawable.settings_background)
        root.layoutManager = LinearLayoutManager(this)
        root.adapter = Adapter(this, settings)
        root.setPadding(resources.getDimension(R.dimen.padding_round_small).toInt(), 0, resources.getDimension(R.dimen.padding_round_small).toInt(), resources.getDimension(R.dimen.padding_round_large).toInt())
        root.clipToPadding = false
        setContentView(root)
    }
}