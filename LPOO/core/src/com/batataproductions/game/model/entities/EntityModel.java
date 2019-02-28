package com.batataproductions.game.model.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.batataproductions.game.model.GameModel;

/**
 * An abstract model representing an entity belonging to a game model.
 */
public abstract class EntityModel {

	/**
	 * The entity type this model represents.
	 */
	public enum ModelType {
		ZOMBIE_MEDIUM, ZOMBIE_SMALL, ZOMBIE_BIG, PLAYER, BULLET, BLOOD_SPILL};

	/**
	 * The x-coordinate of this model in meters.
	 */
	private float x;

	/**
	 * The y-coordinate of this model in meters.
	 */
	private float y;

	/**
	 * The current rotation of this model in radians.
	 */
	private float rotation;

	/**
	 * Has this model been flagged for removal?
	 */
	private boolean flaggedForRemoval = false;

	/** Time in ms a boddy will be knocked-back from his position. */
	final static float KNOCKBACK_MAX_TIME = 0.090f;

	/** Current knockback of the boddy. */
	private int current_knockBack;

	/** Current knockback direction. */
	private Vector2 knockback_direction;

	/** Knockback timer -> starts at 0 and ends at KNOCKBACK_TIME */
	private float current_knockBack_timer;

	/**
	 * Constructs a model with a position and a rotation.
	 *
	 * @param x The x-coordinate of this entity in meters.
	 * @param y The y-coordinate of this entity in meters.
	 * @param rotation The current rotation of this entity in radians.
	 */
	EntityModel(float x, float y, float rotation) {
		this.x = x;
		this.y = y;
		this.rotation = rotation;

		current_knockBack = 0;
		current_knockBack_timer = 0f;
		knockback_direction = null;
	}
	/**
	 * Returns the x-coordinate of this entity.
	 *
	 * @return The x-coordinate of this entity in meters.
	 */
	public float getX() {
		return x;
	}

	/**
	 * Returns the y-coordinate of this entity.
	 *
	 * @return The y-coordinate of this entity in meters.
	 */
	public float getY() {
		return y;
	}

	/**
	 * Returns the rotation of this entity.
	 *
	 * @return The rotation of this entity in radians.
	 */
	public float getRotation() {
		return rotation;
	}

	/**
	 * Sets the position of this entity.
	 *
	 * @param x The x-coordinate of this entity in meters.
	 * @param y The y-coordinate of this entity in meters.
	 */
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Sets the new knockback for this body.
	 * @param knockBack A int which represents the new knockback.
	 * @param direction Knockback direction.
	 */
	public void setKnockBack(int knockBack, Vector2 direction)
	{
		current_knockBack = knockBack;
		knockback_direction = direction;
		current_knockBack_timer = 0;
	}

	/**
	 * Returns the knockBack vector that should be applied to the body's linear velocity.
	 * @param dt time since last game update
	 * @return a vector representing the knockBack of the entity. Should be added to the entity's linear velocity.
	 */
	public Vector2 get_knockBack_vector(float dt)
	{
		if(GameModel.getInstance().getSelfPlayer().isDead())
			return null;

		// Check if the body is being knocked-back.
		if(current_knockBack == 0 || knockback_direction == null)
			return null;

		current_knockBack_timer += dt;

		// Check if the body knocoback should end in this frame update.
		if(current_knockBack_timer > KNOCKBACK_MAX_TIME) {
			current_knockBack = 0;
			return null;
		}

		// Calculate knockBack value for the current update.
		float knockBack_value = current_knockBack * ( 1 - (float) Math.pow( current_knockBack_timer/KNOCKBACK_MAX_TIME, 0.5) );

		//Gdx.app.log("knockBack_value", "" + knockBack_value);

		Vector2 knock_back_vector = knockback_direction.scl(knockBack_value);

		return knock_back_vector;
	}


	/**
	 * Sets the rotation of this entity.
	 *
	 * @param rotation The new rotation of this entity in radians.
	 */
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	/**
	 * Returns if this entity has been flagged for removal
	 *
	 * @return
	 */
	public boolean isFlaggedToBeRemoved() {
		return flaggedForRemoval;
	}

	/**
	 * Makes this model flagged for removal on next step
	 */
	public void setFlaggedForRemoval(boolean flaggedForRemoval) {
		this.flaggedForRemoval = flaggedForRemoval;
	}

	/**
	 * @return Entity type this model represents.
	 */
	public abstract ModelType getType();


}
