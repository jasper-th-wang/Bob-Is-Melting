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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.jasper.game.BobIsMelting;
import dev.jasper.game.scenes.Hud;
import dev.jasper.game.sprites.Bear;
import dev.jasper.game.sprites.Bob;
import dev.jasper.game.sprites.Kid;
import dev.jasper.game.sprites.Snowball;
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
    /**
     * The jump velocity for the player character when it is invincible.
     * This value is used when handling the player's jump input.
     */
    protected static final float INVINCIBLE_JUMP_VELOCITY = 2.2f;

    /**
     * The normal jump velocity for the player character.
     * This value is used when handling the player's jump input.
     */
    protected static final float NORMAL_JUMP_VELOCITY = 3.2f;

    /**
     * The run velocity for the player character when it is invincible.
     * This value is used when handling the player's run input.
     */
    protected static final float INVINCIBLE_RUN_VELOCITY = 0.04f;

    /**
     * The normal run velocity for the player character.
     * This value is used when handling the player's run input.
     */
    protected static final float NORMAL_RUN_VELOCITY = 0.1f;

    /**
     * The time step for the Box2D world's physics simulation.
     * This value is used when updating the Box2D world's state.
     */
    protected static final float TIME_STEP = 1 / 60f;

    /**
     * The velocity iterations for the Box2D world's physics simulation.
     * This value is used when updating the Box2D world's state.
     */
    protected static final int VELOCITY_ITERATIONS = 6;

    /**
     * The position iterations for the Box2D world's physics simulation.
     * This value is used when updating the Box2D world's state.
     */
    protected static final int POSITION_ITERATIONS = 2;
    /**
     * The interval at which snowballs spawn in the game.
     * This value is used when spawning snowballs in the game world.
     */
    protected static final float SNOWBALL_SPAWN_INTERVAL = 3f;
    /**
     * The gravity value for the game world in the Y-axis.
     * This value is used when creating the Box2D world for physics simulation.
     */
    private static final int GRAVITY_Y = -10;
    /**
     * The maximum number of snowballs that can exist in the game at any given time.
     * This value is used when initializing the snowballs in the game.
     */
    private static final int MAX_SNOWBALL_COUNT = 5;
    /**
     * The main game instance.
     */
    private final BobIsMelting game;

    private final Array<Snowball> currentSpawnedSnowballs;
    /**
     * TextureAtlas instance for loading textures for the game.
     */
    private final TextureAtlas atlas;
    /**
     * The player character in the game.
     */
    private final Kid player;
    /**
     * The Bob character in the game.
     */
    private final Bob bob;
    /**
     * Temporary Bear character in the game.
     * TODO: This is temporary and needs to be replaced.
     */
    private final Bear tempBear;
    /**
     * The main camera used in the game.
     */
    private final OrthographicCamera gameCam;
    /**
     * The viewport of the game, used for handling screen sizes.
     */
    private final Viewport gamePort;
    /**
     * The HUD (Heads-Up Display) used in the game.
     */
    private final Hud hud;
    /**
     * The TiledMap instance used for the game map.
     */
    private final TiledMap map;
    /**
     * The renderer used for rendering the TiledMap.
     */
    private final OrthogonalTiledMapRenderer renderer;
    /**
     * The Box2D world used for physics simulation in the game.
     */
    private final World world;
    /**
     * The Box2DDebugRenderer used for rendering debug information of the Box2D world.
     */
    private final Box2DDebugRenderer b2dr;
    /**
     * Timer for spawning snowballs.
     */
    private float snowballSpawnTimer;

    /**
     * Constructs a PlayScreen instance.
     * @param gameInstance - the main game instance
     */
    public PlayScreen(final BobIsMelting gameInstance) {
        this.atlas = new TextureAtlas("Characters.atlas");
        this.game = gameInstance;

        // Set up game camera
        gameCam = new OrthographicCamera();
        // Create a FitViewport to maintain virtual aspect ratio
        gamePort = new FitViewport(BobIsMelting.V_WIDTH / BobIsMelting.PPM,
                BobIsMelting.V_HEIGHT / BobIsMelting.PPM, gameCam);
        // Set up game camera to be centered correctly
        gameCam.position.set(gamePort.getWorldWidth() / POSITION_ITERATIONS,
                gamePort.getWorldHeight() / POSITION_ITERATIONS, 0);

        hud = new Hud(gameInstance.getBatch());

        // Load the map and set up map renderer
        // Tiled map variables
        TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load("mainNew.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / BobIsMelting.PPM);

        // Initialize Box2d game world
        world = new World(new Vector2(0, GRAVITY_Y), true);
        b2dr = new Box2DDebugRenderer();
        final B2WorldCreator b2WorldCreator = new B2WorldCreator(this);

        // Initialize in game objects
        this.player = new Kid(this);
        this.bob = new Bob(this);
        world.setContactListener(new WorldContactListener());
        tempBear = new Bear(this, .32f, .32f);

        this.snowballSpawnSpots = b2WorldCreator.getSnowballSpawnSpots();
        this.nextSnowballSpawnSpots = new Array<>();
        nextSnowballSpawnSpots.addAll(snowballSpawnSpots);
        nextSnowballSpawnSpots.shuffle();
        Gdx.app.log("snow", String.valueOf(nextSnowballSpawnSpots.size));

        currentSpawnedSnowballs = initializeSnowballs();
    }

    private Array<Snowball> initializeSnowballs() {
        final Array<Snowball> snowballs;
        snowballs = new Array<>();
        for (int i = 0; i < MAX_SNOWBALL_COUNT; i++) {
            snowballs.add(null);
        }
        return snowballs;
    }

    /**
     * List of snowball spawn spots.
     */
    private final Vector2[] snowballSpawnSpots;

    private final Array<Vector2> nextSnowballSpawnSpots;


    /**
     * List of snowballs in the game.
     */

    private void spawnSnowballs(final float dt) {
        snowballSpawnTimer += dt;
        if (snowballSpawnTimer <= SNOWBALL_SPAWN_INTERVAL) {
            return;
        }
        snowballSpawnTimer = 0;

        // make one snowball
        for (int i = 0; i < currentSpawnedSnowballs.size; i++) {
            if (currentSpawnedSnowballs.get(i) == null) {
                final Vector2 spawnSpot = nextSnowballSpawnSpots.pop();
                currentSpawnedSnowballs.set(i, new Snowball(this, spawnSpot, currentSpawnedSnowballs, i));
//                nextSnowballSpawnSpots.addLast(spawnSpot);

                if (nextSnowballSpawnSpots.size == 0) {
                    nextSnowballSpawnSpots.addAll(snowballSpawnSpots);
                    nextSnowballSpawnSpots.shuffle();
                }
                return;
            }
        }
    }

    /**
     * Handles the user input for controlling the player character.
     * @param dt - delta time
     */
    public void handleInput(final float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && player.getState() != Kid.State.JUMPING
                && player.getState() != Kid.State.FALLING) {

            handlePlayerJump();

        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)
                && player.getB2body().getLinearVelocity().x <= POSITION_ITERATIONS) {
            handlePlayerRun(true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)
                && player.getB2body().getLinearVelocity().x >= -POSITION_ITERATIONS) {
                    handlePlayerRun(false);
        }
    }

    private void handlePlayerRun(final boolean isRunningRight) {
        float speed;
        if (player.getIsInvincibleToEnemy()) {
            speed = INVINCIBLE_RUN_VELOCITY;
        } else {
            speed = NORMAL_RUN_VELOCITY;
        }

        if (!isRunningRight) {
            speed = speed * -1;
        }

        player.getB2body().applyLinearImpulse(new Vector2(speed, 0),
                player.getB2body().getWorldCenter(), true);
    }

    private void handlePlayerJump() {
        final float jumpVelocity;
        if (player.getIsInvincibleToEnemy()) {
            jumpVelocity = INVINCIBLE_JUMP_VELOCITY;
        } else {
            jumpVelocity = NORMAL_JUMP_VELOCITY;
        }

        player.getB2body().applyLinearImpulse(new Vector2(0, jumpVelocity),
                player.getB2body().getWorldCenter(), true);
    }

    /**
     * Takes in a delta time to update the game world's state.
     * @param dt - delta time
     */
    public void update(final float dt) {
        // Handle user input first
        handleInput(dt);
        world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);

        hud.update(dt);
        player.update(dt);
        bob.update(dt);
        tempBear.update(dt);
