package com.dinodevs.greatfitwatchface.resource

import com.ingenic.iwds.slpt.view.analog.SlptAnalogTimeView
import com.ingenic.iwds.slpt.view.core.SlptViewComponent

class SlptAnalogHourView : SlptAnalogTimeView() {
    override fun initType(): Short {
        return SlptViewComponent.SVIEW_ANALOG_HOUR
    }
}