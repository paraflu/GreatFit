package it.vergeit.gtrclassic.settings

import android.graphics.drawable.Drawable
import android.widget.CompoundButton

/**
 * Created by Kieron on 20/01/2018.
 */
class SwitchSetting(var icon: Drawable?, var title: String, var subtitle: String, var changeListener: CompoundButton.OnCheckedChangeListener, //Setting with a change listener, two strings, a state and an icon
                    var isChecked: Boolean) : IBaseSettings()