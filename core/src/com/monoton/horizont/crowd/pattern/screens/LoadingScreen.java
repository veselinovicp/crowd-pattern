package com.monoton.horizont.crowd.pattern.screens;


import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.monoton.horizont.crowd.pattern.CrowndPatternCommand;
import com.monoton.horizont.crowd.pattern.ui.UIBuilder;

/**
 * Created by monoton on 2.10.2017.
 */
public class LoadingScreen implements Screen{//
    private ShapeRenderer shapeRenderer;
    private float progress;


    private CrowndPatternCommand crowndPatternCommand;
    private UIBuilder uiBuilder;
    private StretchViewport stretchViewport;

    public LoadingScreen(CrowndPatternCommand crowndPatternCommand) {

        this.crowndPatternCommand = crowndPatternCommand;
        this.shapeRenderer = new ShapeRenderer();
        uiBuilder = crowndPatternCommand.getUiBuilder();
        stretchViewport = crowndPatternCommand.getStretchViewport();
    }


    private void queueAssets() {
        uiBuilder.load("img/splash.png", Texture.class);
        uiBuilder.load("ui/uiskin.atlas", TextureAtlas.class);
    }

    @Override
    public void show() {
        System.out.println("LOADING");
        shapeRenderer.setProjectionMatrix(stretchViewport.getCamera().combined);
        this.progress = 0f;
        queueAssets();
    }

    private void update(float delta) {
        progress = MathUtils.lerp(progress, uiBuilder.getProgress(), .1f);
        if (uiBuilder.update() && progress >= uiBuilder.getProgress() - .001f) {
            crowndPatternCommand.setScreen(new MainScreen(crowndPatternCommand));
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(32, stretchViewport.getCamera().viewportHeight / 2 - 8, stretchViewport.getCamera().viewportWidth - 64, 16);

        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(32, stretchViewport.getCamera().viewportHeight / 2 - 8, progress * (stretchViewport.getCamera().viewportWidth - 64), 16);
        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
