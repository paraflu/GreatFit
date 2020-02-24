
package com.dinodevs.greatfitwatchface.theme.bin;

import java.util.HashMap;
import java.util.Map;

public class Pulse {

    private Integer topLeftX;
    private Integer topLeftY;
    private Integer bottomRightX;
    private Integer bottomRightY;
    private String alignment;
    private Integer spacing;
    private Integer imageIndex;
    private Integer imagesCount;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getTopLeftX() {
        return topLeftX;
    }

    public void setTopLeftX(Integer topLeftX) {
        this.topLeftX = topLeftX;
    }

    public Integer getTopLeftY() {
        return topLeftY;
    }

    public void setTopLeftY(Integer topLeftY) {
        this.topLeftY = topLeftY;
    }

    public Integer getBottomRightX() {
        return bottomRightX;
    }

    public void setBottomRightX(Integer bottomRightX) {
        this.bottomRightX = bottomRightX;
    }

    public Integer getBottomRightY() {
        return bottomRightY;
    }

    public void setBottomRightY(Integer bottomRightY) {
        this.bottomRightY = bottomRightY;
    }

    public String getAlignment() {
        return alignment;
    }

    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }

    public Integer getSpacing() {
        return spacing;
    }

    public void setSpacing(Integer spacing) {
        this.spacing = spacing;
    }

    public Integer getImageIndex() {
        return imageIndex;
    }

    public void setImageIndex(Integer imageIndex) {
        this.imageIndex = imageIndex;
    }

    public Integer getImagesCount() {
        return imagesCount;
    }

    public void setImagesCount(Integer imagesCount) {
        this.imagesCount = imagesCount;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
