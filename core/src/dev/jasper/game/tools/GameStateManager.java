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
    private int healthDecreasePerSecond;
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
        this.healthDecreasePerSecond = 2;
        this.kid = b2BodyObjectFactory.createKid();
        this.bob = b2BodyObjectFactory.createBob();
        this.enemies = new Array<>();
        try {
            final float positionX = 3.52f;
            final float positionY = .48f;
            enemies.add(b2BodyObjectFactory.createEnemy("bear", positionX, positionY));
        } catch (IllegalArgumentException e) {
            System.out.println(e + " Fail to initialize enemies.");
        }
        currentSpawnedSnowballs = b2BodyObjectFactory.initializeSnowballsSpawnSpots(MAX_SNOWBALL_COUNT);
    }

    /**
     * Returns the current world timer.
     * The world timer is a counter that increments every second
     * and is used to track the total time elapsed in the game.
     *
     * @return The current world timer.
     */
    public Integer getWorldTimer() {
        return worldTimer;
    }

    /**
     * Increases Bob's health by the value of SNOWBALL_HEALTH_INCREASE, up to a maximum of MAX_HEALTH.
     * This method is typically called when a snowball is collected in the game.
     */
    public void addSnowball() {
        final int newHealth = Math.min(getBobsHealth() + SNOWBALL_HEALTH_INCREASE, MAX_HEALTH);
        setBobsHealth(newHealth);
    }

    /**
     * Returns the current health of Bob.
     * Bob's health decreases over time and when he is hit by enemies, and increases when he collects snowballs.
     *
     * @return The current health of Bob.
     */
    public Integer getBobsHealth() {
        return bobsHealth;
    }

    /**
     * Sets the current health of Bob.
     * This method is used to update Bob's health during the game.
     *
     * @param bobsHealth The new health value for Bob.
     */
    public void setBobsHealth(final Integer bobsHealth) {
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
            setBobsHealth(getBobsHealth() - getHealthDecreasePerSecond());
            adjustDifficultyToTime(worldTimer);
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

    private int getHealthDecreasePerSecond() {
        return healthDecreasePerSecond;
    }

    private void setHealthDecreasePerSecond(final int healthDecreasePerSecond) {
        this.healthDecreasePerSecond = healthDecreasePerSecond;
    }

    private void adjustDifficultyToTime(final int time) {
        switch (time) {
            case Difficulty.DIFFICULTY_1_TIME:
                enemies.add(b2BodyObjectFactory.createEnemy("bear", Difficulty.ENEMY_SPAWN_POSITION_X1,
                        Difficulty.ENEMY_SPAWN_POSITION_Y1));
                break;
            case Difficulty.DIFFICULTY_2_TIME:
                enemies.add(b2BodyObjectFactory.createEnemy("bear", Difficulty.ENEMY_SPAWN_POSITION_X2,
                        Difficulty.ENEMY_SPAWN_POSITION_Y2));
                setHealthDecreasePerSecond(Difficulty.HEALTH_DECREASE_PER_SECOND_2);
                break;
            case Difficulty.DIFFICULTY_3_TIME:
                enemies.add(b2BodyObjectFactory.createEnemy("chicken", Difficulty.ENEMY_SPAWN_POSITION_X3,
                        Difficulty.ENEMY_SPAWN_POSITION_Y3));
                break;
            case Difficulty.DIFFICULTY_4_TIME:
                enemies.add(b2BodyObjectFactory.createEnemy("chicken", Difficulty.ENEMY_SPAWN_POSITION_X4,
                        Difficulty.ENEMY_SPAWN_POSITION_Y4));
                setHealthDecreasePerSecond(Difficulty.HEALTH_DECREASE_PER_SECOND_4);
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

    private static final class Difficulty {
        private static final float ENEMY_SPAWN_POSITION_X1 = .32f;
        private static final float ENEMY_SPAWN_POSITION_Y1 = .32f;
        private static final float ENEMY_SPAWN_POSITION_X2 = 1.6f;
        private static final float ENEMY_SPAWN_POSITION_Y2 = .48f;
        private static final float ENEMY_SPAWN_POSITION_X3 = 2.56f;
        private static final float ENEMY_SPAWN_POSITION_Y3 = 1.28f;
        private static final float ENEMY_SPAWN_POSITION_X4 = .32f;
        private static final float ENEMY_SPAWN_POSITION_Y4 = 1.28f;
        private static final int DIFFICULTY_1_TIME = 10;
        private static final int DIFFICULTY_2_TIME = 20;
        private static final int DIFFICULTY_3_TIME = 30;
        private static final int DIFFICULTY_4_TIME = 40;
        private static final int HEALTH_DECREASE_PER_SECOND_2 = 3;
        private static final int HEALTH_DECREASE_PER_SECOND_4 = 4;

    }
}
