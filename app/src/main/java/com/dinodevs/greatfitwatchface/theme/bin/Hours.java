
package com.dinodevs.greatfitwatchface.theme.bin;

import java.util.HashMap;
import java.util.Map;

public class Hours {

    private Tens tens;
    private Ones ones;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Tens getTens() {
        return tens;
    }

    public void setTens(Tens tens) {
        this.tens = tens;
    }

    public Ones getOnes() {
        return ones;
    }

    public void setOnes(Ones ones) {
        this.ones = ones;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
