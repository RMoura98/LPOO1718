package com.batataproductions.game.controller.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.batataproductions.game.model.entities.PlayerModel;

/**
 * A concrete representation of an EntityBody
 * representing a player.
 */
public class PlayerBody extends EntityBody {

	/* Fixture placed at the edge of where the sprite's rifle barrel is located. */
	private Fixture rifleBarrel;
	/* Fixture placed at the edge of where the sprite's pistol barrel is located. */
	private Fixture pistolBarrel;
	/* Fixture placed at the point where the knife hits zombies. */
	private Fixture knifePoint;

	/**
	 * Constructs a player body according to
	 * a player model.
	 *
	 * @param world the physical world this player belongs to.
	 * @param model the model representing this player.
	 */
	public PlayerBody(World world, PlayerModel model) {
		super(world, model);

		float density = 0.5f, friction = 0.4f, restitution = 0.5f;
		int width = 155, height = 105;

		// Body
		createFixture(body, new float[]{
		//		0, 0, 300, 0, 300, 205, 0 , 205
				//15, 18,
				15, 35,
				30, 18,
				15, 96,
				120, 96,
				120, 60,
				//115, 18
				90, 18
		}, width, height, density, friction, restitution, PLAYER_BODY, (short) ~0, false);
		// Pistol Fixture.
		rifleBarrel = createFixture(body, new float[]{
				100 + 5 + 35, 70 + 5 - 3,
				100 + 5 + 35, 70 + 10 - 3,
				100 + 10 + 35, 70 + 10 - 3,
				100 + 10 + 35, 70 + 5 - 3
		}, width, height, density, friction, restitution, (short) 0, (short) 0, true);
		// Rifle Fixture.
		pistolBarrel = createFixture(body, new float[]{
				100 + 5 + 20, 70 + 5,
				100 + 5 + 20, 70 + 10,
				100 + 10 + 20, 70 + 10,
				100 + 10 + 20, 70 + 5
		}, width, height, density, friction, restitution, (short) 0, (short) 0, true);
		// Knife Fixture.
		knifePoint = createFixture(body, new float[]{
				100 + 5 + 35 + 15, 70 + 5 - 3,
				100 + 5 + 35 + 15, 70 + 10 - 3,
				100 + 10 + 35 + 15, 70 + 10 - 3,
				100 + 10 + 35 + 15, 70 + 5 - 3
		}, width, height, density, friction, restitution, PLAYER_BODY, (short) 0, true);
	}

	/**
	 * Returns the position from which the bullets should exit the weapon.
	 * @return A vector2 object holding the information about the world point of the player's weapon barrel.
	 */
	public Vector2 getWeaponFiringPosition(){
		Vector2 returnVector = new Vector2();

		PlayerModel.Weapons currentWeapon = ((PlayerModel)body.getUserData()).getCurrentWeapon();
		// Rifle
		if( currentWeapon == PlayerModel.Weapons.RIFLE )
		{
			PolygonShape shape = ((PolygonShape)rifleBarrel.getShape());
			shape.getVertex(0, returnVector);
		}
		// Pistol
		else if( currentWeapon == PlayerModel.Weapons.PISTOL )
		{
			PolygonShape shape = ((PolygonShape)pistolBarrel.getShape());
			shape.getVertex(0, returnVector);
		}

		returnVector = body.getWorldPoint(returnVector);
		return returnVector;
	}

	public void enableKnifeHitFixture()
	{
		Filter curFilter = knifePoint.getFilterData();
		curFilter.maskBits = ZOMBIE_BODY;
		knifePoint.setFilterData(curFilter);
	}

	public void disableKnifeHitFixture()
	{
		Filter curFilter = knifePoint.getFilterData();
		curFilter.maskBits = 0;
		knifePoint.setFilterData(curFilter);
	}
}