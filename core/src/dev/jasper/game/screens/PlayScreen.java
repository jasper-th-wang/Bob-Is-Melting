package dev.jasper.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.jasper.game.BobIsMelting;
import dev.jasper.game.scenes.Hud;
import dev.jasper.game.tools.GameStateManager;
import dev.jasper.game.tools.InputHandler;

/**
 * The PlayScreen class represents the main game screen where the gameplay happens.
 * It implements the Screen interface from the libGDX library.
 *
 * @author Jasper Wang
 * @version 2024
 */
public class PlayScreen implements Screen {


    /**
     * The position iterations for the Box2D world's physics simulation.
     * This value is used when updating the Box2D world's state.
     */
    protected static final int POSITION_ITERATIONS = 2;
    private final BobIsMelting game;
    private final GameStateManager gameStateManager;
    private final InputHandler inputHandler;
    private final OrthographicCamera gameCam;
    private final Viewport gamePort;
    private final Hud hud;
    private final OrthogonalTiledMapRenderer renderer;
    private final Box2DDebugRenderer b2dr;

    /**
     * Constructs a PlayScreen instance.
     * @param gameInstance - the main game instance
     */
    public PlayScreen(final BobIsMelting gameInstance) {
        this.game = gameInstance;

        // Set up game camera
        gameCam = new OrthographicCamera();
        // Create a FitViewport to maintain virtual aspect ratio
        gamePort = new FitViewport(BobIsMelting.V_WIDTH / BobIsMelting.PPM,
                BobIsMelting.V_HEIGHT / BobIsMelting.PPM, gameCam);
        // Set up game camera to be centered correctly
        gameCam.position.set(gamePort.getWorldWidth() / POSITION_ITERATIONS,
                gamePort.getWorldHeight() / POSITION_ITERATIONS, 0);

        hud = new Hud(game.getBatch());

        this.gameStateManager = new GameStateManager();
        inputHandler = new InputHandler(gameStateManager.getKid());
        renderer = new OrthogonalTiledMapRenderer(gameStateManager.getMap(), 1 / BobIsMelting.PPM);

        b2dr = new Box2DDebugRenderer();
    }

    @Override
    public void show() {

    }

    /**
     * Renders the game world and its components.
     * @param delta - the time difference between the current and the last frame
     */
    @Override
    public void render(final float delta) {
        update(delta);

        // Clear the screen
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render game map
        renderer.render();

        // Render Box2DDebugLines
        b2dr.render(gameStateManager.getWorld(), gameCam.combined);

        game.getBatch().setProjectionMatrix(gameCam.combined);
        game.getBatch().begin();
        gameStateManager.draw(game.getBatch());
        game.getBatch().end();

        // Set our batch to draw what the HUD camera sees
        game.getBatch().setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.getStage().draw();
    }

    /**
     * Takes in a delta time to update the game world's state.
     * @param dt - delta time
     */
    public void update(final float dt) {
        // Handle user input first
        inputHandler.handleInput();

        hud.update(dt);
        gameStateManager.update(dt);

        // Avoid camera go over boundary
        MapProperties prop = gameStateManager.getMap().getProperties();
        int mapWidth = prop.get("width", Integer.class);
        int tilePixelWidth = prop.get("tilewidth", Integer.class);
        float mapPixelWidth = mapWidth * tilePixelWidth / BobIsMelting.PPM;

        // The right boundary of the map (x + width)
        final float cameraHalfWidth = gamePort.getCamera().viewportWidth * .5f;

        gameCam.position.x = gameStateManager.getKid().getB2body().getPosition().x;
        gameCam.position.x = MathUtils.clamp(gameCam.position.x,
                cameraHalfWidth + tilePixelWidth / BobIsMelting.PPM,
                mapPixelWidth - cameraHalfWidth - tilePixelWidth / BobIsMelting.PPM);

        // Update our gameCam with correct coordinates after changes
        gameCam.update();
        // Tell the renderer to draw only what our camera can see in our game world.
        renderer.setView(gameCam);
    }

    /**
     * Resizes the game viewport based on the new width and height.
     * @param width - the new width
     * @param height - the new height
     */
    @Override
    public void resize(final int width, final int height) {
        gamePort.update(width, height);
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

    /**
     * Disposes of all the resources used in the game.
     */
    @Override
    public void dispose() {
        gameStateManager.dispose();
        renderer.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
