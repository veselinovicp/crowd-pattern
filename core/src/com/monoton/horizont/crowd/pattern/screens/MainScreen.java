package com.monoton.horizont.crowd.pattern.screens;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.monoton.horizont.crowd.pattern.Constants;
import com.monoton.horizont.crowd.pattern.CrowndPatternCommand;
import com.monoton.horizont.crowd.pattern.SystemState;
import com.monoton.horizont.crowd.pattern.engine.SteeringActor;
import com.monoton.horizont.crowd.pattern.engine.SteeringActorEngine;
import com.monoton.horizont.crowd.pattern.engine.border.BorderControl;
import com.monoton.horizont.crowd.pattern.engine.border.BorderControlFactory;
import com.monoton.horizont.crowd.pattern.painter.colors.ColorMachineFactory;
import com.monoton.horizont.crowd.pattern.performance.PerformanceControl;
import com.monoton.horizont.crowd.pattern.scene.LightScene;
import com.monoton.horizont.crowd.pattern.scene.SteeringActorsScene;
import com.monoton.horizont.crowd.pattern.ui.NamedTexture;
import com.monoton.horizont.crowd.pattern.ui.UIBuilder;
import com.monoton.horizont.crowd.pattern.utils.DrawUtils;

/**
 * Created by monoton on 2.10.2017.
 */
public class MainScreen implements Screen{


    private Skin skin;


    private Texture background;
/*
    private Texture tfBackground;
    private Texture scroll_horizontal;
    private Texture knob_scroll;*/

    Array<SteeringActor> characters = new Array<SteeringActor>();


    private Stage actionStage;
    private Stage controlsStage;





    private BorderControl borderControl = BorderControlFactory.getBorderControl(Constants.BORDER_CONTROL_BOUNCE);

    private SteeringActorEngine steeringActorEngine;

    private Table mainControls = new Table();
    private Table minimalControls = new Table();


    /*private TextButton exitButton;
    private TextButton resetButton;*/


    private World world;

    private RayHandler rayHandler;






    private Viewport viewport;

    private Array<NamedTexture> shapes = null;
    private NamedTexture selectedShape;

    private SteeringActorsScene steeringActorsScene;





    private LightScene lightScene;

    private PerformanceControl performanceControl;

    private UIBuilder uiBuilder;

    private CrowndPatternCommand crowndPatternCommand;
    private StretchViewport stretchViewport;

    public MainScreen(CrowndPatternCommand crowndPatternCommand) {
        this.crowndPatternCommand = crowndPatternCommand;
        this.stretchViewport = crowndPatternCommand.getStretchViewport();
    }

    @Override
    public void show() {



        System.out.println("start creating");

        uiBuilder = crowndPatternCommand.getUiBuilder();

//		SystemState.getInstance().setParticleStartNumber(2);

        performanceControl = new PerformanceControl();

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
        rayHandler.setShadows(false);
        rayHandler.resizeFBO(200,200);
        rayHandler.setCulling(true);




        skin = uiBuilder.get(UIBuilder.DEFAULT_SKIN, Skin.class);





        // Create a actionStage

        actionStage = new Stage(stretchViewport);
        controlsStage = new Stage(stretchViewport);//new ScreenViewport()


        createMainControls();
        createMinimalControls();




        controlsStage.addActor(mainControls);







        characters = new Array<SteeringActor>();

        background = uiBuilder.get(UIBuilder.BACKGROUND, Texture.class);


        lightScene = new LightScene(characters, rayHandler);
        actionStage.addActor(lightScene);


        steeringActorsScene = new SteeringActorsScene(characters, rayHandler, background, selectedShape.getTextureRegion());

        actionStage.addActor(steeringActorsScene);

        steeringActorEngine = new SteeringActorEngine(characters, steeringActorsScene, borderControl, SystemState.getInstance().getRadiusFactor());

        steeringActorEngine.createSteeringActors(SystemState.getInstance().getParticleStartNumber(),rayHandler, shapes.get(0).getTextureRegion());

        actionStage.setScrollFocus(steeringActorsScene);





        // ORDER IS IMPORTANT!
        InputMultiplexer inputMultiplexer = new InputMultiplexer(controlsStage, actionStage);
        Gdx.input.setInputProcessor(inputMultiplexer);

        System.out.println("end creating");

    }

