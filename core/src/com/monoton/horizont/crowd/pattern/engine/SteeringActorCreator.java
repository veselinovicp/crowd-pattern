package com.monoton.horizont.crowd.pattern.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.ai.steer.limiters.LinearAccelerationLimiter;
import com.badlogic.gdx.ai.steer.proximities.RadiusProximity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.monoton.horizont.crowd.pattern.engine.border.BorderControl;
import com.monoton.horizont.crowd.pattern.scene.SteeringActorsScene;
import com.monoton.horizont.crowd.pattern.steering.PassingNeighboursSteering;

/**
 * Created by monoton on 13.8.2017.
 */
public class SteeringActorCreator {

    private Array<SteeringActor> characters;
    //private Table table;
    private Texture img;
    private BorderControl borderControl;
    private int PROXIMITY_FACTOR;
    private SteeringActorsScene steeringActorsScene;

    public SteeringActorCreator(Array<SteeringActor> characters, SteeringActorsScene steeringActorsScene, Texture img, BorderControl borderControl, int PROXIMITY_FACTOR) {
        this.characters = characters;
//        this.table = table;
        this.steeringActorsScene = steeringActorsScene;
        this.img = img;
        this.borderControl = borderControl;
        this.PROXIMITY_FACTOR = PROXIMITY_FACTOR;
    }


    public void createSteeringActors(int number){
        for (int i = 0; i < number;i++) {
            createSteeringActor(MathUtils.random(Gdx.graphics.getWidth()), MathUtils.random(Gdx.graphics.getHeight()));
        }
    }

    public void createSteeringActor(float x, float y) {
        final SteeringActor character = new SteeringActor(new TextureRegion(img), false, borderControl);
        character.setMaxLinearSpeed(50);
        character.setMaxLinearAcceleration(100);

        RadiusProximity<Vector2> proximity = new RadiusProximity<Vector2>(character, characters,
                character.getBoundingRadius() * PROXIMITY_FACTOR);

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

        setPosition(character, x, y);
        setRandomOrientation(character);
        speedUp(character);


        characters.add(character);
//        table.addActor(character);
        steeringActorsScene.addSteeringActor(character);

    }

    private void setPosition(SteeringActor character, float x, float y) {
      /*  int maxTries = Math.max(100, others.size * others.size);
        SET_NEW_POS:
        while (--maxTries >= 0) {*/
            character.setPosition(x, y, Align.center);
            character.getPosition().set(character.getX(Align.center), character.getY(Align.center));
            /*for (int i = 0; i < others.size; i++) {
                SteeringActor other = (SteeringActor)others.get(i);
                if (character.getPosition().dst(other.getPosition()) <= character.getBoundingRadius() + other.getBoundingRadius()
                        + minDistanceFromBoundary) continue SET_NEW_POS;
            }
            return;
        }
        throw new GdxRuntimeException("Probable infinite loop detected");*/
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
