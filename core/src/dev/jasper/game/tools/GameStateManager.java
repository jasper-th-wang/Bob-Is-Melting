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
    private static final int SNOWBALL_HEALTH_INCREASE = 10;
    private static final int MAX_SNOWBALL_COUNT = 5;
    private static final int MAX_HEALTH = 100;
    private static final int GRAVITY_Y = -10;
    private final World world;
    private final TiledMap map;
    private final B2BodyObjectFactory b2BodyObjectFactory;
    private final AbstractPlayer kid;
    private final InteractiveEnviromentB2BodySprite bob;
    private final Array<AbstractEnemy> enemies;
    private final Array<Snowball> currentSpawnedSnowballs;
    private final Vector2[] snowballSpawnSpots;
    private final Array<Vector2> nextSnowballSpawnSpots;
    private Integer worldTimer;
    private float timeCount;
    private Integer bobsHealth;
    private float snowballSpawnTimer;
    /**
     * Constructs a GameStateManager instance.
     * It creates the game world, the characters, the ground, and initializes the snowballs.
     */
    public GameStateManager() {
        this.world = new World(new Vector2(0, GRAVITY_Y), true);
        this.b2BodyObjectFactory = new B2BodyObjectFactory(world);
        worldTimer = 0;
        timeCount = 0;
        WorldContactListener worldContactListener = new WorldContactListener(this);
        world.setContactListener(worldContactListener);
        this.map = b2BodyObjectFactory.getMap();
        this.bobsHealth = MAX_HEALTH;

        this.snowballSpawnSpots = b2BodyObjectFactory.getSnowballSpawnSpots();
        this.nextSnowballSpawnSpots = new Array<>();
        nextSnowballSpawnSpots.addAll(snowballSpawnSpots);
        nextSnowballSpawnSpots.shuffle();

        Gdx.app.log("snow", String.valueOf(nextSnowballSpawnSpots.size));

        // initialize game states by instantiating b2d bodies
        this.kid = b2BodyObjectFactory.createKid();
        this.bob = b2BodyObjectFactory.createBob();
        this.enemies = new Array<>();
        try {
//            enemies.add(b2BodyObjectFactory.createEnemy("bear",.32f, .32f));
            enemies.add(b2BodyObjectFactory.createEnemy("bear",3.52f, .48f));
        } catch (IllegalArgumentException e) {
            System.out.println(e + " Fail to initialize enemies.");
        }
        currentSpawnedSnowballs = b2BodyObjectFactory.initializeSnowballsSpawnSpots(MAX_SNOWBALL_COUNT);
    }

    public Integer getWorldTimer() {
        return worldTimer;
    }

    public void addSnowball() {
        final int newHealth = Math.min(getBobsHealth() + SNOWBALL_HEALTH_INCREASE, MAX_HEALTH);
        setBobsHealth(newHealth);
    }

    public Integer getBobsHealth() {
        return bobsHealth;
    }

    public void setBobsHealth(Integer bobsHealth) {
        this.bobsHealth = bobsHealth;
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

        timeCount += dt;
        // When exactly 1 second has passed, increment and update the world timer and corresponding HUD element
        if (timeCount >= 1) {
            worldTimer++;
            setBobsHealth(getBobsHealth() - 2);
            adjustDifficulty(worldTimer);
            timeCount = 0;
        }

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

    private void adjustDifficulty(final int time) {
        final int difficulty1 = 10;
        final int difficulty2 = 20;
        final int difficulty3 = 30;
        final int difficulty4 = 40;
        switch (time) {
            case difficulty1:
                enemies.add(b2BodyObjectFactory.createEnemy("bear",.32f, .32f));
                break;
            case difficulty2:
                enemies.add(b2BodyObjectFactory.createEnemy("bear",1.6f, .48f));
                break;
            case difficulty3:
                enemies.add(b2BodyObjectFactory.createEnemy("chicken",2.56f, 1.28f));
                break;
            case difficulty4:
                enemies.add(b2BodyObjectFactory.createEnemy("chicken",.32f, 1.28f));
                break;
            default:
                break;
        }
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
    /**
     * Checks if the game is over.
     *
     * @return true if Bob's health is less than 0, false otherwise.
     */
    public boolean isGameOver() {
        return bobsHealth < 0;
    }
}
