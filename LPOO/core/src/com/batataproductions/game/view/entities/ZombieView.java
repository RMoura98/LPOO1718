package com.batataproductions.game.view.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.batataproductions.game.DeathmatchMania;
import com.batataproductions.game.model.GameModel;
import com.batataproductions.game.model.entities.EntityModel;
import com.batataproductions.game.model.entities.ZombieModel;

/**
 * A view representing a zombie.
 */
public class ZombieView extends EntityView  {

    /** The time between the animation frames for the idle animation. */
    private static final float FRAME_TIME_IDLE = 1/10f;

    /** The time between the animation frames for the walk animation. */
    private static final float FRAME_TIME_WALK = 1/13f;

    /** The time between the animation frames for the attack animation. */
    private static final float FRAME_TIME_ATTACK = 1/13f;

    /**
     * Sprite scale to be applied before drawing.
     */
    private float SCALE = 0.5f;


    /**
     * The animation used when the zombie is still.
     */
    private Animation<TextureRegion> idleAnimation;

    /**
     * The animation used when the zombie is walking.
     */
    private Animation<TextureRegion> walkAnimation;

    /**
     * The animation used when the zombie is attacking.
     */
    private Animation<TextureRegion> attackAnimation;


    /**
     * Constructs a zombie model.
     *
     * @param game the game this view belongs to. Needed to access the
     *             asset manager to get textures.
     */
    public ZombieView(DeathmatchMania game) {
        super(game);
    }

    /**
     * Creates a sprite representing this zombie.
     *
     * @param game the game this view belongs to. Needed to access the
     *             asset manager to get textures.
     * @return the sprite representing this zombie
     */
    @Override
    public Sprite createSprite(DeathmatchMania game) {
        idleAnimation = createIdleAnimation(game);
        walkAnimation = createWalkAnimation(game);
        attackAnimation = createAttackAnimation(game);
        return new Sprite(idleAnimation.getKeyFrame(0, true));
    }

    /**
     * Creates the animation used when the zombie is attacking.
     *
     * @param game the game this view belongs to. Needed to access the
     *             asset manager to get textures.
     * @return the animation used when the zombie is attacking
     */
    private Animation<TextureRegion> createAttackAnimation(DeathmatchMania game) {
        TextureAtlas textureAtlas = game.getAssetManager().get("zombieAtlas.atlas");

        return new Animation(FRAME_TIME_ATTACK, textureAtlas.findRegions("skeleton-attack"));
    }

    /**
     * Creates the animation used when the zombie is walking.
     *
     * @param game the game this view belongs to. Needed to access the
     *             asset manager to get textures.
     * @return the animation used when the zombie is walking
     */
    private Animation<TextureRegion> createWalkAnimation(DeathmatchMania game) {
        TextureAtlas textureAtlas = game.getAssetManager().get("zombieAtlas.atlas");

        return new Animation(FRAME_TIME_WALK, textureAtlas.findRegions("skeleton-move"));
    }

    /**
     * Creates the animation used when the zombie is standing still.
     *
     * @param game the game this view belongs to. Needed to access the
     *             asset manager to get textures.
     * @return the animation used when the zombie is idle
     */
    private Animation<TextureRegion> createIdleAnimation(DeathmatchMania game) {
        TextureAtlas textureAtlas = game.getAssetManager().get("zombieAtlas.atlas");

        return new Animation(FRAME_TIME_IDLE, textureAtlas.findRegions("skeleton-idle"));
    }

    /**
     * Updates this player view based on the zombie model.
     *
     * @param model the model used to update this view
     */
    @Override
    public void update(EntityModel model) {
        super.update(model);

        ((ZombieModel) model).increaseStateTime(Gdx.graphics.getDeltaTime());
        float stateTime = ((ZombieModel) model).getStateTime();

        if (((ZombieModel) model).getCurrentState() == ZombieModel.States.IDLE) // Zombie is standing still
            sprite.setRegion(idleAnimation.getKeyFrame(stateTime, true));
        else if (((ZombieModel) model).getCurrentState() == ZombieModel.States.MOVE) // Zombie is moving.
            sprite.setRegion(walkAnimation.getKeyFrame(stateTime, true));
        else if (((ZombieModel) model).getCurrentState() == ZombieModel.States.ATTACK) // Zombie is attacking.
            sprite.setRegion(walkAnimation.getKeyFrame(stateTime, true));


        if ((((ZombieModel) model).getSize() == ZombieModel.Sizes.MEDIUM)) {
            if (((ZombieModel) model).getCurrentState() == ZombieModel.States.ATTACK)
                sprite.setScale(SCALE+0.2f);
            else
                sprite.setScale(SCALE+0.19f);
        }
        else if((((ZombieModel) model).getSize() == ZombieModel.Sizes.SMALL)){
            if (((ZombieModel) model).getCurrentState() == ZombieModel.States.ATTACK)
                sprite.setScale(SCALE+0.01f);
            else
                sprite.setScale(SCALE);
        }
        else if((((ZombieModel) model).getSize() == ZombieModel.Sizes.BIG)){
            if (((ZombieModel) model).getCurrentState() == ZombieModel.States.ATTACK)
                sprite.setScale(SCALE + 0.39f);
            else
                sprite.setScale(SCALE + 0.4f);
        }



        /* UPDATE POR SER FEITo*/
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
        sprite.draw(batch);
    }
}
