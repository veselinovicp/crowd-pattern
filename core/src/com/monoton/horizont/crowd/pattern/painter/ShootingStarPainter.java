package com.monoton.horizont.crowd.pattern.painter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
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




    ShootingStarPainter(final Array<SteeringActor> characters, SteeringActor steeringActor) {
        this.characters = characters;
        this.steeringActor = steeringActor;



    }

    protected abstract Vector2 getVelocity();
    protected abstract Vector2 getPosition();
    protected abstract TextureRegion getRegion();


    public Array<DrawPoint> draw(Batch batch, float parentAlpha) {



        TextureRegion region = getRegion();

        Vector2 velocity = getVelocity();
        Vector2 position = getPosition();
        /**
         * first draw old ones
         */

        Array<DrawPoint> drawPoints = SystemState.getInstance().getTailPainter().drawTail(this.drawPoints, region, batch, parentAlpha);


//        float[] color = DrawUtils.getColor(velocity.angleRad());
        float[] color = SystemState.getInstance().getColorMachine().getColor(position, velocity);


        batch.setColor(color[0], color[1], color[2], parentAlpha);

/*        public void draw (TextureRegion region, float x, float y, float originX, float originY, float width, float height,
        float scaleX, float scaleY, float rotation);*/

//        batch.draw(region, position.x, position.y, region.getRegionWidth(), region.getRegionHeight());
        batch.draw(region, position.x, position.y,region.getRegionWidth()/2,region.getRegionHeight()/2, region.getRegionWidth(), region.getRegionHeight(),1,1,velocity.angle());


        this.drawPoints.add(new DrawPoint(position, velocity));


        removeTail();

        return drawPoints;



    }

    public static ShootingStarPainter getShootingStarPainter(String type, Array<SteeringActor> characters, SteeringActor steeringActor){
        if(type.equals(Constants.SHOOTING_STAR_PAINTER_AVERAGE)){
            return new AverageShootingStarPainter(characters, steeringActor);
        }
        if(type.equals(Constants.SHOOTING_STAR_PAINTER_SINGLE)){
            return new SingleShootingStartPainter(characters, steeringActor);
        }


        throw new RuntimeException("No shooting star painter by type: "+type+" exists.");

    }

    private void removeTail() {
        int i=0;
        while(drawPoints.size()>SystemState.getInstance().getTailLengthFactor()){
            if(i<drawPoints.size()) {
                drawPoints.remove(i);
                i++;
            }else{
                break;
            }
        }
    }


}
