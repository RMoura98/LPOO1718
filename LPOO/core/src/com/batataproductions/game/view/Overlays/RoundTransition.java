package com.batataproductions.game.view.Overlays;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.batataproductions.game.DeathmatchMania;
import com.batataproductions.game.view.GameView;

public class RoundTransition extends Overlay {

	public RoundTransition(DeathmatchMania game)
	{
		Texture texture = game.getAssetManager().get("new_round.png");
		sprite = new Sprite (texture);
		sprite.setSize(DeathmatchMania.WIDTH, DeathmatchMania.HEIGHT);
	}

	public void display(SpriteBatch batch, float timeLeftToNextOverlay)
	{
		float alpha = (timeLeftToNextOverlay - 3f) / 3f;
		sprite.setAlpha(alpha);
		sprite.draw(batch);
	}
}
