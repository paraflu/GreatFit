package com.dinodevs.greatfitwatchface.settings

import android.graphics.drawable.Drawable
import android.view.View

/**
 * Created by Kieron on 20/01/2018.
 */
class IncrementalSetting(icon: Drawable?, var title: String, subtitle: String?, //Setting
                         var onClickLessListener: View.OnClickListener, var onClickMoreListener: View.OnClickListener, current: String) : IBaseSettings() {
    var subtitle: String
    var icon: Drawable?
    var value: String

    init {
        this.subtitle = subtitle ?: "Current: $current (Max:)"
        this.icon = icon
        value = current
    }
}