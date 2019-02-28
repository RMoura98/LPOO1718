package com.batataproductions.game.view.Overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.batataproductions.game.DeathmatchMania;
import com.batataproductions.game.controller.topscores.TopScores;
import com.batataproductions.game.view.GameView;

public class GameOver extends Overlay {

	BitmapFont font;

	public GameOver(DeathmatchMania game)
	{
		Texture texture = game.getAssetManager().get("game_over.png");
		sprite = new Sprite (texture);
		sprite.setSize(DeathmatchMania.WIDTH, DeathmatchMania.HEIGHT);
		// Generate font
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("top_scores_font_helvetica.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 34;
		parameter.borderWidth = 2;
		font = generator.generateFont(parameter); // font size 12 pixels

	}

	@Override
	public void display(SpriteBatch batch, float timeLeftToNextOverlay)
	{
		float alpha;
		if(timeLeftToNextOverlay == Float.MAX_VALUE)
			alpha = 1;
		else
			alpha = Math.abs( 1 - (GameView.OVERLAY_FADEOUT_TIME - timeLeftToNextOverlay) / GameView.OVERLAY_FADEOUT_TIME);

		sprite.setAlpha(alpha);
		sprite.draw(batch);

		font.getCache().setColor(1, 1, 1, alpha);

		//font.draw(batch, "461", 400, 400);

		if(TopScores.getInstance().getTopScores().isEmpty())
			return;

		int y = 420;
		int pos = 1;
		for(TopScores.Score score : TopScores.getInstance().getTopScores())
		{
			font.draw(batch, "" + pos + ".", 440, y);
			font.draw(batch, "" + score.getScore(), 500, y);
			font.draw(batch, "" + score.getName(), 580, y);
			pos++;
			y -= 46;
		}
		if(TopScores.getInstance().isNewHighScore())
			font.draw(batch, "**NEW TOP SCORE**", 450, y);
	}
}
