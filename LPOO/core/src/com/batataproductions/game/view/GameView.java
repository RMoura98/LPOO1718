package com.batataproductions.game.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.batataproductions.game.DeathmatchMania;
import com.batataproductions.game.controller.GameController;
import com.batataproductions.game.controller.entities.EntityBody;
import com.batataproductions.game.controller.topscores.TopScores;
import com.batataproductions.game.model.GameModel;
import com.batataproductions.game.model.entities.BloodSpillModel;
import com.batataproductions.game.model.entities.BulletModel;
import com.batataproductions.game.model.entities.PlayerModel;
import com.batataproductions.game.model.entities.ZombieModel;
import com.batataproductions.game.view.Overlays.ChooseDifficulty;
import com.batataproductions.game.view.Overlays.ClickAnywhere;
import com.batataproductions.game.view.Overlays.GameOver;
import com.batataproductions.game.view.Overlays.Overlay;
import com.batataproductions.game.view.Overlays.RoundTransition;
import com.batataproductions.game.view.entities.EntityView;
import com.batataproductions.game.view.entities.ViewFactory;
import com.batataproductions.game.view.hud.Hud;

import java.awt.Color;
import java.util.List;

import javax.swing.plaf.nimbus.State;

import static com.badlogic.gdx.Application.ApplicationType.Android;

/**
 * A view representing the game screen. Draws all the other views and
 * controls the camera.
 */

public class GameView extends ScreenAdapter {

	/**
	 * Used to debug the position of the physics fixtures
	 */
	private static final boolean DEBUG_PHYSICS = false;

	/**
	 * The game this screen belongs to.
	 */
	private final DeathmatchMania game;

	/**
	 * The game's Hud which has information for the user.
	 */
	private static Hud hud;

	/**
	 * The camera used to show the viewport.
	 */
	private final OrthographicCamera camera;

	/**
	 * How much meters does a pixel represent.
	 */
	public final static float PIXEL_TO_METER = 0.04f;

	/**
	 * The width of the viewport in meters. The height is
	 * automatically calculated using the screen ratio.
	 * Set in the constructor
	 */
	public final float VIEWPORT_WIDTH;

	/**
	 * A renderer used to debug the physical fixtures.
	 */
	private Box2DDebugRenderer debugRenderer;

	/**
	 * The transformation matrix used to transform meters into
	 * pixels in order to show fixtures in their correct places.
	 */
	private Matrix4 debugCamera;

	/**
	 * Posible Game States.
	 */
	public enum States { CLICK_TO_PLAY, CHOOSE_DIFFICULTY, GAME_RUNNING, GAME_OVER };

	/**
	 * Game Current State;
	 */
	public States currentState;

	/**
	 * Current hue for the background color of the screen.
	 */
	private float currentHue;

	/**
	 * Tiled map related variables.
	 */
	private TmxMapLoader mapLoader;
	private TiledMap tiledMap;
	private OrthoCachedTiledMapRenderer renderer;

	/**
	 * Screen overlays.
	 */
	Overlay clickAnywhereOverlay;
	Overlay chooseDifficultyOverlay;
	Overlay gameOverOverlay;
	Overlay roundTransitionOverlay;
	float timeLeftToNextOverlay = Float.MAX_VALUE;
	float newOverlayDelay = 0;
	public static final float OVERLAY_FADEOUT_TIME = 1.5f;
	public static final float TIME_BETWEEN_OVERLAYS = 0.6f;

