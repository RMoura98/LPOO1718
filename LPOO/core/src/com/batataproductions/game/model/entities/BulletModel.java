package com.batataproductions.game.model.entities;

import com.badlogic.gdx.utils.Pool;

public class BulletModel extends EntityModel implements Pool.Poolable {

    /**
     * Time since the current animation started.
     */
    private float stateTime = 0;

    /**
     * Time since the last moment the zombie has moved.
     */
    private float moveStateTime = 0;

    /**
     * The damage a rifle bullet can give by default.
     */
    public static final float DEFAULT_DAMAGE_RIFLE_BULLET = 30f;

    /**
     * The damage a pistol bullet can give by default.
     */
    public static final float DEFAULT_DAMAGE_PISTOL_BULLET = 50f;

    private PlayerModel.Weapons gun;

    /**
     * Constructs a zombie model belonging to a game model.
     *
     * @param x The x-coordinate of this bullet.
     * @param y The y-coordinate of this bullet.
     * @param rotation The rotation of this bullet.
     */
    public BulletModel(float x, float y, float rotation) {
        super(x, y, rotation);
    }

    /**
     * Increases this model state time.
     * @param deltatime time variation.
     */
    public void increaseStateTime(float deltatime)
    {
        this.stateTime += deltatime;
    }

    /**
     * Returns the state time for this bullet model.
     * @return the state time for the bullet model.
     */
    public float getStateTime(){
        return stateTime;
    }

    /**
     * Resets the state time for the zombie model.
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
     * Returns the move state time for this bullet model.
     * @return the move state time for the bullet model.
     */
    public float getMoveStateTime(){
        return moveStateTime;
    }

    /**
     * Returns the damage this bullet can give.
     * @return damage this bullet can give
     */
    public float getDamageToGive(){
        if (this.gun == PlayerModel.Weapons.PISTOL)
            return DEFAULT_DAMAGE_PISTOL_BULLET;
        if (this.gun == PlayerModel.Weapons.RIFLE)
            return DEFAULT_DAMAGE_RIFLE_BULLET;
        return 0f;
    }

    /**
     * defines which gun shot this bullet.
     * @param gun gun that shot this bullet
     */
    public void setGun(PlayerModel.Weapons gun) {
        this.gun = gun;
    }

    /**
     * @return Entity type this model represents.
     */
    @Override
    public ModelType getType() { return ModelType.BULLET; }

    @Override
    public void reset() {
        this.setPosition(0,0);
        this.setFlaggedForRemoval(false);
    }


}
