package com.monoton.horizont.crowd.pattern.steering;

import com.badlogic.gdx.ai.steer.*;
import com.badlogic.gdx.ai.steer.behaviors.CollisionAvoidance;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.monoton.horizont.crowd.pattern.SystemState;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by monoton on 8.8.2017.
 */
public class PassingNeighboursSteering<T extends Vector<T>> extends GroupBehavior<T> implements Proximity.ProximityCallback<T> {

    private List<Steerable<T>> neighbours;


    private float shortestTime;
    private Steerable<T> firstNeighbor;
    private float firstMinSeparation;
    private float firstDistance;
    private T firstRelativePosition;
    private T firstRelativeVelocity;
    private T relativePosition;
    private T relativeVelocity;


    /**
     * Creates a GroupBehavior for the specified owner and proximity.
     *
     * @param owner     the owner of this behavior.
     * @param proximity the proximity to detect the owner's neighbors
     */
    public PassingNeighboursSteering(Steerable<T> owner, Proximity<T> proximity) {
        super(owner, proximity);

        neighbours = new ArrayList<Steerable<T>>();

        this.firstRelativePosition = newVector(owner);
        this.firstRelativeVelocity = newVector(owner);

        this.relativeVelocity = newVector(owner);
    }

    @Override
    public boolean reportNeighbor(Steerable<T> neighbor) {
        neighbours.add(neighbor);

// Calculate the time to collision
        relativePosition.set(neighbor.getPosition()).sub(owner.getPosition());
        relativeVelocity.set(neighbor.getLinearVelocity()).sub(owner.getLinearVelocity());
        float relativeSpeed2 = relativeVelocity.len2();

        // Collision can't happen when the agents have the same linear velocity.
        // Also, note that timeToTarget would be NaN due to the indeterminate form 0/0 and,
        // since any comparison involving NaN returns false, it would become the shortestTime,
        // so defeating the algorithm.
        if (relativeSpeed2 == 0) return false;

        float timeToCollision = -relativePosition.dot(relativeVelocity) / relativeSpeed2;

        // If timeToCollision is negative, i.e. the owner is already moving away from the the neighbor,
        // or it's not the most imminent collision then no action needs to be taken.
        if (timeToCollision <= 0 || timeToCollision >= shortestTime) return false;

        // Check if it is going to be a collision at all
        float distance = relativePosition.len();
        float minSeparation = distance - (float)Math.sqrt(relativeSpeed2) * timeToCollision /* shortestTime */;
        if (minSeparation > owner.getBoundingRadius() + neighbor.getBoundingRadius()) return false;

        // Store most imminent collision data
        shortestTime = timeToCollision;
        firstNeighbor = neighbor;
        firstMinSeparation = minSeparation;
        firstDistance = distance;
        firstRelativePosition.set(relativePosition);
        firstRelativeVelocity.set(relativeVelocity);

        return true;
    }

    @Override
    protected SteeringAcceleration<T> calculateRealSteering(SteeringAcceleration<T> steering) {

        shortestTime = Float.POSITIVE_INFINITY;
        firstNeighbor = null;
        firstMinSeparation = 0;
        firstDistance = 0;
        relativePosition = steering.linear;

        neighbours.clear();

        // Take into consideration each neighbor to find the most imminent collision.
        int neighborCount = proximity.findNeighbors(this);

        // If we have no target, then return no steering acceleration
        if (neighborCount == 0 || firstNeighbor == null) return steering.setZero();

       /* // If we're going to hit exactly, or if we're already
        // colliding, then do the steering based on current position.
        if (firstMinSeparation <= 0 || firstDistance < owner.getBoundingRadius() + firstNeighbor.getBoundingRadius()) {
            relativePosition.set(firstNeighbor.getPosition()).sub(owner.getPosition());
        } else {
            // Otherwise calculate the future relative position
            relativePosition.set(firstRelativePosition).mulAdd(firstRelativeVelocity, shortestTime);
        }*/

        T neighboursResult = newVector(owner);
        neighboursResult.setZero();
        for(Steerable<T> neighbour :  neighbours){
            neighboursResult.add(neighbour.getLinearVelocity());
        }

        /**
         *
         * make neighbours result small as to not disturb the particle much
         */
        T smallNeighboursResult = neighboursResult.cpy().nor().scl(SystemState.getInstance().getOrderFactor());

        T ownerVelocity = newVector(owner);
        ownerVelocity.setZero();
        ownerVelocity.add(owner.getLinearVelocity());
        ownerVelocity.nor();

        steering.linear = ownerVelocity.cpy().add(smallNeighboursResult);

        /**
         * end of comment
         */

        /**
         * normalize speed difference
         */
        steering.linear.nor();



        SystemState.getInstance().getClosenessResponse().determineAndExecute(steering, owner, neighbours);





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
