package com.monoton.horizont.crowd.pattern;

import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.monoton.horizont.crowd.pattern.steering.closeness.ClosenessResponse;
import com.monoton.horizont.crowd.pattern.steering.closeness.ClosenessResponseFactory;

/**
 * Created by monoton on 14.8.2017.
 */
public class SystemState<T extends Vector<T>> {
    float orderFactor=1.0f;
    float distanceFactor=0.1f;
    float radiusFactor=10;

    private ClosenessResponse<T> closenessResponse;

    private static SystemState ourInstance = new SystemState();

    public static SystemState getInstance() {
        return ourInstance;
    }

    private SystemState() {
        ClosenessResponseFactory<T> closenessResponseFactory = new ClosenessResponseFactory<T>();
        closenessResponse = closenessResponseFactory.getClosenessResponse(Constants.CLOSENESS_RESPONSE_SIMILAR_VELOCITY);
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

    public ClosenessResponse<T> getClosenessResponse() {
        return closenessResponse;
    }

    public void setClosenessResponse(ClosenessResponse<T> closenessResponse) {
        this.closenessResponse = closenessResponse;
    }
}
