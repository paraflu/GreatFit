package com.dinodevs.greatfitwatchface.widget

import android.app.Service
import android.graphics.Canvas
import com.dinodevs.greatfitwatchface.data.DataType
import com.dinodevs.greatfitwatchface.settings.LoadSettings
import com.ingenic.iwds.slpt.view.core.SlptViewComponent

open class TextWidget(private val settings: LoadSettings): AbstractWidget() {

    companion object {
        const val TAG = "VergeIT-LOG"
    }

    private lateinit var mService: Service

    override fun buildSlptViewComponent(service: Service?): List<SlptViewComponent?>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun buildSlptViewComponent(service: Service?, better_resolution: Boolean): List<SlptViewComponent?>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun draw(canvas: Canvas?, width: Float, height: Float, centerX: Float, centerY: Float) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun init(service: Service) {
        mService = service
    }

    override fun onDataUpdate(type: DataType, value: Any) {
        super.onDataUpdate(type, value)
    }
}