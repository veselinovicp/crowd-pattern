package com.monoton.horizont.crowd.pattern.steering.closeness;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.math.Vector;
import com.monoton.horizont.crowd.pattern.SystemState;

import java.util.List;

/**
 * Created by monoton on 15.8.2017.
 */
public class SimilarVelocityClosenessResponse<T extends Vector<T>> implements ClosenessResponse<T>{

    @Override
    public void determineAndExecute(SteeringAcceleration<T> steering, Steerable<T> owner, List<Steerable<T>> neighbours) {
        /**
         * if owner goes in approximately the same direction as neighbour go perpendicular to them
         */
        T velocityDifference = owner.getLinearVelocity().cpy().setZero();
        velocityDifference.setZero();
        velocityDifference.add(steering.linear).sub(owner.getLinearVelocity().cpy().nor());
        //velocityDifference.add(neighboursResult.cpy().nor()).sub(ownerVelocity);
        float velocityDifferenceSize = velocityDifference.len();
        if(velocityDifferenceSize< SystemState.getInstance().getDistanceFactor()){
//            steering.linear = (T) getPerpendicularNormalizedVector((Vector2)ownerVelocity);//neighboursResult ownerVelocity
            /**
             * go away from the closest neighbour
             */
            Steerable<T> closestNeighbour = getClosestNeighbour(owner, neighbours);
            steering.linear = getRelativeVector(owner, closestNeighbour).nor();

        }
    }

    private Steerable<T> getClosestNeighbour(Steerable<T> owner,List<Steerable<T>> neighbours){
        Steerable<T> result = neighbours.get(0);
        for(Steerable<T> neighbour :  neighbours){
            float min = getDistance(result, owner);
            float current = getDistance(neighbour, owner);
            if(current<=min){
                result = neighbour;
            }
        }
        return result;

    }

    private float getDistance(Steerable<T> first, Steerable<T> second){
        T relative = getRelativeVector(first, second);
        return relative.len();
    }

    private T getRelativeVector(Steerable<T> first, Steerable<T> second){
        T relative = first.getLinearVelocity().cpy().setZero();
        relative.setZero();
        relative.add(first.getPosition()).sub(second.getPosition());
        return relative;
    }


}
