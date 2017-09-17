package com.monoton.horizont.crowd.pattern.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.monoton.horizont.crowd.pattern.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by monoton on 12.8.2017.
 */
public class DrawUtils {

    public static float getBox2DWidth(float x){
        float factorX = x / Gdx.graphics.getWidth();
        return factorX * Constants.LIGHT_SCENE_WIDTH;
    }

    public static String formatFloat(float value){
        return String.format("%.1f", value);
    }

    public static Vector2 getBox2DCoords(Vector2 vector){
        return getBox2DCoords(vector.x, vector.y);
    }


    public static Vector2 getBox2DCoords(float x, float y){
        Vector2 vector2 = new Vector2();
        float factorX = x / Gdx.graphics.getWidth();
        float factorY = y / Gdx.graphics.getHeight();
        vector2.set(factorX * Constants.LIGHT_SCENE_WIDTH, factorY*Constants.LIGHT_SCENE_HEIGHT);

        return vector2;
    }

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

        return getColor(radAngle, rainbowColorChain);


    }

    public static float[] getRastaColor(float radAngle){

        return getColor(radAngle, rastaColorChain);


    }

    public static float[] getDreamMagnetColor(float radAngle){

        return getColor(radAngle, dreamMagnetColorChain);


    }

    public static float[] getEightiesColor(float radAngle){

        return getColor(radAngle, eightiesColorChain);


    }

    public static float[] getRandomColor(float radAngle){

        return getColor(radAngle, randomColorChain);


    }

    public static float[] getColor(float radAngle, List<float[]> colorChain){
        if(radAngle<0){
            radAngle+=2*Math.PI;
        }
        float percent = radAngle/(float)(Math.PI*2);


        int size = colorChain.size();
        int index = (int)(size * percent);
        if (index==size){
            index--;
        }
        return colorChain.get(index);

    }

    static List<float[]> rainbowColorChain;
    static List<float[]> rastaColorChain;
    static List<float[]> dreamMagnetColorChain;
    static List<float[]> eightiesColorChain;
    static List<float[]> randomColorChain;

    static{
        rainbowColorChain = getRainbowColorChain(500);
        rastaColorChain = getRastaColorChain(500);
        dreamMagnetColorChain = getDreamMagnetColorChain(500);
        eightiesColorChain = getEightiesrChain(500);
        randomColorChain = getRandomColorChain(500);
    }

    public static void reinitializeRandomColorChain(){
        randomColorChain = getRandomColorChain(200);
    }

    public static List<float[]> getRastaColorChain(int perColorTransitions){
        List<float[]> rastaColors = new ArrayList<float[]>();


        float[] red = new float[]{1,0,0,1};
        //22,94,0
        float[] green = new float[]{22/255f,94/255f,0,1};
        //255,221,0
        float[] yellow = new float[]{1,221/255f,0,1};


        rastaColors.add(red);
        rastaColors.add(red);
        rastaColors.add(green);
        rastaColors.add(green);
        rastaColors.add(yellow);
        rastaColors.add(yellow);

        rastaColors.add(red);
        rastaColors.add(red);
        rastaColors.add(green);
        rastaColors.add(green);
        rastaColors.add(yellow);
        rastaColors.add(yellow);


        return getColorChain(rastaColors, perColorTransitions);

    }

    public static List<float[]> getDreamMagnetColorChain(int perColorTransitions){
        List<float[]> dreamMagnet = new ArrayList<float[]>();

        //52,56,56
        //0,95,107
        //0,140,158

        //0,180,204
        //0,223,252
        float[] color1 = getColorFrom256(52,56,56);
        float[] color2 = getColorFrom256(0,95,107);
        float[] color3 = getColorFrom256(0,140,158);
        float[] color4 = getColorFrom256(0,180,204);
        float[] color5 = getColorFrom256(0,223,252);



        dreamMagnet.add(color1);

        dreamMagnet.add(color2);
        dreamMagnet.add(color2);
        dreamMagnet.add(color3);
        dreamMagnet.add(color3);
        dreamMagnet.add(color4);
        dreamMagnet.add(color4);
        dreamMagnet.add(color5);
        dreamMagnet.add(color5);

        dreamMagnet.add(color1);
        dreamMagnet.add(color1);

        dreamMagnet.add(color2);
        dreamMagnet.add(color2);
        dreamMagnet.add(color3);
        dreamMagnet.add(color3);
        dreamMagnet.add(color4);
        dreamMagnet.add(color4);
        dreamMagnet.add(color5);
        dreamMagnet.add(color5);

        dreamMagnet.add(color1);



        return getColorChain(dreamMagnet, perColorTransitions);

    }

    public static List<float[]> getRandomColorChain(int perColorTransitions){
        List<float[]> random = new ArrayList<float[]>();

        //52,56,56
        //0,95,107
        //0,140,158

        //0,180,204
        //0,223,252
        float[] color1 = getRandomColor();
        float[] color2 = getRandomColor();
        float[] color3 = getRandomColor();
        float[] color4 = getRandomColor();
        float[] color5 = getRandomColor();



        random.add(color1);

        random.add(color2);
        random.add(color2);
        random.add(color3);
        random.add(color3);
        random.add(color4);
        random.add(color4);
        random.add(color5);
        random.add(color5);

        random.add(color1);
        random.add(color1);

        random.add(color2);
        random.add(color2);
        random.add(color3);
        random.add(color3);
        random.add(color4);
        random.add(color4);
        random.add(color5);
        random.add(color5);

        random.add(color1);



        return getColorChain(random, perColorTransitions);

    }

    static float[] getRandomColor(){
        return new float[]{(float)Math.random(),(float)Math.random(),(float)Math.random(),1};
    }

    public static List<float[]> getEightiesrChain(int perColorTransitions){
        List<float[]> eighties = new ArrayList<float[]>();

        //52,56,56
        //0,95,107
        //0,140,158

        //0,180,204
        //0,223,252
        float[] color1 = getColorFrom256(73,10,61);
        float[] color2 = getColorFrom256(189,21,80);
        float[] color3 = getColorFrom256(233,127,2);
        float[] color4 = getColorFrom256(248,202,0);
        float[] color5 = getColorFrom256(138,155,15);



        eighties.add(color1);

        eighties.add(color2);
        eighties.add(color2);
        eighties.add(color3);
        eighties.add(color3);
        eighties.add(color4);
        eighties.add(color4);
        eighties.add(color5);
        eighties.add(color5);

        eighties.add(color1);
        eighties.add(color1);

        eighties.add(color2);
        eighties.add(color2);
        eighties.add(color3);
        eighties.add(color3);
        eighties.add(color4);
        eighties.add(color4);
        eighties.add(color5);
        eighties.add(color5);

        eighties.add(color1);



        return getColorChain(eighties, perColorTransitions);

    }

    static float[] getColorFrom256(float r, float g, float b){
        return new float[]{r/255f,g/255f,b/255f,1};
    }

    public static List<float[]> getRainbowColorChain(int perColorTransitions){
        List<float[]> rainbowColors = new ArrayList<float[]>();

        float[] red = new float[]{1,0,0,1};
        float[] orange = new float[]{1,0.5f,0,1};
        float[] orange2 = new float[]{1,0.5f,0,1};
        float[] yellow = new float[]{1,1,0,1};
        float[] yellow2 = new float[]{1,1,0,1};
        float[] green = new float[]{0,1,0,1};
        float[] green2 = new float[]{0,1,0,1};
        float[] blue = new float[]{0,0,1,1};
        float[] blue2 = new float[]{0,0,1,1};
        float[] indigo = new float[]{75f/255f,0,130f/255f,1};
        float[] indigo2 = new float[]{75f/255f,0,130f/255f,1};
        float[] violet = new float[]{143f/255f,0,1,1};
        float[] violet2 = new float[]{143f/255f,0,1,1};
        /**
         * add another red at the end to make final circular transition color smooth
         */
        float[] red2 = new float[]{1,0,0,1};
        rainbowColors.add(red);
        rainbowColors.add(orange);
        rainbowColors.add(orange2);
        rainbowColors.add(yellow);
        rainbowColors.add(yellow2);
        rainbowColors.add(green);
        rainbowColors.add(green2);
        rainbowColors.add(blue);
        rainbowColors.add(blue2);
        rainbowColors.add(indigo);
        rainbowColors.add(indigo2);
        rainbowColors.add(violet);
        rainbowColors.add(violet2);
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
