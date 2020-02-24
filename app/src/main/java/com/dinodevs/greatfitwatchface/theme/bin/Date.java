
package com.dinodevs.greatfitwatchface.theme.bin;

import java.util.HashMap;
import java.util.Map;

public class Date {

    private MonthAndDay monthAndDay;
    private WeekDay weekDay;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public MonthAndDay getMonthAndDay() {
        return monthAndDay;
    }

    public void setMonthAndDay(MonthAndDay monthAndDay) {
        this.monthAndDay = monthAndDay;
    }

    public WeekDay getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(WeekDay weekDay) {
        this.weekDay = weekDay;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
