package com.monoton.horizont.crowd.pattern.engine;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.ai.steer.limiters.LinearAccelerationLimiter;
import com.badlogic.gdx.ai.steer.proximities.RadiusProximity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.monoton.horizont.crowd.pattern.Constants;
import com.monoton.horizont.crowd.pattern.SystemState;
import com.monoton.horizont.crowd.pattern.engine.border.BorderControl;
import com.monoton.horizont.crowd.pattern.scene.SteeringActorsScene;
import com.monoton.horizont.crowd.pattern.steering.PassingNeighboursSteering;

/**
 * Created by monoton on 13.8.2017.
 */
public class SteeringActorCreator {

    private Array<SteeringActor> characters;
    //private Table table;

    private BorderControl borderControl;
    private float PROXIMITY_FACTOR;
    private SteeringActorsScene steeringActorsScene;

    public SteeringActorCreator(Array<SteeringActor> characters, SteeringActorsScene steeringActorsScene, BorderControl borderControl, float PROXIMITY_FACTOR) {
        this.characters = characters;
//        this.table = table;
        this.steeringActorsScene = steeringActorsScene;

        this.borderControl = borderControl;
        this.PROXIMITY_FACTOR = PROXIMITY_FACTOR;
    }


    public void createSteeringActors(int number, RayHandler rayHandler, TextureRegion textureRegion){
        for (int i = 0; i < number;i++) {
            createSteeringActor(MathUtils.random(Gdx.graphics.getWidth()), MathUtils.random(Gdx.graphics.getHeight()),rayHandler, textureRegion);
        }
    }

    public boolean createSteeringActor(float x, float y, RayHandler rayHandler, TextureRegion textureRegion) {
        if(characters.size>= Constants.MAX_PARTICLE_NUMBER){
            return false;
        }
        final SteeringActor character = new SteeringActor(textureRegion, false, borderControl, this,rayHandler, x, y);
        character.setMaxLinearSpeed(SystemState.getInstance().getSpeedFactor());
        character.setMaxLinearAcceleration(100);

        RadiusProximity<Vector2> proximity = new RadiusProximity<Vector2>(character, characters,
                character.getBoundingRadius() * PROXIMITY_FACTOR);
        character.setProximity(proximity);



//			CollisionAvoidance<Vector2> collisionAvoidanceSB = new CollisionAvoidance<Vector2>(character, proximity);
        PassingNeighboursSteering<Vector2> passingNeighboursSB = new PassingNeighboursSteering<Vector2>(character, proximity);


        Wander<Vector2> wanderSB = new Wander<Vector2>(character) //
                // Don't use Face internally because independent facing is off
                .setFaceEnabled(false) //
                // We don't need a limiter supporting angular components because Face is not used
                // No need to call setAlignTolerance, setDecelerationRadius and setTimeToTarget for the same reason
                .setLimiter(new LinearAccelerationLimiter(30)) //
                .setWanderOffset(60) //
                .setWanderOrientation(0) //
                .setWanderRadius(40) //
                .setWanderRate(MathUtils.PI2 * 4);

        PrioritySteering<Vector2> prioritySteeringSB = new PrioritySteering<Vector2>(character, 0.0001f);
//			prioritySteeringSB.add(collisionAvoidanceSB);
        prioritySteeringSB.add(passingNeighboursSB);
//			prioritySteeringSB.add(wanderSB);

        character.setSteeringBehavior(prioritySteeringSB);

//        setRandomOrientation(character);
        setOrientationTowardsCenter(x, y, character);
        speedUp(character);


        characters.add(character);
//        table.addActor(character);
//        steeringActorsScene.addSteeringActor(character);

        return true;

    }

    public boolean removeSteeringActor(){
        if(characters.size>0){
            /*SteeringActor steeringActor = characters.get(0);
            steeringActor.cleanResources();
            characters.removeIndex(0);*/
            return true;
        }
        return false;
    }

    private void setOrientationTowardsCenter(float x, float y, SteeringActor character){
        Vector2 center = new Vector2(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        Vector2 current = new Vector2(x, y);
        float orientation = current.sub(center).angleRad();
        character.setOrientation(orientation);
        if (!character.isIndependentFacing()) {
            // Set random initial non-zero linear velocity since independent facing is off
            character.angleToVector(character.getLinearVelocity(), orientation).scl(character.getMaxLinearSpeed()/5);
        }


    }

    public void setSpeed(float speed){
        for(SteeringActor steeringActor : characters){
            steeringActor.setMaxLinearSpeed(speed);
        }

    }

    public void setShape(TextureRegion textureRegion){
        for(SteeringActor steeringActor : characters){
            steeringActor.setRegion(textureRegion);
        }

    }

    public void setRadius(float radius){
        for(SteeringActor steeringActor : characters){
           steeringActor.getProximity().setRadius(radius);
        }
    }
   public void dispose(){
       for(SteeringActor steeringActor : characters){
           steeringActor.cleanResources();
       }
   }

    private void speedUp(SteeringActor character) {
        //character.getLinearVelocity().set(new Vector2(MathUtils.random(-1, 1),MathUtils.random(-1, 1))).nor().scl(character.getMaxLinearAcceleration());
        character.getLinearVelocity().scl(character.getMaxLinearAcceleration());
    }

    private void setRandomOrientation (SteeringActor character) {
        float orientation = MathUtils.random(-MathUtils.PI, MathUtils.PI);
        character.setOrientation(orientation);
        if (!character.isIndependentFacing()) {
            // Set random initial non-zero linear velocity since independent facing is off
            character.angleToVector(character.getLinearVelocity(), orientation).scl(character.getMaxLinearSpeed()/5);
        }
    }
}
