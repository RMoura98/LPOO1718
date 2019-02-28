package com.batataproductions.game.model.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.batataproductions.game.DeathmatchMania;
import com.batataproductions.game.model.GameModel;

/**
 * A model representing a player.
 */
public class PlayerModel extends EntityModel{

    /**
	 * Weapons the player can hold.
	 */
	public enum Weapons { RIFLE, PISTOL, KNIFE };

	/**
	 * States the player may be in.
	 */
	public enum States { IDLE, MOVE, RELOAD, SHOOT };

	/**
	 * Max number of bullets a magazine for the rifle holds.
	 */
	public final static int MAX_RIFLE_AMMO = 30;

	/**
	 * Max number of bullets a magazine for the pistol holds.
	 */
	public final static int MAX_PISTOL_AMMO = 15;

	/**
	 * Default Volume.
	 */
	public final static float DEFAULT_VOLUME = 0.01f;

	/**
	 * Number of bullets in the current rifle magazine.
	 */
	private int rifleCurrentAmmo;

	/**
	 * rifle shot sound.
	 */
	private static Sound rifleShotSound;

	/**
	 * rifle reload sound.
	 */
	private static Sound rifleReloadSound;

	/**
	 * rifle Draw sound.
	 */
	private static Sound rifleDrawSound;

	/**
	 * pistol shot sound.
	 */
	private static Sound pistolShotSound;

	/**
	 * pistol reload sound.
	 */
	private static Sound pistolReloadSound;

	/**
	 * pistol Draw sound.
	 */
	private static Sound pistolDrawSound;

	/**
	 * Number of bullets in the current rifle magazine.
	 */
	private int pistolCurrentAmmo;

	/**
	 * knife shot sound.
	 */
	private static Sound knifeShotSound;

	/**
	 * knife Draw sound.
	 */
	private static Sound knifeDrawSound;

	/**
	 * footstep sound.
	 */
	private static Sound footstepSound;

	/**
	 * footstep sound ?.
	 */
	private boolean walking = false;

	/**
	 * Weapon the player is currently holding.
	 */
	private Weapons currentWeapon;

	/**
	 * State the player is currently in.
	 */
	private States currentState;

	/**
	 * Time since the current animation started.
	 */
	private float stateTime = 0;

	/**
	 * Time since the last moment the player has moved.
	 */
	private float moveStateTime = 0;

	/**
	 * The player health.
	 */
	private float health;

	/**
	 * Time in seconds since the last moment the player was damaged by a zombie.
	 */
	private float time_since_last_damage_taken;

	/**
	 * Time in seconds the player must not suffer damage in order to gain hp.
	 */
	private static final float NOT_DAMAGED_TIME_TO_REGENERATE_LIFE = 3.0f;

	/**
	 * Health points the player gains per second if he hasn't been damaged in NOT_DAMAGED_TIME_TO_REGENERATE_LIFE seconds.
	 */
	private static final int HP_REGENERATION_PER_SECOND = 10;

	/**
	 * The player health by default.
	 */
	public static final float DEFAULT_HEALTH = 100f;

	/**
	 * The damage a knife can give by default.
	 */
	public static final float DEFAULT_DAMAGE_KNIFE = 125f;

	/**
	 * Constructs a player model belonging to a game model.
	 *
	 * @param x The x-coordinate of this player.
	 * @param y The y-coordinate of this player.
	 * @param rotation The rotation of this player.
	 */
	public PlayerModel(float x, float y, float rotation) {
		super(x, y, rotation);
		currentWeapon = Weapons.RIFLE;
		currentState = States.IDLE;
		rifleCurrentAmmo = MAX_RIFLE_AMMO;
		pistolCurrentAmmo = MAX_PISTOL_AMMO;
		health = DEFAULT_HEALTH;
		time_since_last_damage_taken = 0;
	}

	public void createSounds(AssetManager assetManager) {
		if(assetManager == null)
			return;
		rifleShotSound = assetManager.get("ak47_shot.ogg", Sound.class);
		rifleDrawSound = assetManager.get("ak47_draw.ogg", Sound.class); //Gdx.audio.newSound(Gdx.files.internal("ak47_draw.ogg"));
		rifleReloadSound = assetManager.get("ak47_reload.ogg", Sound.class); // Gdx.audio.newSound(Gdx.files.internal("ak47_reload.ogg"));
		pistolShotSound = assetManager.get("glock_shot.ogg", Sound.class); //Gdx.audio.newSound(Gdx.files.internal("glock_shot.ogg"));
		pistolReloadSound = assetManager.get("glock_reload.ogg", Sound.class); //Gdx.audio.newSound(Gdx.files.internal("glock_reload.ogg"));
		pistolDrawSound = assetManager.get("glock_draw.ogg", Sound.class); //Gdx.audio.newSound(Gdx.files.internal("glock_draw.ogg"));
		knifeShotSound = assetManager.get("knife_shot.ogg", Sound.class); //Gdx.audio.newSound(Gdx.files.internal("knife_shot.ogg"));
		knifeDrawSound = assetManager.get("knife_draw.ogg", Sound.class); //Gdx.audio.newSound(Gdx.files.internal("knife_draw.ogg"));
		footstepSound = assetManager.get("footstep.ogg", Sound.class); //Gdx.audio.newSound(Gdx.files.internal("footstep.ogg"));
	}

