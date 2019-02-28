package com.batataproductions.game.controller.entities;

import com.badlogic.gdx.physics.box2d.World;
import com.batataproductions.game.model.entities.EntityModel;

public class MediumZombieBody extends EntityBody {
    /**
     * Constructs a Medium zombie body according to
     * a zombie model.
     *
     * @param world the physical world this player belongs to.
     * @param model the model representing this Medium zombie.
     */
    public MediumZombieBody(World world, EntityModel model) {
        super(world, model);

        float density = 0.5f, friction = 0.4f, restitution = 0.5f;
        int width = 155, height = 105;

        // Body
        createFixture(body, new float[]{
                //		0, 0, 300, 0, 300, 205, 0 , 205
                //15, 18,
                25, 10,
                25, 100,
                120, 100,
                120, 10
        }, width, height, density, friction, restitution, ZOMBIE_BODY, (short) ~0, false);
    }
}
