package com.monoton.horizont.crowd.pattern.utils;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.monoton.horizont.crowd.pattern.engine.SteeringActor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by monoton on 28.8.2017.
 */
public class SteeringActorUtils {

    public static Vector2 getNorSumVelocity(Array<SteeringActor> characters) {
        List<Vector2> linearVelocities = new ArrayList<Vector2>();
        for (SteeringActor steeringActor : characters){

            linearVelocities.add(steeringActor.getLinearVelocity());

        }

        return DrawUtils.calculateNorSum(linearVelocities);

    }

    public static Vector2 getAveragePosition(Array<SteeringActor> characters) {

        List<Vector2> positions = new ArrayList<Vector2>();

        for (SteeringActor steeringActor : characters){
            positions.add(steeringActor.getPosition());


        }
        TextureRegion region = characters.get(0).getRegion();
        return DrawUtils.calculateAvarage(positions).add(-region.getRegionWidth()/2,-region.getRegionHeight()/2);
    }
}
