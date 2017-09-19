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
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.monoton.horizont.crowd.pattern.engine.SteeringActor;
import com.monoton.horizont.crowd.pattern.engine.SteeringActorCreator;
import com.monoton.horizont.crowd.pattern.engine.border.BorderControl;
import com.monoton.horizont.crowd.pattern.engine.border.BorderControlFactory;
import com.monoton.horizont.crowd.pattern.painter.colors.ColorMachineFactory;
import com.monoton.horizont.crowd.pattern.scene.LightScene;
import com.monoton.horizont.crowd.pattern.scene.SteeringActorsScene;
import com.monoton.horizont.crowd.pattern.ui.NamedTexture;
import com.monoton.horizont.crowd.pattern.ui.UIBuilder;
import com.monoton.horizont.crowd.pattern.utils.DrawUtils;



public class CrowndPatternCommand extends ApplicationAdapter{

	private Skin skin;


	private Texture background;

	private Texture tfBackground;
	private Texture scroll_horizontal;
	private Texture knob_scroll;

	Array<SteeringActor> characters = new Array<SteeringActor>();


	private Stage actionStage;
	private Stage controlsStage;





	private BorderControl borderControl = BorderControlFactory.getBorderControl(Constants.BORDER_CONTROL_BOUNCE);

	private SteeringActorCreator steeringActorCreator;

	private Table controlsTable;


	private TextButton exitButton;
	private TextButton resetButton;


	private World world;

	private RayHandler rayHandler;






	private Viewport viewport;

	private Array<NamedTexture> shapes = null;

	private SteeringActorsScene steeringActorsScene;

	private UIBuilder uiBuilder = new UIBuilder();

	private LightScene lightScene;


	
	@Override
	public void create () {

		System.out.println("start creating");
		uiBuilder.load();

		createShapes();

		viewport = new FitViewport(Constants.LIGHT_SCENE_WIDTH, Constants.LIGHT_SCENE_HEIGHT);
		// Center camera
		viewport.getCamera().position.set(viewport.getCamera().position.x + Constants.LIGHT_SCENE_WIDTH *0.5f,
				viewport.getCamera().position.y + Constants.LIGHT_SCENE_HEIGHT *0.5f
				, 0);
		viewport.getCamera().update();




		// Create Physics World

		world = new World(new Vector2(0,0), true);





		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(0.25f, 0.2f, 0.2f, 0.25f);



		skin = uiBuilder.get(UIBuilder.DEFAULT_SKIN, Skin.class);



		// Create a actionStage
		StretchViewport viewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		actionStage = new Stage(viewport);
		controlsStage = new Stage(viewport);//new ScreenViewport()




		controlsTable = new Table();
		controlsTable.setWidth(Gdx.graphics.getWidth());
		controlsTable.setHeight(Gdx.graphics.getHeight());
		controlsTable.align(Align.top|Align.right);



		controlsTable.padTop(20);




		BitmapFont font = uiBuilder.get(UIBuilder.DEFAULT_FONT, BitmapFont.class);
		Label.LabelStyle ls = new Label.LabelStyle(font, Color.WHITE);



		createSpeedControls(ls);
		createTailLengthControls(ls);
		createLightControls(ls);

		createAmbientControls(ls);



		createColorChooseControls(font, ls);

		createShapeChooseControls(font, ls);

		createResetButton();
		createExitButton();

		controlsStage.addActor(controlsTable);






		characters = new Array<SteeringActor>();

		background = uiBuilder.get(UIBuilder.BACKGROUND, Texture.class);


		lightScene = new LightScene(characters, rayHandler);
		actionStage.addActor(lightScene);


		steeringActorsScene = new SteeringActorsScene(characters, rayHandler, background, shapes.get(0).getTextureRegion());

		actionStage.addActor(steeringActorsScene);

		steeringActorCreator = new SteeringActorCreator(characters, steeringActorsScene, borderControl, SystemState.getInstance().getRadiusFactor());

		steeringActorCreator.createSteeringActors(SystemState.getInstance().getParticleStartNumber(),rayHandler, shapes.get(0).getTextureRegion());

		actionStage.setScrollFocus(steeringActorsScene);





		// ORDER IS IMPORTANT!
		InputMultiplexer inputMultiplexer = new InputMultiplexer(controlsStage, actionStage);
		Gdx.input.setInputProcessor(inputMultiplexer);

		System.out.println("end creating");


	}

