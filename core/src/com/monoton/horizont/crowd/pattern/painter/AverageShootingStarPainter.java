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
    private Vector2 norSumVelocity = new Vector2();
    private Vector2 averagePosition = new Vector2();


    AverageShootingStarPainter(Array<SteeringActor> characters, SteeringActor steeringActor) {
        super(characters, steeringActor);
    }

    @Override
    protected Vector2 getVelocity() {

        return SteeringActorUtils.getNorSumVelocity(characters, norSumVelocity);

    }

    @Override
    protected Vector2 getPosition() {

        return SteeringActorUtils.getAveragePosition(characters, averagePosition);
    }

    @Override
    protected TextureRegion getRegion() {
        return characters.get(0).getRegion();
    }
}
