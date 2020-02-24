
package com.dinodevs.greatfitwatchface.theme.bin;

import java.util.HashMap;
import java.util.Map;

public class Bluetooth {

    private Coordinates coordinates;
    private Integer imageIndexOn;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Integer getImageIndexOn() {
        return imageIndexOn;
    }

    public void setImageIndexOn(Integer imageIndexOn) {
        this.imageIndexOn = imageIndexOn;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
