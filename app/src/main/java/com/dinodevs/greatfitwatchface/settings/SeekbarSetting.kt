package com.dinodevs.greatfitwatchface.settings

import android.graphics.drawable.Drawable
import android.widget.SeekBar.OnSeekBarChangeListener

/**
 * Created by Kieron on 20/01/2018.
 */
class SeekbarSetting(icon: Drawable, var title: String, subtitle: String?, //Setting
                     var onChangeListener: OnSeekBarChangeListener, current: Int, max: Int) : IBaseSettings() {
    var subtitle: String
    var icon: Drawable
    var current: Int
    var max: Int

    init {
        this.subtitle = subtitle ?: "Current: $current (Max: $max)"
        this.icon = icon
        this.current = current
        this.max = max
    }
}