package com.monoton.horizont.crowd.pattern.utils;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by monoton on 12.8.2017.
 */
public class DrawUtils {

    public static Vector2 calculateNorSum(List<Vector2> vectors){
        Vector2 result = vectors.get(0).cpy().setZero();
        for(Vector2 vector: vectors){
            result = result.add(vector);
        }
        return result.nor();
    }

    public static Vector2 calculateAvarage(List<Vector2> vectors){
        Vector2 result = vectors.get(0).cpy().setZero();
        for(Vector2 vector: vectors){
            result = result.add(vector);
        }
        return result.scl(1f/(float)vectors.size());

    }

    public static float[] getRainbowColor(float radAngle){
        if(radAngle<0){
            radAngle+=2*Math.PI;
        }
        float percent = radAngle/(float)(Math.PI*2);

        List<float[]> rainbowColorChain = getRainbowColorChain(100);
        int size = rainbowColorChain.size();
        int index = (int)(size * percent);
        if (index==size){
            index--;
        }
        return rainbowColorChain.get(index);


    }

    public static List<float[]> getRainbowColorChain(int perColorTransitions){
        List<float[]> rainbowColors = new ArrayList<float[]>();

        float[] red = new float[]{1,0,0,1};
        float[] orange = new float[]{1,0.5f,0,1};
        float[] yellow = new float[]{1,1,0,1};
        float[] green = new float[]{0,1,0,1};
        float[] blue = new float[]{0,0,1,1};
        float[] indigo = new float[]{75f/255f,0,130f/255f,1};
        float[] violet = new float[]{143f/255f,0,1,1};
        /**
         * add another red at the end to make final circular transition color smooth
         */
        float[] red2 = new float[]{1,0,0,1};
        rainbowColors.add(red);
        rainbowColors.add(orange);
        rainbowColors.add(yellow);
        rainbowColors.add(green);
        rainbowColors.add(blue);
        rainbowColors.add(indigo);
        rainbowColors.add(violet);
        rainbowColors.add(red2);
        return getColorChain(rainbowColors, perColorTransitions);

    }

    public static List<float[]> getColorChain(List<float[]> colorSpecter, int perColorTransitions){
        float[] firstInSpecter = colorSpecter.get(0);
        colorSpecter.add(firstInSpecter);

        List<float[]> result = new ArrayList<float[]>();

        int colorChainLength = colorSpecter.size() * perColorTransitions;

        int chainCounter =1;
        int colorInSpecterCounter=0;
        result.add(colorSpecter.get(0));
        do{
            colorInSpecterCounter=chainCounter/perColorTransitions;

            float[] firstColor = colorSpecter.get(colorInSpecterCounter);
            float[] secondColor = colorSpecter.get(colorInSpecterCounter + 1);

            float deltaRed = secondColor[0] - firstColor[0];
            float deltaGreen = secondColor[1] - firstColor[1];
            float deltaBlue = secondColor[2] - firstColor[2];

            float singleDeltaRed = deltaRed / (float) perColorTransitions;
            float singleDeltaGreen = deltaGreen / (float) perColorTransitions;
            float singleDeltaBlue = deltaBlue / (float) perColorTransitions;

            float newRed = firstColor[0]+(singleDeltaRed * (chainCounter%(perColorTransitions)));
            float newGreen = firstColor[1] +(singleDeltaGreen * (chainCounter%(perColorTransitions)));
            float newBlue = firstColor[2] + (singleDeltaBlue * (chainCounter%(perColorTransitions)));

            result.add(new float[]{newRed,newGreen,newBlue,1f});


            chainCounter++;



        }while (chainCounter<(colorSpecter.size()-1)*perColorTransitions);

        colorSpecter.remove(colorSpecter.size()-1);

        return result;
    }

}
