package com.batataproductions.game.view.entities;

import com.batataproductions.game.DeathmatchMania;
import com.batataproductions.game.model.entities.EntityModel;

import java.util.HashMap;
import java.util.Map;

/**
 * A factory for EntityView objects with cache
 */

public class ViewFactory {
	private static Map<EntityModel.ModelType, EntityView> cache =
			new HashMap<EntityModel.ModelType, EntityView>();

	public static EntityView makeView(DeathmatchMania game, EntityModel model) {
		if (!cache.containsKey(model.getType())) {
			if (model.getType() == EntityModel.ModelType.PLAYER)
				cache.put(model.getType(), new PlayerView(game));
			if (model.getType() == EntityModel.ModelType.ZOMBIE_MEDIUM || model.getType() == EntityModel.ModelType.ZOMBIE_BIG || model.getType() == EntityModel.ModelType.ZOMBIE_SMALL)
				cache.put(model.getType(), new ZombieView(game));
			if (model.getType() == EntityModel.ModelType.BULLET)
				cache.put(model.getType(), new BulletView(game));
			if (model.getType() == EntityModel.ModelType.BLOOD_SPILL)
				cache.put(model.getType(), new BloodSpillView(game));
		}
		return cache.get(model.getType());
	}
}
