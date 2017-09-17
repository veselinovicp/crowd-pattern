package com.monoton.horizont.crowd.pattern.engine.border;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by monoton on 13.8.2017.
 */
public class BounceBorderControl implements BorderControl {
    @Override
    public void overBorderX(Vector2 pos, Vector2 linearVelocity, float maxX, float maxY, float regionWidth, float regionHeight) {
        pos.x = maxX-regionWidth/2;
        linearVelocity.x = -linearVelocity.x;
    }

    @Override
    public void negativeX(Vector2 pos, Vector2 linearVelocity, float maxX, float maxY, float regionWidth, float regionHeight) {
        pos.x = regionWidth/2;
        linearVelocity.x = -linearVelocity.x;
    }

    @Override
    public void overBorderY(Vector2 pos, Vector2 linearVelocity, float maxX, float maxY, float regionWidth, float regionHeight) {
        pos.y = maxY -regionHeight/2;
        linearVelocity.y = -linearVelocity.y;
    }

    @Override
    public void negativeY(Vector2 pos, Vector2 linearVelocity, float maxX, float maxY, float regionWidth, float regionHeight) {
        pos.y = regionHeight/2;
        linearVelocity.y = -linearVelocity.y;
    }
}
