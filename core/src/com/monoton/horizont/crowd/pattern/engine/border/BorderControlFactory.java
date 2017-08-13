package com.monoton.horizont.crowd.pattern.engine.border;

import com.monoton.horizont.crowd.pattern.Constants;

/**
 * Created by monoton on 13.8.2017.
 */
public class BorderControlFactory {

    public static BorderControl getBorderControl(String type){
        if(type.equals(Constants.BORDER_CONTROL_FLY_THROUGH)){
            return new FlyThroughBorderControl();
        }
        if(type.equals(Constants.BORDER_CONTROL_BOUNCE)){
            return new BounceBorderControl();
        }
        throw new RuntimeException("No border control by type: "+type+" exists.");
    }
}
