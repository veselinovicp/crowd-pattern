package com.monoton.horizont.crowd.pattern.engine.border;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by monoton on 13.8.2017.
 */
public class FlyThroughBorderControl implements BorderControl {
    @Override
    public void overBorderX(Vector2 pos, Vector2 linearVelocity, float maxX, float maxY, float regionWidth, float regionHeight) {
        pos.x = 0.0f;
    }

    @Override
    public void negativeX(Vector2 pos, Vector2 linearVelocity, float maxX, float maxY, float regionWidth, float regionHeight) {
        pos.x = maxX;
    }

    @Override
    public void overBorderY(Vector2 pos, Vector2 linearVelocity, float maxX, float maxY, float regionWidth, float regionHeight) {
        pos.y = 0.0f;
    }

    @Override
    public void negativeY(Vector2 pos, Vector2 linearVelocity, float maxX, float maxY, float regionWidth, float regionHeight) {
        pos.y = maxY;
    }
}
