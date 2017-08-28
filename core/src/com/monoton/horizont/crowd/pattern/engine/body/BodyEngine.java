package com.monoton.horizont.crowd.pattern.engine.body;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.monoton.horizont.crowd.pattern.utils.DrawUtils;

/**
 * Created by monoton on 28.8.2017.
 */
public class BodyEngine {

    public Body createBody(float x, float y, World world){
        CircleShape circle = new CircleShape();
        // 0.5 metres for radius
        circle.setRadius(0.1f);


        // Fixture definition for our shapes
        FixtureDef circleFixtureDef = new FixtureDef();
        circleFixtureDef.shape = circle;
        circleFixtureDef.density = 0.5f;
        circleFixtureDef.friction = 0.4f;
        circleFixtureDef.restitution = 0.6f;


        BodyDef defaultDynamicBodyDef = new BodyDef();
        defaultDynamicBodyDef.type = BodyDef.BodyType.DynamicBody;

        Vector2 box2DCoords = DrawUtils.getBox2DCoords(x, y);

        defaultDynamicBodyDef.position.set(box2DCoords.x,box2DCoords.y);

        Body body = world.createBody(defaultDynamicBodyDef);

        body.createFixture(circleFixtureDef);

        return body;



    }
}
