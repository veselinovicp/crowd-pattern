package com.monoton.horizont.crowd.pattern.steering.closeness;

/**
 * Created by monoton on 15.8.2017.
 */

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.math.Vector;

import java.util.List;

public interface ClosenessResponse<T extends Vector<T>>  {

    void determineAndExecute(SteeringAcceleration<T> steering, Steerable<T> owner, List<Steerable<T>> neighbours);
}
