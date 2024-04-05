package dev.jasper.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import dev.jasper.game.sprites.Bob;
import dev.jasper.game.sprites.Enemy;
import dev.jasper.game.sprites.Ground;
import dev.jasper.game.sprites.Kid;
import dev.jasper.game.sprites.Snowball;

public final class GameStateManager {
    private static final float TIME_STEP = 1 / 60f;
    private static final int VELOCITY_ITERATIONS = 6;
    private static final float SNOWBALL_SPAWN_INTERVAL = 3f;
    private static final int MAX_SNOWBALL_COUNT = 5;

    public World getWorld() {
        return world;
    }

    private final World world;

    public TiledMap getMap() {
        return map;
    }

    private final TiledMap map;
    private final B2BodyObjectFactory b2BodyObjectFactory;
    private final int positionIteration;

    public Kid getKid() {
        return kid;
    }

    private final Kid kid;
    private final Bob bob;
    private final Array<Ground> grounds;
    private final Array<Enemy> enemies;
    private final Array<Snowball> currentSpawnedSnowballs;
    private final Vector2[] snowballSpawnSpots;
    private final Array<Vector2> nextSnowballSpawnSpots;
    private float snowballSpawnTimer;

    public GameStateManager(final int positionIterations) {
        this.b2BodyObjectFactory = new B2BodyObjectFactory();
        this.world = b2BodyObjectFactory.getWorld();
        this.map = b2BodyObjectFactory.getMap();
        this.positionIteration = positionIterations;

        this.snowballSpawnSpots = b2BodyObjectFactory.getSnowballSpawnSpots();
        this.nextSnowballSpawnSpots = new Array<>();
        nextSnowballSpawnSpots.addAll(snowballSpawnSpots);
        nextSnowballSpawnSpots.shuffle();

        Gdx.app.log("snow", String.valueOf(nextSnowballSpawnSpots.size));

        // initialize kid, bob and bear
        this.grounds = b2BodyObjectFactory.getGrounds();
        this.kid = b2BodyObjectFactory.getKid();
        this.bob = b2BodyObjectFactory.getBob();
        // TODO: Might be buggy
        this.enemies = new Array<>();
        enemies.add(b2BodyObjectFactory.getBear(.32f, .32f));
        currentSpawnedSnowballs = b2BodyObjectFactory.initializeSnowballsSpawnSpots(MAX_SNOWBALL_COUNT);
    }


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
                currentSpawnedSnowballs.set(i, b2BodyObjectFactory.getSnowball());
//                nextSnowballSpawnSpots.addLast(spawnSpot);

                if (nextSnowballSpawnSpots.size == 0) {
                    nextSnowballSpawnSpots.addAll(snowballSpawnSpots);
                    nextSnowballSpawnSpots.shuffle();
                }
                return;
            }
        }
    }
    public void update(final float dt) {

        world.step(TIME_STEP, VELOCITY_ITERATIONS, positionIteration);
        kid.update(dt);
        bob.update(dt);
//        tempBear.update(dt);
//        tempSnow.update(dt);
        enemies.forEach(enemy -> enemy.update(dt));
        currentSpawnedSnowballs.forEach(snowball -> {
            if (snowball == null) {
                return;
            }
            snowball.update(dt);
        });
        spawnSnowballs(dt);
    }
    public void draw(SpriteBatch batch) {
        bob.draw(batch);
        // TODO: might be buggy
        enemies.forEach(enemy -> enemy.draw(batch));
        currentSpawnedSnowballs.forEach(snowball -> {
            if (snowball == null) {
                return;
            }
            snowball.draw(batch);
        });
        kid.draw(batch);
    }
    public void dispose() {
        map.dispose();
        world.dispose();
    }
}
