package com.monoton.horizont.crowd.pattern.painter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.monoton.horizont.crowd.pattern.Constants;
import com.monoton.horizont.crowd.pattern.engine.SteeringActor;
import com.monoton.horizont.crowd.pattern.utils.DrawUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by monoton on 16.8.2017.
 */
public abstract class ShootingStarPainter extends Actor {

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

    @Override
    public void draw(Batch batch, float parentAlpha) {



        TextureRegion region = getRegion();

        Vector2 velocity = getVelocity();
        Vector2 position = getPosition();
        /**
         * first draw old ones
         */
        for(int i=0;i<drawPoints.size();i++){
            DrawPoint drawPoint = drawPoints.get(i);
            float[] drawPointColor = drawPoint.getColor();
            float factor = i / (float) drawPoints.size();
            batch.setColor(drawPointColor[0], drawPointColor[1], drawPointColor[2], factor);//i/(float)drawPoints.size()
            batch.draw(region, drawPoint.getPosition().x, drawPoint.getPosition().y, region.getRegionWidth()*factor, region.getRegionHeight()*factor);

        }





        float[] color = DrawUtils.getRainbowColor(velocity.angleRad());


        batch.setColor(color[0], color[1], color[2], parentAlpha);
        batch.draw(region, position.x, position.y, region.getRegionWidth(), region.getRegionHeight());

        drawPoints.add(new DrawPoint(position, color));


        removeTail();



    }

    public static ShootingStarPainter getShootingStarPainter(String type, Array<SteeringActor> characters, SteeringActor steeringActor, int tailSize){
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
        while(drawPoints.size()>100){
            drawPoints.remove(i);
            i++;
        }
    }


}
