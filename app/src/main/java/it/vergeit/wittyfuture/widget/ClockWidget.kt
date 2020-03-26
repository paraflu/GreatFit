package it.vergeit.wittyfuture.widget

import android.app.Service

interface ClockWidget : HasSlptViewComponent {
    fun init(service: Service?)
}