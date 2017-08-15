package com.monoton.horizont.crowd.pattern;

/**
 * Created by monoton on 14.8.2017.
 */
public class SystemState {
    float orderFactor=1.0f;
    float distanceFactor=0.1f;
    float radiusFactor=10;

    private static SystemState ourInstance = new SystemState();

    public static SystemState getInstance() {
        return ourInstance;
    }

    private SystemState() {
    }

    public float getOrderFactor() {
        return orderFactor;
    }

    public void setOrderFactor(float orderFactor) {
        this.orderFactor = orderFactor;
    }

    public float getDistanceFactor() {
        return distanceFactor;
    }

    public void setDistanceFactor(float distanceFactor) {
        this.distanceFactor = distanceFactor;
    }

    public float getRadiusFactor() {
        return radiusFactor;
    }

    public void setRadiusFactor(float radiusFactor) {
        this.radiusFactor = radiusFactor;
    }
}
