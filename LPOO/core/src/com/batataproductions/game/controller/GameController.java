package com.batataproductions.game.controller;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.batataproductions.game.controller.entities.BigZombieBody;
import com.batataproductions.game.controller.entities.BulletBody;
import com.batataproductions.game.controller.entities.EntityBody;
import com.batataproductions.game.controller.entities.PlayerBody;
import com.batataproductions.game.controller.entities.MediumZombieBody;
import com.batataproductions.game.controller.entities.SmallZombieBody;
import com.batataproductions.game.model.GameModel;
import com.batataproductions.game.model.entities.BloodSpillModel;
import com.batataproductions.game.model.entities.BulletModel;
import com.batataproductions.game.model.entities.EntityModel;
import com.batataproductions.game.model.entities.PlayerModel;
import com.batataproductions.game.model.entities.ZombieModel;
import com.batataproductions.game.view.GameView;
import com.batataproductions.game.view.hud.Hud;

import net.dermetfan.utils.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.batataproductions.game.model.entities.PlayerModel.MAX_PISTOL_AMMO;
import static com.batataproductions.game.model.entities.PlayerModel.MAX_RIFLE_AMMO;

/**
 * Controls the physics aspect of the game.
 */

public class GameController implements ContactListener {
	/**
	 * The singleton instance of this controller
	 */
	private static GameController instance;

	/**
	 * The physics world controlled this controller.
	 */
	private World world;

	/**
	 * Accumulator used to calculate the simulation step.
	 */
	private float accumulator;

	/**
	 * phone rotation detector (gyroscope)
	 */
	private MotionDetector md;

	/**
	 * Our player body.
	 */
	private PlayerBody selfPlayerBody;

	/**
	 * Max linear velocity that a player body may have.
	 */
	public static short MAX_LINEAR_VEL_PLAYER = 15;

	/**
	 * The speed of bullets
	 */
	private static final float BULLET_SPEED = 70f;

	/**
	 * Minimum time between consecutive rifle shots in seconds
	 */
	private static final float TIME_BETWEEN_SHOTS_RIFLE = .1f;

	/**
	 * Minimum time between consecutive pistol shots in seconds
	 */
	private static final float TIME_BETWEEN_SHOTS_PISTOL = .2f;

	/**
	 * Minimum time between consecutive knife shots in seconds
	 */
	private static final float TIME_BETWEEN_SHOTS_KNIFE = .6f;

	/**
	 * Time left until gun is ready to shoot
	 */
	private float timeToNextShot;

	/**
	 * Time left until player state is ready to change
	 */
	private float timeToNextState;

	/**
	 * default Time until state is ready to change when using riffle
	 */
	private static final float DEFAULT_TIME_TO_NEXT_STATE_RIFLE = .1f;

	/**
	 * default Time until state is ready to change when using pistol
	 */
	private static final float DEFAULT_TIME_TO_NEXT_STATE_PISTOL = .2f;

	/**
	 * default Time until state is ready to change when using riffle
	 */
	private static final float DEFAULT_TIME_TO_NEXT_STATE_KNIFE = 0.6f;

	/**
	 * default Time until state is ready to change when reloading riffle
	 */
	private static final float DEFAULT_TIME_TO_NEXT_STATE_RELOAD = 0.8f;

	/**
	 * Player movement flag bits for the movement bit mask.
	 */
	public static short PLAYER_MOVE_LEFT = 0x01;
	public static short PLAYER_MOVE_RIGHT = 0x02;
	public static short PLAYER_MOVE_UP = 0x04;
	public static short PLAYER_MOVE_DOWN = 0x08;

	/**
	 * Current game round info.
	 */
	public enum Difficulty {EASY, NORMAL, HARD};
	int currentRound;
	Difficulty currentDifficulty;
	int zombiesLeftToSpawn;
	final static int zombies_per_round_easy = 8;
	final static int zombies_per_round_normal = 13;
	final static int zombies_per_round_hard = 20;

	int zombies_killed;
	private boolean motionDetectorOn;



	static final Map<Pair<Integer, Integer>, Float> ZOMBIE_SPAWNS = createZombieSpawnsMap();

