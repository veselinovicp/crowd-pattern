package com.monoton.horizont.crowd.pattern.painter.tail;

import com.monoton.horizont.crowd.pattern.Constants;
import com.monoton.horizont.crowd.pattern.painter.colors.*;

/**
 * Created by monoton on 20.8.2017.
 */
public class TailPainterFactory {

    public static TailPainter getTailPainter(String type){
        if(type.equals(Constants.TAIL_PAINTER_SPERM)){
            return new SpermTailPainter();
        }
        if(type.equals(Constants.TAIL_PAINTER_SNAKE)){
            return new SnakeTailPainter();
        }

        throw new RuntimeException("No tail painter by type: "+type+" exists.");
    }
}
