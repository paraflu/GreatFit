package it.vergeit.watchface.widget

import android.app.Service
import com.ingenic.iwds.slpt.view.core.SlptViewComponent

interface HasSlptViewComponent {
    fun buildSlptViewComponent(service: Service?): List<SlptViewComponent?>?
    fun buildSlptViewComponent(service: Service?, better_resolution: Boolean): List<SlptViewComponent?>?
}