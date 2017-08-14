package com.monoton.horizont.crowd.pattern.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.monoton.horizont.crowd.pattern.engine.SteeringActor;

/**
 * Created by monoton on 14.8.2017.
 */
public class SteeringActorsScene extends Actor {
    private Array<SteeringActor> characters;


    public SteeringActorsScene(Array<SteeringActor> characters) {
        this.characters = characters;
        this.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for (SteeringActor steeringActor : characters){
            steeringActor.draw(batch, parentAlpha);
        }
    }
    float lastUpdateTime = 0;
    @Override
    public void act (float delta) {
        float time = GdxAI.getTimepiece().getTime();
        if (lastUpdateTime != time) {
            lastUpdateTime = time;
            for (SteeringActor steeringActor : characters){
                steeringActor.act(delta);
            }
        }
    }

    public void addSteeringActor(SteeringActor steeringActor){
        characters.add(steeringActor);

    }
}