	private static Map<Pair<Integer, Integer>, Float> createZombieSpawnsMap() {
		Map<Pair<Integer, Integer>, Float> result = new HashMap<Pair<Integer, Integer>, Float>();
		// Fill map.
		//// Left spawn points.
		Pair spawn1 = new Pair(14, 98);
		result.put(spawn1, 0f);

		Pair spawn2 = new Pair(14, 90);
		result.put(spawn2, 0f);

		//// Bottom spawn points.
		Pair spawn3 = new Pair(50, 70);
		result.put(spawn3, (float) Math.PI/2);

		Pair spawn4 = new Pair(57, 70);
		result.put(spawn4, (float) Math.PI/2);

		Pair spawn5 = new Pair(84, 75);
		result.put(spawn5, (float) Math.PI/2);

		//// Right spawn points.
		Pair spawn6 = new Pair(121, 98);
		result.put(spawn6, (float) Math.PI);

		Pair spawn7 = new Pair(121, 90);
		result.put(spawn7, (float) Math.PI);

		//// Top spawn points.
		Pair spawn8 = new Pair(50, 148);
		result.put(spawn8, (float) Math.PI * 1.5f);

		Pair spawn9 = new Pair(58, 148);
		result.put(spawn9, (float) Math.PI * 1.5f);

		Pair spawn10 = new Pair(112, 148);
		result.put(spawn10, (float) Math.PI * 1.5f);
		return result;
	}


	/**
	 * Creates a new GameController that controls the physics of a certain GameModel.
	 *
	 */
	private GameController() {
	    startNewGame();
	}

	/**
	 * Returns a singleton instance of a game controller
	 *
	 * @return the singleton instance
	 */
	public static GameController getInstance() {
		if (instance == null)
			instance = new GameController();
		return instance;
	}

