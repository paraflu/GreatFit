package com.dinodevs.greatfitwatchface.resource

import com.ingenic.iwds.slpt.view.core.SlptViewComponent
import com.ingenic.iwds.slpt.view.digital.SlptTimeView

class SlptSecondHView : SlptTimeView() {
    override fun initType(): Short {
        return SlptViewComponent.SVIEW_SECOND_H
    }
}