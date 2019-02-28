package com.batataproductions.game.tests;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;
import com.batataproductions.game.controller.GameController;
import com.batataproductions.game.controller.entities.BigZombieBody;
import com.batataproductions.game.controller.entities.BulletBody;
import com.batataproductions.game.controller.entities.EntityBody;
import com.batataproductions.game.controller.entities.PlayerBody;
import com.batataproductions.game.model.GameModel;
import com.batataproductions.game.model.entities.BulletModel;
import com.batataproductions.game.model.entities.PlayerModel;
import com.batataproductions.game.model.entities.ZombieModel;
import com.batataproductions.game.view.GameView;

import org.junit.Test;
import static org.junit.Assert.*;

public class ControllerTests {

	/**
	 * Tests if there are zombies spawning in the round beginning.
	 */
	@Test
	public void zombieSpawning()
	{
		GameController.getInstance().startNewGame();
		GameController.getInstance().setGameDifficulty(GameController.Difficulty.HARD);
		GameController.getInstance().startNewRound();
		GameController.getInstance().handleRounds(GameView.States.GAME_RUNNING);
		assertNotEquals(GameModel.getInstance().getZombies().size(), 0);
	}

	/**
	 * Tests if the player body is created when the game begins.
	 */
	@Test
	public void playerSpawning()
	{
		GameController.getInstance().startNewGame();
		assertEquals(GameModel.getInstance().getPlayers().size(), 1);
		assertNotNull(GameModel.getInstance().getSelfPlayer());
		assertNotNull(GameController.getInstance().getSelfPlayerBody());
	}

	/**
	 * Tests a body's linear movement.
	 */
	@Test
	public void bodyMovement()
	{
		GameController.getInstance().startNewGame();
		ZombieModel newZombie = new ZombieModel(2, 0, 0, ZombieModel.Sizes.BIG);
		EntityBody zombie = new BigZombieBody(GameController.getInstance().getWorld(), newZombie);
		zombie.setLinearVelocity(20, 0);
		GameController.getInstance().update(0.5f);
		assertTrue(zombie.getX() > 2);
		assertTrue(zombie.getY() == 0);
	}

	/**
	 * Tests player weapon reload.
	 */
	@Test
	public void weaponReload()
	{
		GameController.getInstance().startNewGame();
		GameController.getInstance().shoot();
		assertEquals(GameModel.getInstance().getSelfPlayer().getRifleCurrentAmmo(), 29);
		GameController.getInstance().reload();
		assertEquals(GameModel.getInstance().getSelfPlayer().getRifleCurrentAmmo(), 29);
		GameController.getInstance().update(5f);
		assertEquals(GameModel.getInstance().getSelfPlayer().getRifleCurrentAmmo(), 30);
	}

	/**
	 * Tests some methods of EntityBody.
	 */
	@Test
	public void entityBodyMethods()
	{
		EntityBody entityBody = new PlayerBody(GameController.getInstance().getWorld(), new PlayerModel(0, 0 , 0));
		assertTrue(entityBody.getUserData() instanceof PlayerModel);
		entityBody.setTransform(20 , 25, 30);
		assertEquals(entityBody.getX(), 20 ,0.001f);
		assertEquals(entityBody.getY(), 25, 0.001f);
		assertEquals(entityBody.getAngle(), 30, 0.001f);
	}

	/**
	 * Tests GameController switchToWeapon() methods.
	 */
	@Test
	public void switchWeapon()
	{
		GameController.getInstance().startNewGame();
		assertEquals(GameModel.getInstance().getSelfPlayer().getCurrentWeapon(), PlayerModel.Weapons.RIFLE);
		GameController.getInstance().switchToWeapon(PlayerModel.Weapons.PISTOL);
		assertEquals(GameModel.getInstance().getSelfPlayer().getCurrentWeapon(), PlayerModel.Weapons.PISTOL);
		GameController.getInstance().switchToWeapon(PlayerModel.Weapons.KNIFE);
		assertEquals(GameModel.getInstance().getSelfPlayer().getCurrentWeapon(), PlayerModel.Weapons.KNIFE);
		GameController.getInstance().switchToWeapon(true);
		assertEquals(GameModel.getInstance().getSelfPlayer().getCurrentWeapon(), PlayerModel.Weapons.RIFLE);
		GameController.getInstance().switchToWeapon(false);
		assertEquals(GameModel.getInstance().getSelfPlayer().getCurrentWeapon(), PlayerModel.Weapons.KNIFE);
		GameController.getInstance().switchToWeapon(false);
		assertEquals(GameModel.getInstance().getSelfPlayer().getCurrentWeapon(), PlayerModel.Weapons.PISTOL);
	}

	/**
	 * Tests if zombies aim at the player.
	 */
	@Test
	public void zombieAim()
	{
		GameController.getInstance().startNewGame();
		GameController.getInstance().setGameDifficulty(GameController.Difficulty.HARD);
		GameController.getInstance().startNewRound();
		GameController.getInstance().handleRounds(GameView.States.GAME_RUNNING);
		GameController.getInstance().update(0.1f);
		for(ZombieModel zombie : GameModel.getInstance().getZombies())
		{
			Vector2 playerPos = new Vector2(0, 0);
			playerPos.x = GameModel.getInstance().getSelfPlayer().getX();
			playerPos.y = GameModel.getInstance().getSelfPlayer().getY();
			assertEquals(Math.atan2(playerPos.y - zombie.getY() , playerPos.x - zombie.getX()), zombie.getRotation(), 0.001f);
		}
	}
}
