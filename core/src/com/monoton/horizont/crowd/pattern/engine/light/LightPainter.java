package com.monoton.horizont.crowd.pattern.engine.light;

import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.monoton.horizont.crowd.pattern.SystemState;
import com.monoton.horizont.crowd.pattern.painter.DrawPoint;
import com.monoton.horizont.crowd.pattern.utils.DrawUtils;

/**
 * Created by monoton on 1.9.2017.
 */
public class LightPainter {


    private Array<Light> tailLights = new Array<Light>();
    private RayHandler rayHandler;
    private Vector2 fixedPosition;
    private Vector2 box2dPosition;


    public LightPainter(RayHandler rayHandler) {

        this.rayHandler = rayHandler;
        fixedPosition = new Vector2();
        box2dPosition = new Vector2();

    }


    public void drawLight(Array<DrawPoint> tail){

        if(tail.size==0){
            return;
        }
        setTailLightsInactive();

        int j=0;
        if(tailLights.size> tail.size){


//            tailLights.removeIndex(j).setActive(false);
            tailLights.removeIndex(j).remove(false);
            j++;

        }


        while(tail.size> tailLights.size){
            tailLights.add(new PointLight(rayHandler, 5));

        }



        for(int i=0; i<tail.size;i++){
            DrawPoint drawPoint = tail.get(i);
            Light light = tailLights.get(i);

            setFixedPosition(drawPoint);

            adjustLight(fixedPosition, drawPoint.getVelocity(), light);
            light.setActive(true);
            light.setDistance(DrawUtils.getBox2DWidth(drawPoint.getWidth())*SystemState.getInstance().getLightSize());
        }

    }

    private void setFixedPosition(DrawPoint drawPoint) {

        fixedPosition.set(drawPoint.getPosition().x + drawPoint.getWidth() / 2, drawPoint.getPosition().y + drawPoint.getHeight()  / 2);
    }



    public void setTailLightsInactive(){
        for(Light light : tailLights){
            light.setActive(false);
        }
    }

    private void adjustLight(Vector2 position, Vector2 velocity, Light light){
        setLightColor(position, velocity, light);

        setLightDirection(velocity, light);

        setLightPosition(position, light);


    }

    private void setLightPosition(Vector2 position, Light light) {
        DrawUtils.getBox2DCoords(position, box2dPosition);
        light.setPosition(box2dPosition);

    }

    private void setLightDirection(Vector2 velocity, Light light) {
        light.setDirection(velocity.angle());
    }

    private void setLightColor(Vector2 position, Vector2 velocity, Light light) {
        float[] color = SystemState.getInstance().getColorMachine().getColor(position, velocity);

        light.setColor(color[0],color[1],color[2],1);
    }

}
