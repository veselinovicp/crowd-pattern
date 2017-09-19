package com.monoton.horizont.crowd.pattern;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector;
import com.monoton.horizont.crowd.pattern.painter.colors.ColorMachine;
import com.monoton.horizont.crowd.pattern.painter.tail.TailPainter;
import com.monoton.horizont.crowd.pattern.painter.tail.TailPainterFactory;
import com.monoton.horizont.crowd.pattern.steering.closeness.ClosenessResponse;
import com.monoton.horizont.crowd.pattern.steering.closeness.ClosenessResponseFactory;

/**
 * Created by monoton on 14.8.2017.
 */
public class SystemState<T extends Vector<T>> {
    float orderFactor=Constants.DEFAULT_ORDER_FACTOR;
    float distanceFactor=Constants.DEFAULT_DISTANCE_FACTOR;
    float radiusFactor=Constants.DEFAULT_RADIUS_FACTOR;
    float speedFactor=Constants.DEFAULT_SPEED_FACTOR;
    float tailLengthFactor=Constants.DEFAULT_TAIL_LENGTH_FACTOR;
    float tailDensityFactor=Constants.DEFAULT_TAIL_DENSITY_FACTOR;
    float ambientFactor =Constants.DEFAULT_AMBINENT_FACTOR;
    int tailSize = Constants.DEFAULT_TAIL_SIZE;
    float lightSize = Constants.DEFAULT_LIGHT_SIZE;
    int particleStartNumber;
    int maxParticles;


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

        particleStartNumber = (int)(Constants.PARTICLE_DENSITY * (float)Gdx.graphics.getWidth() * (float)Gdx.graphics.getHeight());
        maxParticles = particleStartNumber * Constants.MAX_PARTICLE_FACTOR;
        System.out.println("particleStartNumber: "+particleStartNumber);

    }

    public void reset(){
         orderFactor=Constants.DEFAULT_ORDER_FACTOR;
         distanceFactor=Constants.DEFAULT_DISTANCE_FACTOR;
         radiusFactor=Constants.DEFAULT_RADIUS_FACTOR;
         speedFactor=Constants.DEFAULT_SPEED_FACTOR;
         tailLengthFactor=Constants.DEFAULT_TAIL_LENGTH_FACTOR;
         tailDensityFactor=Constants.DEFAULT_TAIL_DENSITY_FACTOR;
         ambientFactor =Constants.DEFAULT_AMBINENT_FACTOR;
         tailSize = Constants.DEFAULT_TAIL_SIZE;
         lightSize = Constants.DEFAULT_LIGHT_SIZE;
    }

    public int getMaxParticles() {
        return maxParticles;
    }

    public int getParticleStartNumber() {
        return particleStartNumber;
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

    public float getAmbientFactor() {
        return ambientFactor;
    }

    public void setAmbientFactor(float ambientFactor) {
        this.ambientFactor = ambientFactor;
    }

    public int getTailSize() {
        return tailSize;
    }

    public void setTailSize(int tailSize) {
        this.tailSize = tailSize;
    }

    public float getLightSize() {
        return lightSize;
    }

    public void setLightSize(float lightSize) {
        this.lightSize = lightSize;
    }
}
