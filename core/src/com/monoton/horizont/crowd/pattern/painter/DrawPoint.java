package com.monoton.horizont.crowd.pattern.painter;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by monoton on 16.8.2017.
 */
public class DrawPoint {

    private Vector2 position;
    private float[]  color;

    public DrawPoint(Vector2 position, float[]  color) {
        this.position = position;
        this.color = color;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float[]  getColor() {
        return color;
    }
}
