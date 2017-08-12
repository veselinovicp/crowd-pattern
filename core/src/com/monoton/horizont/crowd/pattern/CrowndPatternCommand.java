package com.monoton.horizont.crowd.pattern;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.ai.steer.limiters.LinearAccelerationLimiter;
import com.badlogic.gdx.ai.steer.proximities.RadiusProximity;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.monoton.horizont.crowd.pattern.engine.SteeringActor;
import com.monoton.horizont.crowd.pattern.steering.PassingNeighboursSteering;

public class CrowndPatternCommand extends ApplicationAdapter {

	Texture img;

	Array<SteeringActor> characters;


	Stage stage;
	Table table;
	Stack stack;

	private float lastUpdateTime;
	private final static int PROXIMITY_FACTOR=10;



	
	@Override
	public void create () {
		lastUpdateTime =0;

		// Create a stage
		stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

//		table = new Table(){
		table = new Table(){
			@Override
			public void act (float delta) {
				float time = GdxAI.getTimepiece().getTime();
				if (lastUpdateTime != time) {
					lastUpdateTime = time;
					super.act(GdxAI.getTimepiece().getDeltaTime());
				}
			}
		};
		table.setFillParent(true);
		stage.addActor(table);


		img = new Texture("green_fish.png");

		characters = new Array<SteeringActor>();

		for (int i = 0; i < 100;i++) {
			final SteeringActor character = new SteeringActor(new TextureRegion(img), false);
			character.setMaxLinearSpeed(50);
			character.setMaxLinearAcceleration(100);

			RadiusProximity<Vector2> proximity = new RadiusProximity<Vector2>(character, characters,
					character.getBoundingRadius() * PROXIMITY_FACTOR);

//			CollisionAvoidance<Vector2> collisionAvoidanceSB = new CollisionAvoidance<Vector2>(character, proximity);
			PassingNeighboursSteering<Vector2> passingNeighboursSB = new PassingNeighboursSteering<Vector2>(character, proximity);


			Wander<Vector2> wanderSB = new Wander<Vector2>(character) //
					// Don't use Face internally because independent facing is off
					.setFaceEnabled(false) //
					// We don't need a limiter supporting angular components because Face is not used
					// No need to call setAlignTolerance, setDecelerationRadius and setTimeToTarget for the same reason
					.setLimiter(new LinearAccelerationLimiter(30)) //
					.setWanderOffset(60) //
					.setWanderOrientation(0) //
					.setWanderRadius(40) //
					.setWanderRate(MathUtils.PI2 * 4);

			PrioritySteering<Vector2> prioritySteeringSB = new PrioritySteering<Vector2>(character, 0.0001f);
//			prioritySteeringSB.add(collisionAvoidanceSB);
			prioritySteeringSB.add(passingNeighboursSB);
//			prioritySteeringSB.add(wanderSB);

			character.setSteeringBehavior(prioritySteeringSB);

			setRandomNonOverlappingPosition(character, characters, 5);
			setRandomOrientation(character);
			speedUp(character);



			characters.add(character);
			table.addActor(character);
		}

	}

	private void speedUp(SteeringActor character) {
		//character.getLinearVelocity().set(new Vector2(MathUtils.random(-1, 1),MathUtils.random(-1, 1))).nor().scl(character.getMaxLinearAcceleration());
		character.getLinearVelocity().scl(character.getMaxLinearAcceleration());
	}

	protected void setRandomOrientation (SteeringActor character) {
		float orientation = MathUtils.random(-MathUtils.PI, MathUtils.PI);
		character.setOrientation(orientation);
		if (!character.isIndependentFacing()) {
			// Set random initial non-zero linear velocity since independent facing is off
			character.angleToVector(character.getLinearVelocity(), orientation).scl(character.getMaxLinearSpeed()/5);
		}
	}

	protected void setRandomNonOverlappingPosition (SteeringActor character, Array<SteeringActor> others,
													float minDistanceFromBoundary) {
		int maxTries = Math.max(100, others.size * others.size);
		SET_NEW_POS:
		while (--maxTries >= 0) {
			character.setPosition(MathUtils.random(Gdx.graphics.getWidth()), MathUtils.random(Gdx.graphics.getHeight()), Align.center);
			character.getPosition().set(character.getX(Align.center), character.getY(Align.center));
			for (int i = 0; i < others.size; i++) {
				SteeringActor other = (SteeringActor)others.get(i);
				if (character.getPosition().dst(other.getPosition()) <= character.getBoundingRadius() + other.getBoundingRadius()
						+ minDistanceFromBoundary) continue SET_NEW_POS;
			}
			return;
		}
		throw new GdxRuntimeException("Probable infinite loop detected");
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
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
}
