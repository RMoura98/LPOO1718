package com.batataproductions.game.controller.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.batataproductions.game.model.entities.EntityModel;

import static com.batataproductions.game.view.GameView.PIXEL_TO_METER;

/**
 * Wrapper class that represents an abstract physical
 * body supported by a Box2D body.
 */
public abstract class EntityBody {
	public final static short PLAYER_BODY = 0x0001;

	public final static short ZOMBIE_BODY = 0x0002;

	public final static short BULLET_BODY = 0x0004;

	public final static short MAP_WALL = 0x0008;


	/**
	 * The Box2D body that supports this body.
	 */
	final Body body;

	/**
	 * Constructs a body representing a model in a certain world.
	 *
	 * @param world The world this body lives on.
	 * @param model The model representing the body.
	 */
	EntityBody(World world, EntityModel model) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(model.getX(), model.getY());
		bodyDef.angle = model.getRotation();

		body = world.createBody(bodyDef);
		body.setUserData(model);
	}

	/**
	 * Helper method to create a polygon fixture represented by a set of vertexes.
	 * @param body The body the fixture is to be attached to.
	 * @param vertexes The vertexes defining the fixture in pixels so it is
	 *                 easier to get them from a bitmap image.
	 * @param width The width of the bitmap the vertexes where extracted from.
	 * @param height The height of the bitmap the vertexes where extracted from.
	 * @param density The density of the fixture. How heavy it is in relation to its area.
	 * @param friction The friction of the fixture. How slippery it is.
	 * @param restitution The restitution of the fixture. How much it bounces.
	 * @param category
	 * @param mask
	 * @param isSensor Is this fixture a sensor?
	 * @return The fixture created.
	 */
	final Fixture createFixture(Body body, float[] vertexes, int width, int height, float density, float friction, float restitution, short category, short mask, boolean isSensor) {
		// Transform pixels into meters, center and invert the y-coordinate
		for (int i = 0; i < vertexes.length; i++) {
			if (i % 2 == 0) vertexes[i] -= width / 2;   // center the vertex x-coordinate
			if (i % 2 != 0) vertexes[i] -= height / 2;  // center the vertex y-coordinate

			if (i % 2 != 0) vertexes[i] *= -1;          // invert the y-coordinate

			vertexes[i] *= PIXEL_TO_METER;              // scale from pixel to meter
		}

		PolygonShape polygon = new PolygonShape();
		polygon.set(vertexes);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = polygon;

		fixtureDef.density = density;
		fixtureDef.friction = friction;
		fixtureDef.restitution = restitution;
		fixtureDef.filter.categoryBits = category;
		fixtureDef.filter.maskBits = mask;
		fixtureDef.isSensor = isSensor;

		Fixture newFixture = body.createFixture(fixtureDef);

		polygon.dispose();

		return newFixture;
	}

	/**
	 * Wraps the getX method from the Box2D body class.
	 *
	 * @return the x-coordinate of this body.
	 */
	public float getX() {
		return body.getPosition().x;
	}

	/**
	 * Wraps the getY method from the Box2D body class.
	 *
	 * @return the y-coordinate of this body.
	 */
	public float getY() {
		return body.getPosition().y;
	}

	/**
	 * Wraps the getAngle method from the Box2D body class.
	 *
	 * @return the angle of rotation of this body.
	 */
	public float getAngle() {
		return body.getAngle();
	}

	/**
	 * Changes the body's angle.
	 *
	 * @param angle the new angle of rotation for this body.
	 */
	public void setAngle(float angle) {
		body.setTransform(body.getPosition(), angle);
	}

	/**
	 * Wraps the setTransform method from the Box2D body class.
	 *
	 * @param x the new x-coordinate for this body
	 * @param y the new y-coordinate for this body
	 * @param angle the new rotation angle for this body
	 */
	public void setTransform(float x, float y, float angle) {
		body.setTransform(x, y, angle);
	}

	/**
	 * Sets the linear velocity of this object.
	 *
	 * @param velocity the new linear velocity angle for this body
	 */
	public void setLinearVelocity(float velocity) {
		body.setLinearVelocity((float)(velocity * -Math.sin(getAngle())), (float) (velocity * Math.cos(getAngle())));
	}

	/**
	 * Wraps the setAngularVelocity method from the Box2D body class.
	 *
	 * @param omega the new angular velocity angle for this body
	 */
	public void setAngularVelocity(float omega) {
		body.setAngularVelocity(omega);
	}

	/**
	 * Wraps the applyForceToCenter method from the Box2D body class.
	 *
	 * @param forceX the x-component of the force to be applied
	 * @param forceY the y-component of the force to be applied
	 * @param awake should the body be awaken
	 */
	public void applyForceToCenter(float forceX, float forceY, boolean awake) {
		body.applyForceToCenter(forceX, forceY, awake);
	}

	/**
	 * Wraps the applyLinearImpulse method from the Box2D body class. Applies the impulse in the body center.
	 *
	 * @param impulseX the x-component of the impulse to be applied
	 * @param impulseY the y-component of the impulse to be applied
	 * @param awake should the body be awaken
	 */
	public void applyLinearImpulseToCenter(float impulseX, float impulseY, boolean awake) {
		body.applyForceToCenter(impulseX, impulseY, awake);
	}

	/**
	 * Wraps the setLinearVelocity method from the Box2D body class.
	 *
	 * @param vX the x-component of the velocity to be set
	 * @param vY the y-component of the velocity to be set
	 */
	public void setLinearVelocity(float vX, float vY) {
		body.setLinearVelocity(vX, vY);
	}


	/**
	 * Wraps the getUserData method from the Box2D body class.
	 *
	 * @return the user data
	 */
	public Object getUserData() {
		return body.getUserData();
	}
}
