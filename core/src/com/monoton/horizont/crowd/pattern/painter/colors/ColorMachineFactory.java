package com.monoton.horizont.crowd.pattern.painter.colors;

import com.monoton.horizont.crowd.pattern.Constants;
import com.monoton.horizont.crowd.pattern.engine.border.BounceBorderControl;
import com.monoton.horizont.crowd.pattern.engine.border.FlyThroughBorderControl;

/**
 * Created by monoton on 20.8.2017.
 */
public class ColorMachineFactory {

    public static ColorMachine getColorMachine(String type){
        if(type.equals(Constants.COLOR_MACHINE_RAINBOW)){
            return new RainbowColorMachine();
        }
        if(type.equals(Constants.COLOR_MACHINE_RASTA)){
            return new RastaColorMachine();
        }
        if(type.equals(Constants.COLOR_MACHINE_DREAM_MAGNET)){
            return new DreamMagnetColorMachine();
        }
        if(type.equals(Constants.COLOR_MACHINE_EIGHTIES)){
            return new EightiesColorMachine();
        }
        throw new RuntimeException("No color machine by type: "+type+" exists.");

    }
}
