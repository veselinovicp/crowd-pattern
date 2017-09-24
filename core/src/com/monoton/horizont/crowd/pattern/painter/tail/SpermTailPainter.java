package com.monoton.horizont.crowd.pattern.painter.tail;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.monoton.horizont.crowd.pattern.SystemState;
import com.monoton.horizont.crowd.pattern.painter.DrawPoint;

/**
 * Created by monoton on 20.8.2017.
 */
public class SpermTailPainter implements TailPainter{
    @Override
    public Array<DrawPoint> drawTail(Array<DrawPoint> drawPoints, TextureRegion region, Batch batch, float parentAlpha) {
        Array<DrawPoint> result = new Array<DrawPoint>();
        for(int i=0;i<drawPoints.size;i++){
            DrawPoint drawPoint = drawPoints.get(i);
            float[] drawPointColor = SystemState.getInstance().getColorMachine().getColor(drawPoint.getPosition(), drawPoint.getVelocity());
            float factor = i / (float) drawPoints.size;
            batch.setColor(drawPointColor[0], drawPointColor[1], drawPointColor[2], factor);//i/(float)drawPoints.size()
            batch.draw(region, drawPoint.getPosition().x, drawPoint.getPosition().y, region.getRegionWidth()*factor, region.getRegionHeight()*factor);

            drawPoint.setWidth(region.getRegionWidth()*factor);
            drawPoint.setHeight(region.getRegionHeight()*factor);
            result.add(drawPoint);



        }

        return result;
    }
}