	/**
	 * Calculates the next game update of duration delta (in seconds).
	 *
	 * @param delta The time since the last update.
	 */
	public void update(float delta) {

		if (!(timeToNextShot < 0))
			timeToNextShot -= delta;

		if (!(timeToNextState < 0))
			timeToNextState -= delta;

		if(Gdx.app != null && Gdx.app.getType() == Application.ApplicationType.Android && motionDetectorOn)
			md.update(delta);

		float frameTime = Math.min(delta, 0.25f);
		accumulator += frameTime;
		while (accumulator >= 1/60f) {
			world.step(1/60f, 6, 2);
			accumulator -= 1/60f;
		}

		Array<Body> bodies = new Array<Body>();
		world.getBodies(bodies);

		for (Body body : bodies) {
			if(! (body.getUserData() instanceof EntityModel) ) continue;

			((EntityModel) body.getUserData()).setPosition(body.getPosition().x, body.getPosition().y);
			((EntityModel) body.getUserData()).setRotation(body.getAngle());

			if(body.getUserData() instanceof PlayerModel){
				PlayerModel playerModel = (PlayerModel) body.getUserData();
				if (playerModel.getHealth() <= 0)
					playerModel.die();
			}


			if(body.getUserData() instanceof ZombieModel)
			{
				// Fazer calculo com base no corpo do zombie model.
				ZombieModel zombie = (ZombieModel) body.getUserData();

				if (zombie.getHealth() < 0)//zombie dies
				{
					BloodSpillModel spill = GameModel.getInstance().getBloodSpill();
					spill.setPosition(zombie.getX(), zombie.getY());
					spill.setZombieSize(zombie.getSize());
					zombie.goToHell();
					zombies_killed++;
					continue;
				}
				float xPlayer = selfPlayerBody.getX();
				float yPlayer = selfPlayerBody.getY();

				zombie.calculateNewAngle(xPlayer, yPlayer);

				body.setTransform(body.getPosition(), zombie.getRotation());

				float velZombie = zombie.getVel();

				// If player is alive make the zombie go to him.
				if(!GameModel.getInstance().getSelfPlayer().isDead())
					body.setLinearVelocity(velZombie * (float) Math.cos(zombie.getRotation()),velZombie * (float) Math.sin(zombie.getRotation()));
				// Otherwise make the zombie stay static.
				else
					body.setLinearVelocity(0, 0);

                zombie.updateTimeToNextAttack(delta);

				Vector2 knocBack_vec = zombie.get_knockBack_vector(delta);

				if(knocBack_vec != null)
				{
					Vector2 curVelocity = body.getLinearVelocity();
					curVelocity.add(knocBack_vec);
					body.setLinearVelocity(curVelocity);
				}

			}
		}

		if(timeToNextState < 0){
			PlayerModel player = GameModel.getInstance().getSelfPlayer();
			selfPlayerBody.disableKnifeHitFixture();
			if((player.getCurrentState() == PlayerModel.States.SHOOT || player.getCurrentState() == PlayerModel.States.RELOAD)){

				/*ATUALIZAR O HUD!*/
				if (player.getCurrentState() == PlayerModel.States.RELOAD){
					if (player.getCurrentWeapon() == PlayerModel.Weapons.RIFLE)
						player.replenishRifleAmmo();
					else if(player.getCurrentWeapon() == PlayerModel.Weapons.PISTOL)
						player.replenishPistolAmmo();
				}

				GameModel.getInstance().getSelfPlayer().setCurrentState(PlayerModel.States.IDLE);
				GameModel.getInstance().getSelfPlayer().resetStateTime();
				timeToNextState = Float.MAX_VALUE;
			}
		}

		PlayerModel player = GameModel.getInstance().getSelfPlayer();
		if (player.getCurrentState() == PlayerModel.States.SHOOT
				&& player.getCurrentWeapon() == PlayerModel.Weapons.KNIFE)
		{
			// Enable knife hitting if the animation has started a while ago.
			if(timeToNextState <= TIME_BETWEEN_SHOTS_KNIFE * 0.6)
			{
				selfPlayerBody.enableKnifeHitFixture();
			}
		}

		// Regenerate player health.
		GameModel.getInstance().getSelfPlayer().regenerateHealth(delta);

		// Update blood spills.
		List<BloodSpillModel> spills = GameModel.getInstance().getBloodSpills();
		List<BloodSpillModel> bloodSpillsToRemove = new ArrayList<BloodSpillModel>();
		for(BloodSpillModel spill : spills)
		{
			spill.updateSpillTime(delta);
			if(spill.isFlaggedToBeRemoved()) bloodSpillsToRemove.add(spill);
		}
		// Remove blood spills no longer needed.
		for(BloodSpillModel spill : bloodSpillsToRemove)
		{
			GameModel.getInstance().remove(spill);
		}
	}

	/**
	 * Processes current round information.
	 */
	public void handleRounds(GameView.States currentState)
	{
		motionDetectorOn = false;
		/** THE VIEW IS DISPLAYING THE GAME AND NOT ANY START MENU / OVERLAY. */
		if(currentState == GameView.States.GAME_RUNNING)
		{
			motionDetectorOn = true;
			// Set zombie count at the start of the round.
			if(zombiesLeftToSpawn == Integer.MAX_VALUE)
			{
				zombiesLeftToSpawn = getNumberOfZombiesThisRound();
			}
			if(zombiesLeftToSpawn > 0)
			{
				for(Pair<Integer, Integer> p : ZOMBIE_SPAWNS.keySet())
				{
					if(zombiesLeftToSpawn == 0) break;
					// Check if spawn point is free
					if(isSpawnPointFree(p))
					{
						// Spawn zombie of random size.
						int random = (int) (Math.random() * 3f);
						ZombieModel newZombie;
						if(random == 0) // Small zombie
						{
							newZombie = new ZombieModel(p.getKey(),p.getValue(), ZOMBIE_SPAWNS.get(p), ZombieModel.Sizes.SMALL);
							SmallZombieBody newZombieBody = new SmallZombieBody(world, newZombie);
						}
						else if(random == 1) // Medium zombie
						{
							newZombie = new ZombieModel(p.getKey(),p.getValue(), ZOMBIE_SPAWNS.get(p), ZombieModel.Sizes.MEDIUM);
							MediumZombieBody newZombieBody = new MediumZombieBody(world, newZombie);
						}
						else // Big zombie (random == 2)
						{
							newZombie = new ZombieModel(p.getKey(),p.getValue(), ZOMBIE_SPAWNS.get(p), ZombieModel.Sizes.BIG);
							BigZombieBody newZombieBody = new BigZombieBody(world, newZombie);
						}
						GameModel.getInstance().addZombie(newZombie);
						zombiesLeftToSpawn--;
					}
				}
			}
		}
	}

