package com.monoton.horizont.crowd.pattern.painter;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by monoton on 16.8.2017.
 */
public class DrawPoint {

    private Vector2 position;
    private Vector2 velocity;
    private float[]  color;

    public DrawPoint(Vector2 position, Vector2 velocity, float[] color) {
        this.position = position;
        this.color = color;
        this.velocity = velocity;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float[]  getColor() {
        return color;
    }

    public Vector2 getVelocity() {
        return velocity;
    }
}
