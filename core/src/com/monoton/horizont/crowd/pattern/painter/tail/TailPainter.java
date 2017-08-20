package com.monoton.horizont.crowd.pattern.painter.tail;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.monoton.horizont.crowd.pattern.painter.DrawPoint;

import java.util.List;

/**
 * Created by monoton on 20.8.2017.
 */
public interface TailPainter {
    void drawTail(List<DrawPoint> drawPoints, TextureRegion region, Batch batch, float parentAlpha);
}
