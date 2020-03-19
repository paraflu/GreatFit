package it.vergeit.galaxian.widget

import android.app.Service
import android.graphics.drawable.Drawable
import it.vergeit.galaxian.data.DataType
import it.vergeit.galaxian.data.MultipleWatchDataListener

abstract class AbstractWidget : Widget, MultipleWatchDataListener {
    private var x = 0
    private var y = 0
    override fun getX(): Int {
        return x
    }

    override fun getY(): Int {
        return y
    }

    override fun setX(x: Int) {
        this.x = x
    }

    override fun setY(y: Int) {
        this.y = y
    }

    override fun init(service: Service) {}
    override fun getDataTypes(): List<DataType> {
        return emptyList()
    }

    override fun onDataUpdate(type: DataType, value: Any) {}
    protected fun setDrawableBounds(drawable: Drawable, x: Float, y: Float) {
        drawable.setBounds(x.toInt(), y.toInt(), x.toInt() + drawable.minimumWidth, y.toInt() + drawable.minimumHeight)
    }
}