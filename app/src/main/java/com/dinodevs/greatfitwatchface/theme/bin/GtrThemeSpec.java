
package com.dinodevs.greatfitwatchface.theme.bin;

import java.util.HashMap;
import java.util.Map;

public class GtrThemeSpec {

    private Info info;
    private Background background;
    private Time time;
    private Activity activity;
    private Date date;
    private StepsProgress stepsProgress;
    private Status status;
    private Battery battery;
    private AnalogDialFace analogDialFace;
    private Shortcuts shortcuts;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public Background getBackground() {
        return background;
    }

    public void setBackground(Background background) {
        this.background = background;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public StepsProgress getStepsProgress() {
        return stepsProgress;
    }

    public void setStepsProgress(StepsProgress stepsProgress) {
        this.stepsProgress = stepsProgress;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Battery getBattery() {
        return battery;
    }

    public void setBattery(Battery battery) {
        this.battery = battery;
    }

    public AnalogDialFace getAnalogDialFace() {
        return analogDialFace;
    }

    public void setAnalogDialFace(AnalogDialFace analogDialFace) {
        this.analogDialFace = analogDialFace;
    }

    public Shortcuts getShortcuts() {
        return shortcuts;
    }

    public void setShortcuts(Shortcuts shortcuts) {
        this.shortcuts = shortcuts;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
