
package com.dinodevs.greatfitwatchface.theme.bin;

import java.util.HashMap;
import java.util.Map;

public class Coordinates {

    private Integer x;
    private Integer y;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
