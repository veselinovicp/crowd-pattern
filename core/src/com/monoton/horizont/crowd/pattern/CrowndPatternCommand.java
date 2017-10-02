package com.monoton.horizont.crowd.pattern;

import com.badlogic.gdx.Game;
import com.monoton.horizont.crowd.pattern.screens.MainScreen;


public class CrowndPatternCommand extends Game{



	
	@Override
	public void create () {


		System.out.println("start creating");
		setScreen(new MainScreen());

		System.out.println("end creating");


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
		getScreen().dispose();


	}

}
