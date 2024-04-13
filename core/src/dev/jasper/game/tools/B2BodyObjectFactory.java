package dev.jasper.game.tools;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import dev.jasper.game.sprites.EnemyBoundary;
import dev.jasper.game.sprites.Ground;
import dev.jasper.game.sprites.InitializableB2Body;
import dev.jasper.game.sprites.TileB2Body;
import dev.jasper.game.sprites.dynamicSprites.AbstractEnemy;
import dev.jasper.game.sprites.dynamicSprites.Bear;
import dev.jasper.game.sprites.dynamicSprites.Chicken;
import dev.jasper.game.sprites.dynamicSprites.Kid;
import dev.jasper.game.sprites.enviromentSprites.Bob;
import dev.jasper.game.sprites.enviromentSprites.Snowball;

/**
 * The B2WorldCreator class is responsible for creating the physical world in the game.
 * It uses the Box2D physics engine to create and manage the game world's physical entities.
 *
 * @author Jasper Wang
 * @version 2024
 */
public final class B2BodyObjectFactory {
    private static final int SNOWBALL_SPAWN_LAYER = 6;
    private static final int GROUND_LAYER = 5;
    private static final int ENEMY_BOUNDARY_LAYER = 7;
    private final World world;
    private final TiledMap map;
    private final TextureAtlas atlas;

    /**
     * Constructs a B2BodyObjectFactory instance.
     * It sets the world for the game and initializes the non-character bodies and fixtures in the game world.
     *
     * @param world The World object representing the physical world in the game.
     */
    public B2BodyObjectFactory(final World world) {
        this.world = world;
        this.atlas = new TextureAtlas("Characters.atlas");
        final TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load("mainNew.tmx");
        initializeTileB2Bodies(GROUND_LAYER);
        initializeTileB2Bodies(ENEMY_BOUNDARY_LAYER);
    }

    /**
     * Initializes the TileB2Body objects in the game.
     * This method is used to create the physical bodies for the tiles in the game world.
     * The layerNumber parameter determines which layer of the TiledMap is used to create the bodies.
     *
     * @param layerNumber The layer number in the TiledMap from which the bodies are created.
     */
    private void initializeTileB2Bodies(final int layerNumber) {
        for (RectangleMapObject object : getMap().getLayers().get(layerNumber)
                .getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            TileB2Body tileB2Body;
            if (layerNumber == GROUND_LAYER) {
                tileB2Body = new Ground(rect);
            } else {
                tileB2Body = new EnemyBoundary(rect);
            }
            initializeB2Body(tileB2Body);
        }
    }

    /**
     * Returns the TiledMap instance used for the game map.
     *
     * @return The TiledMap instance used for the game map.
     */
    public TiledMap getMap() {
        return map;
    }

    private void initializeB2Body(final InitializableB2Body b2Body) {
        Body body = world.createBody(b2Body.getBodyDef());
        Fixture fixture = body.createFixture(b2Body.getFixtureDef());
        fixture.setUserData(b2Body);
        b2Body.setB2body(body);
        b2Body.setFixture(fixture);
    }

    /**
     * Initializes the snowballs spawn spots in the game.
     *
     * @param snowballCount The number of snowballs to be spawned in the game.
     * @return An array of Snowball objects representing the snowballs in the game.
     */
    public Array<Snowball> initializeSnowballsSpawnSpots(final int snowballCount) {
        final Array<Snowball> snowballs;
        snowballs = new Array<>();
        for (int i = 0; i < snowballCount; i++) {
            snowballs.add(null);
        }
        return snowballs;
    }

    /**
     * Creates a new Snowball in the game.
     *
     * @param spawnSpot               The spawn spot of the Snowball.
     * @param currentSpawnedSnowballs The current spawned Snowballs in the game.
     * @param currentIndex            The current index of the Snowball.
     * @return A new Snowball object.
     */
    public Snowball createSnowball(final Vector2 spawnSpot,
                                   final Array<Snowball> currentSpawnedSnowballs, final int currentIndex) {
        final Snowball snowball = Snowball.snowballFactory(atlas, spawnSpot, currentSpawnedSnowballs, currentIndex);
        initializeB2Body(snowball);
        return snowball;
    }

    /**
     * Creates a new enemy in the game based on the provided enemy type.
     *
     * @param enemyType The type of the enemy to be created. This is a string that can be "bear" or "chicken".
     * @param positionX The x-coordinate of the enemy's initial position.
     * @param positionY The y-coordinate of the enemy's initial position.
     * @return A new AbstractEnemy object representing the created enemy.
     * @throws IllegalArgumentException If the provided enemy type is not recognized.
     */
    public AbstractEnemy createEnemy(final String enemyType, final float positionX, final float positionY) {
        AbstractEnemy enemy;
        switch (enemyType) {
            case "bear":
                enemy = Bear.enemyFactory(atlas, positionX, positionY);
                break;
            case "chicken":
                enemy = Chicken.enemyFactory(atlas, positionX, positionY);
                break;
            default:
                throw new IllegalArgumentException("No enemy type found named: " + enemyType);
        }

        initializeB2Body(enemy);
        return enemy;
    }

    /**
     * Creates a new Bob in the game.
     *
     * @return A new Bob object.
     */
    public Bob createBob() {
        final Bob bob = Bob.getInstance(atlas);
        initializeB2Body(bob);
        return bob;
    }

    /**
     * Creates a new Kid in the game.
     *
     * @return A new Kid object.
     */
    public Kid createKid() {
        final Kid kid = Kid.getInstance(atlas);
        initializeB2Body(kid);
        return kid;
    }

    /**
     * Returns the array of snowball spawn spots in the game.
     *
     * @return Array of Vector2 objects representing the snowball spawn spots.
     */
    public Vector2[] getSnowballSpawnSpots() {
        Array<RectangleMapObject> snowballTileObjects = getMap().getLayers().get(SNOWBALL_SPAWN_LAYER)
                .getObjects().getByType(RectangleMapObject.class);

        final int spotsCount = snowballTileObjects.size;

        Vector2[] snowballSpawnSpots = new Vector2[spotsCount];
        for (int i = 0; i < spotsCount; i++) {
            Rectangle rect = snowballTileObjects.get(i).getRectangle();
            snowballSpawnSpots[i] = new Vector2(rect.getX(), rect.getY());
        }
        return snowballSpawnSpots;
    }
}
