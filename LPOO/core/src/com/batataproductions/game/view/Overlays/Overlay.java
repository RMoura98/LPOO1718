package com.batataproductions.game.view.Overlays;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.batataproductions.game.view.GameView;

public abstract class Overlay {

	protected Sprite sprite;

	public void display(SpriteBatch batch, float timeLeftToNextOverlay)
	{
		float alpha = (timeLeftToNextOverlay - GameView.OVERLAY_FADEOUT_TIME) / GameView.OVERLAY_FADEOUT_TIME;
		sprite.setAlpha(alpha);
		sprite.draw(batch);
	}
}
