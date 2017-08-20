package com.monoton.horizont.crowd.pattern.painter.colors;

import com.badlogic.gdx.math.Vector2;
import com.monoton.horizont.crowd.pattern.utils.DrawUtils;

/**
 * Created by monoton on 20.8.2017.
 */
public class RainbowColorMachine implements ColorMachine {
    @Override
    public float[] getColor(Vector2 position, Vector2 velocity) {
        return  DrawUtils.getRainbowColor(velocity.angleRad());
    }
}
