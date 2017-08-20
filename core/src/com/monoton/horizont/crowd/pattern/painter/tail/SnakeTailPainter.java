package com.monoton.horizont.crowd.pattern.painter.tail;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.monoton.horizont.crowd.pattern.painter.DrawPoint;

import java.util.List;

/**
 * Created by monoton on 20.8.2017.
 */
public class SnakeTailPainter implements TailPainter {
    float TAIL_PAINTER_PROXIMITY_FACTOR=0.5f;

    @Override
    public void drawTail(List<DrawPoint> drawPoints, TextureRegion region, Batch batch, float parentAlpha) {
        if(drawPoints==null || drawPoints.size()==0){
            return;
        }
        int regionWidth = region.getRegionWidth();
        int regionHeight = region.getRegionHeight();
        float radius = regionWidth * TAIL_PAINTER_PROXIMITY_FACTOR;
        DrawPoint justDrawn = drawPoints.get(0);
        for(int i=drawPoints.size()-1;i>=0;i--){
            DrawPoint drawPoint = drawPoints.get(i);
            int distance = calculateDistance(justDrawn.getPosition(), drawPoint.getPosition());
            if(distance>=radius){
                float[] drawPointColor = drawPoint.getColor();
                float factor = i / (float) drawPoints.size();
                batch.setColor(drawPointColor[0], drawPointColor[1], drawPointColor[2], factor);//i/(float)drawPoints.size()
                batch.draw(region, drawPoint.getPosition().x, drawPoint.getPosition().y, region.getRegionWidth()*factor, region.getRegionHeight()*factor);
                radius = region.getRegionWidth()*factor * TAIL_PAINTER_PROXIMITY_FACTOR;
                justDrawn = drawPoint;
            }



        }


    }

    private int calculateDistance(Vector2 vector1, Vector2 vector2){
        Vector2 result = vector1.cpy().setZero();
        return (int)result.add(vector1).sub(vector2).len();
    }

    private int calculateRadius(int regionWidth, int regionHeight){
        return (int)(0.5*Math.sqrt(regionHeight*regionHeight + regionWidth*regionWidth));
    }
}
