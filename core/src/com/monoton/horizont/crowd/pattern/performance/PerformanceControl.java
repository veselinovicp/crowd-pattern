package com.monoton.horizont.crowd.pattern.performance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;
import com.monoton.horizont.crowd.pattern.Constants;

/**
 * Created by monoton on 24.9.2017.
 */
public class PerformanceControl {

    long lastTimeCounted;
    private float sinceChange;
    private float frameRate;



    public PerformanceControl() {
        lastTimeCounted = TimeUtils.millis();
        sinceChange = 0;
        frameRate = Gdx.graphics.getFramesPerSecond();

    }



    private void update() {
        long delta = TimeUtils.timeSinceMillis(lastTimeCounted);
        lastTimeCounted = TimeUtils.millis();

        sinceChange += delta;
        if(sinceChange >= 100) {
            sinceChange = 0;
            frameRate = Gdx.graphics.getFramesPerSecond();
        }
    }

    public float getFrameRate() {
        return frameRate;
    }

    public boolean isLagging(){
        update();
        if(frameRate == 0){
            return false;
        }
        return frameRate <= Constants.MIN_FRAME_RATE;
    }

}