    private void populateMainControls() {
        createSpeedControls();
        createTailLengthControls();
        createLightControls();

        createAmbientControls();


        createColorChooseControls();

        createShapeChooseControls();

        createButtons();
    }

    private void createMinimalControls() {
        createTable(minimalControls);
        populateMinimalControls();
    }

    private void populateMinimalControls(){
        TextButton controlsButton = createControlsButton();

        minimalControls.add(controlsButton).colspan(1).padBottom(0);
        minimalControls.row();

       /* TextButton exitButton = createExitButton();
        minimalControls.add(exitButton).colspan(1).padBottom(0);
        minimalControls.row();*/


    }

    private TextButton createControlsButton() {
        TextButton controlsButton = new TextButton("Controls", skin);

        controlsButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                controlsStage.clear();
                controlsStage.addActor(mainControls);
                return true;
            }
        });



        return controlsButton;

    }

    private void createMainControls() {
        createTable(mainControls);
        populateMainControls();
    }

    private void createTable(Table table) {

        table.setWidth(Gdx.graphics.getWidth());
        table.setHeight(Gdx.graphics.getHeight());
        table.align(Align.top|Align.right);
        table.padTop(20);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        GdxAI.getTimepiece().update(Gdx.graphics.getDeltaTime());


	/*	if (performanceControl.isLagging()){
			System.out.println("lagging: "+performanceControl.getFrameRate());
			steeringActorEngine.removeSteeringActors(5);
		}*/


        /**
         *
         * 1. draw particles
         */
        actionStage.act();
        actionStage.draw();

        /**
         * 2. draw controls
         */
        controlsStage.act();
        controlsStage.draw();

        /**
         * 3. draw lights
         */
        world.step(1/60f, 6, 2);

        rayHandler.setCombinedMatrix(viewport.getCamera().combined);
        rayHandler.updateAndRender();


    }

    @Override
    public void resize(int width, int height) {
        System.out.println("start resizing");
        viewport.update(width, height);
        System.out.println("end resizing");

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

        System.out.println("start disposing");


        steeringActorEngine.dispose();


        actionStage.dispose();
        controlsStage.dispose();



        rayHandler.dispose();

        world.dispose();

        System.out.println("end disposing");

    }




    private void createButtons() {
        TextButton removeParticlesButton = createRemoveParticlesButton();
        TextButton addParticlesButton = createAddParticlesButton();
        TextButton resetButton = createResetButton();
        TextButton exitButton = createExitButton();
        TextButton hideControlsButton = createHideControlsButton();

        mainControls.add(resetButton).colspan(1).padBottom(0);
        mainControls.add(removeParticlesButton).colspan(1).padBottom(0);
        mainControls.add(addParticlesButton).colspan(1).padBottom(0);

        mainControls.row();
        mainControls.add(hideControlsButton).align(Align.right).colspan(2).padBottom(0);
        mainControls.add(exitButton).colspan(1).padBottom(0);
        mainControls.row();
    }

    private TextButton createHideControlsButton() {
        TextButton resetButton = new TextButton("Hide controls", skin);

        resetButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                controlsStage.clear();
                controlsStage.addActor(minimalControls);
                return true;
            }
        });

        return resetButton;

    }

    private TextButton createResetButton() {
        TextButton resetButton = new TextButton("Reset", skin);

        resetButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SystemState.getInstance().reset();
                resetControls();
                return true;
            }
        });

        return resetButton;

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

    private TextButton createAddParticlesButton() {
        TextButton addButton = new TextButton("Add 5", skin);

        addButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                steeringActorEngine.createSteeringActors(5, rayHandler, selectedShape.getTextureRegion());
                return true;
            }
        });

//        mainControls.add(addButton).colspan(1).center().padBottom(20);

        return addButton;

    }

    private TextButton createRemoveParticlesButton() {
        TextButton removeButton = new TextButton("Remove 5", skin);

        removeButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                steeringActorEngine.removeSteeringActors(5);
                return true;
            }
        });

