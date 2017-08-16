package com.monoton.horizont.crowd.pattern.painter;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.monoton.horizont.crowd.pattern.engine.SteeringActor;
import com.monoton.horizont.crowd.pattern.utils.DrawUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by monoton on 16.8.2017.
 */
public class CrowdPainter extends Actor {

    private List<DrawPoint> drawPoints = new ArrayList<DrawPoint>();

    private Array<SteeringActor> characters;


    public CrowdPainter(final Array<SteeringActor> characters) {
        this.characters = characters;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {



        TextureRegion region = characters.get(0).getRegion();

        /**
         * first draw old ones
         */
        for(int i=0;i<drawPoints.size();i++){
            DrawPoint drawPoint = drawPoints.get(i);
            float[] drawPointColor = drawPoint.getColor();
            batch.setColor(drawPointColor[0], drawPointColor[1], drawPointColor[2], i/(float)drawPoints.size());
            batch.draw(region, drawPoint.getPosition().x, drawPoint.getPosition().y, region.getRegionWidth()/2, region.getRegionHeight()/2);
        }

        List<Vector2> positions = new ArrayList<Vector2>();
        List<Vector2> linearVelocities = new ArrayList<Vector2>();
        for (SteeringActor steeringActor : characters){
            positions.add(steeringActor.getPosition());
            linearVelocities.add(steeringActor.getLinearVelocity());

        }

        Vector2 averageVelocity = DrawUtils.calculateNorSum(linearVelocities);
        Vector2 averagePosition = DrawUtils.calculateAvarage(positions);

        float[] color = DrawUtils.getRainbowColor(averageVelocity.angleRad());


        batch.setColor(color[0], color[1], color[2], parentAlpha);
        batch.draw(region, averagePosition.x, averagePosition.y, region.getRegionWidth()/2, region.getRegionHeight()/2);

        drawPoints.add(new DrawPoint(averagePosition, color));



    }


}
