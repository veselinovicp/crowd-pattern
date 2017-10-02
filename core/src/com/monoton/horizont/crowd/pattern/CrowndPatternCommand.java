package com.monoton.horizont.crowd.pattern;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.monoton.horizont.crowd.pattern.screens.LoadingScreen;
import com.monoton.horizont.crowd.pattern.screens.MainScreen;
import com.monoton.horizont.crowd.pattern.ui.UIBuilder;


public class CrowndPatternCommand extends Game{

	private UIBuilder uiBuilder = new UIBuilder();
	private StretchViewport stretchViewport;



	
	@Override
	public void create () {


		System.out.println("start creating CrowndPatternCommand");

		stretchViewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		setScreen(new LoadingScreen(this));

		System.out.println("end creating CrowndPatternCommand");


	}



	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}


	@Override
	public void render () {
		super.render();


	}
	
	@Override
	public void dispose () {
		super.dispose();
		uiBuilder.dispose();
		getScreen().dispose();


	}

	public UIBuilder getUiBuilder() {
		return uiBuilder;
	}

	public StretchViewport getStretchViewport() {
		return stretchViewport;
	}
}
