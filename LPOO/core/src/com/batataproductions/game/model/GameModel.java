package com.batataproductions.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Pool;
import com.batataproductions.game.model.entities.BloodSpillModel;
import com.batataproductions.game.model.entities.BulletModel;
import com.batataproductions.game.model.entities.EntityModel;
import com.batataproductions.game.model.entities.PlayerModel;
import com.batataproductions.game.model.entities.ZombieModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A model representing a game.
 */


public class GameModel {
	/**
	 * The singleton instance of the game model
	 */
	private static GameModel instance;

	/**
	 * Asset manager of the game.
	 */
	private AssetManager assetManager;

	/**
	 * The players currently on the server.
	 */
	private List<PlayerModel> players;

	/**
	 * The Zombies currently on the server.
	 */
	private List<ZombieModel> zombies;

	/**
	 * The Bullets currently on the server.
	 */
	private List<BulletModel> bullets;

	/**
	 * The Blood Spills currently on the server.
	 */
	private List<BloodSpillModel> bloodSpills;

	/**
	 * A pool of blood spills.
	 */
	Pool<BloodSpillModel> bloodSpillPool = new Pool<BloodSpillModel>() {
		@Override
		protected BloodSpillModel newObject() {
			return new BloodSpillModel(0, 0, ZombieModel.Sizes.MEDIUM);
		}
	};

	/**
	 * A pool of bullets
	 */
	Pool<BulletModel> bulletPool = new Pool<BulletModel>() {
		@Override
		protected BulletModel newObject() {
			return new BulletModel(0, 0, 0);
		}
	};

	/**
	 * Our player model.
	 */
	private PlayerModel selfPlayer;

	/**
	 * Returns a singleton instance of the game model
	 *
	 * @return the singleton instance
	 */
	public static GameModel getInstance() {
		if (instance == null)
			instance = new GameModel();
		return instance;
	}

	/**
	 * Constructs a new game model.
	 */
	private GameModel() {

		startNewGame();
	}


	/**
	 * Returns the players on the server.
	 *
	 * @return the player list
	 */
	public List<PlayerModel> getPlayers() {
		return players;
	}

	/**
	 * Returns the zombies on the server.
	 *
	 * @return the zombie list
	 */
	public List<ZombieModel> getZombies() {
		return zombies;
	}

	/**
	 * Returns the bullets on the server.
	 *
	 * @return the bullets list
	 */
	public List<BulletModel> getBullets() {
		return bullets;
	}

	/**
	 * Returns the blood spills on the server.
	 *
	 * @return the spills list
	 */
	public List<BloodSpillModel> getBloodSpills() {
		return bloodSpills;
	}

	/**
	 * Adds a new player to the model
	 *
	 * @param playerModel the player model to be added
	 */
	public void addPlayer(PlayerModel playerModel) {
		players.add(playerModel);
	}

	/**
	 * Adds a new zombie to the model
	 *
	 * @param zombieModel the zombie model to be added
	 */
	public void addZombie(ZombieModel zombieModel) {
		zombies.add(zombieModel);
	}

	/**
	 * Adds a new bullet to the model
	 *
	 * @param bulletModel the bullet model to be added
	 */
	public void addBullet(BulletModel bulletModel) {
		bullets.add(bulletModel);
	}

	/**
	 * Adds a new blood spill to the model
	 *
	 * @param bloodSpillModel the bullet model to be added
	 */
	public void addBloodSpill(BloodSpillModel bloodSpillModel) {
		bloodSpills.add(bloodSpillModel);
	}

	/**
	 * Returns a BulletModel object from the bullet pool. Adds the returned BulletModel to the bullet array.
	 * @return A unique BulletModel object.
	 */
	public BulletModel getBullet(PlayerModel.Weapons weapon) {
		BulletModel bullet = bulletPool.obtain();
		bullet.setGun(weapon);
		addBullet(bullet);
		return bullet;
	}

	/**
	 * Returns a BloodSpill object from the bullet pool. Adds the returned BloodSpillModel to the blood spill array.
	 * @return A unique BloodSpillModel object.
	 */
	public BloodSpillModel getBloodSpill() {
		BloodSpillModel spill = bloodSpillPool.obtain();
		addBloodSpill(spill);
		return spill;
	}

	/**
	 * Returns the player of this game aka the local player. This function is relevant because this is a multi-player game.
	 * @return The playermodel of the user playing this game.
	 */
	public PlayerModel getSelfPlayer() { return selfPlayer; }

	/**
	 * Removes a model from this game.
	 *
	 * @param model the model to be removed
	 */
	public void remove(EntityModel model) {
		if (model instanceof PlayerModel) {
			players.remove(model);
		}
		if (model instanceof ZombieModel) {
			zombies.remove(model);
		}
		if (model instanceof BulletModel) {
			bullets.remove(model);
			bulletPool.free((BulletModel) model);
		}
		if (model instanceof BloodSpillModel) {
			bloodSpills.remove(model);
			bloodSpillPool.free((BloodSpillModel) model);
		}
	}

	/**
	 * Starts a new game.
	 */
	public void startNewGame()
	{
		if(players == null) players = new ArrayList<PlayerModel>();
		if(zombies == null) zombies = new ArrayList<ZombieModel>();
		if(bullets == null) bullets = new ArrayList<BulletModel>();
		if(bloodSpills == null) bloodSpills = new ArrayList<BloodSpillModel>();

		players.clear();
		zombies.clear();
		bullets.clear();
		bloodSpills.clear();

		bloodSpillPool.clear();
		bulletPool.clear();

		PlayerModel player = new PlayerModel(50, 100, 0);

		selfPlayer = player;	// Local player.
		addPlayer(player);

		//ZombieModel zombieTest3 = new ZombieModel(50,105,0, ZombieModel.Sizes.MEDIUM);
		//addZombie(zombieTest3);
	}

	/**
	 * Sets the game asset manager (used to load cached sounds).
	 * @param a the game asset manager.
	 */
	public void setAssetManager(AssetManager a)
	{
		assetManager = a;
		getSelfPlayer().createSounds(assetManager);
		ZombieModel.createSounds(assetManager);
	}
}
