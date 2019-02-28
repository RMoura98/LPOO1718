package com.batataproductions.game.view.Overlays;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.batataproductions.game.DeathmatchMania;

public class ChooseDifficulty extends Overlay {

	public ChooseDifficulty(DeathmatchMania game)
	{
		Texture texture = game.getAssetManager().get("choose_difficulty.png");
		sprite = new Sprite (texture);
		sprite.setSize(DeathmatchMania.WIDTH, DeathmatchMania.HEIGHT);
	}
}
