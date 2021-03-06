package com.batataproductions.game.view.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.batataproductions.game.DeathmatchMania;
import com.batataproductions.game.model.entities.EntityModel;

import static com.batataproductions.game.view.GameView.PIXEL_TO_METER;

/**
 * A abstract view capable of holding a sprite with a certain
 * position and rotation.
 *
 * This view is able to update its data based on a entity model.
 */
public abstract class EntityView {
	/**
	 * The sprite representing this entity view.
	 */
	Sprite sprite;

	/**
	 * Creates a view belonging to a game.
	 *
	 * @param game the game this view belongs to. Needed to access the
	 *             asset manager to get textures.
	 */
	EntityView(DeathmatchMania game) {
		sprite = createSprite(game);
	}

	/**
	 * Draws the sprite from this view using a sprite batch.
	 *
	 * @param batch The sprite batch to be used for drawing.
	 */
	public void draw(SpriteBatch batch) {
		sprite.draw(batch);
	}

	/**
	 * Abstract method that creates the view sprite. Concrete
	 * implementation should extend this method to create their
	 * own sprites.
	 *
	 * @param game the game this view belongs to. Needed to access the
	 *             asset manager to get textures.
	 * @return the sprite representing this view.
	 */
	public abstract Sprite createSprite(DeathmatchMania game);

	/**
	 * Updates this view based on a certain model.
	 *
	 * @param model the model used to update this view
	 */
	public void update(EntityModel model) {
		sprite.setCenter(model.getX() / PIXEL_TO_METER, model.getY() / PIXEL_TO_METER);
		sprite.setRotation((float) Math.toDegrees(model.getRotation()));
	}
}