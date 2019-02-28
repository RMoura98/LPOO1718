package com.batataproductions.game.view.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.batataproductions.game.DeathmatchMania;
import com.batataproductions.game.model.entities.EntityModel;
import com.batataproductions.game.model.entities.PlayerModel;

import static com.batataproductions.game.view.GameView.PIXEL_TO_METER;

/**
 * A view representing a player.
 */
public class PlayerView extends EntityView {

	/** The time between the animation frames for the rifle idle animation. */
	private static final float FRAME_TIME_IDLE_RIFLE = 1/13f;

	/** The time between the animation frames for the rifle walk animation. */
	private static final float FRAME_TIME_WALK_RIFLE = 1/20f;

	/** The time between the animation frames for the rifle shoot animation. */
	private static final float FRAME_TIME_SHOOT_RIFLE = 1/20f;

	/** The time between the animation frames for the rifle reload animation. */
	private static final float FRAME_TIME_RELOAD_RIFLE = 1/20f;

	/** The time between the animation frames for the pistol idle animation. */
	private static final float FRAME_TIME_IDLE_PISTOL = 1/13f;

	/** The time between the animation frames for the pistol walk animation. */
	private static final float FRAME_TIME_WALK_PISTOL = 1/20f;

	/** The time between the animation frames for the pistol shoot animation. */
	private static final float FRAME_TIME_SHOOT_PISTOL = 1/10f;

	/** The time between the animation frames for the pistol shoot animation. */
	private static final float FRAME_TIME_RELOAD_PISTOL = 1/20f;

	/** The time between the animation frames for the knife idle animation. */
	private static final float FRAME_TIME_IDLE_KNIFE = 1/13f;

	/** The time between the animation frames for the knife walk animation. */
	private static final float FRAME_TIME_WALK_KNIFE = 1/20f;

	/** The time between the animation frames for the knife shoot animation. */
	private static final float FRAME_TIME_SHOOT_KNIFE = 1/20f;

	/**
	 * Sprite scale to be applied before drawing.
	 */
	private float SCALE = 0.5f;

	/**
	 * The animation used when the player is still holding his rifle.
	 */
	private Animation<TextureRegion> idleRifleAnimation;

	/**
	 * The animation used when the player is walking holding his rifle.
	 */
	private Animation<TextureRegion> walkRifleAnimation;

	/**
	 * The animation used when the player is shooting his rifle.
	 */
	private Animation<TextureRegion> shootRifleAnimation;

	/**
	 * The animation used when the player is reloading his rifle.
	 */
	private Animation<TextureRegion> reloadRifleAnimation;

	/**
	 * The animation used when the player is still holding his pistol.
	 */
	private Animation<TextureRegion> idlePistolAnimation;

	/**
	 * The animation used when the player is walking holding his pistol.
	 */
	private Animation<TextureRegion> walkPistolAnimation;

	/**
	 * The animation used when the player is shooting his pistol.
	 */
	private Animation<TextureRegion> shootPistolAnimation;

    /**
     * The animation used when the player is reloading his pistol.
     */
    private Animation<TextureRegion> reloadPistolAnimation;

	/**
	 * The animation used when the player is still holding his knife.
	 */
	private Animation<TextureRegion> idleKnifeAnimation;

	/**
	 * The animation used when the player is walking holding his knife.
	 */
	private Animation<TextureRegion> walkKnifeAnimation;

	/**
	 * The animation used when the player is using his knife.
	 */
	private Animation<TextureRegion> shootKnifeAnimation;

	/**
	 * The animation used when for the player's feet when he is standing still.
	 */
	private Animation<TextureRegion> idleFeetAnimation;

	/**
	 * The animation used when for the player's feet when he is standing walking.
	 */
	private Animation<TextureRegion> walkFeetAnimation;

	/**
	 * The sprite representing the player's feet.
	 */
	private Sprite spriteFeet;

	/**
	 * Constructs a space ship model.
	 *
	 * @param game the game this view belongs to. Needed to access the
	 *             asset manager to get textures.
	 */
	public PlayerView(DeathmatchMania game) {
		super(game);
	}

