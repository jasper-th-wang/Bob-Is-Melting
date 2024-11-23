package dev.jasper.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.jasper.game.BobIsMelting;
import dev.jasper.game.tools.GameStateManager;

/**
 * The GameOverScreen class represents the game over screen in the game.
 * It implements the Screen interface from the libGDX library.
 * This screen is displayed when the player loses the game.
 *
 * @author Jasper Wang
 * @version 2024
 */
public final class GameOverScreen implements Screen {
    private static final int TABLE_PAD_TOP = 10;
    private final Stage stage;

    private final BobIsMelting game;

    /**
     * Constructs a GameOverScreen with the specified game.
     * It initializes the viewport and stage, and sets up the game over screen layout.
     *
     * @param game The game instance.
     */
    public GameOverScreen(final BobIsMelting game) {
        this.game = game;
        Viewport viewport = new FitViewport(BobIsMelting.V_WIDTH, BobIsMelting.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.getBatch());

        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        GameStateManager gameStateManager = game.getGameStateManager();
        final int timeElapsed = gameStateManager.getWorldTimer();
        Label gameOverLabel = new Label("Bob's a puddle now!", font);
        String timeElapsedMessage = "Bob lasted " + timeElapsed + " seconds before melting away";
        Label timeElapsedLabel = new Label(timeElapsedMessage, font);
        Label playAgainLabel = new Label("Click to Play Again", font);

        table.add(gameOverLabel).expandX();
        table.row();
        table.add(timeElapsedLabel).expandX();
        table.row();
        table.add(playAgainLabel).expandX().padTop(TABLE_PAD_TOP);

        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(final float delta) {
        if (Gdx.input.justTouched()) {
            game.startNewGame();
            dispose();
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(final int width, final int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
