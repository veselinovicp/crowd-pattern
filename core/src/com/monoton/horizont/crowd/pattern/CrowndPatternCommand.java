package com.monoton.horizont.crowd.pattern;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.monoton.horizont.crowd.pattern.engine.SteeringActor;
import com.monoton.horizont.crowd.pattern.engine.SteeringActorCreator;
import com.monoton.horizont.crowd.pattern.engine.border.BorderControl;
import com.monoton.horizont.crowd.pattern.engine.border.BorderControlFactory;
import com.monoton.horizont.crowd.pattern.scene.SteeringActorsScene;

public class CrowndPatternCommand extends ApplicationAdapter implements InputProcessor {

	Texture img;

	Array<SteeringActor> characters = new Array<SteeringActor>();


	Stage stage;


	private final static int PARTICLE_START_NUMBER =100;
	private final static int PROXIMITY_FACTOR=10;

	private BorderControl borderControl = BorderControlFactory.getBorderControl(Constants.BORDER_CONTROL_BOUNCE);

	private SteeringActorCreator steeringActorCreator;



	
	@Override
	public void create () {

		Gdx.input.setInputProcessor(this);


		// Create a stage
		stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));



		img = new Texture("circle.png");

		characters = new Array<SteeringActor>();

		SteeringActorsScene steeringActorsScene = new SteeringActorsScene(characters);

		stage.addActor(steeringActorsScene);

		steeringActorCreator = new SteeringActorCreator(characters, steeringActorsScene, img, borderControl, PROXIMITY_FACTOR);

		steeringActorCreator.createSteeringActors(PARTICLE_START_NUMBER);


	}


	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		/*batch.begin();
		batch.draw(img, 0, 0);*/



		GdxAI.getTimepiece().update(Gdx.graphics.getDeltaTime());



		stage.act();
		stage.draw();

//		batch.end();
	}
	
	@Override
	public void dispose () {

		img.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(button == Input.Buttons.LEFT){
			float posX = screenX - img.getWidth()/2;
			float posY = Gdx.graphics.getHeight() - screenY - img.getHeight()/2;
			steeringActorCreator.createSteeringActor(posX, posY);
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
