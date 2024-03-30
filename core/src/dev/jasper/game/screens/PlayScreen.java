package dev.jasper.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.jasper.game.BobIsMelting;
import dev.jasper.game.scenes.Hud;
import dev.jasper.game.sprites.Bear;
import dev.jasper.game.sprites.Kid;
import dev.jasper.game.tools.B2WorldCreator;
import dev.jasper.game.tools.WorldContactListener;

/**
 * The PlayScreen class represents the main game screen where the gameplay happens.
 * It implements the Screen interface from the libGDX library.
 *
 * @author Jasper Wang
 * @version 2024
 */
public class PlayScreen implements Screen {
    private final BobIsMelting game;
    private final TextureAtlas atlas;
    private final Kid player;
    private final Bear tempBear;
    private final OrthographicCamera gameCam;
    private final Viewport gamePort;
    private final Hud hud;
    private final TiledMap map;
    private final OrthogonalTiledMapRenderer renderer;
    // Box2d variables
    private final World world;
    private final Box2DDebugRenderer b2dr;

    /**
     * Handles the user input for controlling the player character.
     * @param dt - delta time
     */
    public void handleInput(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && player.getState() != Kid.State.JUMPING && player.getState() != Kid.State.FALLING) {
            final float jumpVelocity = player.getIsInvincibleToEnemy() ? 2f : 3.2f;
            player.getB2body().applyLinearImpulse(new Vector2(0, jumpVelocity), player.getB2body().getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.getB2body().getLinearVelocity().x <= 2) {
            final float speed = player.getIsInvincibleToEnemy() ? 0.04f : 0.1f;
            player.getB2body().applyLinearImpulse(new Vector2(speed, 0), player.getB2body().getWorldCenter(),true );
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.getB2body().getLinearVelocity().x >= -2) {
            final float speed = player.getIsInvincibleToEnemy() ? 0.04f : 0.1f;
            player.getB2body().applyLinearImpulse(new Vector2(-speed, 0), player.getB2body().getWorldCenter(),true );
        }
    }
    /**
     * Takes in a delta time to update the game world's state.
     * @param dt - delta time
     */
    public void update(float dt) {
        // Handle user input first
        handleInput(dt);
        world.step(1/60f, 6, 2);

        player.update(dt);
        tempBear.update(dt);

        // Avoid camera go over boundary
        MapProperties prop = map.getProperties();
        int mapWidth = prop.get("width", Integer.class);
        int tilePixelWidth = prop.get("tilewidth", Integer.class);
        float mapPixelWidth = mapWidth * tilePixelWidth / BobIsMelting.PPM;

        // The right boundary of the map (x + width)
        float cameraHalfWidth = gamePort.getCamera().viewportWidth * .5f;

        gameCam.position.x = player.getB2body().getPosition().x;
        gameCam.position.x = MathUtils.clamp(gameCam.position.x, cameraHalfWidth + tilePixelWidth / BobIsMelting.PPM, mapPixelWidth - cameraHalfWidth - tilePixelWidth / BobIsMelting.PPM);

        // Update our gameCam with correct coordinates after changes
        gameCam.update();
        // Tell the renderer to draw only what our camera can see in our game world.
        renderer.setView(gameCam);
    }
    /**
     * Constructs a PlayScreen instance.
     * @param game - the main game instance
     */
    public PlayScreen(BobIsMelting game) {
        this.atlas = new TextureAtlas("Characters.atlas");
        this.game = game;
        gameCam = new OrthographicCamera();
        // Create a FitViewport to maintain virtual aspect ratio
        gamePort = new FitViewport(BobIsMelting.V_WIDTH / BobIsMelting.PPM, BobIsMelting.V_HEIGHT / BobIsMelting.PPM, gameCam);
        hud = new Hud(game.getBatch());
        // Load the map and set up map renderer
        // Tiled map variables
        TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load("mainNew.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / BobIsMelting.PPM);
        // Set up game camera to be centered correctly
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        // Initialize Box2d game world
        world = new World(new Vector2(0,-10), true);
        b2dr = new Box2DDebugRenderer();
        new B2WorldCreator(this);
        this.player = new Kid(this);
        world.setContactListener(new WorldContactListener());
        tempBear = new Bear(this, .32f, .32f);

    }
    /**
     * Returns the TextureAtlas instance used in the game.
     * @return atlas - the TextureAtlas instance
     */
    public TextureAtlas getAtlas() {
        return atlas;
    }
    @Override
    public void show() {

    }

    /**
     * Renders the game world and its components.
     * @param delta - the time difference between the current and the last frame
     */
    @Override
    public void render(float delta) {
        update(delta);

        // Clear the screen
        Gdx.gl.glClearColor (1, 0, 0, 1);
        Gdx.gl.glClear (GL20.GL_COLOR_BUFFER_BIT);

        // Render game map
        renderer.render();

        // Render Box2DDebugLines
        b2dr.render(world, gameCam.combined);

        game.getBatch().setProjectionMatrix(gameCam.combined);
        game.getBatch().begin();
        player.draw(game.getBatch());
        tempBear.draw(game.getBatch());
        game.getBatch().end();

        // Set our batch to draw what the HUD camera sees
        game.getBatch().setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.getStage().draw();
    }


    /**
     * Resizes the game viewport based on the new width and height.
     * @param width - the new width
     * @param height - the new height
     */
    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    /**
     * Returns the TiledMap instance used in the game.
     * @return map - the TiledMap instance
     */
    public TiledMap getMap() {
        return map;
    }

    /**
     * Returns the World instance used in the game.
     * @return world - the World instance
     */
    public World getWorld() {
        return world;
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
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
