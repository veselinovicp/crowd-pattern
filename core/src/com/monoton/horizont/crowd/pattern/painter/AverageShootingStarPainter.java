package com.monoton.horizont.crowd.pattern.painter;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.monoton.horizont.crowd.pattern.engine.SteeringActor;
import com.monoton.horizont.crowd.pattern.utils.SteeringActorUtils;

/**
 * Created by monoton on 19.8.2017.
 */
public class AverageShootingStarPainter extends ShootingStarPainter {
    AverageShootingStarPainter(Array<SteeringActor> characters, SteeringActor steeringActor, int tailSize) {
        super(characters, steeringActor,tailSize);
    }

    @Override
    protected Vector2 getVelocity() {

        return SteeringActorUtils.getNorSumVelocity(characters);

    }

    @Override
    protected Vector2 getPosition() {

        return SteeringActorUtils.getAveragePosition(characters);
    }

    @Override
    protected TextureRegion getRegion() {
        return characters.get(0).getRegion();
    }
}
