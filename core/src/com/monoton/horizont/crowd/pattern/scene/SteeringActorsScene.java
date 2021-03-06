package com.monoton.horizont.crowd.pattern.scene;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.monoton.horizont.crowd.pattern.Constants;
import com.monoton.horizont.crowd.pattern.engine.SteeringActor;
import com.monoton.horizont.crowd.pattern.engine.SteeringActorEngine;
import com.monoton.horizont.crowd.pattern.painter.ShootingStarPainter;

/**
 * Created by monoton on 14.8.2017.
 */
public class SteeringActorsScene extends Actor {
    private ShootingStarPainter shootingStarPainter;

    private Array<SteeringActor> characters;

    private Texture background;

    private TextureRegion mShape;



    public SteeringActorsScene(final Array<SteeringActor> characters, final RayHandler rayHandler, Texture background, final TextureRegion shape) {
        this.background = background;
        this.characters = characters;
        this.mShape = shape;

        this.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        shootingStarPainter = ShootingStarPainter.getShootingStarPainter(Constants.SHOOTING_STAR_PAINTER_AVERAGE, characters, null);

        addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(button == Input.Buttons.LEFT){
                    TextureRegion region = characters.get(0).getRegion();
                    SteeringActorEngine steeringActorEngine = characters.get(0).getSteeringActorEngine();
                    float posX = x + region.getRegionWidth()/2;
                    float posY = y + region.getRegionHeight()/2;

                    steeringActorEngine.createSteeringActor(posX, posY, rayHandler, mShape);
                    
                }
                return true;
            }
        });//addListener



    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(background,0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        for (SteeringActor steeringActor : characters){
            steeringActor.draw(batch, parentAlpha);
        }
//        shootingStarPainter.draw(batch, parentAlpha);
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

    public void setShape(TextureRegion shape) {
        this.mShape = shape;
    }


}
