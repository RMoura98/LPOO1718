package com.batataproductions.game.view.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.batataproductions.game.DeathmatchMania;
import com.batataproductions.game.model.entities.BloodSpillModel;
import com.batataproductions.game.model.entities.EntityModel;
import com.batataproductions.game.model.entities.ZombieModel;

public class BloodSpillView extends EntityView {

	/** Transparency of the sprite. */
	private float currentAlpha;

	/** Size of the zombie that originated this blood spill. */
	public ZombieModel.Sizes zombieSize;

	BloodSpillView(DeathmatchMania game) {
		super(game);
		currentAlpha = 1;
	}

	@Override
	public Sprite createSprite(DeathmatchMania game) {
		Texture texture = game.getAssetManager().get("blood_spill.png");
		return new Sprite(texture);
	}

	@Override
	public void update(EntityModel model) {
		BloodSpillModel spill = (BloodSpillModel) model;
		zombieSize = ((BloodSpillModel) model).getZombieSize();
		currentAlpha = (BloodSpillModel.SPILL_TIME - spill.getTimeSinceSpill()) / BloodSpillModel.SPILL_TIME;

		if(zombieSize == ZombieModel.Sizes.SMALL)
			sprite.setSize(75,65);
		else if(zombieSize == ZombieModel.Sizes.MEDIUM)
			sprite.setSize(125,110);
		else if(zombieSize == ZombieModel.Sizes.BIG)
			sprite.setSize(180,165);

		super.update(model);
	}

	@Override
	public void draw(SpriteBatch batch) {
		if(currentAlpha - 0.2f <= 0)
			sprite.setAlpha(0);
		else
			sprite.setAlpha(currentAlpha - 0.2f);

		sprite.draw(batch);
	}
}
