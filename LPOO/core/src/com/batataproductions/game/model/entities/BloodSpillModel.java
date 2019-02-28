package com.batataproductions.game.model.entities;

import com.badlogic.gdx.utils.Pool;

public class BloodSpillModel extends EntityModel implements Pool.Poolable {

	/**
	 * Time since the spill has started.
	 */
	private float timeSinceSpill;

	/** Time a blood a spill will stay in the floor (in seconds).*/
	public final static float SPILL_TIME = 10.0f;

	/** Zombie type that is leaking blood */
	public ZombieModel.Sizes zombieType;

	/**
	 * Constructs a blood spill model belonging to a game model.
	 *
	 * @param x The x-coordinate of this blood spill.
	 * @param y The y-coordinate of this blood spill.
	 */
	public BloodSpillModel(float x, float y, ZombieModel.Sizes type) {
		super(x, y, 0);
		timeSinceSpill = 0;
		zombieType = type;
	}

	@Override
	public void reset() {
		setFlaggedForRemoval(false);
		timeSinceSpill = 0;
	}

	@Override
	public ModelType getType() {
		return ModelType.BLOOD_SPILL;
	}

	/**
	 * Returns the time since the blood has been spilled in seconds.
	 * @return time since the spill has started.
	 */
	public float getTimeSinceSpill() {
		return timeSinceSpill;
	}

	/**
	 * Updates the spill time given the time variation.
	 * @param dt delta time since last frame update
	 */
	public void updateSpillTime(float dt) {
		timeSinceSpill += dt;
		if(timeSinceSpill > SPILL_TIME)
			this.setFlaggedForRemoval(true);
	}

	/**
	 * Changes the size of the spill based on the zombie size.
	 * @param size the size of the zombie that is leaking blood.
	 */
	public void setZombieSize(ZombieModel.Sizes size)
	{
		zombieType = size;
	}

	/**
	 * Returns the zombie size that is leaking the blood.
	 * @return The size of the zombie that is leaking blood.
	 */
	public ZombieModel.Sizes getZombieSize(){
		return zombieType;
	}
}
