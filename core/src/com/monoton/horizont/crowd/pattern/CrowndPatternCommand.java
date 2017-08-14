package com.monoton.horizont.crowd.pattern;

import com.badlogic.gdx.*;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.monoton.horizont.crowd.pattern.engine.SteeringActor;
import com.monoton.horizont.crowd.pattern.engine.SteeringActorCreator;
import com.monoton.horizont.crowd.pattern.engine.border.BorderControl;
import com.monoton.horizont.crowd.pattern.engine.border.BorderControlFactory;
import com.monoton.horizont.crowd.pattern.scene.SteeringActorsScene;

public class CrowndPatternCommand extends ApplicationAdapter{

	private Skin skin;

	Texture img;

	Array<SteeringActor> characters = new Array<SteeringActor>();


	private Stage actionStage;
	private Stage controlsStage;


	private final static int PARTICLE_START_NUMBER =300;
	private final static int PROXIMITY_FACTOR=10;
	private final static float START_ORDER_VALUE=1.0f;

	private BorderControl borderControl = BorderControlFactory.getBorderControl(Constants.BORDER_CONTROL_FLY_THROUGH);

	private SteeringActorCreator steeringActorCreator;

	private Table controlsTable;


	private TextButton exitButton;



	private Slider radiusSlider;

	private Label radiusLabelName;
	private Label radiusValue;

	private Slider orderSlider;

	private Label orderLabelName;
	private Label orderValue;



	
	@Override
	public void create () {

		SystemState.getInstance().setOrderFactor(START_ORDER_VALUE);

		skin = new Skin(Gdx.files.internal("uiskin.json"));



		// Create a actionStage
		StretchViewport viewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		actionStage = new Stage(viewport);
		controlsStage = new Stage(new ScreenViewport());



		controlsTable = new Table();
		controlsTable.setWidth(Gdx.graphics.getWidth());
		controlsTable.setHeight(Gdx.graphics.getHeight());
		controlsTable.align(Align.top|Align.right);
		//controlsTable.setPosition(0, Gdx.graphics.getHeight());


		controlsTable.padTop(20);



		BitmapFont font = new BitmapFont(Gdx.files.internal("default.fnt"));
		Label.LabelStyle ls = new Label.LabelStyle(font, Color.WHITE);

		/**
		 * radius slider and labels
		 */
		radiusLabelName = new Label("Radius: ", ls);
		controlsTable.add(radiusLabelName).padRight(10);

		radiusSlider = new Slider(1f,15f,0.1f, false, skin);

		radiusSlider.setValue(PROXIMITY_FACTOR);
		radiusSlider.addListener(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//				Gdx.app.log("TAG", "slider changed to: " + radiusSlider.getValue());
				steeringActorCreator.setRadius(radiusSlider.getValue());
				radiusValue.setText(""+radiusSlider.getValue());

			}
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				steeringActorCreator.setRadius(radiusSlider.getValue());

				return true;
			};
		});

		controlsTable.add(radiusSlider).padRight(10);

		radiusValue = new Label(""+radiusSlider.getValue(), ls);
		controlsTable.add(radiusValue);
		controlsTable.row();
		/**
		 * end radius slider and labels
		 */

		/**
		 * order slider and labels
		 */
		orderLabelName = new Label("Order: ", ls);
		controlsTable.add(orderLabelName).padRight(10);

		orderSlider = new Slider(0.1f,5f,0.05f, false, skin);

		orderSlider.setValue(START_ORDER_VALUE);
		orderSlider.addListener(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				SystemState.getInstance().setOrderFactor(orderSlider.getValue());
				orderValue.setText(""+orderSlider.getValue());
			}
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				SystemState.getInstance().setOrderFactor(orderSlider.getValue());
				return true;
			};
		});

		controlsTable.add(orderSlider).padRight(10);

		orderValue = new Label(""+orderSlider.getValue(), ls);
		controlsTable.add(orderValue);
		controlsTable.row();
		/**
		 * end order slider and labels
		 */


		exitButton = new TextButton("exit", skin);

		exitButton.addListener(new ClickListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				System.exit(0);
				return true;
			}
		});

		controlsTable.add(exitButton).padBottom(20);
		controlsTable.row();

		controlsStage.addActor(controlsTable);




		img = new Texture("circle.png");

		characters = new Array<SteeringActor>();

		SteeringActorsScene steeringActorsScene = new SteeringActorsScene(characters);

		actionStage.addActor(steeringActorsScene);

		steeringActorCreator = new SteeringActorCreator(characters, steeringActorsScene, img, borderControl, PROXIMITY_FACTOR);

		steeringActorCreator.createSteeringActors(PARTICLE_START_NUMBER);

		actionStage.setScrollFocus(steeringActorsScene);

		// ORDER IS IMPORTANT!
		InputMultiplexer inputMultiplexer = new InputMultiplexer(controlsStage, actionStage);
		Gdx.input.setInputProcessor(inputMultiplexer);


	}


	@Override
	public void render () {
//		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);




		GdxAI.getTimepiece().update(Gdx.graphics.getDeltaTime());



		actionStage.act();
		actionStage.draw();

		controlsStage.act();
		controlsStage.draw();

	}
	
	@Override
	public void dispose () {

		img.dispose();
		skin.dispose();
	}

}
