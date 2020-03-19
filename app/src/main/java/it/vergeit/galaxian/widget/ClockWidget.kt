package it.vergeit.galaxian.widget

import android.app.Service

interface ClockWidget : HasSlptViewComponent {
    fun init(service: Service?)
}