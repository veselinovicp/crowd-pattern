package com.monoton.horizont.crowd.pattern.painter.colors;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by monoton on 20.8.2017.
 */
public interface ColorMachine {
    float[] getColor(Vector2 position, Vector2 velocity);
}