	/**
	 * Creates a sprite representing this player.
	 *
	 * @param game the game this view belongs to. Needed to access the
	 *             asset manager to get textures.
	 * @return the sprite representing this player
	 */
	@Override
	public Sprite createSprite(DeathmatchMania game) {
		idleRifleAnimation = createIdleRifleAnimation(game);
		walkRifleAnimation = createWalkRifleAnimation(game);
		shootRifleAnimation = createShootRifleAnimation(game);
		reloadRifleAnimation = createReloadRifleAnimation(game);
		idlePistolAnimation = createIdlePistolAnimation(game);
		walkPistolAnimation = createWalkPistolAnimation(game);
		shootPistolAnimation = createShootPistolAnimation(game);
        reloadPistolAnimation = createReloadPistolAnimation(game);
		idleKnifeAnimation = createIdleKnifeAnimation(game);
		walkKnifeAnimation = createWalkKnifeAnimation(game);
		shootKnifeAnimation = createShootKnifeAnimation(game);
		spriteFeet = createFeetSprite(game); // Initialize feet sprite.
		return new Sprite(idleRifleAnimation.getKeyFrame(0, true));
	}

	/**
	 * Creates a sprite representing this player feet.
	 *
	 * @param game the game this view belongs to. Needed to access the
	 *             asset manager to get textures.
	 * @return the sprite representing the player feet
	 */
	public Sprite createFeetSprite(DeathmatchMania game){
		idleFeetAnimation = createIdleFeetAnimation(game);
		walkFeetAnimation = createWalkFeetAnimation(game);
		return new Sprite(idleFeetAnimation.getKeyFrame(0, true));
	}

	/**
	 * Creates the animation used when the player is standing still holding his rifle.
	 *
	 * @param game the game this view belongs to. Needed to access the
	 *             asset manager to get textures.
	 * @return the animation used when the player is idle with his rifle
	 */
	private Animation<TextureRegion> createIdleRifleAnimation(DeathmatchMania game) {
		TextureAtlas textureAtlas = game.getAssetManager().get("playerAtlas.atlas");

		return new Animation(FRAME_TIME_IDLE_RIFLE, textureAtlas.findRegions("survivor-idle_rifle"));
	}

	/**
	 * Creates the animation used when the player is walking holding his rifle.
	 *
	 * @param game the game this view belongs to. Needed to access the
	 *             asset manager to get textures.
	 * @return the animation used when the player is walking with his rifle
	 */
	private Animation<TextureRegion> createWalkRifleAnimation(DeathmatchMania game) {
		TextureAtlas textureAtlas = game.getAssetManager().get("playerAtlas.atlas");

		return new Animation(FRAME_TIME_WALK_RIFLE, textureAtlas.findRegions("survivor-move_rifle"));
	}

	/**
	 * Creates the animation used when the player is shooting his rifle.
	 *
	 * @param game the game this view belongs to. Needed to access the
	 *             asset manager to get textures.
	 * @return the animation used when the player is shooting his rifle
	 */
	private Animation<TextureRegion> createShootRifleAnimation(DeathmatchMania game) {
		TextureAtlas textureAtlas = game.getAssetManager().get("playerAtlas.atlas");

		return new Animation(FRAME_TIME_SHOOT_RIFLE, textureAtlas.findRegions("survivor-shoot_rifle"));
	}

	/**
	 * Creates the animation used when the player is reloading his rifle.
	 *
	 * @param game the game this view belongs to. Needed to access the
	 *             asset manager to get textures.
	 * @return the animation used when the player is reloading his rifle
	 */
	private Animation<TextureRegion> createReloadRifleAnimation(DeathmatchMania game) {
		TextureAtlas textureAtlas = game.getAssetManager().get("playerAtlas.atlas");

		return new Animation(FRAME_TIME_RELOAD_RIFLE, textureAtlas.findRegions("survivor-reload_rifle"));
	}

	/**
	 * Creates the animation used when the player is standing still holding his pistol.
	 *
	 * @param game the game this view belongs to. Needed to access the
	 *             asset manager to get textures.
	 * @return the animation used when the player is idle with his pistol
	 */
	private Animation<TextureRegion> createIdlePistolAnimation(DeathmatchMania game) {
		TextureAtlas textureAtlas = game.getAssetManager().get("playerAtlas.atlas");

		return new Animation(FRAME_TIME_IDLE_PISTOL, textureAtlas.findRegions("survivor-idle_handgun"));
	}

