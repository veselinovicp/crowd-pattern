package com.monoton.horizont.crowd.pattern.painter;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.monoton.horizont.crowd.pattern.engine.SteeringActor;
import com.monoton.horizont.crowd.pattern.painter.colors.ColorMachine;
import com.monoton.horizont.crowd.pattern.utils.DrawUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by monoton on 19.8.2017.
 */
public class AverageShootingStarPainter extends ShootingStarPainter {
    AverageShootingStarPainter(Array<SteeringActor> characters, SteeringActor steeringActor, int tailSize) {
        super(characters, steeringActor, tailSize);
    }

    @Override
    protected Vector2 getVelocity() {
        List<Vector2> linearVelocities = new ArrayList<Vector2>();
        for (SteeringActor steeringActor : characters){

            linearVelocities.add(steeringActor.getLinearVelocity());

        }

        return DrawUtils.calculateNorSum(linearVelocities);

    }

    @Override
    protected Vector2 getPosition() {

        List<Vector2> positions = new ArrayList<Vector2>();

        for (SteeringActor steeringActor : characters){
            positions.add(steeringActor.getPosition());


        }
        TextureRegion region = characters.get(0).getRegion();
        return DrawUtils.calculateAvarage(positions).add(-region.getRegionWidth()/2,-region.getRegionHeight()/2);
    }

    @Override
    protected TextureRegion getRegion() {
        return characters.get(0).getRegion();
    }
}
