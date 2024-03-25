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