	/**
	 * Creates the animation used when the player is walking holding his pistol.
	 *
	 * @param game the game this view belongs to. Needed to access the
	 *             asset manager to get textures.
	 * @return the animation used when the player is walking with his pistol
	 */
	private Animation<TextureRegion> createWalkPistolAnimation(DeathmatchMania game) {
		TextureAtlas textureAtlas = game.getAssetManager().get("playerAtlas.atlas");

		return new Animation(FRAME_TIME_WALK_PISTOL, textureAtlas.findRegions("survivor-move_handgun"));
	}

	/**
	 * Creates the animation used when the player is shooting his pistol.
	 *
	 * @param game the game this view belongs to. Needed to access the
	 *             asset manager to get textures.
	 * @return the animation used when the player is shooting his pistol
	 */
	private Animation<TextureRegion> createShootPistolAnimation(DeathmatchMania game) {
		TextureAtlas textureAtlas = game.getAssetManager().get("playerAtlas.atlas");

		return new Animation(FRAME_TIME_SHOOT_PISTOL, textureAtlas.findRegions("survivor-shoot_handgun"));
	}

    /**
     * Creates the animation used when the player is reloading his pistol.
     *
     * @param game the game this view belongs to. Needed to access the
     *             asset manager to get textures.
     * @return the animation used when the player is reloading his pistol
     */
    private Animation<TextureRegion> createReloadPistolAnimation(DeathmatchMania game) {
        TextureAtlas textureAtlas = game.getAssetManager().get("playerAtlas.atlas");

        return new Animation(FRAME_TIME_RELOAD_PISTOL, textureAtlas.findRegions("survivor-reload_handgun"));
    }

	/**
	 * Creates the animation used when the player is standing still holding his knife.
	 *
	 * @param game the game this view belongs to. Needed to access the
	 *             asset manager to get textures.
	 * @return the animation used when the player is idle with his knife
	 */
	private Animation<TextureRegion> createIdleKnifeAnimation(DeathmatchMania game) {
		TextureAtlas textureAtlas = game.getAssetManager().get("playerAtlas.atlas");

		return new Animation(FRAME_TIME_IDLE_KNIFE, textureAtlas.findRegions("survivor-idle_knife"));
	}

	/**
	 * Creates the animation used when the player is walking holding his knife.
	 *
	 * @param game the game this view belongs to. Needed to access the
	 *             asset manager to get textures.
	 * @return the animation used when the player is walking with his knife
	 */
	private Animation<TextureRegion> createWalkKnifeAnimation(DeathmatchMania game) {
		TextureAtlas textureAtlas = game.getAssetManager().get("playerAtlas.atlas");

		return new Animation(FRAME_TIME_WALK_KNIFE, textureAtlas.findRegions("survivor-move_knife"));
	}

	/**
	 * Creates the animation used when the player is using his knife.
	 *
	 * @param game the game this view belongs to. Needed to access the
	 *             asset manager to get textures.
	 * @return the animation used when the player is using his knife
	 */
	private Animation<TextureRegion> createShootKnifeAnimation(DeathmatchMania game) {
		TextureAtlas textureAtlas = game.getAssetManager().get("playerAtlas.atlas");

		return new Animation(FRAME_TIME_SHOOT_KNIFE, textureAtlas.findRegions("survivor-meleeattack_knife"));
	}

	/**
	 * Creates the animation used for the player's feet when he is standing still.
	 *
	 * @param game the game this view belongs to. Needed to access the
	 *             asset manager to get textures.
	 * @return the animation used for the player's feet when he is standing still
	 */
	private Animation<TextureRegion> createIdleFeetAnimation(DeathmatchMania game) {
		TextureAtlas textureAtlas = game.getAssetManager().get("playerAtlas.atlas");

		return new Animation(FRAME_TIME_IDLE_RIFLE, textureAtlas.findRegions("survivor-idle"));
	}

