package dev.jasper.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import dev.jasper.game.sprites.dynamicSprites.AbstractEnemy;
import dev.jasper.game.sprites.dynamicSprites.AbstractPlayer;
import dev.jasper.game.sprites.enviromentSprites.InteractiveEnviromentB2BodySprite;
import dev.jasper.game.sprites.enviromentSprites.Snowball;

/**
 * The GameStateManager class is responsible for managing the state of the game.
 * It creates and updates the game world, including the characters, the ground, and the snowballs.
 * It also handles the spawning of snowballs at regular intervals.
 *
 * @author Jasper Wang
 * @version 2024
 */
public final class GameStateManager {
    private static final float TIME_STEP = 1 / 60f;
    private static final int VELOCITY_ITERATIONS = 6;
    private static final int POSITION_ITERATIONS = 2;
    private static final float SNOWBALL_SPAWN_INTERVAL = 3f;
    private static final int MAX_SNOWBALL_COUNT = 5;
    private final World world;
    private final TiledMap map;
    private final B2BodyObjectFactory b2BodyObjectFactory;
    private final AbstractPlayer kid;
    private final InteractiveEnviromentB2BodySprite bob;
    private final Array<AbstractEnemy> enemies;
    private final Array<Snowball> currentSpawnedSnowballs;
    private final Vector2[] snowballSpawnSpots;
    private final Array<Vector2> nextSnowballSpawnSpots;
    private float snowballSpawnTimer;
    /**
     * Constructs a GameStateManager instance.
     * It creates the game world, the characters, the ground, and initializes the snowballs.
     */
    public GameStateManager() {
        this.b2BodyObjectFactory = new B2BodyObjectFactory();
        this.world = b2BodyObjectFactory.getWorld();
        this.map = b2BodyObjectFactory.getMap();

        this.snowballSpawnSpots = b2BodyObjectFactory.getSnowballSpawnSpots();
        this.nextSnowballSpawnSpots = new Array<>();
        nextSnowballSpawnSpots.addAll(snowballSpawnSpots);
        nextSnowballSpawnSpots.shuffle();

        Gdx.app.log("snow", String.valueOf(nextSnowballSpawnSpots.size));

        // initialize game states by instantiating b2d bodies
        b2BodyObjectFactory.createGrounds();
        this.kid = b2BodyObjectFactory.createKid();
        this.bob = b2BodyObjectFactory.createBob();
        this.enemies = new Array<>();
        enemies.add(b2BodyObjectFactory.createBear(.32f, .32f));
        currentSpawnedSnowballs = b2BodyObjectFactory.initializeSnowballsSpawnSpots(MAX_SNOWBALL_COUNT);
    }

    /**
     * Returns the Box2D world used for physics simulation in the game.
     *
     * @return The Box2D world used for physics simulation in the game.
     */
    public World getWorld() {
        return world;
    }

    /**
     * Returns the TiledMap instance used for the game map.
     *
     * @return The TiledMap instance used for the game map.
     */
    public TiledMap getMap() {
        return map;
    }

    /**
     * Returns the Kid character in the game.
     *
     * @return The Kid character in the game.
     */
    public AbstractPlayer getKid() {
        return kid;
    }

    /**
     * Updates the state of the game.
     * This method is called periodically to update the state of the game world.
     *
     * @param dt The time delta, representing the amount of time passed since the last update.
     */
    public void update(final float dt) {
        world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);

        kid.update(dt);
        bob.update();
        enemies.forEach(enemy -> enemy.update(dt));
        currentSpawnedSnowballs.forEach(snowball -> {
            if (snowball == null) {
                return;
            }

            snowball.update();

            if (snowball.isCollected()) {
                world.destroyBody(snowball.getB2body());
            }
        });
        spawnSnowballs(dt);
    }

    /**
     * Spawns snowballs at regular intervals in the game.
     * This method is called periodically to spawn snowballs in the game world.
     *
     * @param dt The time delta, representing the amount of time passed since the last update.
     */
    public void spawnSnowballs(final float dt) {
        snowballSpawnTimer += dt;
        if (snowballSpawnTimer <= SNOWBALL_SPAWN_INTERVAL) {
            return;
        }
        snowballSpawnTimer = 0;

        // make one snowball
        for (int i = 0; i < currentSpawnedSnowballs.size; i++) {
            if (currentSpawnedSnowballs.get(i) == null) {
                final Vector2 spawnSpot = nextSnowballSpawnSpots.pop();
                currentSpawnedSnowballs.set(i,
                        b2BodyObjectFactory.createSnowball(spawnSpot, currentSpawnedSnowballs, i));

                if (nextSnowballSpawnSpots.size == 0) {
                    nextSnowballSpawnSpots.addAll(snowballSpawnSpots);
                    nextSnowballSpawnSpots.shuffle();
                }
                return;
            }
        }
    }

    /**
     * Draws the game world on the screen.
     * This method is called to draw the game world on the screen.
     *
     * @param batch The Batch used to draw the game world.
     */
    public void draw(final SpriteBatch batch) {
        bob.draw(batch);
        enemies.forEach(enemy -> enemy.draw(batch));
        currentSpawnedSnowballs.forEach(snowball -> {
            if (snowball == null) {
                return;
            }
            snowball.draw(batch);
        });
        kid.draw(batch);
    }
    /**
     * Disposes of the resources used by the game.
     * This method is called when the game is closed to free up resources.
     */
    public void dispose() {
        map.dispose();
        world.dispose();
    }
}
