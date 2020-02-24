
package com.dinodevs.greatfitwatchface.theme.bin;

import java.util.HashMap;
import java.util.Map;

public class Shortcuts {

    private State state;
    private Pulse pulse;
    private Weather weather;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Pulse getPulse() {
        return pulse;
    }

    public void setPulse(Pulse pulse) {
        this.pulse = pulse;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
