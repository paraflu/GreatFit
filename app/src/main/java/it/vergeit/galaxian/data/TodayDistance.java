package it.vergeit.galaxian.data;

/**
 * Distance
 */

public class TodayDistance {

    private final double distance;

    public TodayDistance(double distance) {
        this.distance = distance;
    }

    public float getDistance() {
        return (float) distance;
    }
}
