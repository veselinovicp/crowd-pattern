package com.monoton.horizont.crowd.pattern.steering.closeness;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.math.Vector;

import java.util.List;

/**
 * Created by monoton on 16.8.2017.
 */
public class EmptyClosenessResponse<T extends Vector<T>> implements ClosenessResponse<T> {
    @Override
    public void determineAndExecute(SteeringAcceleration<T> steering, Steerable<T> owner, List<Steerable<T>> neighbours) {
        /**
         * nothing to do here
         */
    }
}
