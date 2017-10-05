package com.monoton.horizont.crowd.pattern.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

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




    public static final String BACKGROUND = "background2.jpg";//"background2.jpg"



    public UIBuilder() {
        super();
    }



    public void startLoading(){



        super.load(DEFAULT_SKIN_ATLAS, TextureAtlas.class);

        super.load(DEFAULT_SKIN, Skin.class, new SkinLoader.SkinParameter(DEFAULT_SKIN_ATLAS));






        super.load(STAR_1, Texture.class);
        super.load(STAR_2, Texture.class);
        super.load(CIRCLE, Texture.class);
        super.load(DIAMOND, Texture.class);
        super.load(SPACESHIP_1, Texture.class);
        super.load(SPACESHIP_2, Texture.class);
        super.load(TRIANGLE, Texture.class);





        super.load(BACKGROUND, Texture.class);



/*
        while (!super.update()){

        }*/

    }

    public void dispose(){
        super.dispose();
    }




}
