package com.monoton.horizont.crowd.pattern.steering.closeness;

import com.badlogic.gdx.math.Vector;
import com.monoton.horizont.crowd.pattern.Constants;

/**
 * Created by monoton on 15.8.2017.
 */
public class ClosenessResponseFactory<T extends Vector<T>>  {

    public  ClosenessResponse<T> getClosenessResponse(String type){
        if(type.equals(Constants.CLOSENESS_RESPONSE_SIMILAR_VELOCITY)){
            return new SimilarVelocityClosenessResponse<T>();
        }

        throw new RuntimeException("No closeness response by type: "+type+" exists.");

    }
}
