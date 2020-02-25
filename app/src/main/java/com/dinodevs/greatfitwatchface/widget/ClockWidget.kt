package com.dinodevs.greatfitwatchface.widget

import android.app.Service

interface ClockWidget : HasSlptViewComponent {
    fun init(service: Service?)
}