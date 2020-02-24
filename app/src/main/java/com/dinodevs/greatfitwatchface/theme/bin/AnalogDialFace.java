
package com.dinodevs.greatfitwatchface.theme.bin;

import java.util.HashMap;
import java.util.Map;

public class AnalogDialFace {

    private HourCenterImage hourCenterImage;
    private MinCenterImage minCenterImage;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public HourCenterImage getHourCenterImage() {
        return hourCenterImage;
    }

    public void setHourCenterImage(HourCenterImage hourCenterImage) {
        this.hourCenterImage = hourCenterImage;
    }

    public MinCenterImage getMinCenterImage() {
        return minCenterImage;
    }

    public void setMinCenterImage(MinCenterImage minCenterImage) {
        this.minCenterImage = minCenterImage;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