	/**
	 * Creates the animation used for the player's feet when he is moving.
	 *
	 * @param game the game this view belongs to. Needed to access the
	 *             asset manager to get textures.
	 * @return the animation used for the player's feet when he is moving
	 */
	private Animation<TextureRegion> createWalkFeetAnimation(DeathmatchMania game) {
		TextureAtlas textureAtlas = game.getAssetManager().get("playerAtlas.atlas");

		return new Animation(FRAME_TIME_WALK_RIFLE, textureAtlas.findRegions("survivor-walk"));
	}

	/**
	 * Updates this player view based on the player model.
	 *
	 * @param model the model used to update this view
	 */
	@Override
	public void update(EntityModel model) {
		super.update(model);

		((PlayerModel) model).increaseStateTime(Gdx.graphics.getDeltaTime());
		float stateTime = ((PlayerModel) model).getStateTime();
		if(((PlayerModel) model).getCurrentWeapon() == PlayerModel.Weapons.RIFLE) // Is holding the rifle.
		{
			if (((PlayerModel) model).getCurrentState() == PlayerModel.States.IDLE) // Player is standing still
				sprite.setRegion(idleRifleAnimation.getKeyFrame(stateTime, true));
			else if (((PlayerModel) model).getCurrentState() == PlayerModel.States.MOVE) // Player is moving.
				sprite.setRegion(walkRifleAnimation.getKeyFrame(stateTime, true));
			else if (((PlayerModel) model).getCurrentState() == PlayerModel.States.SHOOT) // Player is shooting.
				sprite.setRegion(shootRifleAnimation.getKeyFrame(stateTime, false));
			else if (((PlayerModel) model).getCurrentState() == PlayerModel.States.RELOAD) // Player is reloading.
			{
				sprite.setRegion(reloadRifleAnimation.getKeyFrame(stateTime, false));
				// Adjust position.
				Vector2 unitVector = new Vector2(0, -2);
				float playerAngle = model.getRotation();
				float playerX = model.getX() / PIXEL_TO_METER;
				float playerY = model.getY() / PIXEL_TO_METER;
				unitVector = unitVector.rotateRad(playerAngle);
				playerX += unitVector.x;
				playerY += unitVector.y;
				sprite.setCenter(playerX, playerY);
			}

			if (((PlayerModel) model).getCurrentState() == PlayerModel.States.RELOAD)
				sprite.setScale(SCALE+0.025f);
			else
				sprite.setScale(SCALE);

		}
		else if(((PlayerModel) model).getCurrentWeapon() == PlayerModel.Weapons.PISTOL) // Is holding the pistol.
		{
			// Adjust center because the pistol's sprite is herpa derpa.
			float playerAngle = model.getRotation();
			Vector2 unitVector = new Vector2(-10, 0);  // Relative center offset.
			unitVector = unitVector.rotateRad(playerAngle);
			float playerX = model.getX() / PIXEL_TO_METER;
			float playerY = model.getY() / PIXEL_TO_METER;
			playerX += unitVector.x;
			playerY += unitVector.y;
			sprite.setCenter(playerX, playerY);

			if (((PlayerModel) model).getCurrentState() == PlayerModel.States.IDLE) // Player is standing still
				sprite.setRegion(idlePistolAnimation.getKeyFrame(stateTime, true));
			else if (((PlayerModel) model).getCurrentState() == PlayerModel.States.MOVE) // Player is moving.
				sprite.setRegion(walkPistolAnimation.getKeyFrame(stateTime, true));
			else if (((PlayerModel) model).getCurrentState() == PlayerModel.States.SHOOT) // Player is shooting.
				sprite.setRegion(shootPistolAnimation.getKeyFrame(stateTime, false));
			else if (((PlayerModel) model).getCurrentState() == PlayerModel.States.RELOAD) // Player is reloading.
			{
				sprite.setRegion(reloadPistolAnimation.getKeyFrame(stateTime, false));
				// Re-Adjust position.
				unitVector.set(0, -4);
				unitVector = unitVector.rotateRad(playerAngle);
				playerX += unitVector.x;
				playerY += unitVector.y;
				sprite.setCenter(playerX, playerY);
			}

			if (((PlayerModel) model).getCurrentState() == PlayerModel.States.RELOAD)
				sprite.setScale(SCALE - 0.05f, SCALE + 0.06f);
			else
				sprite.setScale(SCALE - 0.055f, SCALE + 0.025f);


		}
		else if(((PlayerModel) model).getCurrentWeapon() == PlayerModel.Weapons.KNIFE) // Is holding the knife.
		{

			// Adjust center because the pistol's sprite is herpa derpa too :(
			float playerAngle = model.getRotation();
			Vector2 unitVector = new Vector2(-10, -10); // Relative center offset.
			unitVector = unitVector.rotateRad(playerAngle);
			float playerX = model.getX() / PIXEL_TO_METER;
			float playerY = model.getY() / PIXEL_TO_METER;
			playerX += unitVector.x;
			playerY += unitVector.y;
			sprite.setCenter(playerX, playerY);
			if (((PlayerModel) model).getCurrentState() == PlayerModel.States.IDLE) // Player is standing still
				sprite.setRegion(idleKnifeAnimation.getKeyFrame(stateTime, true));
			else if (((PlayerModel) model).getCurrentState() == PlayerModel.States.MOVE) // Player is moving.
				sprite.setRegion(walkKnifeAnimation.getKeyFrame(stateTime, true));
			else if (((PlayerModel) model).getCurrentState() == PlayerModel.States.SHOOT) // Player is shooting.
				sprite.setRegion(shootKnifeAnimation.getKeyFrame(stateTime, false));


			if (((PlayerModel) model).getCurrentState() == PlayerModel.States.SHOOT) {
				sprite.setScale(SCALE + 0.07f, SCALE + 0.20f);
				// Re-adjust sprite origin position.
				unitVector.set(8, -15);
				unitVector = unitVector.rotateRad(playerAngle);
				playerX += unitVector.x;
				playerY += unitVector.y;
				sprite.setCenter(playerX, playerY);
			}
			else if (((PlayerModel) model).getCurrentState() == PlayerModel.States.MOVE)
				sprite.setScale(SCALE - 0.01f, SCALE + 0.04f);
			else if (((PlayerModel) model).getCurrentState() == PlayerModel.States.IDLE)
				sprite.setScale(SCALE, SCALE + 0.04f);
		}
		updateFeetSprite(model);
	}