//        mainControls.add(removeButton).colspan(1).right().padBottom(20);

        return removeButton;

    }

    private TextButton createExitButton() {
        TextButton exitButton = new TextButton("Exit", skin);

        exitButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                Gdx.app.exit();
                return true;
            }
        });



        return exitButton;

    }

    private SelectBox colorSelectBox = null;

    private Array<String> colors = new Array<String>();

    private void createColorChooseControls() {



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

        colorSelectBox = createDropDownControl("Color: ", colors, changeListener, 1);


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

        mainControls.add(randomColor).colspan(1).center();
        mainControls.row();
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

        selectedShape = shapes.get(0);

    }

    SelectBox shapeSelectBox = null;


    private void createShapeChooseControls() {

        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectedShape = (NamedTexture) shapeSelectBox.getSelected();
                steeringActorEngine.setShape(selectedShape.getTextureRegion());
                steeringActorsScene.setShape(selectedShape.getTextureRegion());


            }
        };

        shapeSelectBox = createDropDownControl("Shape: ", shapes, changeListener, 2);

        mainControls.row();
    }

    private SelectBox createDropDownControl(String labelName, Array items, ChangeListener changeListener, int colSpan) {
//        Label label = new Label(labelName, ls);
        Label label = new Label(labelName, skin);
        mainControls.add(label).align(Align.right).padRight(10);



//        final SelectBox selectBox = new SelectBox<String>(sbs);
        final SelectBox selectBox = new SelectBox<String>(skin);




        selectBox.setItems(items);
        selectBox.pack(); // To get the actual size



        selectBox.addListener(changeListener);

        mainControls.add(selectBox).align(Align.left).colspan(colSpan).padRight(0);

        return selectBox;
    }



    private Slider tailLengthSlider = null;

    private void createTailLengthControls() {


        InputListener listener = new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

//				tailLengthValue.setText("" + tailLengthSlider.getValue());//String.format("%.2f", floatValue);

                SystemState.getInstance().setTailSize((int)tailLengthSlider.getValue());


            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SystemState.getInstance().setTailSize((int)tailLengthSlider.getValue());
                return true;
            }

            ;
        };

        tailLengthSlider =  createSlider("Tail length: ",2,8,1f,SystemState.getInstance().getTailSize(), listener);


    }




    private Slider speedSlider = null;

    private void createSpeedControls() {


        InputListener listener = new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {


                steeringActorEngine.setSpeed(speedSlider.getValue());
                SystemState.getInstance().setSpeedFactor(speedSlider.getValue());


            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                steeringActorEngine.setSpeed(speedSlider.getValue());
                SystemState.getInstance().setSpeedFactor(speedSlider.getValue());
                return true;
            }

            ;
        };

        speedSlider = createSlider("Speed: ",20f,300f,1f,SystemState.getInstance().getSpeedFactor(), listener);

    }



    private Slider lightSlider = null;

    private void createLightControls() {


        InputListener listener = new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {


                SystemState.getInstance().setLightSize(lightSlider.getValue());


            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                SystemState.getInstance().setLightSize(lightSlider.getValue());

                return true;
            }

            ;
        };

        lightSlider = createSlider("Light size: ",0.2f,2.0f,0.1f,SystemState.getInstance().getLightSize(), listener);

    }





    private Slider ambientSlider = null;

    private void createAmbientControls() {


        InputListener listener = new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {


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

        ambientSlider = createSlider("Ambient: ",Constants.LIGHT_SCENE_WIDTH *0.1f,Constants.LIGHT_SCENE_WIDTH *2.0f,0.11f,SystemState.getInstance().getAmbientFactor(), listener);

    }






    private Slider createSlider(String label, float min, float max, float stepSize, float defaultValue, InputListener inputListener){
        Label labelName = new Label(label, skin);
        mainControls.add(labelName).align(Align.right).padRight(10);

        Slider slider = new Slider(min,max,stepSize, false, skin);

        slider.setValue(defaultValue);//
        slider.addListener(inputListener);

        mainControls.add(slider).width((float)Gdx.graphics.getWidth()/3).colspan(2).padRight(10);//growX().

      /*  Label value = new Label(""+slider.getValue(), skin);
        mainControls.add(value);*/
        mainControls.row();
        return slider;

    }

}
