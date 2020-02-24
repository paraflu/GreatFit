
package com.dinodevs.greatfitwatchface.theme.bin;

import java.util.HashMap;
import java.util.Map;

public class Info {

    private Integer deviceId;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
