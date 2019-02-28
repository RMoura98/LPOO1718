package com.batataproductions.game.tests;

import com.badlogic.gdx.math.Vector2;
import com.batataproductions.game.model.GameModel;
import com.batataproductions.game.model.entities.BloodSpillModel;
import com.batataproductions.game.model.entities.BulletModel;
import com.batataproductions.game.model.entities.EntityModel;
import com.batataproductions.game.model.entities.PlayerModel;
import com.batataproductions.game.model.entities.ZombieModel;

import org.junit.Test;

import static org.junit.Assert.*;

public class ModelTests {

	/**
	 * Tests weapon mechanics such as firing and reloading.
	 */
	@Test
	public void playerModelWeapon()
	{
		PlayerModel player = new PlayerModel(0,0,0);
		// Test knife weapon change.
		player.setCurrentWeapon(PlayerModel.Weapons.KNIFE);
		assertEquals(player.getCurrentWeapon(), PlayerModel.Weapons.KNIFE);
		// Test rifle weapon change.
		player.setCurrentWeapon(PlayerModel.Weapons.RIFLE);
		assertEquals(player.getCurrentWeapon(), PlayerModel.Weapons.RIFLE);
		// Test pistol weapon change.
		player.setCurrentWeapon(PlayerModel.Weapons.PISTOL);
		assertEquals(player.getCurrentWeapon(), PlayerModel.Weapons.PISTOL);
		//// Test rifle ammo consumption.
		for(int i = PlayerModel.MAX_RIFLE_AMMO ; i >= 0; i--)
		{
			assertEquals(player.getRifleCurrentAmmo(), i);
			player.fireRifle();
		}
		assertEquals(player.getRifleCurrentAmmo(), 0);
		player.fireRifle();
		assertEquals(player.getRifleCurrentAmmo(), 0);
		player.replenishRifleAmmo();
		assertEquals(player.getRifleCurrentAmmo(), PlayerModel.MAX_RIFLE_AMMO);
		//// Test pistol ammo consumption.
		for(int i = PlayerModel.MAX_PISTOL_AMMO ; i >= 0; i--)
		{
			assertEquals(player.getPistolCurrentAmmo(), i);
			player.firePistol();
		}
		assertEquals(player.getPistolCurrentAmmo(), 0);
		player.fireRifle();
		assertEquals(player.getPistolCurrentAmmo(), 0);
		player.replenishPistolAmmo();
		assertEquals(player.getPistolCurrentAmmo(), PlayerModel.MAX_PISTOL_AMMO);
	}

	/**
	 * Tests player health mechanics such as taking damage and life regeneration.
	 */
	@Test
	public void PlayerModelHealth()
	{
		PlayerModel player = new PlayerModel(0, 0, 0);
		assertEquals(player.isDead(), false);
		for(int i = 100; i >= 0 ; i--)
		{
			assertEquals(player.getHealth(), i, 0.001f);
			player.takeDamage(1);
		}
		player.takeDamage(15);
		assertEquals(player.getHealth(), 0, 0.001f);
		player.takeDamage(50);
		assertEquals(player.getHealth(), 0, 0.001f);
		assertEquals(player.isDead(), true);
		player.setHealth(1);
		player.regenerateHealth(100);
		assertEquals(player.isDead(), false);
		assertEquals(player.getHealth(), PlayerModel.DEFAULT_HEALTH, 0.001f);
	}

	/**
	 * Tests entities knockback mechanic.
	 */
	@Test
	public void EntityModelTestKnockback()
	{
		GameModel.getInstance().startNewGame();
		EntityModel model = new PlayerModel(0, 0, 0);
		Vector2 apply_knockback = new Vector2(1.4f, 1.4f);
		model.setKnockBack(10, apply_knockback);
		Vector2 knockback_vector = model.get_knockBack_vector(0.05f);
		float k = (float) (knockback_vector.x / 1.4);
		float u = (float) (knockback_vector.y / 1.4);
		assertEquals(k, u, 0.001);
		assertEquals(knockback_vector.angle(), apply_knockback.angle(), 0.001f);
	}

	/**
	 * Tests the getType() of each Entity Model sub class.
	 */
	@Test
	public void EntityModelsTypes(){
		EntityModel player = new PlayerModel(0, 0,0 );
		EntityModel zombie = new ZombieModel(0, 0,0, ZombieModel.Sizes.BIG );
		EntityModel spill = new BloodSpillModel(0, 0, ZombieModel.Sizes.BIG );
		EntityModel bullet = new BulletModel(0, 0, 0);
		assertEquals(player.getType(), EntityModel.ModelType.PLAYER);
		assertEquals(zombie.getType(), EntityModel.ModelType.ZOMBIE_BIG);
		assertEquals(spill.getType(), EntityModel.ModelType.BLOOD_SPILL);
		assertEquals(bullet.getType(), EntityModel.ModelType.BULLET);
	}

	/**
	 * Tests if the bullets report the correct damage they hit for.
	 */
	@Test
	public void BulletModelDamage()
	{
		BulletModel bullet = new BulletModel(0, 0 ,0);
		bullet.setGun(PlayerModel.Weapons.PISTOL);
		assertEquals(bullet.getDamageToGive(), BulletModel.DEFAULT_DAMAGE_PISTOL_BULLET, 0.001f);
		bullet.setGun(PlayerModel.Weapons.RIFLE);
		assertEquals(bullet.getDamageToGive(), BulletModel.DEFAULT_DAMAGE_RIFLE_BULLET, 0.001f);
	}

	/**
	 * Tests the getX(), getY(), getAngle(), setPosition(), setRotation() methods of the EntityModel class.
	 */
	@Test
	public void EntityModelPosAngle()
	{
		EntityModel player = new PlayerModel(0 ,0 ,0);
		assertEquals(player.getX(), 0, 0.001f);
		assertEquals(player.getY(), 0, 0.001f);
		assertEquals(player.getRotation(), 0, 0.001f);
		player.setPosition(20, 25);
		assertEquals(player.getX(), 20, 0.001f);
		assertEquals(player.getY(), 25, 0.001f);
		player.setRotation(1);
		assertEquals(player.getRotation(), 1, 0.001f);
	}

	/**
	 * Tests the GameModel bullet methods.
	 */
	@Test
	public void GameModelBullet()
	{
		GameModel.getInstance().startNewGame();
		BulletModel bullet = GameModel.getInstance().getBullet(PlayerModel.Weapons.PISTOL);
		assertNotNull(bullet);
		assertEquals(GameModel.getInstance().getBullets().size(), 1);
		GameModel.getInstance().remove(bullet);
		assertEquals(GameModel.getInstance().getBullets().size(), 0);
	}

	/**
	 * Tests the GameModel newGame method.
	 */
	@Test
	public void GameModelNewGame()
	{
		GameModel.getInstance().startNewGame();
		BulletModel bullet = GameModel.getInstance().getBullet(PlayerModel.Weapons.PISTOL);
		BulletModel bullet2 = GameModel.getInstance().getBullet(PlayerModel.Weapons.RIFLE);
		ZombieModel zombie = new ZombieModel(0, 0, 0, ZombieModel.Sizes.BIG);
		GameModel.getInstance().addZombie(zombie);
		GameModel.getInstance().startNewGame();
		assertEquals(GameModel.getInstance().getBullets().size(), 0);
		assertEquals(GameModel.getInstance().getZombies().size(), 0);
		assertEquals(GameModel.getInstance().getPlayers().size(), 1);
	}
}