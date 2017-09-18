package com.monoton.horizont.crowd.pattern.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by monoton on 18.9.2017.
 */
public class  UIBuilder extends AssetManager {

    public static final String STAR_1 = "star.png";
    public static final String STAR_2 = "star2.png";
    public static final String CIRCLE = "circle.png";
    public static final String DIAMOND = "diamond.png";
    public static final String SPACESHIP_1 = "ship1.png";
    public static final String SPACESHIP_2 = "spaceship.png";
    public static final String TRIANGLE = "triangle.png";
    public static final String DEFAULT_SKIN_ATLAS = "uiskin.atlas";
    public static final String DEFAULT_SKIN = "uiskin.json";
    public static final String DEFAULT_FONT = "default.fnt";//"tfbackground.png"
    public static final String TF_BACKGROUND = "tfbackground.png";//"scroll_horizontal.png"
    public static final String SCROLL_HORIZONTAL = "scroll_horizontal.png";//"knob_scroll.png"
    public static final String KNOB_SCROLL = "knob_scroll.png";//"background2.jpg"
    public static final String BACKGROUND = "background2.jpg";//"background2.jpg"



    public UIBuilder() {
        super();
    }



    public void load(){



        super.load(STAR_1, Texture.class);
        super.load(STAR_2, Texture.class);
        super.load(CIRCLE, Texture.class);
        super.load(DIAMOND, Texture.class);
        super.load(SPACESHIP_1, Texture.class);
        super.load(SPACESHIP_2, Texture.class);
        super.load(TRIANGLE, Texture.class);

        super.load(DEFAULT_SKIN_ATLAS, TextureAtlas.class);

        super.load(DEFAULT_SKIN, Skin.class, new SkinLoader.SkinParameter(DEFAULT_SKIN_ATLAS));

        super.load(DEFAULT_FONT, BitmapFont.class);

        super.load(TF_BACKGROUND, Texture.class);

        super.load(SCROLL_HORIZONTAL, Texture.class);
        super.load(KNOB_SCROLL, Texture.class);
        super.load(BACKGROUND, Texture.class);




        while (!super.update()){

        }

    }

    public void dispose(){
        super.dispose();
    }




}
