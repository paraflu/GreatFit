
package com.dinodevs.greatfitwatchface.theme.bin;

import java.util.HashMap;
import java.util.Map;

public class Activity {

    private Calories calories;
    private Pulse pulse;
    private Distance distance;
    private Steps steps;
    private CaloriesGraph caloriesGraph;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Calories getCalories() {
        return calories;
    }

    public void setCalories(Calories calories) {
        this.calories = calories;
    }

    public Pulse getPulse() {
        return pulse;
    }

    public void setPulse(Pulse pulse) {
        this.pulse = pulse;
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public Steps getSteps() {
        return steps;
    }

    public void setSteps(Steps steps) {
        this.steps = steps;
    }

    public CaloriesGraph getCaloriesGraph() {
        return caloriesGraph;
    }

    public void setCaloriesGraph(CaloriesGraph caloriesGraph) {
        this.caloriesGraph = caloriesGraph;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
