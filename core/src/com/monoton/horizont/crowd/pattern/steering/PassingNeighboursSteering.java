package com.monoton.horizont.crowd.pattern.steering;

import com.badlogic.gdx.ai.steer.*;
import com.badlogic.gdx.math.Vector;
import com.monoton.horizont.crowd.pattern.SystemState;
import com.monoton.horizont.crowd.pattern.steering.closeness.ClosenessResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by monoton on 8.8.2017.
 */
public class PassingNeighboursSteering<T extends Vector<T>> extends GroupBehavior<T> implements Proximity.ProximityCallback<T> {

    private List<Steerable<T>> neighbours;

    private Steerable<T> firstNeighbor;

    private T neighboursResult;
    private T smallNeighboursResult;
    private  T ownerVelocity;
    private ClosenessResponse closenessResponse;


    /**
     * Creates a GroupBehavior for the specified owner and proximity.
     *
     * @param owner     the owner of this behavior.
     * @param proximity the proximity to detect the owner's neighbors
     */
    public PassingNeighboursSteering(Steerable<T> owner, Proximity<T> proximity) {
        super(owner, proximity);

        neighbours = new ArrayList<Steerable<T>>();


        neighboursResult = newVector(owner);
        smallNeighboursResult = newVector(owner);
        ownerVelocity = newVector(owner);
        closenessResponse = SystemState.getInstance().getClosenessResponse(owner);
    }

    @Override
    public boolean reportNeighbor(Steerable<T> neighbor) {
        neighbours.add(neighbor);

        firstNeighbor = neighbor;


        return true;
    }

    @Override
    protected SteeringAcceleration<T> calculateRealSteering(SteeringAcceleration<T> steering) {


        firstNeighbor = null;



        neighbours.clear();

        // Take into consideration each neighbor to find the most imminent collision.
        int neighborCount = proximity.findNeighbors(this);

        // If we have no target, then return no steering acceleration
        if (neighborCount == 0 || firstNeighbor == null) return steering.setZero();



        neighboursResult.setZero();
        for(Steerable<T> neighbour :  neighbours){
            neighboursResult.add(neighbour.getLinearVelocity());
        }

        /**
         *
         * make neighbours result small as to not disturb the particle much
         */
        smallNeighboursResult = neighboursResult.cpy().nor().scl(SystemState.getInstance().getOrderFactor());

        ownerVelocity.set(owner.getLinearVelocity()).nor();


        steering.linear = ownerVelocity.cpy().add(smallNeighboursResult);

        /**
         * end of comment
         */

        /**
         * normalize speed difference
         */
        steering.linear.nor();



        closenessResponse.determineAndExecute(steering, owner, neighbours);





        /**
         * make result as big as owner linear velocity
         */
        steering.linear.scl(owner.getLinearVelocity().len());

        // No angular acceleration
        steering.angular = 0.0f;

        // Output the steering
        return steering;
    }



    //
    // Setters overridden in order to fix the correct return type for chaining
    //

    @Override
    public PassingNeighboursSteering<T> setOwner (Steerable<T> owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public PassingNeighboursSteering<T> setEnabled (boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    /** Sets the limiter of this steering behavior. The given limiter must at least take care of the maximum linear acceleration.
     * @return this behavior for chaining. */
    @Override
    public PassingNeighboursSteering<T> setLimiter (Limiter limiter) {
        this.limiter = limiter;
        return this;
    }

}
