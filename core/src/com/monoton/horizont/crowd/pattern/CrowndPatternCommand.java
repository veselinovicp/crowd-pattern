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


	/*	createRadiusControls(ls);

		createOrderControls(ls);

		createDistanceControls(ls);*/


		createSpeedControls(ls);
		createTailLengthControls(ls);
		createTailDensityControls(ls);
		createLightSizeControls(ls);


		/**
		 * color chooser select box
		 */
		createColorChooseControls(font, ls);

		/**
		 * end color chooser select box
		 */


		createExitButton();

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

	private void createExitButton() {
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
	}

	private void createColorChooseControls(BitmapFont font, Label.LabelStyle ls) {
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
	}



	private Label speedValue = null;
	private Slider speedSlider = null;

	private void createSpeedControls(Label.LabelStyle ls) {


		InputListener listener = new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

				speedValue.setText("" + speedSlider.getValue());
				steeringActorCreator.setSpeed(speedSlider.getValue());
				SystemState.getInstance().setSpeedFactor(speedSlider.getValue());


			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				steeringActorCreator.setSpeed(speedSlider.getValue());
				SystemState.getInstance().setSpeedFactor(speedSlider.getValue());
				return true;
			}

			;
		};

		Controls controls = createControls("Speed: ",20f,200f,1f,SystemState.getInstance().getSpeedFactor(), listener, ls);
		speedSlider = controls.getSlider();
		speedValue = controls.getValue();
	}

	private Label lightSizeValue = null;
	private Slider lightSizeSlider = null;

	private void createLightSizeControls(Label.LabelStyle ls) {


		InputListener listener = new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

				lightSizeValue.setText("" + lightSizeSlider.getValue());

				SystemState.getInstance().setLightSizeFactor(lightSizeSlider.getValue());
				light.setDistance(lightSizeSlider.getValue());


			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

				SystemState.getInstance().setLightSizeFactor(lightSizeSlider.getValue());
				light.setDistance(lightSizeSlider.getValue());
				return true;
			}

			;
		};

		Controls controls = createControls("Light size: ",Constants.LIGHT_SCENE_WIDTH *0.1f,Constants.LIGHT_SCENE_WIDTH *2.0f,0.11f,SystemState.getInstance().getLightSizeFactor(), listener, ls);
		lightSizeSlider = controls.getSlider();
		lightSizeValue = controls.getValue();
	}

	private Label tailLengthValue = null;
	private Slider tailLengthSlider = null;

	private void createTailLengthControls(Label.LabelStyle ls) {


		InputListener listener = new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

				tailLengthValue.setText("" + tailLengthSlider.getValue());

				SystemState.getInstance().setTailLengthFactor(tailLengthSlider.getValue());


			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				SystemState.getInstance().setTailLengthFactor(tailLengthSlider.getValue());
				return true;
			}

			;
		};

		Controls controls = createControls("Tail length: ",50f,500f,1f,SystemState.getInstance().getTailLengthFactor(), listener, ls);
		tailLengthSlider = controls.getSlider();
		tailLengthValue = controls.getValue();
	}

	private Label tailDensityValue = null;
	private Slider tailDensitySlider = null;

	private void createTailDensityControls(Label.LabelStyle ls) {


		InputListener listener = new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

				tailDensityValue.setText("" + tailDensitySlider.getValue());

				SystemState.getInstance().setTailDensityFactor(tailDensitySlider.getValue());


			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				SystemState.getInstance().setTailDensityFactor(tailDensitySlider.getValue());
				return true;
			}

			;
		};

		Controls controls = createControls("Tail density: ",0.1f,2f,0.1f,SystemState.getInstance().getTailDensityFactor(), listener, ls);
		tailDensitySlider = controls.getSlider();
		tailDensityValue = controls.getValue();
	}


	private class Controls{
		Slider slider;
		Label value;

		public Controls(Slider slider, Label value) {
			this.slider = slider;
			this.value = value;
		}

		public Slider getSlider() {
			return slider;
		}

		public Label getValue() {
			return value;
		}
	}

	private Controls createControls(String label,float min, float max, float stepSize, float defaultValue, InputListener inputListener, Label.LabelStyle ls){
		Label labelName = new Label(label, ls);
		controlsTable.add(labelName).padRight(10);

		Slider slider = new Slider(min,max,stepSize, false, skin);

		slider.setValue(defaultValue);//
		slider.addListener(inputListener);

		controlsTable.add(slider).padRight(10);

		Label value = new Label(""+slider.getValue(), ls);
		controlsTable.add(value);
		controlsTable.row();
		return new Controls(slider, value);

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
