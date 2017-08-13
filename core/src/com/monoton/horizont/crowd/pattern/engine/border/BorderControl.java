package com.monoton.horizont.crowd.pattern.engine.border;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by monoton on 13.8.2017.
 */
public interface BorderControl {

    void overBorderX (Vector2 pos, Vector2 linearVelocity, float maxX, float maxY);
    void negativeX (Vector2 pos, Vector2 linearVelocity, float maxX, float maxY);
    void overBorderY (Vector2 pos, Vector2 linearVelocity, float maxX, float maxY);
    void negativeY (Vector2 pos, Vector2 linearVelocity, float maxX, float maxY);
}