	/**
	 * Creates this screen.
	 *
	 * @param game The game this screen belongs to
	 */
	public GameView(DeathmatchMania game) {
		this.game = game;
		currentState = States.CLICK_TO_PLAY;
		currentHue = 0;

		VIEWPORT_WIDTH = game.WIDTH*PIXEL_TO_METER;

		camera = createCamera();

		hud = new Hud(game.getBatch(), game.WIDTH, game.HEIGHT, game);
		hud.setTime(1.03f);

		mapLoader = new TmxMapLoader();
		tiledMap = mapLoader.load("mapG.tmx");
		renderer = new OrthoCachedTiledMapRenderer(tiledMap);
		camera.position.set(new Vector2(0,0),0);

						/* POR ISTO NUMA CLASS MAP ou algo do genero*/

		// Create GameController and GameModel singleton instances. We'll do it this way since the order they are initialized might be important.
		GameModel.getInstance();
		GameController.getInstance();
		GameModel.getInstance().setAssetManager(game.getAssetManager());

		//create body and fixture variables
		BodyDef bdef = new BodyDef();
		PolygonShape shape = new PolygonShape();
		FixtureDef fdef = new FixtureDef();
		Body body;

		// limitas do mapa
		for(MapObject object : tiledMap.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)){
			Rectangle rect = ((RectangleMapObject) object).getRectangle();

			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX() + rect.getWidth() / 2) * PIXEL_TO_METER, (rect.getY() + rect.getHeight() / 2 ) * PIXEL_TO_METER);

			body = GameController.getInstance().getWorld().createBody(bdef);

			shape.setAsBox((rect.getWidth() / 2) * PIXEL_TO_METER, (rect.getHeight() / 2) * PIXEL_TO_METER);
			fdef.shape = shape;
			fdef.filter.categoryBits = EntityBody.MAP_WALL;
			fdef.filter.maskBits = ~0;
			body.createFixture(fdef);
		}

		/////////// Debug
		//bdef.type = BodyDef.BodyType.StaticBody;
		//bdef.position.set(85, 120);
		//body = GameController.getInstance().getWorld().createBody(bdef);
		//shape.setAsBox(10, 10);
		//body.createFixture(fdef);
		///////////

		//GameMap.getInstance().load(tiledMap);

		// Initialize overlays.
		clickAnywhereOverlay = new ClickAnywhere(game);
		chooseDifficultyOverlay = new ChooseDifficulty(game);
		gameOverOverlay = new GameOver(game);
		roundTransitionOverlay = new RoundTransition(game);
	}

	/**
	 * Creates the camera used to show the viewport.
	 *
	 * @return the camera
	 */
	private OrthographicCamera createCamera() {
		OrthographicCamera camera = new OrthographicCamera(VIEWPORT_WIDTH / PIXEL_TO_METER, VIEWPORT_WIDTH / PIXEL_TO_METER * ((float) Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth()));

		camera.position.set(0, 0, 0);
		camera.update();

		if (DEBUG_PHYSICS) {
			debugRenderer = new Box2DDebugRenderer();
			debugCamera = camera.combined.cpy();
			debugCamera.scl(1 / PIXEL_TO_METER);
		}

		return camera;
	}


	/**
	 * Renders this screen.
	 *
	 * @param delta time since last renders in seconds.
	 */
	@Override
	public void render(float delta) {
		GameController.getInstance().removeFlagged();

		handleInputs(delta);



		GameController.getInstance().update(delta);
		GameController.getInstance().handleRounds(currentState);
		hud.update(delta, game.WIDTH, game.HEIGHT);

		// Center camera on player.
		camera.position.set(GameModel.getInstance().getSelfPlayer().getX() / PIXEL_TO_METER, GameModel.getInstance().getSelfPlayer().getY() / PIXEL_TO_METER, 0);

		camera.update();
		game.getBatch().setProjectionMatrix(camera.combined);

		hud.update(GameModel.getInstance().getSelfPlayer().getCurrentWeapon().toString());

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		renderer.setView(camera);

		renderer.render();

		game.getBatch().begin();

		drawBloodSpills();
		drawPlayers();
		drawZombies();
		drawBullets();

		game.getBatch().setProjectionMatrix(hud.stage.getCamera().combined);

		if(currentState == States.GAME_RUNNING || currentState == States.GAME_OVER)
			hud.drawSprite(game.getBatch());

		if(GameModel.getInstance().getSelfPlayer().isDead()) {
			setGameOver();
			GameModel.getInstance().getSelfPlayer().stopFootstepSound();
		}

		drawOverlays(delta);

		game.getBatch().end();

		if(currentState == States.GAME_RUNNING)
			hud.draw();

		if (DEBUG_PHYSICS) {
			debugCamera = camera.combined.cpy();
			debugCamera.scl(1 / PIXEL_TO_METER);
			debugRenderer.render(GameController.getInstance().getWorld(), debugCamera);
		}

	}

	/**
	 * Draws the overlays.
	 *
	 * @param delta time since last renders in seconds.
	 */
	private void drawOverlays(float delta)
	{
		if(newOverlayDelay >= 0){
			newOverlayDelay -= delta;
			return;
		}

		if(timeLeftToNextOverlay != Float.MAX_VALUE) timeLeftToNextOverlay -= delta;

		if(currentState == States.CLICK_TO_PLAY)	/* DISPLAY: CLICK ANYWHERE TO START */
		{
			if(timeLeftToNextOverlay <= 0)
			{
				currentState = States.CHOOSE_DIFFICULTY;
				newOverlayDelay = TIME_BETWEEN_OVERLAYS;
				timeLeftToNextOverlay = Float.MAX_VALUE;
			}
			else
				clickAnywhereOverlay.display(game.getBatch(), timeLeftToNextOverlay);
		}
		else if(currentState == States.CHOOSE_DIFFICULTY)	/* DISPLAY: CHOOSE DIFFICULTY */
		{
			if(timeLeftToNextOverlay <= 0)
			{
				currentState = States.GAME_RUNNING;
				timeLeftToNextOverlay = Float.MAX_VALUE;
			}
			else
				chooseDifficultyOverlay.display(game.getBatch(), timeLeftToNextOverlay);
		}
		else if(currentState == States.GAME_OVER)	/* DISPLAY: GAME_OVER */
		{
			if(timeLeftToNextOverlay <= 0)
			{
				currentState = States.GAME_RUNNING;
				timeLeftToNextOverlay = Float.MAX_VALUE;
				GameController.getInstance().startNewGame();
				TopScores.getInstance().resetNewHighScore();
				firstTrigger = true;
			}
			else
				gameOverOverlay.display(game.getBatch(), timeLeftToNextOverlay);
		}
		else if(currentState == States.GAME_RUNNING)
		{
			if(GameModel.getInstance().getZombies().isEmpty()) // Player killed all the zombies!
			{
				if(timeLeftToNextOverlay <= 0)
				{
					timeLeftToNextOverlay = Float.MAX_VALUE;
					GameController.getInstance().startNewRound();
				}
				else if(timeLeftToNextOverlay == Float.MAX_VALUE)
					timeLeftToNextOverlay = 3.0f;
				else
					roundTransitionOverlay.display(game.getBatch(), timeLeftToNextOverlay);
			}
		}
	}

	/**
	 * Changes the background colour to a new one.
	 * @param delta time since last renders in seconds.
	 * @note Based on a continuous function which implies a conversion from HSV color model to RGB.
	 */
	private void setNewBackgroundColor(float delta){
		int newRGBColor = Color.HSBtoRGB((float)Math.toRadians(currentHue), 0.6f, 0.65f);
		int red = newRGBColor & 0xFF;
		int green = (newRGBColor & 0xFF00) >> 8;
		int blue = (newRGBColor & 0xFF0000) >> 16;
		Gdx.gl.glClearColor(red/255f, green/255f, blue/255f, 1);
		currentHue += 0.025 * delta * 60;
	}

	/**
	 * Handles any inputs and passes them to the controller.
	 *
	 * @param delta time since last time inputs where handled in seconds
	 */
	private void handleInputs(float delta) {

		if(currentState == States.CLICK_TO_PLAY)
		{
			process_input_click_to_play(delta);
		}
		else if(currentState == States.CHOOSE_DIFFICULTY)
		{
			process_input_choose_difficulty(delta);
		}
		else if(currentState == States.GAME_RUNNING)
		{
			process_input_game_running(delta);
		}
		else if(currentState == States.GAME_OVER)
		{
			process_input_game_over(delta);
			GameController.getInstance().makeBodiesStill();
		}

	}

	/**
	 * Process Input (when the game state is set to CLICK_TO_PLAY)
	 * @param delta time since last time inputs where handled in seconds
	 */
	private void process_input_click_to_play(float delta)
	{
		if(Gdx.input.justTouched() && timeLeftToNextOverlay == Float.MAX_VALUE)
			timeLeftToNextOverlay = OVERLAY_FADEOUT_TIME;
	}

	/**
	 * Process Input (when the game state is set to CHOOSE_DIFFICULTY)
	 * @param delta time since last time inputs where handled in seconds
	 */
	private void process_input_choose_difficulty(float delta)
	{
		if(Gdx.input.justTouched() && timeLeftToNextOverlay == Float.MAX_VALUE) {

			float scale_factor_x = (float) Gdx.graphics.getWidth() / game.WIDTH;
			float scale_factor_y = (float) Gdx.graphics.getHeight() / game.HEIGHT;

			int non_scaled_x = (int) ( (float) Gdx.input.getX() / scale_factor_x);
			int non_scaled_y = (int) ( (float) Gdx.input.getY() / scale_factor_y);

			// Debug touch position.
			/* if(Gdx.input.justTouched())
			{
				Gdx.app.log("x: ", "" + non_scaled_x);
				Gdx.app.log("y: ", "" + non_scaled_y);
			} */

			// Find out which difficulty the player has chosen.
			if(non_scaled_x > 435 && non_scaled_x < 850)
			{
				if(non_scaled_y > 218 && non_scaled_y < 336) {
					GameController.getInstance().setGameDifficulty(GameController.Difficulty.EASY);
					timeLeftToNextOverlay = OVERLAY_FADEOUT_TIME;
					//Gdx.app.log("diff", "easy");
				}
				else if(non_scaled_y > 345 && non_scaled_y < 461) {
					GameController.getInstance().setGameDifficulty(GameController.Difficulty.NORMAL);
					timeLeftToNextOverlay = OVERLAY_FADEOUT_TIME;
					//Gdx.app.log("diff", "normal");
				}
				else if(non_scaled_y > 471 && non_scaled_y < 581) {
					GameController.getInstance().setGameDifficulty(GameController.Difficulty.HARD);
					timeLeftToNextOverlay = OVERLAY_FADEOUT_TIME;
					//Gdx.app.log("diff", "hard");
				}
			}
		}
	}

	/**
	 * Process Input (when the game state is set to GAME_RUNNING)
	 * @param delta time since last time inputs where handled in seconds
	 */
	private void process_input_game_running(float delta)
	{
		// Process movement.
		short playerMovement = 0;

		if (Gdx.app.getType() == Android || DEBUG_PHYSICS){
			playerMovement = hud.handleController1();

			hud.handleController2();
		}

		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D))
			playerMovement |= GameController.PLAYER_MOVE_RIGHT;
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A))
			playerMovement |= GameController.PLAYER_MOVE_LEFT;
		if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W))
			playerMovement |= GameController.PLAYER_MOVE_UP;
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S))
			playerMovement |= GameController.PLAYER_MOVE_DOWN;

		GameController.getInstance().processMovement(playerMovement, delta, camera);

		// Process weapon switching/firing.
		if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)){
			GameController.getInstance().switchToWeapon(PlayerModel.Weapons.RIFLE);
			hud.update("RIFLE");
		}

		if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)){
			GameController.getInstance().switchToWeapon(PlayerModel.Weapons.PISTOL);
			hud.update("PISTOL");
		}

		if (Gdx.input.isKeyPressed(Input.Keys.NUM_3)){
			GameController.getInstance().switchToWeapon(PlayerModel.Weapons.KNIFE);
			hud.update("KNIFE");
		}

		if (Gdx.input.isKeyPressed(Input.Keys.F11) && Gdx.app.getType() != Android){
			if (Gdx.graphics.isFullscreen())
				Gdx.graphics.setWindowedMode(game.WIDTH, game.HEIGHT);
			else
				Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.R) && GameModel.getInstance().getSelfPlayer().getCurrentState() != PlayerModel.States.RELOAD)
			GameController.getInstance().reload();
		if (Gdx.app.getType() != Android){
			if(GameModel.getInstance().getSelfPlayer().getCurrentWeapon() == PlayerModel.Weapons.RIFLE) {
				if (Gdx.input.isTouched())
					GameController.getInstance().shoot();
			}
			else {
				if (Gdx.input.justTouched())
					GameController.getInstance().shoot();
			}
		}

	}

	/**
	 * Process Input (when the game state is set to GAME_OVER)
	 * @param delta time since last time inputs where handled in seconds
	 */
	private void process_input_game_over(float delta)
	{
		if(Gdx.input.justTouched() && timeLeftToNextOverlay == Float.MAX_VALUE)
			timeLeftToNextOverlay = OVERLAY_FADEOUT_TIME;
	}

	/**
	 * Draws the Players to the screen.
	 */
	private void drawPlayers() {
		List<PlayerModel> players = GameModel.getInstance().getPlayers();
		for(PlayerModel player : players) {
			EntityView view = ViewFactory.makeView(game, player);
			view.update(player);
			view.draw(game.getBatch());
		}
	}

	/**
	 * Draws the zombies to the screen.
	 */
	private void drawZombies() {
		List<ZombieModel> zombies = GameModel.getInstance().getZombies();
		for(ZombieModel zombie : zombies) {
			EntityView view = ViewFactory.makeView(game, zombie);
			view.update(zombie);
			view.draw(game.getBatch());
		}
	}

	/**
	 * Draws the zombies to the screen.
	 */
	private void drawBullets() {
		List<BulletModel> bullets = GameModel.getInstance().getBullets();
		for(BulletModel bullet : bullets) {
			EntityView view = ViewFactory.makeView(game, bullet);
			view.update(bullet);
			view.draw(game.getBatch());
		}
	}

	/**
	 * Draws the blood spills on the screen.
	 */
	private void drawBloodSpills()
	{
		List<BloodSpillModel> spills = GameModel.getInstance().getBloodSpills();
		for(BloodSpillModel spill : spills)
		{
			EntityView view = ViewFactory.makeView(game, spill);
			view.update(spill);
			view.draw(game.getBatch());
		}
	}

	/**
	 * Returns the current GameView state.
	 */
	GameView.States getCurrentState(){
    	return currentState;
	}

	/**
	 * Changes the view state to GAME_OVER.
	 */
	boolean firstTrigger = true;
	public void setGameOver() {
		currentState = States.GAME_OVER;
		if(firstTrigger)
		{
			TopScores.getInstance().newScore(GameController.getInstance().getNrZombiedKilled());
			firstTrigger = false;
		}
	}
}
