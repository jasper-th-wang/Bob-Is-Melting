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


/**
 * The Hud class represents the heads-up display (HUD) in the game.
 * It implements the Disposable interface from the libGDX library
 * to properly dispose of resources when they are no longer needed.
 *
 * @author Jasper Wang
 * @version 2024
 */
public final class Hud implements Disposable {
    private static final int SNOWBALL_HEALTH_INCREASE = 10;
    private static final int MAX_HEALTH = 100;
    private static final int TABLE_PAD_TOP = 10;
    private static Integer health;
    private final Stage stage;
    private final Label timeLabel;
    private final Label healthLabel;
    private Integer worldTimer;
    private float timeCount;

    /**
     * Constructs a Hud instance.
     *
     * @param sb - the SpriteBatch instance used for drawing
     */
    public Hud(final SpriteBatch sb) {
        worldTimer = 0;
        timeCount = 0;
        health = MAX_HEALTH;
        Viewport viewport = new FitViewport(BobIsMelting.V_WIDTH, BobIsMelting.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);
        Label timeTitleLabel = new Label("TIME ELAPSED", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        // Health as a temp name for now
        Label healthTitleLabel = new Label("HEALTH", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        healthLabel = new Label(String.format("%02d", health), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(healthTitleLabel).expandX().padTop(TABLE_PAD_TOP);
        table.add(timeTitleLabel).expandX().padTop(TABLE_PAD_TOP);
        table.row();

        table.add(healthLabel).expandX();
        table.add(timeLabel).expandX();
        getStage().addActor(table);
    }

    /**
     * Returns the health of the HUD.
     *
     * @return health - the health of the HUD
     */
    public static Integer getHealth() {
        return health;
    }

    /**
     * Sets the health of the HUD.
     *
     * @param newHealth - the health to be set
     */
    public static void setHealth(final Integer newHealth) {
        Hud.health = newHealth;
    }

    /**
     * Adds a snowball to the HUD.
     * This increases the health by the value of snowballHealthIncrease.
     */
    public static void addSnowball() {
        final int newHealth = Math.min(getHealth() + SNOWBALL_HEALTH_INCREASE, MAX_HEALTH);
        setHealth(newHealth);
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
        timeCount += dt;
        // When exactly 1 second has passed, increment and update the world timer and corresponding HUD element
        if (timeCount >= 1) {
            worldTimer++;
            timeLabel.setText(String.format("%03d", worldTimer));
            setHealth(health - 2);

            timeCount = 0;
        }
        healthLabel.setText(String.format("%02d", health));
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
