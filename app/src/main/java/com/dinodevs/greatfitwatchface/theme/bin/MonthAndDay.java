
package com.dinodevs.greatfitwatchface.theme.bin;

import java.util.HashMap;
import java.util.Map;

public class MonthAndDay {

    private Separate separate;
    private Boolean twoDigitsMonth;
    private Boolean twoDigitsDay;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Separate getSeparate() {
        return separate;
    }

    public void setSeparate(Separate separate) {
        this.separate = separate;
    }

    public Boolean getTwoDigitsMonth() {
        return twoDigitsMonth;
    }

    public void setTwoDigitsMonth(Boolean twoDigitsMonth) {
        this.twoDigitsMonth = twoDigitsMonth;
    }

    public Boolean getTwoDigitsDay() {
        return twoDigitsDay;
    }

    public void setTwoDigitsDay(Boolean twoDigitsDay) {
        this.twoDigitsDay = twoDigitsDay;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
