package com.monoton.horizont.crowd.pattern.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by monoton on 17.9.2017.
 */
public class NamedTexture {

    private TextureRegion textureRegion;
    private String name;

    public NamedTexture(TextureRegion textureRegion, String name) {
        this.textureRegion = textureRegion;
        this.name = name;
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
