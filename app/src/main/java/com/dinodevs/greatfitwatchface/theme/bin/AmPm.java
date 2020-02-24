
package com.dinodevs.greatfitwatchface.theme.bin;

import java.util.HashMap;
import java.util.Map;

public class AmPm {

    private Integer x;
    private Integer y;
    private Integer imageIndexAMCN;
    private Integer imageIndexPMCN;
    private Integer imageIndexAMEN;
    private Integer imageIndexPMEN;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getImageIndexAMCN() {
        return imageIndexAMCN;
    }

    public void setImageIndexAMCN(Integer imageIndexAMCN) {
        this.imageIndexAMCN = imageIndexAMCN;
    }

    public Integer getImageIndexPMCN() {
        return imageIndexPMCN;
    }

    public void setImageIndexPMCN(Integer imageIndexPMCN) {
        this.imageIndexPMCN = imageIndexPMCN;
    }

    public Integer getImageIndexAMEN() {
        return imageIndexAMEN;
    }

    public void setImageIndexAMEN(Integer imageIndexAMEN) {
        this.imageIndexAMEN = imageIndexAMEN;
    }

    public Integer getImageIndexPMEN() {
        return imageIndexPMEN;
    }

    public void setImageIndexPMEN(Integer imageIndexPMEN) {
        this.imageIndexPMEN = imageIndexPMEN;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