	/**
	 * Checks if a given point is not colliding with any box2d zombie or player body.
	 * @param point The point which will be checked for collisions.
	 */
	private boolean isSpawnPointFree(Pair<Integer, Integer> point)
	{
		Array<Body> bodies = new Array<Body>();
		Vector2 point_vector = new Vector2(point.getKey(), point.getValue());
		world.getBodies(bodies);
		for (Body body : bodies ) {
			if ( body.getUserData() instanceof EntityModel ) {
				Vector2 body_pos = body.getPosition();
				if(body_pos.dst2(point_vector) < 36)
				{
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Returns the world controlled by this controller. Needed for debugging purposes only.
	 *
	 * @return The world controlled by this controller.
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * Processes this player movement.
	 *
	 * @param movementMask A bit mask which represents the direction the player is going.
	 * @param dt time since last time inputs where handled in seconds
	 * @param camera the camera being used in the gameview class for the libgdx screen.
	 */
	public void processMovement(short movementMask, float dt, OrthographicCamera camera) {
		PlayerModel selfPlayerModel = GameModel.getInstance().getSelfPlayer();
		selfPlayerBody.setLinearVelocity(0, 0);    // Make the player stand still.

		// Calculate new linear velocity for the player.
		float newVel_X = 0, newVel_Y = 0;
		if ((movementMask & PLAYER_MOVE_LEFT) != 0)
			newVel_X -= MAX_LINEAR_VEL_PLAYER;
		if ((movementMask & PLAYER_MOVE_RIGHT) != 0)
			newVel_X += MAX_LINEAR_VEL_PLAYER;
		if ((movementMask & PLAYER_MOVE_UP) != 0)
			newVel_Y += MAX_LINEAR_VEL_PLAYER;
		if ((movementMask & PLAYER_MOVE_DOWN) != 0)
			newVel_Y -= MAX_LINEAR_VEL_PLAYER;

		// Normalize speed.
		float vec_mov_modulus = (float) Math.sqrt((float) (newVel_X * newVel_X + newVel_Y * newVel_Y));
		if (vec_mov_modulus != 0) {
			newVel_X = MAX_LINEAR_VEL_PLAYER / vec_mov_modulus * newVel_X;
			newVel_Y = MAX_LINEAR_VEL_PLAYER / vec_mov_modulus * newVel_Y;
			// Set new speed.
			selfPlayerBody.setLinearVelocity(newVel_X, newVel_Y);
			// Change state if the player was idle.
			if (selfPlayerModel.getCurrentState() == PlayerModel.States.IDLE) {
				selfPlayerModel.setCurrentState(PlayerModel.States.MOVE);
				selfPlayerModel.resetStateTime();
			}
			selfPlayerModel.increaseMoveStateTime(dt);
			selfPlayerModel.doFootstepSound();
		}
		// Change state if player went from walking to standing still
		if (vec_mov_modulus == 0){
			selfPlayerModel.stopFootstepSound();
			if (selfPlayerModel.getCurrentState() == PlayerModel.States.MOVE) {
				selfPlayerModel.setCurrentState(PlayerModel.States.IDLE);
				selfPlayerModel.resetStateTime();
			}
		}

		// Apply knockback.
		Vector2 knocBack_vec = selfPlayerModel.get_knockBack_vector(dt);

		if(knocBack_vec != null)
		{
			Vector2 curVelocity = new Vector2(newVel_X, newVel_Y);
			curVelocity.add(knocBack_vec);
			selfPlayerBody.setLinearVelocity(curVelocity.x, curVelocity.y);
		}


		// Calculate relative gun position to the player center.
		Vector2 gunPos = selfPlayerBody.getWeaponFiringPosition();
		// Make the player body be pointed towards the crosshair.
		Vector3 screen_coords = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
		Vector3 world_coords = camera.unproject(screen_coords);
		world_coords.x = (float)world_coords.x * GameView.PIXEL_TO_METER;//
		world_coords.y = (float)world_coords.y * GameView.PIXEL_TO_METER;//

		// Band-Aid over here! If we assume player position = weapon position then
		// the player will rotate infinitely if the crosshair is near the player.
		// The strategy is to calculate the distance between the crosshair and the weapon position
		// and apply a linear function to it.
		float gunPos_weight;
		float distance = world_coords.dst2(gunPos.x, gunPos.y, 0);

		final float upper_bound = 15;
		final float lower_bound = 9;

		if(distance > upper_bound)
			gunPos_weight = 1;
		else if (distance < lower_bound)
			gunPos_weight = 0;
		else
		{
			gunPos_weight = (distance - lower_bound) / (upper_bound - lower_bound);
		}

		// Transform GunsPos coordinates to relative ones.
		gunPos.x -= selfPlayerBody.getX();
		gunPos.y -= selfPlayerBody.getY();

		float 	player_x 	= 	selfPlayerModel.getX() + gunPos.x * gunPos_weight,
				player_y 	= 	selfPlayerModel.getY() + gunPos.y * gunPos_weight;
		float angle = (float) Math.atan2(world_coords.y - player_y , world_coords.x - player_x);
		if (Gdx.app.getType() != Application.ApplicationType.Android)
			selfPlayerBody.setAngle(angle);

		// Remove momentum.
		selfPlayerBody.setAngularVelocity(0);
	}

	public PlayerBody getSelfPlayerBody() {
		return selfPlayerBody;
	}

	/**
	 * Switches player weapon to the specified weapon.
	 * @param weapon weapon the player will hold.
	 */
	public void switchToWeapon(PlayerModel.Weapons weapon){
		PlayerModel selfPlayerModel = GameModel.getInstance().getSelfPlayer();
		PlayerModel.Weapons currentWeapon = selfPlayerModel.getCurrentWeapon();
		if(weapon == currentWeapon) return; // Do nothing if player is already holding this weapon
		else if (Gdx.app != null){
			selfPlayerModel.doDrawSound();
		}
		selfPlayerModel.setCurrentWeapon(weapon);
		selfPlayerModel.resetStateTime();
		selfPlayerModel.setCurrentState(PlayerModel.States.IDLE);

	}

	/**
	 * Switches player weapon to the specified weapon.
	 * @param next if true switch to the next weapon otherwise switch to the previous weapon
	 */
	public void switchToWeapon(boolean next){
		PlayerModel selfPlayerModel = GameModel.getInstance().getSelfPlayer();
		PlayerModel.Weapons currentWeapon = selfPlayerModel.getCurrentWeapon();



		switch (currentWeapon){
			case RIFLE:
				if (next){
                    selfPlayerModel.setCurrentWeapon(PlayerModel.Weapons.PISTOL);
                }
				else{
                    selfPlayerModel.setCurrentWeapon(PlayerModel.Weapons.KNIFE);
                }
				break;
			case KNIFE:
				if (next){
                    selfPlayerModel.setCurrentWeapon(PlayerModel.Weapons.RIFLE);
                }
				else{
                    selfPlayerModel.setCurrentWeapon(PlayerModel.Weapons.PISTOL);
                }
				break;
			case PISTOL:
				if (next){
                    selfPlayerModel.setCurrentWeapon(PlayerModel.Weapons.KNIFE);
                }
				else{
                    selfPlayerModel.setCurrentWeapon(PlayerModel.Weapons.RIFLE);
				}
				break;

		}


		if(Gdx.app != null) selfPlayerModel.doDrawSound();
		selfPlayerModel.resetStateTime();
		selfPlayerModel.setCurrentState(PlayerModel.States.IDLE);
	}

	public void reload() {
		PlayerModel selfPlayerModel = GameModel.getInstance().getSelfPlayer();
		if(selfPlayerModel.getCurrentState() == PlayerModel.States.RELOAD)
			return;
		if((selfPlayerModel.getPistolCurrentAmmo() != MAX_PISTOL_AMMO && selfPlayerModel.getCurrentWeapon() == PlayerModel.Weapons.PISTOL) ||
				(selfPlayerModel.getRifleCurrentAmmo() != MAX_RIFLE_AMMO && selfPlayerModel.getCurrentWeapon() == PlayerModel.Weapons.RIFLE)){
			selfPlayerModel.setCurrentState(PlayerModel.States.RELOAD);
			selfPlayerModel.resetStateTime();
			timeToNextState = DEFAULT_TIME_TO_NEXT_STATE_RELOAD;
			if(Gdx.app != null)
				selfPlayerModel.doReloadSound();
		}

	}

	/**
	 * Makes the player shoot a bullet if he is able to.
	 */
	public void shoot() {

		PlayerModel player = GameModel.getInstance().getSelfPlayer();

		if(player.getCurrentState() == PlayerModel.States.RELOAD)
			return;

		if (timeToNextShot < 0) {


			// Make sure the player has ammo.
			if(player.getCurrentWeapon() == PlayerModel.Weapons.RIFLE)
				if(player.getRifleCurrentAmmo() == 0)
					return;

			if(player.getCurrentWeapon() == PlayerModel.Weapons.PISTOL)
				if(player.getPistolCurrentAmmo() == 0)
					return;

			if(player.getCurrentWeapon() != PlayerModel.Weapons.KNIFE){
				// Get bullet from pools.
				BulletModel bullet = GameModel.getInstance().getBullet(player.getCurrentWeapon());
				// Set bullet position & angle.
				Vector2 weaponPosition = selfPlayerBody.getWeaponFiringPosition();
				bullet.setPosition(weaponPosition.x, weaponPosition.y);
				bullet.setRotation(selfPlayerBody.getAngle() - (float)Math.PI/2);

				BulletBody body = new BulletBody(world, bullet);
				body.setLinearVelocity(BULLET_SPEED);
			}

			if(player.getCurrentWeapon() == PlayerModel.Weapons.RIFLE){
				timeToNextShot = TIME_BETWEEN_SHOTS_RIFLE;
				timeToNextState = DEFAULT_TIME_TO_NEXT_STATE_RIFLE;
				player.fireRifle();
			}
			else if(player.getCurrentWeapon() == PlayerModel.Weapons.PISTOL){
				timeToNextShot = TIME_BETWEEN_SHOTS_PISTOL;
				timeToNextState = DEFAULT_TIME_TO_NEXT_STATE_PISTOL;
				player.firePistol();
			}
			else if(player.getCurrentWeapon() == PlayerModel.Weapons.KNIFE){
				timeToNextShot = TIME_BETWEEN_SHOTS_KNIFE;
				timeToNextState = DEFAULT_TIME_TO_NEXT_STATE_KNIFE;
			}

			player.setCurrentState(PlayerModel.States.SHOOT);
			player.resetStateTime();

			player.doShotSound();
		}

	}

	public void removeFlagged() {
		Array<Body> bodies = new Array<Body>();
		world.getBodies(bodies);
		for (Body body : bodies) {
			if ( body.getUserData() instanceof EntityModel && ((EntityModel)body.getUserData()).isFlaggedToBeRemoved()) {
				GameModel.getInstance().remove((EntityModel) body.getUserData());
				world.destroyBody(body);
			}
		}
	}

	@Override
	public void beginContact(Contact contact) {
		// Knife Sensor touched
		if(contact.getFixtureA().isSensor() || contact.getFixtureB().isSensor())
		{
			ZombieModel zombieModel;
			PlayerModel playerModel;

			contact.getFixtureA().getBody().setAngularVelocity(0);
			contact.getFixtureB().getBody().setAngularVelocity(0);

			if(contact.getFixtureA().isSensor())
			{
				playerModel = (PlayerModel) contact.getFixtureA().getBody().getUserData();
				zombieModel = (ZombieModel) contact.getFixtureB().getBody().getUserData();
			}
			else
			{
				playerModel = (PlayerModel) contact.getFixtureB().getBody().getUserData();
				zombieModel = (ZombieModel) contact.getFixtureA().getBody().getUserData();
			}

			// Is the player holding the knife and is he attacking with it?
			if(playerModel.getCurrentWeapon() == PlayerModel.Weapons.KNIFE &&
					playerModel.getCurrentState() == PlayerModel.States.SHOOT)
			{
				// Dont hit the zombie if the animation if nearly done.
				if(timeToNextState >= TIME_BETWEEN_SHOTS_KNIFE * 0.3)
				{
					zombieModel.setKnockBack(10, new Vector2(1, 0).rotateRad(playerModel.getRotation()));
					zombieModel.takeDamage(PlayerModel.DEFAULT_DAMAGE_KNIFE);;
				}
			}
		}
	}

    /**
     * A bullet colided with something. Lets remove it.
     *
     * @param bulletBody the bullet that colided
     */
    private void bulletCollision(Body bulletBody) {
        ((BulletModel)bulletBody.getUserData()).setFlaggedForRemoval(true);
    }

    /**
     * A bullet collided with an zombie.
     * @param bulletBody the bullet that collided
     * @param zombieBody the zombie that collided
     */
    private void bulletZombieCollision(Body bulletBody, Body zombieBody) {
        ZombieModel zombieModel = (ZombieModel) zombieBody.getUserData();
		BulletModel bulletModel = (BulletModel) bulletBody.getUserData();

		/*Vector2 test = zombieBody.getPosition();
		zombieBody.getPosition().x += (float) (zombieModel.getX() + 200*Math.cos(zombieModel.getRotation()));
		zombieBody.getPosition().y += (float) (zombieModel.getY() + 200*Math.sin(zombieModel.getRotation()));*/
		//zombieModel.setPosition((float) (zombieModel.getX() + 200*Math.cos(zombieModel.getRotation())), (float) (zombieModel.getY() + 200*Math.sin(zombieModel.getRotation())));

        /* TIRAR VIDA AO ZOMBIE_MEDIUM! */
		zombieModel.takeDamage(bulletModel.getDamageToGive());
		//Gdx.app.log("bulletBody.getAngle()", "" + bulletBody.getAngle());
		zombieModel.setKnockBack(9, new Vector2(1,0).rotateRad(bulletBody.getAngle() + (float) Math.PI/2));
		zombieBody.setAngularVelocity(0);
    }

    /**
     * the player collided with an zombie (attacking).
     * @param playerBody the player that collided
     * @param zombieBody the zombie that collided
     */
    private void playerZombieCollision(Body playerBody, Body zombieBody) {
		ZombieModel zombieModel = (ZombieModel) zombieBody.getUserData();
		zombieModel.setCurrentState(ZombieModel.States.ATTACK);
		PlayerModel player = GameModel.getInstance().getSelfPlayer();

        if (zombieModel.getTimeToNextAttack() < 0){
			player.takeDamage(zombieModel.getDamageToGive());
			zombieModel.setNewTimeToNextAttack();
			if(!player.isDead()) {
				if(zombieModel.getSize() == ZombieModel.Sizes.SMALL)
					player.setKnockBack(10, new Vector2(1, 0).rotateRad(zombieBody.getAngle()));
				else if(zombieModel.getSize() == ZombieModel.Sizes.MEDIUM)
					player.setKnockBack(12, new Vector2(1, 0).rotateRad(zombieBody.getAngle()));
				else if(zombieModel.getSize() == ZombieModel.Sizes.BIG)
					player.setKnockBack(14, new Vector2(1, 0).rotateRad(zombieBody.getAngle()));
			}
		}

		if (player.getCurrentState() == PlayerModel.States.SHOOT){
			//zombieModel.takeDamage(player.getKnifeDamage());
			//timeToNextShot = DEFAULT_TIME_TO_NEXT_STATE_KNIFE;
			//Gdx.app.log("Zombie Health", "" + zombieModel.getHealth());
		}
		zombieBody.setAngularVelocity(0);
    }

	@Override
	public void endContact(Contact contact) {
		contact.getFixtureA().getBody().setAngularVelocity(0);
		contact.getFixtureB().getBody().setAngularVelocity(0);
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

		Body bodyA = contact.getFixtureA().getBody();
		Body bodyB = contact.getFixtureB().getBody();

		if (bodyA.getUserData() instanceof BulletModel)
			bulletCollision(bodyA);
		if (bodyB.getUserData() instanceof BulletModel)
			bulletCollision(bodyB);

		if (bodyA.getUserData() instanceof BulletModel && bodyB.getUserData() instanceof ZombieModel)
			bulletZombieCollision(bodyA, bodyB);
		if (bodyA.getUserData() instanceof ZombieModel && bodyB.getUserData() instanceof BulletModel)
			bulletZombieCollision(bodyB, bodyA);
		if (bodyA.getUserData() instanceof PlayerModel && bodyB.getUserData() instanceof ZombieModel)
			playerZombieCollision(bodyA, bodyB);
		if (bodyA.getUserData() instanceof ZombieModel && bodyB.getUserData() instanceof PlayerModel)
			playerZombieCollision(bodyB, bodyA);
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}

	/**
	 * Starts a new game.
	 */
	public void startNewGame()
	{
		// Create world if it doesn't exist yet.
		if(world == null) world = new World(new Vector2(0, 0), true);

		// Delete all bodies from the world.
		Array<Body> bodies = new Array<Body>();
		world.getBodies(bodies);
		for(int i = 0; i < bodies.size; i++)
		{
			if(!world.isLocked() && bodies.get(i).getUserData() instanceof EntityModel)
				world.destroyBody(bodies.get(i));
		}
		// Do a initial world step to process all the body deletions.
		world.step(1/60f, 6, 2);

		// Signal the model that a new game will begin.
		GameModel.getInstance().startNewGame();

		List<PlayerModel> players = GameModel.getInstance().getPlayers();
		selfPlayerBody = new PlayerBody(world, players.get(0));

		List<ZombieModel> zombies = GameModel.getInstance().getZombies();
		for(ZombieModel zombie : zombies) {
			if (zombie.getSize() == ZombieModel.Sizes.MEDIUM) {
				new MediumZombieBody(world, zombie);
			}
			else if (zombie.getSize() == ZombieModel.Sizes.SMALL) {
				new SmallZombieBody(world, zombie);
			}
			else if (zombie.getSize() == ZombieModel.Sizes.BIG){
				new BigZombieBody(world, zombie);
			}
		}

		List<BulletModel> bullets = GameModel.getInstance().getBullets();
		for(BulletModel bullet : bullets) {
			new BulletBody(world, bullet);
		}

		world.setContactListener(this);

		// Initialize round info.
		currentRound = 1;
		this.zombiesLeftToSpawn = Integer.MAX_VALUE;
		zombies_killed = 0;

		if(Gdx.app != null && Gdx.app.getType() == Application.ApplicationType.Android)
			md = new MotionDetector();
	}

	/**
	 * Changes the game difficulty.
	 * @param difficulty the new difficulty.
	 */
	public void setGameDifficulty(Difficulty difficulty)
	{
		this.currentDifficulty = difficulty;
	}

	public void makeBodiesStill()
	{
		Array<Body> bodies = new Array<Body>();
		world.getBodies(bodies);

		for (Body body : bodies) {
			body.setLinearVelocity(0, 0);
			body.setType(BodyDef.BodyType.StaticBody);

		}
	}

	/**
	 * Returns the current round number.
	 * @return current round number.
	 */
	public int getCurrentRound()
	{
		return currentRound;
	}

	/**
	 * Returns the total number of zombies that will spawn this round.
	 * @return Number of zombies that will spawn this round.
	 */
	public int getNumberOfZombiesThisRound()
	{
		if(currentDifficulty == Difficulty.EASY)
			return currentRound * zombies_per_round_easy;
		else if(currentDifficulty == Difficulty.NORMAL)
			return currentRound * zombies_per_round_normal;
		else if(currentDifficulty == Difficulty.HARD)
			return currentRound * zombies_per_round_hard;
		else
			return 0;
	}

	/**
	 * Returns the number of zombies tha thte player has killed this game.
	 * @return the kill count by the player.
	 */
	public int getNrZombiedKilled(){
		return zombies_killed;
	}

	/**
	 * Starts a new round.
	 */
	public void startNewRound(){
		currentRound++;
		zombiesLeftToSpawn = getNumberOfZombiesThisRound();
	}
}
