package com.dinodevs.greatfitwatchface.settings;

import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by GreatApo on 08/05/2018.
 */

public class ButtonSetting extends IBaseSettings {

    View.OnClickListener onClickListener;
    String title;
    Drawable bg;
    public ButtonSetting(String title,Drawable bg, View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.bg = bg;
        this.title = title;
    }
}
