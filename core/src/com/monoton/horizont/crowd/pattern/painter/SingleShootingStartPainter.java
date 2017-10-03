package com.monoton.horizont.crowd.pattern.painter;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.monoton.horizont.crowd.pattern.engine.SteeringActor;

/**
 * Created by monoton on 19.8.2017.
 */
public class SingleShootingStartPainter extends ShootingStarPainter {
    private Vector2 position = new Vector2();
    private Vector2 velocity = new Vector2();
    SingleShootingStartPainter(Array<SteeringActor> characters, SteeringActor steeringActor) {
        super(characters, steeringActor);
    }

    @Override
    protected Vector2 getVelocity() {
        velocity.set(steeringActor.getLinearVelocity());
//        return steeringActor.getLinearVelocity().cpy();
        return velocity;
    }

    @Override
    protected Vector2 getPosition() {

        TextureRegion region = steeringActor.getRegion();
   /*     position.set(steeringActor.getPosition().x, steeringActor.getPosition().y).add(-region.getRegionWidth()/2,-region.getRegionHeight()/2);
        return position;*/

        return steeringActor.getPosition().cpy().add(-region.getRegionWidth()/2,-region.getRegionHeight()/2);

    }

    @Override
    protected TextureRegion getRegion() {
        return steeringActor.getRegion();
    }
}
