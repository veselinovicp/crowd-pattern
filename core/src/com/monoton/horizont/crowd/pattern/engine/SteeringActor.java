/*******************************************************************************
 * Copyright 2014 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.monoton.horizont.crowd.pattern.engine;

import box2dLight.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.proximities.RadiusProximity;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.monoton.horizont.crowd.pattern.Constants;
import com.monoton.horizont.crowd.pattern.engine.border.BorderControl;
import com.monoton.horizont.crowd.pattern.engine.light.LightPainter;
import com.monoton.horizont.crowd.pattern.painter.DrawPoint;
import com.monoton.horizont.crowd.pattern.painter.ShootingStarPainter;

/** A SteeringActor is a scene2d {@link Actor} implementing the {@link Steerable} interface.
 * 
 * @autor davebaol */
public class SteeringActor extends Actor implements Steerable<Vector2> {

	private static final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<Vector2>(new Vector2());

	TextureRegion region;

	Vector2 position;  // like scene2d centerX and centerY, but we need a vector to implement Steerable
	Vector2 linearVelocity;
	float angularVelocity;
	float boundingRadius;
	boolean tagged;

	float maxLinearSpeed = 100;
	float maxLinearAcceleration = 200;
	float maxAngularSpeed = 5;
	float maxAngularAcceleration = 10;

	boolean independentFacing;

	SteeringBehavior<Vector2> steeringBehavior;

	private BorderControl borderControl;

	private SteeringActorEngine steeringActorEngine;

	private RadiusProximity<Vector2> proximity;

	private ShootingStarPainter shootingStarPainter;


	private LightPainter lightPainter;










	public SteeringActor(TextureRegion region, boolean independentFacing, BorderControl borderControl, SteeringActorEngine steeringActorEngine, RayHandler rayHandler, float x, float y) {

		this.steeringActorEngine = steeringActorEngine;
		this.borderControl = borderControl;
		this.independentFacing = independentFacing;
		this.region = region;
		this.position = new Vector2();
		this.linearVelocity = new Vector2();
		this.setBounds(0, 0, region.getRegionWidth(), region.getRegionHeight());
		this.boundingRadius = (region.getRegionWidth() + region.getRegionHeight()) / 4f;
		this.setOrigin(region.getRegionWidth() * .5f, region.getRegionHeight() * .5f);
		//this.setOrigin(MathUtils.random(Gdx.graphics.getWidth()), MathUtils.random(Gdx.graphics.getHeight()));

		shootingStarPainter = ShootingStarPainter.getShootingStarPainter(Constants.SHOOTING_STAR_PAINTER_SINGLE,null, this);

		setPosition(x, y, Align.center);
		getPosition().set(getX(Align.center), getY(Align.center));



		lightPainter = new LightPainter(rayHandler);



	}





	public TextureRegion getRegion () {
		return region;
	}

	public void setRegion (TextureRegion region) {
		this.region = region;
	}

	@Override
	public Vector2 getPosition () {
		return position;
	}

	@Override
	public float getOrientation () {
		return getRotation() * MathUtils.degreesToRadians;
	}

	@Override
	public void setOrientation (float orientation) {
		setRotation(orientation * MathUtils.radiansToDegrees);
	}

	@Override
	public Vector2 getLinearVelocity () {
		return linearVelocity;
	}

	@Override
	public float getAngularVelocity () {
		return angularVelocity;
	}

	public void setAngularVelocity (float angularVelocity) {
		this.angularVelocity = angularVelocity;
	}

	@Override
	public float getBoundingRadius () {
		return boundingRadius;
	}

	@Override
	public boolean isTagged () {
		return tagged;
	}

	@Override
	public void setTagged (boolean tagged) {
		this.tagged = tagged;
	}

	@Override
	public Location<Vector2> newLocation () {
		return new Scene2dLocation();
	}

	@Override
	public float vectorToAngle (Vector2 vector) {
		return Scene2dSteeringUtils.vectorToAngle(vector);
	}

	@Override
	public Vector2 angleToVector (Vector2 outVector, float angle) {
		return Scene2dSteeringUtils.angleToVector(outVector, angle);
	}

	@Override
	public float getMaxLinearSpeed () {
		return maxLinearSpeed;
	}

	@Override
	public void setMaxLinearSpeed (float maxLinearSpeed) {
		this.maxLinearSpeed = maxLinearSpeed;
	}

	@Override
	public float getMaxLinearAcceleration () {
		return maxLinearAcceleration;
	}

	@Override
	public void setMaxLinearAcceleration (float maxLinearAcceleration) {
		this.maxLinearAcceleration = maxLinearAcceleration;
	}

	@Override
	public float getMaxAngularSpeed () {
		return maxAngularSpeed;
	}