	/**
	 * Updates the player feet sprite based on the player model.
	 *
	 * @param model the model used to update the sprite feet
	 */
	void updateFeetSprite(EntityModel model){
		float moveStateTime = ((PlayerModel)model).getMoveStateTime();
		spriteFeet.setRotation((float) Math.toDegrees(model.getRotation()));

		//TODO: fabio aqui nao da para tirar os ifs todos? depois ve isto!
		if (((PlayerModel) model).getCurrentState() == PlayerModel.States.MOVE) {
			spriteFeet.setRegion(walkFeetAnimation.getKeyFrame(moveStateTime, true));
		}
		else if (((PlayerModel) model).getCurrentState() == PlayerModel.States.IDLE)
			spriteFeet.setRegion(walkFeetAnimation.getKeyFrame(moveStateTime, true));
		else if (((PlayerModel) model).getCurrentState() == PlayerModel.States.RELOAD)
			spriteFeet.setRegion(walkFeetAnimation.getKeyFrame(moveStateTime, true));
		else if (((PlayerModel) model).getCurrentState() == PlayerModel.States.SHOOT)
			spriteFeet.setRegion(walkFeetAnimation.getKeyFrame(moveStateTime, true));
		spriteFeet.setScale(SCALE, SCALE);
		// Fix feet sprite origin.
		// Adjust center because the pistol's sprite is herpa derpa too :(
		float playerAngle = model.getRotation();
		Vector2 unitVector = new Vector2(-7, 0); // Relative center offset.
		unitVector = unitVector.rotateRad(playerAngle);
		float playerX = model.getX() / PIXEL_TO_METER;
		float playerY = model.getY() / PIXEL_TO_METER;
		playerX += unitVector.x;
		playerY += unitVector.y;
		spriteFeet.setCenter(playerX, playerY);
	}

	/**
	 * Draws the sprite from this view using a sprite batch.
	 * Chooses the correct texture or animation to be used
	 * depending on the accelerating flag.
	 *
	 * @param batch The sprite batch to be used for drawing.
	 */
	@Override
	public void draw(SpriteBatch batch) {
		spriteFeet.draw(batch);
		sprite.draw(batch);
	}

}
