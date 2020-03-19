package it.vergeit.galaxian.settings

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View

/**
 * Created by Kieron on 20/01/2018.
 */
class IconSetting(var icon: Drawable, var title: String, var subtitle: String, //Setting with a click listener, two strings and an icon
                  var onClickListener: View.OnClickListener, color: Int?) : IBaseSettings() {
    var color: Int

    init {
        this.color = color ?: Color.parseColor("#ffffff")
    }
}