	private void createResetButton() {
		resetButton = new TextButton("Reset", skin);

		resetButton.addListener(new ClickListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				SystemState.getInstance().reset();
				resetControls();
				return true;
			}
		});

		controlsTable.add(resetButton).colspan(2).right().padBottom(20);

	}

	public void resetControls(){
		resetSliderValues(speedSlider, SystemState.getInstance().getSpeedFactor());
		resetSliderValues(tailLengthSlider, SystemState.getInstance().getTailSize());
		resetSliderValues(lightSlider, SystemState.getInstance().getLightSize());
		resetSliderValues(ambientSlider, SystemState.getInstance().getAmbientFactor());
		resetDropDownValues(shapeSelectBox, shapes.get(0));
		resetDropDownValues(colorSelectBox, colors.get(0));


	}

	public void resetDropDownValues(SelectBox selectBox, Object value){
		selectBox.setSelected(value);

		Array<EventListener> listeners = selectBox.getListeners();
		for (EventListener l : listeners) {
			if (l instanceof ChangeListener) {
				((ChangeListener) l).handle(null);
			}
		}

	}

	public void resetSliderValues(Slider slider, float value){
		slider.setValue(value);


		Array<EventListener> listeners = slider.getListeners();
		for (EventListener l : listeners) {
			if (l instanceof InputListener) {
				((InputListener) l).touchUp(null,0,0,0,0);
			}
		}



	}

	private void createExitButton() {
		exitButton = new TextButton("Exit", skin);

		exitButton.addListener(new ClickListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

				Gdx.app.exit();
				return true;
			}
		});

		controlsTable.add(exitButton).colspan(1).right().padBottom(20);
		controlsTable.row();
	}

	private SelectBox colorSelectBox = null;

	private Array<String> colors = new Array<String>();

	private void createColorChooseControls(BitmapFont font, Label.LabelStyle ls) {



		colors.add(Constants.COLOR_MACHINE_RAINBOW);
		colors.add(Constants.COLOR_MACHINE_RASTA);
		colors.add(Constants.COLOR_MACHINE_DREAM_MAGNET);
		colors.add(Constants.COLOR_MACHINE_EIGHTIES);
		colors.add(Constants.COLOR_MACHINE_RANDOM);



		ChangeListener changeListener = new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				String selected = (String) colorSelectBox.getSelected();

				SystemState.getInstance().setColorMachine(ColorMachineFactory.getColorMachine(selected));


			}
		};

		colorSelectBox = createDropDownControl("Color: ", colors, changeListener, font, ls);


		SystemState.getInstance().setColorMachine(ColorMachineFactory.getColorMachine(colors.first()));

		TextButton randomColor = new TextButton("Random", skin);


		randomColor.addListener(new ClickListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				DrawUtils.reinitializeRandomColorChain();
				SystemState.getInstance().setColorMachine(ColorMachineFactory.getColorMachine(Constants.COLOR_MACHINE_RANDOM));
				colorSelectBox.setSelected(Constants.COLOR_MACHINE_RANDOM);
				return true;
			}
		});

		controlsTable.add(randomColor).center();
		controlsTable.row();
	}

	private void createShapes(){
		shapes = new Array<NamedTexture>();


		shapes.add(new NamedTexture(new TextureRegion(uiBuilder.get(UIBuilder.STAR_1, Texture.class)), "Star 1"));
		shapes.add(new NamedTexture(new TextureRegion(uiBuilder.get(UIBuilder.STAR_2, Texture.class)), "Star 2"));
		shapes.add(new NamedTexture(new TextureRegion(uiBuilder.get(UIBuilder.CIRCLE, Texture.class)), "Circle"));
		shapes.add(new NamedTexture(new TextureRegion(uiBuilder.get(UIBuilder.DIAMOND, Texture.class)),"Diamond"));
		shapes.add(new NamedTexture(new TextureRegion(uiBuilder.get(UIBuilder.SPACESHIP_1, Texture.class)), "Spaceship 1"));
		shapes.add(new NamedTexture(new TextureRegion(uiBuilder.get(UIBuilder.SPACESHIP_2, Texture.class)), "Spaceship2"));
		shapes.add(new NamedTexture(new TextureRegion(uiBuilder.get(UIBuilder.TRIANGLE, Texture.class)), "Triangle"));

	}

	SelectBox shapeSelectBox = null;


	private void createShapeChooseControls(BitmapFont font, Label.LabelStyle ls) {

		ChangeListener changeListener = new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				NamedTexture selected = (NamedTexture) shapeSelectBox.getSelected();
				steeringActorCreator.setShape(selected.getTextureRegion());
				steeringActorsScene.setShape(selected.getTextureRegion());

			}
		};

		shapeSelectBox = createDropDownControl("Shape: ", shapes, changeListener, font, ls);

		controlsTable.row();
	}

	private SelectBox createDropDownControl(String labelName,Array items,ChangeListener changeListener, BitmapFont font, Label.LabelStyle ls) {
		Label label = new Label(labelName, ls);
		controlsTable.add(label).padRight(10);


		if(tfBackground==null) {
			tfBackground = uiBuilder.get(UIBuilder.TF_BACKGROUND, Texture.class);
		}
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
		if(scroll_horizontal==null) {
			scroll_horizontal = uiBuilder.get(UIBuilder.SCROLL_HORIZONTAL, Texture.class);
		}
		if(knob_scroll==null) {
			knob_scroll =uiBuilder.get(UIBuilder.KNOB_SCROLL, Texture.class);
		}
		sps.background = new TextureRegionDrawable(new TextureRegion(tfBackground));
		sps.vScroll = new TextureRegionDrawable(new TextureRegion(scroll_horizontal));
		sps.vScrollKnob = new TextureRegionDrawable(new TextureRegion(knob_scroll));
		sbs.background = new TextureRegionDrawable(new TextureRegion(tfBackground));
		sbs.scrollStyle = sps;
		sbs.font = font;
		sbs.fontColor.set(Color.RED);
		final SelectBox selectBox = new SelectBox<String>(sbs);




		selectBox.setItems(items);
		selectBox.pack(); // To get the actual size
//		colorSelectBox.setPosition(list.getX() + list.getWidth() + 10, secondRowY-colorSelectBox.getHeight());


		selectBox.addListener(changeListener);

		controlsTable.add(selectBox).padRight(10);

		return selectBox;
	}


	private Label tailLengthValue = null;
	private Slider tailLengthSlider = null;

	private void createTailLengthControls(Label.LabelStyle ls) {


		InputListener listener = new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

//				tailLengthValue.setText("" + tailLengthSlider.getValue());//String.format("%.2f", floatValue);
				tailLengthValue.setText(DrawUtils.formatFloat(tailLengthSlider.getValue()));//String.format("%.2f", floatValue);
				SystemState.getInstance().setTailSize((int)tailLengthSlider.getValue());


			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				SystemState.getInstance().setTailSize((int)tailLengthSlider.getValue());
				return true;
			}

			;
		};

		Controls controls = createControls("Tail length: ",2,8,1f,SystemState.getInstance().getTailSize(), listener, ls);
		tailLengthSlider = controls.getSlider();
		tailLengthValue = controls.getValue();
	}



	private Label speedValue = null;
	private Slider speedSlider = null;

	private void createSpeedControls(Label.LabelStyle ls) {


		InputListener listener = new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

				speedValue.setText(DrawUtils.formatFloat(speedSlider.getValue()));
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


	private Label lightValue = null;
	private Slider lightSlider = null;

	private void createLightControls(Label.LabelStyle ls) {


		InputListener listener = new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

				lightValue.setText(DrawUtils.formatFloat(lightSlider.getValue()));

				SystemState.getInstance().setLightSize(lightSlider.getValue());


			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

				SystemState.getInstance().setLightSize(lightSlider.getValue());

				return true;
			}

			;
		};

		Controls controls = createControls("Light size: ",0.2f,2.0f,0.1f,SystemState.getInstance().getLightSize(), listener, ls);
		lightSlider = controls.getSlider();
		lightValue = controls.getValue();
	}




	private Label ambientValue = null;
	private Slider ambientSlider = null;

	private void createAmbientControls(Label.LabelStyle ls) {


		InputListener listener = new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

				ambientValue.setText(DrawUtils.formatFloat(ambientSlider.getValue()));

				SystemState.getInstance().setAmbientFactor(ambientSlider.getValue());
				lightScene.setDistance(ambientSlider.getValue());



			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

				SystemState.getInstance().setAmbientFactor(ambientSlider.getValue());
				lightScene.setDistance(ambientSlider.getValue());
				return true;
			}

			;
		};

		Controls controls = createControls("Ambient: ",Constants.LIGHT_SCENE_WIDTH *0.1f,Constants.LIGHT_SCENE_WIDTH *2.0f,0.11f,SystemState.getInstance().getAmbientFactor(), listener, ls);
		ambientSlider = controls.getSlider();
		ambientValue = controls.getValue();
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


	@Override
	public void resize(int width, int height) {
		System.out.println("start resizing");
		viewport.update(width, height);
		System.out.println("end resizing");
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




		rayHandler.setCombinedMatrix(viewport.getCamera().combined);
		rayHandler.updateAndRender();

	}
	
	@Override
	public void dispose () {
		super.dispose();
		System.out.println("start disposing");


		steeringActorCreator.dispose();
		uiBuilder.dispose();

		actionStage.dispose();
		controlsStage.dispose();



		rayHandler.dispose();

		world.dispose();





	}

}
