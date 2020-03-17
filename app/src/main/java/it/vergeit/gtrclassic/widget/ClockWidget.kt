package it.vergeit.gtrclassic.widget

import android.app.Service

interface ClockWidget : HasSlptViewComponent {
    fun init(service: Service?)
}