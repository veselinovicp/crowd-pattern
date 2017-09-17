package com.monoton.horizont.crowd.pattern;

import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.monoton.horizont.crowd.pattern.painter.colors.ColorMachine;
import com.monoton.horizont.crowd.pattern.painter.colors.ColorMachineFactory;
import com.monoton.horizont.crowd.pattern.painter.tail.TailPainter;
import com.monoton.horizont.crowd.pattern.painter.tail.TailPainterFactory;
import com.monoton.horizont.crowd.pattern.steering.closeness.ClosenessResponse;
import com.monoton.horizont.crowd.pattern.steering.closeness.ClosenessResponseFactory;

/**
 * Created by monoton on 14.8.2017.
 */
public class SystemState<T extends Vector<T>> {
    float orderFactor=3.0f;
    float distanceFactor=0.2f;
    float radiusFactor=10;
    float speedFactor=80;
    float tailLengthFactor=140;
    float tailDensityFactor=0.8f;
    float lightSizeFactor=Constants.LIGHT_SCENE_WIDTH *1.0f;


    private ClosenessResponse<T> closenessResponse;

    private ColorMachine colorMachine;

    private TailPainter tailPainter;

    private static SystemState ourInstance = new SystemState();

    public static SystemState getInstance() {
        return ourInstance;
    }

    private SystemState() {
        ClosenessResponseFactory<T> closenessResponseFactory = new ClosenessResponseFactory<T>();
        closenessResponse = closenessResponseFactory.getClosenessResponse(Constants.CLOSENESS_RESPONSE_SIMILAR_VELOCITY);
        tailPainter = TailPainterFactory.getTailPainter(Constants.TAIL_PAINTER_SNAKE);

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

    public ColorMachine getColorMachine() {
        return colorMachine;
    }

    public void setColorMachine(ColorMachine colorMachine) {
        this.colorMachine = colorMachine;
    }

    public TailPainter getTailPainter() {
        return tailPainter;
    }

    public void setTailPainter(TailPainter tailPainter) {
        this.tailPainter = tailPainter;
    }

    public float getSpeedFactor() {
        return speedFactor;
    }

    public void setSpeedFactor(float speedFactor) {
        this.speedFactor = speedFactor;
    }

    public float getTailLengthFactor() {
        return tailLengthFactor;
    }

    public void setTailLengthFactor(float tailLengthFactor) {
        this.tailLengthFactor = tailLengthFactor;
    }

    public float getTailDensityFactor() {
        return tailDensityFactor;
    }

    public void setTailDensityFactor(float tailDensityFactor) {
        this.tailDensityFactor = tailDensityFactor;
    }

    public float getLightSizeFactor() {
        return lightSizeFactor;
    }

    public void setLightSizeFactor(float lightSizeFactor) {
        this.lightSizeFactor = lightSizeFactor;
    }
}
