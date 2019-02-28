package com.batataproductions.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.batataproductions.game.DeathmatchMania;
import com.batataproductions.game.controller.topscores.TopScores;

public class LoadingScreen extends ScreenAdapter
{
	/** Loading screen camera */
	OrthographicCamera camera;

	/** Loading screen background sprite */
	Sprite ldScreen;
	/** Loadin screen background texture */
	Texture ldScreenTexture;

	DeathmatchMania game;

	/** The stage which will hold the progress bar. */
	Stage stage;

	/** This screen's progress bar. */
	ProgressBar progressBar;


	public LoadingScreen(DeathmatchMania game)
	{
		camera = new OrthographicCamera(game.WIDTH, game.WIDTH * ((float) Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth()));
		camera.setToOrtho(false);

		this.game = game;
	}

	/**
	 * Loads the assets needed by the game.
	 */
	public void loadAssets() {
		this.game.getAssetManager().load("ak47_shot.ogg", Sound.class);
		this.game.getAssetManager().load("ak47_draw.ogg", Sound.class);
		this.game.getAssetManager().load("ak47_reload.ogg", Sound.class);
		this.game.getAssetManager().load("glock_shot.ogg", Sound.class);
		this.game.getAssetManager().load("glock_reload.ogg", Sound.class);
		this.game.getAssetManager().load("glock_draw.ogg", Sound.class);
		this.game.getAssetManager().load("knife_shot.ogg", Sound.class);
		this.game.getAssetManager().load("knife_draw.ogg", Sound.class);
		this.game.getAssetManager().load("footstep.ogg", Sound.class);
		this.game.getAssetManager().load("zombie_sound1.mp3", Sound.class);
		this.game.getAssetManager().load("zombie_sound2.mp3", Sound.class);
		this.game.getAssetManager().load("zombie_sound3.mp3", Sound.class);
		this.game.getAssetManager().load("zombie_sound4.mp3", Sound.class);
		this.game.getAssetManager().load("zombie_sound5.mp3", Sound.class);
		this.game.getAssetManager().load("zombie_sound6.mp3", Sound.class);
		this.game.getAssetManager().load( "blood_spill.png", Texture.class);
		this.game.getAssetManager().load( "click_anywhere.png", Texture.class);
		this.game.getAssetManager().load( "choose_difficulty.png", Texture.class);
		this.game.getAssetManager().load( "new_round.png", Texture.class);
		this.game.getAssetManager().load( "game_over.png", Texture.class);
		this.game.getAssetManager().load( "playerAtlas.atlas" , TextureAtlas.class);
		this.game.getAssetManager().load( "zombieAtlas.atlas" , TextureAtlas.class);
		this.game.getAssetManager().load( "bulletAtlas.atlas" , TextureAtlas.class);
		this.game.getAssetManager().load( "bloodAtlas.atlas" , TextureAtlas.class);
	}


	public void show() {
		ldScreenTexture = new Texture(Gdx.files.internal("DeathMatch Mania Wallpaper.png"));
		ldScreen = new Sprite (ldScreenTexture);
		// Resize sprite to fit screen.
		ldScreen.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		// Create Progress bar;
		createProgressBar();

		game.getAssetManager().finishLoading();
		loadAssets();
		TopScores.getInstance(); /* INITIALIZE TopScores */
	}

	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		game.getBatch().setProjectionMatrix(camera.combined);
		game.getBatch().begin();
		ldScreen.draw(game.getBatch());
		game.getBatch().end();

		// Display Progress Bar.
		progressBar.setValue(game.getAssetManager().getProgress());
		stage.draw();
		stage.act();

		if (game.getAssetManager().update()) {
			game.setNewScreen(new GameView(game)); // All resources have been loaded!
			this.dispose();
		}
	}

	/**
	 * Creates the progress bar for this screen.
	 */
	private void createProgressBar()
	{
		Viewport viewport = new FitViewport(game.WIDTH, game.HEIGHT, new OrthographicCamera());
		stage = new Stage(viewport, game.getBatch());

		Skin skin = new Skin();
		Pixmap pixmap = new Pixmap(25, 25, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		skin.add("white", new Texture(pixmap));

		TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));

		ProgressBar.ProgressBarStyle barStyle = new ProgressBar.ProgressBarStyle(skin.newDrawable("white", Color.DARK_GRAY), drawable);
		progressBar = new ProgressBar(0, 0.90f, 0.01f, false, barStyle);
		progressBar.setPosition(500, 550);
		progressBar.setSize(290, 200);
		progressBar.setAnimateDuration(0.1f);
		stage.addActor(progressBar);
	}

	@Override
	public void dispose() {
		super.dispose();
		stage.dispose();
		ldScreenTexture.dispose();
	}
}
