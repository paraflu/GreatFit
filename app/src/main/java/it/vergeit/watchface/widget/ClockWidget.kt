package it.vergeit.watchface.widget

import android.app.Service

interface ClockWidget : HasSlptViewComponent {
    fun init(service: Service?)
}