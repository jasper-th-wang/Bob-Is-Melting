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
 * It implements the Disposable interface from the libGDX library to properly dispose of resources when they are no longer needed.
 *
 * @author Jasper Wang
 * @version 2024
 */
public class Hud implements Disposable {
    private final Stage stage;
    Label timeTitleLabel;
    Label timeLabel;
    Label healthTitleLabel;
    Label healthLabel;
    // Standalone camera and viewport to have the HUD render independently.
    private Viewport viewport;
    private Integer worldTimer;
    private float timeCount;
    private Integer health;

    /**
     * Constructs a Hud instance.
     * @param sb - the SpriteBatch instance used for drawing
     */
    public Hud(SpriteBatch sb) {
        worldTimer = 0;
        timeCount = 0;
        health  = 100;
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
        table.add(healthTitleLabel).expandX().padTop(10);
        table.add(timeTitleLabel).expandX().padTop(10);
        table.row();
        table.add(healthLabel).expandX();
        table.add(timeLabel).expandX();
        getStage().addActor(table);

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
     * @return stage - the Stage instance
     */
    public Stage getStage() {
        return stage;
    }
}
