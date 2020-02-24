
package com.dinodevs.greatfitwatchface.theme.bin;

import java.util.HashMap;
import java.util.Map;

public class Distance {

    private Number number;
    private Integer suffixImageIndex;
    private Integer decimalPointImageIndex;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Number getNumber() {
        return number;
    }

    public void setNumber(Number number) {
        this.number = number;
    }

    public Integer getSuffixImageIndex() {
        return suffixImageIndex;
    }

    public void setSuffixImageIndex(Integer suffixImageIndex) {
        this.suffixImageIndex = suffixImageIndex;
    }

    public Integer getDecimalPointImageIndex() {
        return decimalPointImageIndex;
    }

    public void setDecimalPointImageIndex(Integer decimalPointImageIndex) {
        this.decimalPointImageIndex = decimalPointImageIndex;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