	/**
	 * Returns the current weapon the player is holding.
	 * @return weapon the player is holding.
	 */
	public Weapons getCurrentWeapon() {
		return currentWeapon;
	}

	/**
	 * Sets a new weapon for the player.
	 * @param newWeapon the new weapon the player will hold.
	 */
	public void setCurrentWeapon(Weapons newWeapon) {
		this.currentWeapon = newWeapon;
	}

	/**
	 * Gets the current state for the player.
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
		if(!this.isDead())
			this.stateTime += deltatime;
	}

	/**
	 * Returns the state time for this player model.
	 * @return the state time for the player model.
	 */
	public float getStateTime(){
		return stateTime;
	}

	/**
	 * Resets the state time for the player model.
	 */
	public void resetStateTime() { stateTime = 0; }

	/**
	 * Increases this model move state time.
	 * @param deltatime time variation.
	 */
	public void increaseMoveStateTime(float deltatime)
	{
		this.moveStateTime += deltatime;
	}

	/**
	 * Returns the move state time for this player model.
	 * @return the move state time for the player model.
	 */
	public float getMoveStateTime(){
		return moveStateTime;
	}

	@Override
	public ModelType getType() {
		return ModelType.PLAYER;
	}

	/**
	 * Function that returns the number of bullets loaded in the player's rifle.
	 * @return Current rifle ammo count
	 */
	public int getRifleCurrentAmmo() {
		return rifleCurrentAmmo;
	}

	/**
	 * Function that returns the number of bullets loaded in the player's pistol.
	 * @return Current pistol ammo count
	 */
	public int getPistolCurrentAmmo() {
		return pistolCurrentAmmo;
	}

	/**
	 * Decreases the rifle ammo by 1.
	 */
	public void fireRifle()
	{
		if(rifleCurrentAmmo > 0) rifleCurrentAmmo--;
	}

	/**
	 * Decreases the pistol ammo by 1.
	 */
	public void firePistol()
	{
		if(pistolCurrentAmmo > 0) pistolCurrentAmmo--;
	}

	/**
	 * Replenishes the rifle ammo.
	 */
	public void replenishRifleAmmo()
	{
		rifleCurrentAmmo = MAX_RIFLE_AMMO;
	}

	/**
	 * Replenishes the rifle ammo.
	 */
	public void replenishPistolAmmo()
	{
		pistolCurrentAmmo = MAX_PISTOL_AMMO;
	}

	/**
	 * Returns this player health.
	 * @return this player health
	 */
	public float getHealth(){
		return this.health;
	}

	/**
	 * Is the player dead?
	 * @return A boolean that is true if the player is dead, false otherwise.
	 */
	public boolean isDead() {
		return health <= 0;
	}

	/**
	 * Sets a new health to this player.
	 * @param newHealth
	 */
	public void setHealth(float newHealth){
		this.health = newHealth;
	}

	/**
	 * Process player life regeneration.
	 * @param dt time since last update.
	 */
	public void regenerateHealth(float dt)
	{
		time_since_last_damage_taken += dt;
		if(time_since_last_damage_taken > NOT_DAMAGED_TIME_TO_REGENERATE_LIFE)
		{
			if(this.isDead()) return;

			float hp_gain = dt * HP_REGENERATION_PER_SECOND;

			if(this.health + hp_gain > 100)
				this.health = 100;
			else
				this.health += hp_gain;
		}
	}

	/**
	 * This player takes damage.
	 * @param damage damage to take to this player health.
	 */
	public void takeDamage(float damage){
		if(this.isDead()) return;
		time_since_last_damage_taken = 0;
		this.health -= damage;
	}

	/**
	 * Returns the damage this player with a knife can give.
	 * @return damage this player with a knife can give.
	 */
	public float getKnifeDamage(){
		return DEFAULT_DAMAGE_KNIFE;
	}

	public void doShotSound(){
		if(Gdx.app == null)
			return;
		if(getCurrentWeapon() == Weapons.RIFLE)
			rifleShotSound.play(DEFAULT_VOLUME);
		else if(getCurrentWeapon() == Weapons.PISTOL)
			pistolShotSound.play(DEFAULT_VOLUME);
		else if(getCurrentWeapon() == Weapons.KNIFE)
			knifeShotSound.play(DEFAULT_VOLUME);

	}

	public void doReloadSound(){
		if(getCurrentWeapon() == Weapons.RIFLE)
			rifleReloadSound.play(DEFAULT_VOLUME);
		else if(getCurrentWeapon() == Weapons.PISTOL)
			pistolReloadSound.play(DEFAULT_VOLUME);

	}

	public void doDrawSound(){
		if(getCurrentWeapon() == Weapons.RIFLE)
			rifleDrawSound.play(DEFAULT_VOLUME);
		else if(getCurrentWeapon() == Weapons.PISTOL)
			pistolDrawSound.play(DEFAULT_VOLUME);
		else if(getCurrentWeapon() == Weapons.KNIFE)
			knifeDrawSound.play(DEFAULT_VOLUME);
	}

	public void doFootstepSound() {
		if(!walking && !isDead())
			footstepSound.setPitch(footstepSound.loop(DEFAULT_VOLUME),1.75f);
		walking = true;
	}

	public void stopFootstepSound() {
		footstepSound.stop();
		walking = false;
	}

	/**
	 * this player dies!
	 */
	public void die() {
		/*fazer alguma coisa ao zombie por ele morrer*/
	}

}
