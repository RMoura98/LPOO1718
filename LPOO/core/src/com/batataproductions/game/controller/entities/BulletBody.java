package com.batataproductions.game.controller.entities;

import com.badlogic.gdx.physics.box2d.World;
import com.batataproductions.game.model.entities.EntityModel;

public class BulletBody extends EntityBody  {
    /**
     * Constructs a bullet body according to
     * a bullet model.
     *
     * @param world the physical world this player belongs to.
     * @param model the model representing this bullet.
     */
    public BulletBody(World world, EntityModel model) {
        super(world, model);

        float density = 0.5f, friction = 0.4f, restitution = 0.5f;
        int width = 155, height = 105;

        // Body
		int variavel_misterio = 25;
		int variavel_misterio2 = -25;
        createFixture(body, new float[]{
                49 + variavel_misterio, 71 + variavel_misterio2,
                54 + variavel_misterio, 71 + variavel_misterio2,
                54 + variavel_misterio, 84 + variavel_misterio2,
                49 + variavel_misterio, 84 + variavel_misterio2
        }, width, height, density, friction, restitution, BULLET_BODY, (short) ~PLAYER_BODY, false);
    }
}
