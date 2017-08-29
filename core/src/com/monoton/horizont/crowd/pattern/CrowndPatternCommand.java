package com.monoton.horizont.crowd.pattern;

import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.*;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.monoton.horizont.crowd.pattern.engine.SteeringActor;
import com.monoton.horizont.crowd.pattern.engine.SteeringActorCreator;
import com.monoton.horizont.crowd.pattern.engine.border.BorderControl;
import com.monoton.horizont.crowd.pattern.engine.border.BorderControlFactory;
import com.monoton.horizont.crowd.pattern.painter.colors.ColorMachineFactory;
import com.monoton.horizont.crowd.pattern.scene.LightScene;
import com.monoton.horizont.crowd.pattern.scene.SteeringActorsScene;
import com.monoton.horizont.crowd.pattern.utils.DrawUtils;

public class CrowndPatternCommand extends ApplicationAdapter{

	private Skin skin;

	Texture img;

	private Texture tfBackground;
	private Texture scroll_horizontal;
	private Texture knob_scroll;

	Array<SteeringActor> characters = new Array<SteeringActor>();


	private Stage actionStage;
	private Stage controlsStage;


	private final static int PARTICLE_START_NUMBER =50;


	private BorderControl borderControl = BorderControlFactory.getBorderControl(Constants.BORDER_CONTROL_BOUNCE);

	private SteeringActorCreator steeringActorCreator;

	private Table controlsTable;


	private TextButton exitButton;



	private Slider radiusSlider;
	private Label radiusLabelName;
	private Label radiusValue;

	private Slider orderSlider;
	private Label orderLabelName;
	private Label orderValue;

	private Slider distanceSlider;
	private Label distanceLabelName;
	private Label distanceValue;


	private World world;
	private Box2DDebugRenderer debugRenderer;
	private RayHandler rayHandler;
	private Light light;



	ShapeRenderer sr;

	private Viewport viewport;


	
	@Override
	public void create () {

		viewport = new FitViewport(Constants.LIGHT_SCENE_WIDTH, Constants.LIGHT_SCENE_HEIGHT);
		// Center camera
		viewport.getCamera().position.set(viewport.getCamera().position.x + Constants.LIGHT_SCENE_WIDTH *0.5f,
				viewport.getCamera().position.y + Constants.LIGHT_SCENE_HEIGHT *0.5f
				, 0);
		viewport.getCamera().update();


		// Create Physics World
//		world = new World(new Vector2(0,-9.8f), true);
		world = new World(new Vector2(0,0), true);


		// Instantiate the class in charge of drawing physics shapes
		debugRenderer = new Box2DDebugRenderer();
		// To add some color to the ground
		sr = new ShapeRenderer();

		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(0.2f, 0.2f, 0.2f, 0.25f);
		light = new PointLight(rayHandler, 32);
		light.setPosition(Constants.LIGHT_SCENE_WIDTH *0.5f, Constants.LIGHT_SCENE_HEIGHT *0.5f);

		light.setColor(Color.YELLOW);
		light.setDistance(Constants.LIGHT_SCENE_WIDTH *0.7f);

//		createBodies();
//		Light conelight = new ConeLight(rayHandler, 32, Color.WHITE, 15,Constants.LIGHT_SCENE_WIDTH*0.5f, Constants.LIGHT_SCENE_HEIGHT-1, 270, 45);



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

		radiusSlider = new Slider(1f,30f,0.1f, false, skin);

		radiusSlider.setValue(SystemState.getInstance().getRadiusFactor());
		radiusSlider.addListener(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//				Gdx.app.log("TAG", "slider changed to: " + radiusSlider.getValue());
				steeringActorCreator.setRadius(radiusSlider.getValue());
				radiusValue.setText(""+radiusSlider.getValue());
				SystemState.getInstance().setRadiusFactor(radiusSlider.getValue());

			}
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				steeringActorCreator.setRadius(radiusSlider.getValue());
				SystemState.getInstance().setRadiusFactor(radiusSlider.getValue());
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

		orderSlider.setValue(SystemState.getInstance().getOrderFactor());
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


		/**
		 * distance slider and labels
		 */
		distanceLabelName = new Label("Distance: ", ls);
		controlsTable.add(distanceLabelName).padRight(10);

		distanceSlider = new Slider(0.01f,1f,0.05f, false, skin);

		distanceSlider.setValue(SystemState.getInstance().getDistanceFactor());
		distanceSlider.addListener(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				SystemState.getInstance().setDistanceFactor(distanceSlider.getValue());
				distanceValue.setText(""+distanceSlider.getValue());
			}
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				SystemState.getInstance().setDistanceFactor(distanceSlider.getValue());
				return true;
			};
		});

		controlsTable.add(distanceSlider).padRight(10);

		distanceValue = new Label(""+distanceSlider.getValue(), ls);
		controlsTable.add(distanceValue);
		controlsTable.row();
		/**
		 * end distance slider and labels
		 */


		/**
		 * color chooser select box
		 */
		Label colorLabel = new Label("Color: ", ls);
		controlsTable.add(colorLabel).padRight(10);


		tfBackground = new Texture(Gdx.files.internal("tfbackground.png"));
		//List
		List.ListStyle listS = new List.ListStyle();
		listS.font = font;
		listS.fontColorSelected = Color.BLACK;
		listS.fontColorUnselected = Color.GRAY;
		listS.selection = new TextureRegionDrawable(new TextureRegion(tfBackground));


		//SelectBox
		SelectBox.SelectBoxStyle sbs = new SelectBox.SelectBoxStyle();
		sbs.listStyle = listS;
		ScrollPane.ScrollPaneStyle sps = new ScrollPane.ScrollPaneStyle();

		scroll_horizontal = new Texture(Gdx.files.internal("scroll_horizontal.png"));
		knob_scroll = new Texture(Gdx.files.internal("knob_scroll.png"));
		sps.background = new TextureRegionDrawable(new TextureRegion(tfBackground));
		sps.vScroll = new TextureRegionDrawable(new TextureRegion(scroll_horizontal));
		sps.vScrollKnob = new TextureRegionDrawable(new TextureRegion(knob_scroll));
		sbs.background = new TextureRegionDrawable(new TextureRegion(tfBackground));
		sbs.scrollStyle = sps;
		sbs.font = font;
		sbs.fontColor.set(Color.RED);
		final SelectBox selectBox = new SelectBox<String>(sbs);

		Array<String> items = new Array<String>();

		items.add(Constants.COLOR_MACHINE_RAINBOW);
		items.add(Constants.COLOR_MACHINE_RASTA);
		items.add(Constants.COLOR_MACHINE_DREAM_MAGNET);
		items.add(Constants.COLOR_MACHINE_EIGHTIES);
		items.add(Constants.COLOR_MACHINE_RANDOM);



		selectBox.setItems(items);
		selectBox.pack(); // To get the actual size
