package com.monoton.horizont.crowd.pattern.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.monoton.horizont.crowd.pattern.engine.SteeringActor;
import com.monoton.horizont.crowd.pattern.engine.SteeringActorCreator;

/**
 * Created by monoton on 14.8.2017.
 */
public class SteeringActorsScene extends Actor {
    private Array<SteeringActor> characters;


    public SteeringActorsScene(final Array<SteeringActor> characters) {
        this.characters = characters;
        this.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(button == Input.Buttons.LEFT){
                    TextureRegion region = characters.get(0).getRegion();
                    SteeringActorCreator steeringActorCreator = characters.get(0).getSteeringActorCreator();
                    float posX = x - region.getRegionHeight()/2;
                    float posY = y - region.getRegionHeight()/2;
                    steeringActorCreator.createSteeringActor(posX, posY);
                }
                return true;
            }
        });
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
