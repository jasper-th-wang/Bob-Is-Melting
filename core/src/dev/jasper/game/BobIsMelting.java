package dev.jasper.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dev.jasper.game.screens.PlayScreen;

/**
 * Main game loop.
 * It extends the Game class, which means it delegates game logic to a Screen class.
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
	public static final short GROUND_BIT = 1;
	public static final short KID_BIT = 2;
	public static final short SNOWBALL_BIT = 4;
	public static final short DESTROYED_BIT = 8;
	public static final short OBJECT_BIT = 16;
	public static final short ENEMY_BIT = 32;
	public SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