//		selectBox.setPosition(list.getX() + list.getWidth() + 10, secondRowY-selectBox.getHeight());

		selectBox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				String selected = (String) selectBox.getSelected();

				SystemState.getInstance().setColorMachine(ColorMachineFactory.getColorMachine(selected));


			}
		});

		controlsTable.add(selectBox).padRight(10);
		SystemState.getInstance().setColorMachine(ColorMachineFactory.getColorMachine(items.first()));

		TextButton randomColor = new TextButton("Random", skin);


		randomColor.addListener(new ClickListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				DrawUtils.reinitializeRandomColorChain();
				SystemState.getInstance().setColorMachine(ColorMachineFactory.getColorMachine(Constants.COLOR_MACHINE_RANDOM));
				selectBox.setSelected(Constants.COLOR_MACHINE_RANDOM);
				return true;
			}
		});

		controlsTable.add(randomColor).center();
		controlsTable.row();

		/**
		 * end color chooser select box
		 */









		exitButton = new TextButton("exit", skin);

		exitButton.addListener(new ClickListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				System.exit(0);
				return true;
			}
		});

		controlsTable.add(exitButton).colspan(3).right().padBottom(20);
		controlsTable.row();

		controlsStage.addActor(controlsTable);




		img = new Texture("circle.png");

		characters = new Array<SteeringActor>();

		SteeringActorsScene steeringActorsScene = new SteeringActorsScene(characters, rayHandler);

		actionStage.addActor(steeringActorsScene);

		steeringActorCreator = new SteeringActorCreator(characters, steeringActorsScene, img, borderControl, SystemState.getInstance().getRadiusFactor());

		steeringActorCreator.createSteeringActors(PARTICLE_START_NUMBER,rayHandler);

		actionStage.setScrollFocus(steeringActorsScene);


		LightScene lightScene = new LightScene(characters, light);
		actionStage.addActor(lightScene);

		// ORDER IS IMPORTANT!
		InputMultiplexer inputMultiplexer = new InputMultiplexer(controlsStage, actionStage);
		Gdx.input.setInputProcessor(inputMultiplexer);


		/*// Tweak debug information
		debugRenderer = new Box2DDebugRenderer(
				true, *//* draw bodies *//*
				false, *//* don't draw joints *//*
				true, *//* draw aabbs *//*
				true, *//* draw inactive bodies *//*
				false, *//* don't draw velocities *//*
				true *//* draw contacts *//*);
*/

	}

	private void createBodies() {

		// Create a static body definition
		BodyDef staticBodyDef = new BodyDef();
		staticBodyDef.type = BodyDef.BodyType.StaticBody;

		//GROUND
		Body groundBody = world.createBody(staticBodyDef);
		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(Constants.LIGHT_SCENE_WIDTH * 0.5f, 0.5f);
		groundBody.createFixture(groundBox, 0.0f);
		groundBox.dispose();

		groundBody.setTransform(new Vector2(Constants.LIGHT_SCENE_WIDTH *0.5f, 0.5f), groundBody.getAngle());

		// BOX
		Body boxBody = world.createBody(staticBodyDef);
		PolygonShape box = new PolygonShape();
		box.setAsBox(.5f, .5f);
		boxBody.createFixture(box, 0.0f);
		box.dispose();

		boxBody.setTransform(new Vector2(Constants.LIGHT_SCENE_WIDTH *0.5f, Constants.LIGHT_SCENE_HEIGHT *0.5f), groundBody.getAngle());
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}


	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);




		GdxAI.getTimepiece().update(Gdx.graphics.getDeltaTime());



		actionStage.act();
		actionStage.draw();

		controlsStage.act();
		controlsStage.draw();




		world.step(1/60f, 6, 2);
		debugRenderer.render(world, viewport.getCamera().combined);
/*


		sr.setProjectionMatrix(viewport.getCamera().combined);
		sr.begin(ShapeRenderer.ShapeType.Filled);
		sr.setColor(Color.RED);
		sr.rect(0, 0, LIGHT_SCENE_WIDTH, 1f);
		sr.end();
*/


		rayHandler.setCombinedMatrix(viewport.getCamera().combined);
		rayHandler.updateAndRender();

	}
	
	@Override
	public void dispose () {

		img.dispose();
		tfBackground.dispose();
		scroll_horizontal.dispose();
		knob_scroll.dispose();
		skin.dispose();
		debugRenderer.dispose();


		rayHandler.dispose();
		world.dispose();

		steeringActorCreator.dispose();
	}

}