	@Override
	public void setMaxAngularSpeed (float maxAngularSpeed) {
		this.maxAngularSpeed = maxAngularSpeed;
	}

	@Override
	public float getMaxAngularAcceleration () {
		return maxAngularAcceleration;
	}

	@Override
	public void setMaxAngularAcceleration (float maxAngularAcceleration) {
		this.maxAngularAcceleration = maxAngularAcceleration;
	}

	@Override
	public float getZeroLinearSpeedThreshold () {
		return 0.001f;
	}

	@Override
	public void setZeroLinearSpeedThreshold (float value) {
		throw new UnsupportedOperationException();
	}

	public boolean isIndependentFacing () {
		return independentFacing;
	}

	public void setIndependentFacing (boolean independentFacing) {
		this.independentFacing = independentFacing;
	}

	public SteeringBehavior<Vector2> getSteeringBehavior () {
		return steeringBehavior;
	}

	public void setSteeringBehavior (SteeringBehavior<Vector2> steeringBehavior) {
		this.steeringBehavior = steeringBehavior;
	}

	@Override
	public void act (float delta) {
		position.set(getX(Align.center), getY(Align.center));
		if (steeringBehavior != null) {

			// Calculate steering acceleration
			steeringBehavior.calculateSteering(steeringOutput);

			/*
			 * Here you might want to add a motor control layer filtering steering accelerations.
			 * 
			 * For instance, a car in a driving game has physical constraints on its movement: it cannot turn while stationary; the
			 * faster it moves, the slower it can turn (without going into a skid); it can brake much more quickly than it can
			 * accelerate; and it only moves in the direction it is facing (ignoring power slides).
			 */

			// Apply steering acceleration
			applySteering(steeringOutput, delta);

//			wrapAround(position, getParent().getWidth(), getParent().getHeight());
			wrapAround(position, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			setPosition(position.x, position.y, Align.center);

		}

		super.act(delta);
	}

	public void  dispose(){
		lightPainter.setTailLightsInactive();
	}



	// the display area is considered to wrap around from top to bottom
	// and from left to right
	protected void wrapAround (Vector2 pos, float maxX, float maxY) {
	/*	if (pos.x > maxX) pos.x = 0.0f;

		if (pos.x < 0) pos.x = maxX;

		if (pos.y < 0) pos.y = maxY;

		if (pos.y > maxY) pos.y = 0.0f;*/

		if (pos.x + region.getRegionWidth()/2> maxX) {
			borderControl.overBorderX(pos, linearVelocity, maxX, maxY, region.getRegionWidth(), region.getRegionHeight());
		}

		if (pos.x - region.getRegionWidth()/2 < 0) {
			borderControl.negativeX(pos, linearVelocity, maxX, maxY, region.getRegionWidth(), region.getRegionHeight());
		}

		if (pos.y - region.getRegionHeight()/2< 0) {
			borderControl.negativeY(pos, linearVelocity, maxX, maxY, region.getRegionWidth(), region.getRegionHeight());
		}

		if (pos.y + region.getRegionHeight()/2> maxY) {
			borderControl.overBorderY(pos, linearVelocity, maxX, maxY, region.getRegionWidth(), region.getRegionHeight());
		}

	}

	private void applySteering (SteeringAcceleration<Vector2> steering, float time) {
		// Update position and linear velocity. Velocity is trimmed to maximum speed
		position.mulAdd(linearVelocity, time);
		linearVelocity.mulAdd(steering.linear, time).limit(getMaxLinearSpeed());

		//NEW
		linearVelocity.setLength(getMaxLinearSpeed());

		// Update orientation and angular velocity
		if (independentFacing) {
			setRotation(getRotation() + (angularVelocity * time) * MathUtils.radiansToDegrees);
			angularVelocity += steering.angular * time;
		} else {
			// If we haven't got any velocity, then we can do nothing.
			if (!linearVelocity.isZero(getZeroLinearSpeedThreshold())) {
				float newOrientation = vectorToAngle(linearVelocity);
				angularVelocity = (newOrientation - getRotation() * MathUtils.degreesToRadians) * time; // this is superfluous if independentFacing is always true
				setRotation(newOrientation * MathUtils.radiansToDegrees);
			}
		}
	}

	@Override
	public void draw (Batch batch, float parentAlpha) {


//		lightPainter.adjustPrimaryLight(position, linearVelocity);


		Array<DrawPoint> tail = shootingStarPainter.draw(batch, parentAlpha);

		lightPainter.drawLight(tail);




	}




	public SteeringActorEngine getSteeringActorEngine() {
		return steeringActorEngine;
	}

	public void setProximity(RadiusProximity<Vector2> proximity) {
		this.proximity = proximity;
	}

	public RadiusProximity<Vector2> getProximity() {
		return proximity;
	}



}
