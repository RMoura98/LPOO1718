package com.batataproductions.game.controller.entities;

import com.badlogic.gdx.physics.box2d.World;
import com.batataproductions.game.model.entities.EntityModel;

public class BigZombieBody extends EntityBody {
    /**
     * Constructs a Big zombie body according to
     * a zombie model.
     *
     * @param world the physical world this player belongs to.
     * @param model the model representing this Big zombie.
     */
    public BigZombieBody(World world, EntityModel model) {
        super(world, model);

        float density = 0.5f, friction = 0.4f, restitution = 0.5f;
        int width = 155, height = 105;

        // Body
        createFixture(body, new float[]{
                //		0, 0, 300, 0, 300, 205, 0 , 205
                //15, 18,
                0, 10,
                0, 110,
                120, 110,
                120, 10
        }, width, height, density, friction, restitution, ZOMBIE_BODY, (short) ~0, false);
    }
}
