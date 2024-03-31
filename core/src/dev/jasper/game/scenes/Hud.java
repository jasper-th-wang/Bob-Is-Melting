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
public class Hud implements Disposable {
    /**
     * The health increase when a snowball is added.
     */
    private static final int SNOWBALL_HEALTH_INCREASE = 10;
    /**
     * The maximum health that the HUD can have.
     */
    private static final int MAX_HEALTH = 100;

    /**
     * The padding at the top of the table in the HUD.
     */
    private static final int TABLE_PAD_TOP = 10;
    /**
     * The health of the HUD in integer format.
     */
    private static Integer health;
    /**
     * The Stage instance used for the HUD.
     */
    private final Stage stage;
    /**
     * The Label instance used for displaying the title of the time elapsed.
     */
    private final Label timeTitleLabel;
    /**
     * The Label instance used for displaying the time elapsed.
     */
    private final Label timeLabel;
    /**
     * The Label instance used for displaying the title of the health.
     */
    private final Label healthTitleLabel;
    /**
     * The Label instance used for displaying the health.
     */
    private final Label healthLabel;
    /**
     * The Viewport instance used for the HUD.
     * This is a standalone camera and viewport to have the HUD render independently.
     */
    private final Viewport viewport;
    /**
     * The world timer in integer format.
     */
    private Integer worldTimer;
    /**
     * The time count in float format.
     */
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
        viewport = new FitViewport(BobIsMelting.V_WIDTH, BobIsMelting.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);
        timeTitleLabel = new Label("TIME ELAPSED", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        // Health as a temp name for now
        healthTitleLabel = new Label("HEALTH", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
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
        setHealth(getHealth() + SNOWBALL_HEALTH_INCREASE);
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
