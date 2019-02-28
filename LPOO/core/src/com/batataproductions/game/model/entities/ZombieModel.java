package com.batataproductions.game.model.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.batataproductions.game.controller.GameController;
import com.batataproductions.game.controller.entities.EntityBody;
import com.batataproductions.game.model.GameModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ZombieModel extends EntityModel {

    /**
     * States the Zombie may be in.
     */
    public enum States { IDLE, MOVE, ATTACK};

    /**
     * Possible zombie size.
     */
    public enum Sizes {BIG, MEDIUM, SMALL}

    /**
     * This zombie size.
     */
    private Sizes size;

    /**
     * State the zombie is currently in.
     */
    private States currentState;

    /**
     * Time to next random zombie sound.
     */
    float timeLeftToNextSound;

    /** Zombie sound related constant */
    private static final int MAX_TIME_TO_NEXT_SOUND = 35;
    private static final int MIN_TIME_TO_NEXT_SOUND = 5;

	static List<Sound> ZOMBIE_SOUNDS;

    /**
     * Time since the current animation started.
     */
    private float stateTime = 0;

    /**
     * Time left until this zombie is ready to attack again.
     */
    private float timeToNextAttack;

    /**
     * Time left until this zombie is ready to attack again by default.
     */
    private static final float DEFAULT_TIME_TO_ATTACK = 1f;

    /**
     * The zombie health.
     */
    private float health;

    /**
     * The small zombie health by default.
     */
    private static final float DEFAULT_HEALTH_SMALL_ZOMBIE = 60f;

    /**
     * The medium zombie health by default.
     */
    private static final float DEFAULT_HEALTH_MEDIUM_ZOMBIE = 100f;

    /**
     * The big zombie health by default.
     */
    private static final float DEFAULT_HEALTH_BIG_ZOMBIE = 200f;

    /**
     * The small zombie velocity.
     */
    private static final float DEFAULT_VELOCITY_SMALL_ZOMBIE = 8f;

    /**
     * The medium zombie velocity.
     */
    private static final float DEFAULT_VELOCITY_MEDIUM_ZOMBIE = 4f;

    /**
     * The big zombie velocity.
     */
    private static final float DEFAULT_VELOCITY_BIG_ZOMBIE = 2f;

    /**
     * The damage a big zombie can give by default.
     */
    private static final float DEFAULT_DAMAGE_BIG_ZOMBIE = 30f;

    /**
     * The damage a small zombie can give by default.
     */
    private static final float DEFAULT_DAMAGE_SMALL_ZOMBIE = 10f;

    /**
     * The damage a medium zombie can give by default.
     */
    private static final float DEFAULT_DAMAGE_MEDIUM_ZOMBIE = 20f;

	/**
	 * Ray cast.
	 */
	ZombieRayCast zombieRayCast;

    /**
     * Constructs a zombie model belonging to a game model.
     *
     * @param x The x-coordinate of this player.
     * @param y The y-coordinate of this player.
     * @param rotation The rotation of this player.
     */
    public ZombieModel(float x, float y, float rotation, Sizes size) {
        super(x, y, rotation);
        currentState = ZombieModel.States.MOVE;
        this.size = size;

        if (size == Sizes.BIG)
            this.health = DEFAULT_HEALTH_BIG_ZOMBIE;
        if (size == Sizes.SMALL)
            this.health = DEFAULT_HEALTH_SMALL_ZOMBIE;
        if (size == Sizes.MEDIUM)
            this.health = DEFAULT_HEALTH_MEDIUM_ZOMBIE;

        this.timeToNextAttack = DEFAULT_TIME_TO_ATTACK;
        setNewTimerSound();
		zombieRayCast = new ZombieRayCast();
    }

    /**
     * Returns the size of this Zombie.
     *
     * @return The size of this zombie.
     */
    public Sizes getSize() {
        return size;
    }

    /**
     * Gets the current state for the zombie.
     * @return the current state.
     */
    public States getCurrentState() {
        return currentState;
    }

    /**
     * Sets a new state for the player.
     * @param newState the new state the player will be in.
     */
    public void setCurrentState(States newState) {
        this.currentState = newState;
    }

    /**
     * Increases this model state time.
     * @param deltatime time variation.
     */
    public void increaseStateTime(float deltatime)
    {
        if(!GameModel.getInstance().getSelfPlayer().isDead()) {
            this.stateTime += deltatime;
            if(timeLeftToNextSound <= 0)
            {
				playRandomSound();
                setNewTimerSound();
            }
            else
			{
				timeLeftToNextSound -= deltatime;
			}
        }
    }

    /**
     * Returns the state time for this zombie model.
     * @return the state time for the zombie model.
     */
    public float getStateTime(){
        return stateTime;
    }

    /**
     * Resets the state time for the zombie model.
     */
    public void resetStateTime() { stateTime = 0; }

    /**
     * @return Velocity of this Zombie.
     */
    public float getVel() {
        if (size == Sizes.BIG)
            return DEFAULT_VELOCITY_BIG_ZOMBIE;
        if (size == Sizes.SMALL)
            return DEFAULT_VELOCITY_SMALL_ZOMBIE;
        if (size == Sizes.MEDIUM)
            return DEFAULT_VELOCITY_MEDIUM_ZOMBIE;
        return 0f;
    }

    /**
     * Returns this zombie health.
     * @return this zombie health
     */
    public float getHealth(){
        return this.health;
    }

    /**
     * Sets a new health to this zombie.
     * @param newHealth
     */
    public void setHealth(float newHealth){
        this.health = newHealth;
    }

    /**
     * This zombie takes damage.
     * @param damage damage to take to this zombie health.
     */
    public void takeDamage(float damage){
        this.health -= damage;
    }

    /**
     * Returns the damage this zombie can give.
     * @return damage this zombie can give
     */
    public float getDamageToGive(){
        if (this.size == Sizes.BIG)
            return DEFAULT_DAMAGE_BIG_ZOMBIE;
        if (this.size == Sizes.SMALL)
            return DEFAULT_DAMAGE_SMALL_ZOMBIE;
        if (this.size == Sizes.MEDIUM)
            return DEFAULT_DAMAGE_MEDIUM_ZOMBIE;
        return 0f;
    }

    /**
     * Updates the time left until this zombie can attack again.
     * @param delta time that has passed since last update.
     */
    public void updateTimeToNextAttack(float delta){
        this.timeToNextAttack -= delta;
    }

    /**
     * Get time left until this zombie can attack again.
     */
    public float getTimeToNextAttack(){
        return this.timeToNextAttack;
    }

    /**
     * set the new time left until this zombie can attack again.
     */
    public void setNewTimeToNextAttack(){
        this.timeToNextAttack = DEFAULT_TIME_TO_ATTACK;
    }

    /**
     * this zombie dies!
     */
    public void goToHell() {
        /*fazer alguma coisa ao zombie por ele morrer*/
        setFlaggedForRemoval(true);
    }

    /**
     * Sets a random sound to be played by the zombie in a random number ammunt of seconds.
     */
    private void setNewTimerSound()
    {
        timeLeftToNextSound = (float) (Math.random() * (MAX_TIME_TO_NEXT_SOUND - MIN_TIME_TO_NEXT_SOUND) + MIN_TIME_TO_NEXT_SOUND);
    }

	/**
	 * Calculates a new angle the zombie should follow.
	 */
	public void calculateNewAngle(float xPlayer, float yPlayer)
	{
		//debug player pos
		//Gdx.app.log("player x", "" + GameModel.getInstance().getSelfPlayer().getX() / GameView.PIXEL_TO_METER / 128);
		//Gdx.app.log("player y", "" + GameModel.getInstance().getSelfPlayer().getY() / GameView.PIXEL_TO_METER / 128);
		/*
		GameController.getInstance().getWorld().rayCast(zombieRayCast, this.getX(), this.getY(), xPlayer, yPlayer);
		if(zombieRayCast.getNearestCollisionBodyType() == EntityBody.MAP_WALL)
		{
			double horizontalDistPZ = 15 - this.getX();
			double verticalDistPZ = 15 - this.getY();
			double zombieNewAngle = Math.atan2(verticalDistPZ , horizontalDistPZ);
			this.setRotation( (float) zombieNewAngle);
		}
		else if (zombieRayCast.getNearestCollisionBodyType() == EntityBody.PLAYER_BODY)
		{
			Gdx.app.log("found player", "");
			double horizontalDistPZ = xPlayer - this.getX();
			double verticalDistPZ = yPlayer - this.getY();
			double zombieNewAngle = Math.atan2(verticalDistPZ , horizontalDistPZ);
			this.setRotation( (float) zombieNewAngle);
		}
		*/
		double horizontalDistPZ = xPlayer - this.getX();
		double verticalDistPZ = yPlayer - this.getY();
		double zombieNewAngle = Math.atan2(verticalDistPZ , horizontalDistPZ);
		this.setRotation( (float) zombieNewAngle);
	}

    private void playRandomSound()
	{
		int rnd = new Random().nextInt(ZOMBIE_SOUNDS.size());
		Sound randomSound = ZOMBIE_SOUNDS.get(rnd);
		// Calculate pan.
		float player_x = GameModel.getInstance().getSelfPlayer().getX();
		float zombie_x = this.getX();
		float pan;

		if(zombie_x > player_x)
		{
			if(zombie_x + 20 > player_x)
				pan = 1;
			else
				pan = (zombie_x - player_x) / 20f;
		}
		else if(zombie_x == player_x)
			pan = 0;
		else
		{
			if(zombie_x - 20 < player_x)
				pan = -1;
			else
				pan = - (player_x - zombie_x) / 20f;
		}

		randomSound.play(0.01f, 1f, pan);
	}

    static public void createSounds(AssetManager a)
	{
		ZOMBIE_SOUNDS = new ArrayList<Sound>();
		ZOMBIE_SOUNDS.add(a.get("zombie_sound1.mp3", Sound.class));
		ZOMBIE_SOUNDS.add(a.get("zombie_sound2.mp3", Sound.class));
		ZOMBIE_SOUNDS.add(a.get("zombie_sound3.mp3", Sound.class));
		ZOMBIE_SOUNDS.add(a.get("zombie_sound4.mp3", Sound.class));
		ZOMBIE_SOUNDS.add(a.get("zombie_sound5.mp3", Sound.class));
		ZOMBIE_SOUNDS.add(a.get("zombie_sound6.mp3", Sound.class));
	}


    /**
     * @return Entity type this model represents.
     */
    @Override
    public ModelType getType() {
        if (size == Sizes.BIG)
            return ModelType.ZOMBIE_BIG;
        if (size == Sizes.SMALL)
            return ModelType.ZOMBIE_SMALL;
        if (size == Sizes.MEDIUM)
            return ModelType.ZOMBIE_MEDIUM;
        return null;

    }


    public class ZombieRayCast implements RayCastCallback {

		private short body_type = EntityBody.PLAYER_BODY;
		Vector2 collPoint = new Vector2(0 ,0);

		@Override
		public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
    		if(fixture.getBody().getUserData() instanceof ZombieModel || fixture.getBody().getUserData() instanceof BulletModel)
			{
				return -1;
			}
			else {
    			body_type = fixture.getFilterData().categoryBits;
				collPoint.x = point.x;
				collPoint.y = point.y;
				return fraction;
			}
		}

		public short getNearestCollisionBodyType()
		{
			return body_type;
		}

		public Vector2 getCollPoint(){
			return collPoint;
		}
	}
}
