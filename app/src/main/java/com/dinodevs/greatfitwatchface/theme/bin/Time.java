
package com.dinodevs.greatfitwatchface.theme.bin;

import java.util.HashMap;
import java.util.Map;

public class Time {

    private Hours hours;
    private Minutes minutes;
    private AmPm amPm;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Hours getHours() {
        return hours;
    }

    public void setHours(Hours hours) {
        this.hours = hours;
    }

    public Minutes getMinutes() {
        return minutes;
    }

    public void setMinutes(Minutes minutes) {
        this.minutes = minutes;
    }

    public AmPm getAmPm() {
        return amPm;
    }

    public void setAmPm(AmPm amPm) {
        this.amPm = amPm;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
