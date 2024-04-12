package dev.jasper.game.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.jasper.game.BobIsMelting;
import dev.jasper.game.tools.GameStateManager;


/**
 * The Hud class represents the heads-up display (HUD) in the game.
 * It implements the Disposable interface from the libGDX library
 * to properly dispose of resources when they are no longer needed.
 *
 * @author Jasper Wang
 * @version 2024
 */
public final class Hud implements Disposable {
    private static final int TABLE_PAD_TOP = 10;
    private final Stage stage;
    private final Label timeLabel;
    private final Label healthLabel;
    private final GameStateManager gameStateManager;

    /**
     * Constructs a Hud instance.
     *
     * @param sb - the SpriteBatch instance used for drawing
     */
    public Hud(final GameStateManager gameStateManager, final SpriteBatch sb) {
        this.gameStateManager = gameStateManager;
        Viewport viewport = new FitViewport(BobIsMelting.V_WIDTH, BobIsMelting.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);
        Label timeTitleLabel = new Label("TIME ELAPSED", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label(String.format("%03d", gameStateManager.getWorldTimer()), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        // Health as a temp name for now
        Label healthTitleLabel = new Label("BOB'S HEALTH", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        healthLabel = new Label(String.format("%02d", gameStateManager.getBobsHealth()), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(healthTitleLabel).expandX().padTop(TABLE_PAD_TOP);
        table.add(timeTitleLabel).expandX().padTop(TABLE_PAD_TOP);
        table.row();

        table.add(healthLabel).expandX();
        table.add(timeLabel).expandX();
        getStage().addActor(table);
    }

    /**
     * Updates the time count and health of the HUD.
     * This method is called periodically to update the time count and health.
     * If the time count exceeds 1 second, the world timer is incremented and the health is decreased by 2.
     * The time count is then reset to 0.
     *
     * @param dt The time delta, representing the amount of time passed since the last update.
     */
    public void update(final float dt) {
        timeLabel.setText(String.format("%03d", gameStateManager.getWorldTimer()));
        healthLabel.setText(String.format("%02d", gameStateManager.getBobsHealth()));
    }

    /**
     * Disposes of all the resources used in the HUD.
     */
    @Override
    public void dispose() {
        getStage().dispose();
    }

    /**
     * Returns the Stage instance used in the HUD.
     *
     * @return stage - the Stage instance
     */
    public Stage getStage() {
        return stage;
    }
}
