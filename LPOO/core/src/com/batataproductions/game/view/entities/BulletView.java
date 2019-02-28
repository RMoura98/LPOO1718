package com.batataproductions.game.view.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.batataproductions.game.DeathmatchMania;
import com.batataproductions.game.model.entities.BulletModel;
import com.batataproductions.game.model.entities.EntityModel;

public class BulletView extends EntityView  {

    /** The time between the animation frames for the animation. */
    private static final float FRAME_TIME = 1/20f;

    /**
     * Sprite scale to be applied before drawing.
     */
    private float SCALE = 0.02f;

    /**
     * The animation used for bullet.
     */
    private Animation<TextureRegion> Animation;

    /**
     * Constructs a bullet model.
     *
     * @param game the game this view belongs to. Needed to access the
     *             asset manager to get textures.
     */
    public BulletView(DeathmatchMania game) {
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
        Animation = createAnimation(game);
        return new Sprite(Animation.getKeyFrame(0, true));
    }

    /**
     * Creates the animation used for a bullet
     *
     * @param game the game this view belongs to. Needed to access the
     *             asset manager to get textures.
     * @return the animation used for a bullet
     */
    private Animation<TextureRegion> createAnimation(DeathmatchMania game) {
        TextureAtlas textureAtlas = game.getAssetManager().get("bulletAtlas.atlas");

        return new Animation(FRAME_TIME, textureAtlas.findRegions("bullet"));
    }

    /**
     * Updates this player view based on the bullet model.
     *
     * @param model the model used to update this view
     */
    @Override
    public void update(EntityModel model) {
        super.update(model);

        ((BulletModel) model).increaseStateTime(Gdx.graphics.getDeltaTime());
        float stateTime = ((BulletModel) model).getStateTime();

        sprite.setRegion(Animation.getKeyFrame(stateTime, false));

        sprite.setScale(SCALE);

        sprite.rotate90(true);
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
