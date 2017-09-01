package com.monoton.horizont.crowd.pattern.painter.tail;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.monoton.horizont.crowd.pattern.painter.DrawPoint;

import java.util.List;

/**
 * Created by monoton on 20.8.2017.
 */
public class SpermTailPainter implements TailPainter{
    @Override
    public void drawTail(List<DrawPoint> drawPoints, TextureRegion region, Batch batch, float parentAlpha) {
        for(int i=0;i<drawPoints.size();i++){
            DrawPoint drawPoint = drawPoints.get(i);
            float[] drawPointColor = drawPoint.getColor();
            float factor = i / (float) drawPoints.size();
            batch.setColor(drawPointColor[0], drawPointColor[1], drawPointColor[2], factor);//i/(float)drawPoints.size()
            batch.draw(region, drawPoint.getPosition().x, drawPoint.getPosition().y, region.getRegionWidth()*factor, region.getRegionHeight()*factor);



        }
    }
}
