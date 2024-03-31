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
public final class BobIsMelting extends Game {
    /**
     * Virtual width of game.
     */
    public static final int V_WIDTH = 400;
    /**
     * Virtual height of game.
     */
    public static final int V_HEIGHT = 208;
    /**
     * Pixel for meter units to scale the render.
     */
    public static final float PPM = 100;
    /**
     * SpriteBatch allows for efficient rendering of sprites.
     * This is used in the game to draw 2D bitmaps that can be composed into complex scenes.
     */
    private SpriteBatch batch;

    /**
     * Initializes the game. This is called when the game is first created.
     */
    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new PlayScreen(this));
    }

    /**
     * Renders the game. This is called every frame.
     */
    @Override
    public void render() {
        super.render();
    }

    /**
     * Disposes of the game resources. This is called when the game is closing.
     */
    @Override
    public void dispose() {
        getBatch().dispose();
    }

    /**
     * Returns the SpriteBatch used for rendering sprites in the game.
     * This SpriteBatch is used to draw 2D bitmaps that can be composed into complex scenes.
     *
     * @return the SpriteBatch instance used in the game.
     */
    public SpriteBatch getBatch() {
        return batch;
    }

}
