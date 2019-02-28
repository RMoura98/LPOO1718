package com.batataproductions.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.batataproductions.game.view.LoadingScreen;

public class DeathmatchMania extends Game {

	// Game constants.
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;

	private SpriteBatch batch;
	private AssetManager assetManager;


	/**
	 * Creates the game. Initializes the sprite batch and asset manager.
	 * Also starts the game until we have a main menu.
	 */
	@Override
	public void create () {
		batch = new SpriteBatch();
		assetManager = new AssetManager();

		startGame();
	}


	/**
	 * Starts the game.
	 */
	private void startGame() {
		setScreen(new LoadingScreen(this));
	}

	/**
	 * Disposes of all assets.
	 */
	@Override
	public void dispose () {
		batch.dispose();
		assetManager.dispose();
	}

	/**
	 * Returns the asset manager used to load all textures and sounds.
	 *
	 * @return the asset manager
	 */
	public AssetManager getAssetManager() {
		return assetManager;
	}

	/**
	 * Returns the sprite batch used to improve drawing performance.
	 *
	 * @return the sprite batch
	 */
	public SpriteBatch getBatch() {
		return batch;
	}

	/** Set a new screen for the game. */
	public void setNewScreen(ScreenAdapter screen) { setScreen(screen); }

}
