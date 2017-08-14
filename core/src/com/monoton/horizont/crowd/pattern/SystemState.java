package com.monoton.horizont.crowd.pattern;

/**
 * Created by monoton on 14.8.2017.
 */
public class SystemState {
    float orderFactor;

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
}
