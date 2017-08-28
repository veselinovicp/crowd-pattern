package com.monoton.horizont.crowd.pattern.painter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.monoton.horizont.crowd.pattern.Constants;
import com.monoton.horizont.crowd.pattern.SystemState;
import com.monoton.horizont.crowd.pattern.engine.SteeringActor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by monoton on 16.8.2017.
 */
public abstract class ShootingStarPainter{

    private List<DrawPoint> drawPoints = new ArrayList<DrawPoint>();

    protected Array<SteeringActor> characters;
    protected SteeringActor steeringActor;
    protected int tailSize;



    ShootingStarPainter(final Array<SteeringActor> characters, SteeringActor steeringActor, int tailSize) {
        this.characters = characters;
        this.steeringActor = steeringActor;
        this.tailSize = tailSize;


    }

    protected abstract Vector2 getVelocity();
    protected abstract Vector2 getPosition();
    protected abstract TextureRegion getRegion();


    public void draw(Batch batch, Body body, float parentAlpha) {



        TextureRegion region = getRegion();

        Vector2 velocity = getVelocity();
        Vector2 position = getPosition();
        /**
         * first draw old ones
         */

        SystemState.getInstance().getTailPainter().drawTail(drawPoints, region, batch, parentAlpha);





//        float[] color = DrawUtils.getColor(velocity.angleRad());
        float[] color = SystemState.getInstance().getColorMachine().getColor(position, velocity);


        batch.setColor(color[0], color[1], color[2], parentAlpha);
        batch.draw(region, position.x, position.y, region.getRegionWidth(), region.getRegionHeight());


        drawPoints.add(new DrawPoint(position, color));


        removeTail();



    }

    public static ShootingStarPainter getShootingStarPainter(String type, Array<SteeringActor> characters, SteeringActor steeringActor, int tailSize){
        if(type.equals(Constants.SHOOTING_STAR_PAINTER_AVERAGE)){
            return new AverageShootingStarPainter(characters, steeringActor, tailSize);
        }
        if(type.equals(Constants.SHOOTING_STAR_PAINTER_SINGLE)){
            return new SingleShootingStartPainter(characters, steeringActor, tailSize);
        }


        throw new RuntimeException("No shooting star painter by type: "+type+" exists.");

    }

    private void removeTail() {
        int i=0;
        while(drawPoints.size()>tailSize){
            drawPoints.remove(i);
            i++;
        }
    }


}
