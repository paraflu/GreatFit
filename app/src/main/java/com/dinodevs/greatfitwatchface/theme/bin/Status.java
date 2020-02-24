
package com.dinodevs.greatfitwatchface.theme.bin;

import java.util.HashMap;
import java.util.Map;

public class Status {

    private Bluetooth bluetooth;
    private Alarm alarm;
    private DoNotDisturb doNotDisturb;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Bluetooth getBluetooth() {
        return bluetooth;
    }

    public void setBluetooth(Bluetooth bluetooth) {
        this.bluetooth = bluetooth;
    }

    public Alarm getAlarm() {
        return alarm;
    }

    public void setAlarm(Alarm alarm) {
        this.alarm = alarm;
    }

    public DoNotDisturb getDoNotDisturb() {
        return doNotDisturb;
    }

    public void setDoNotDisturb(DoNotDisturb doNotDisturb) {
        this.doNotDisturb = doNotDisturb;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
