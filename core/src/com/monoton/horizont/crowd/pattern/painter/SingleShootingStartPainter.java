package com.monoton.horizont.crowd.pattern.painter;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.monoton.horizont.crowd.pattern.engine.SteeringActor;

/**
 * Created by monoton on 19.8.2017.
 */
public class SingleShootingStartPainter extends ShootingStarPainter {
    SingleShootingStartPainter(Array<SteeringActor> characters, SteeringActor steeringActor, int tailSize) {
        super(characters, steeringActor, tailSize);
    }

    @Override
    protected Vector2 getVelocity() {
        return steeringActor.getLinearVelocity().cpy();
    }

    @Override
    protected Vector2 getPosition() {

        TextureRegion region = steeringActor.getRegion();
        return steeringActor.getPosition().cpy().add(-region.getRegionWidth()/2,-region.getRegionHeight()/2);
    }

    @Override
    protected TextureRegion getRegion() {
        return steeringActor.getRegion();
    }
}
