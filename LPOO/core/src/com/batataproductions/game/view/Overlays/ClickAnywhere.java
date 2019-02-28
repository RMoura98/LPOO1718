package com.batataproductions.game.view.Overlays;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.batataproductions.game.DeathmatchMania;

public class ClickAnywhere extends Overlay {

	public ClickAnywhere(DeathmatchMania game)
	{
		Texture texture = game.getAssetManager().get("click_anywhere.png");
		sprite = new Sprite (texture);
		sprite.setSize(DeathmatchMania.WIDTH, DeathmatchMania.HEIGHT);
	}
}
