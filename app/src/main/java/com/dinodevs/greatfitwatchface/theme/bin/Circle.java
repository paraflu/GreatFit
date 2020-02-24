
package com.dinodevs.greatfitwatchface.theme.bin;

import java.util.HashMap;
import java.util.Map;

public class Circle {

    private Integer centerX;
    private Integer centerY;
    private Integer radiusX;
    private Integer radiusY;
    private Integer startAngle;
    private Integer endAngle;
    private Integer width;
    private String color;
    private Integer flatness;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getCenterX() {
        return centerX;
    }

    public void setCenterX(Integer centerX) {
        this.centerX = centerX;
    }

    public Integer getCenterY() {
        return centerY;
    }

    public void setCenterY(Integer centerY) {
        this.centerY = centerY;
    }

    public Integer getRadiusX() {
        return radiusX;
    }

    public void setRadiusX(Integer radiusX) {
        this.radiusX = radiusX;
    }

    public Integer getRadiusY() {
        return radiusY;
    }

    public void setRadiusY(Integer radiusY) {
        this.radiusY = radiusY;
    }

    public Integer getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(Integer startAngle) {
        this.startAngle = startAngle;
    }

    public Integer getEndAngle() {
        return endAngle;
    }

    public void setEndAngle(Integer endAngle) {
        this.endAngle = endAngle;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getFlatness() {
        return flatness;
    }

    public void setFlatness(Integer flatness) {
        this.flatness = flatness;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
