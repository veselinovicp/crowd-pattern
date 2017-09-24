package com.monoton.horizont.crowd.pattern.steering.closeness;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.math.Vector;
import com.monoton.horizont.crowd.pattern.Constants;

/**
 * Created by monoton on 15.8.2017.
 */
public class ClosenessResponseFactory<T extends Vector<T>>  {

    public  ClosenessResponse<T> getClosenessResponse(String type, Steerable<T> owner){
        if(type.equals(Constants.CLOSENESS_RESPONSE_SIMILAR_VELOCITY)){
            return new SimilarVelocityClosenessResponse<T>(owner);
        }
        if(type.equals(Constants.CLOSENESS_RESPONSE_NONE)){
            return new EmptyClosenessResponse<T>();
        }

        throw new RuntimeException("No closeness response by type: "+type+" exists.");

    }
}
