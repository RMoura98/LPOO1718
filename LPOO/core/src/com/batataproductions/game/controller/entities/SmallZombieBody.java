package com.batataproductions.game.controller.entities;

import com.badlogic.gdx.physics.box2d.World;
import com.batataproductions.game.model.entities.EntityModel;

public class SmallZombieBody extends EntityBody {
    /**
     * Constructs a Small zombie body according to
     * a zombie model.
     *
     * @param world the physical world this player belongs to.
     * @param model the model representing this Small zombie.
     */
    public SmallZombieBody(World world, EntityModel model) {
        super(world, model);

        float density = 0.5f, friction = 0.4f, restitution = 0.5f;
        int width = 155, height = 105;

        // Body
        createFixture(body, new float[]{
                //		0, 0, 300, 0, 300, 205, 0 , 205
                //15, 18,
                35, 20,
                35, 90,
                100, 90,
                100, 20
        }, width, height, density, friction, restitution, ZOMBIE_BODY, (short) ~0, false);
    }
}
