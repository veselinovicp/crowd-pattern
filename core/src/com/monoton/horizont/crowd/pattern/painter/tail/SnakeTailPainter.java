package com.monoton.horizont.crowd.pattern.painter.tail;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.monoton.horizont.crowd.pattern.SystemState;
import com.monoton.horizont.crowd.pattern.painter.DrawPoint;

import java.util.List;

/**
 * Created by monoton on 20.8.2017.
 */
public class SnakeTailPainter implements TailPainter {


    @Override
    public Array<DrawPoint> drawTail(Array<DrawPoint> drawPoints, TextureRegion region, Batch batch, float parentAlpha) {
        Array<DrawPoint> result = new Array<DrawPoint>();

        float tailDensityFactor= SystemState.getInstance().getTailDensityFactor();

        if(drawPoints==null || drawPoints.size==0){
            return result;
        }
        int regionWidth = region.getRegionWidth();

        float radius = regionWidth * tailDensityFactor;
        DrawPoint justDrawn = drawPoints.get(0);
        for(int i=drawPoints.size-1;i>=0;i--){
            DrawPoint drawPoint = drawPoints.get(i);
            int distance = calculateDistance(justDrawn.getPosition(), drawPoint.getPosition());
            if(distance>=radius){


                float[] drawPointColor = SystemState.getInstance().getColorMachine().getColor(drawPoint.getPosition(), drawPoint.getVelocity());


                float factor = i / (float) drawPoints.size;

                batch.setColor(drawPointColor[0], drawPointColor[1], drawPointColor[2], factor);//  /2f

                batch.draw(region, drawPoint.getPosition().x, drawPoint.getPosition().y, region.getRegionWidth() / 2 * factor, region.getRegionHeight() / 2 * factor, region.getRegionWidth() * factor, region.getRegionHeight() * factor, 1, 1, drawPoint.getVelocity().angle());


                radius = region.getRegionWidth() * factor * tailDensityFactor;
                justDrawn = drawPoint;

                drawPoint.setWidth(region.getRegionWidth() * factor);
                drawPoint.setHeight(region.getRegionHeight() * factor);
                result.add(drawPoint);


            }
            if(result.size>SystemState.getInstance().getTailSize()){
                break;
            }



        }


        return result;


    }

    private int getIndex(List<DrawPoint> drawPoints,int j, int tailSize){
        int size = drawPoints.size();
        float factor = (j + 1) / (float) tailSize;
        return (int)(factor*size)-1;
    }

    private int calculateDistance(Vector2 vector1, Vector2 vector2){
        Vector2 result = vector1.cpy().setZero();
        return (int)result.add(vector1).sub(vector2).len();
    }

    private int calculateRadius(int regionWidth, int regionHeight){
        return (int)(0.5*Math.sqrt(regionHeight*regionHeight + regionWidth*regionWidth));
    }
}
