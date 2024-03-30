package dev.jasper.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dev.jasper.game.screens.PlayScreen;

/**
 * Main game loop.
 * It extends the Game class, which means it delegates game logic to a Screen class.
 *
 * @author Jasper Wang
 * @version 2024
 */
public class BobIsMelting extends Game {
	/**
	 * Virtual width of game.
	 */
	public static final int V_WIDTH = 400;
	/**
	 * Virtual height of game.
	 */
	public static final int V_HEIGHT = 208;
	/**
	 * Pixel for meter units to scale the render
	 */
	public static final float PPM = 100;
	/**
	 * Bit value representing the ground in the game's physics engine.
	 */
	public static final short GROUND_BIT = 1;
	/**
	 * Bit value representing the kid character in the game's physics engine.
	 */
	public static final short KID_BIT = 2;
	/**
	 * Bit value representing the snowball in the game's physics engine.
	 */
	public static final short SNOWBALL_BIT = 4;
	/**
	 * Bit value representing destroyed objects in the game's physics engine.
	 */
	public static final short DESTROYED_BIT = 8;
	/**
	 * Bit value representing generic objects in the game's physics engine.
	 */
	public static final short OBJECT_BIT = 16;
	/**
	 * Bit value representing enemies in the game's physics engine.
	 */
	public static final short ENEMY_BIT = 32;
	private SpriteBatch batch;

	/**
	 * Initializes the game. This is called when the game is first created.
	 */
	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
	}

	/**
	 * Renders the game. This is called every frame.
	 */
	@Override
	public void render () {
		super.render();
	}

	/**
	 * Disposes of the game resources. This is called when the game is closing.
	 */
	@Override
	public void dispose () {
		getBatch().dispose();
	}

	public SpriteBatch getBatch() {
		return batch;
	}
}
