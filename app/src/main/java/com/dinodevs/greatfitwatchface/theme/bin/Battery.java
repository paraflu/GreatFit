
package com.dinodevs.greatfitwatchface.theme.bin;

import java.util.HashMap;
import java.util.Map;

public class Battery {

    private Text text;
    private Scale scale;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public Scale getScale() {
        return scale;
    }

    public void setScale(Scale scale) {
        this.scale = scale;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