//        tempSnow.update(dt);
        currentSpawnedSnowballs.forEach(snowball -> {
            if (snowball == null) {
                return;
            }
            snowball.update(dt);
        });
        spawnSnowballs(dt);

        // Avoid camera go over boundary
        MapProperties prop = map.getProperties();
        int mapWidth = prop.get("width", Integer.class);
        int tilePixelWidth = prop.get("tilewidth", Integer.class);
        float mapPixelWidth = mapWidth * tilePixelWidth / BobIsMelting.PPM;

        // The right boundary of the map (x + width)
        final float cameraHalfWidth = gamePort.getCamera().viewportWidth * .5f;

        gameCam.position.x = player.getB2body().getPosition().x;
        gameCam.position.x = MathUtils.clamp(gameCam.position.x,
                cameraHalfWidth + tilePixelWidth / BobIsMelting.PPM,
                mapPixelWidth - cameraHalfWidth - tilePixelWidth / BobIsMelting.PPM);

        // Update our gameCam with correct coordinates after changes
        gameCam.update();
        // Tell the renderer to draw only what our camera can see in our game world.
        renderer.setView(gameCam);
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
    public void render(final float delta) {
        update(delta);

        // Clear the screen
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render game map
        renderer.render();

        // Render Box2DDebugLines
        b2dr.render(world, gameCam.combined);

        game.getBatch().setProjectionMatrix(gameCam.combined);
        game.getBatch().begin();
        bob.draw(game.getBatch());
        tempBear.draw(game.getBatch());
//        tempSnow.draw(game.getBatch());
        currentSpawnedSnowballs.forEach(snowball -> {
            if (snowball == null) {
                return;
            }
            snowball.draw(game.getBatch());
        });
        player.draw(game.getBatch());
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
    public void resize(final int width, final int height) {
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
