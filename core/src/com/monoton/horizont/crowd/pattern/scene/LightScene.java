package com.monoton.horizont.crowd.pattern.scene;

import box2dLight.Light;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.monoton.horizont.crowd.pattern.Constants;
import com.monoton.horizont.crowd.pattern.SystemState;
import com.monoton.horizont.crowd.pattern.engine.SteeringActor;
import com.monoton.horizont.crowd.pattern.utils.SteeringActorUtils;

/**
 * Created by monoton on 28.8.2017.
 */
public class LightScene extends Actor {

    private Array<SteeringActor> characters;

    private Light light;
    private Vector3 position = new Vector3();

    float[] color;




    public LightScene(final Array<SteeringActor> characters, Light light) {
        this.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.characters =characters;
        this.light = light;



    }

    @Override
    public void draw(Batch batch, float parentAlpha) {


        Vector2 averagePosition = SteeringActorUtils.getAveragePosition(characters);

        calculateLightPosition(averagePosition);
        calculateColor(averagePosition);

        light.setColor(color[0], color[1], color[2], 1);

        light.setPosition(position.x, position.y);


    }

    private void calculateColor(Vector2 averagePosition){
        Vector2 velocity = SteeringActorUtils.getNorSumVelocity(characters);
        color = SystemState.getInstance().getColorMachine().getColor(averagePosition, velocity);
    }

    private void calculateLightPosition(Vector2 averagePosition){

        float factorX = averagePosition.x / Gdx.graphics.getWidth();
        float factorY = averagePosition.y / Gdx.graphics.getHeight();
        position.set(factorX * Constants.LIGHT_SCENE_WIDTH, factorY*Constants.LIGHT_SCENE_HEIGHT, 0);
    }
}
