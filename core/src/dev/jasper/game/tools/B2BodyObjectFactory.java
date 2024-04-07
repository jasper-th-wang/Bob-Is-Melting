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
import dev.jasper.game.sprites.Ground;
import dev.jasper.game.sprites.InitializableB2Body;
import dev.jasper.game.sprites.dynamicSprites.Bear;
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
    private static final int GRAVITY_Y = -10;
    private static final int SNOWBALL_SPAWN_LAYER = 6;
    private static final int GROUND_LAYER = 5;
    private final World world;
    private final TiledMap map;
    private final TextureAtlas atlas;

    /**
     * Constructs a B2WorldCreator instance.
     * It creates the non-character bodies and fixtures in the game world.
     */
    public B2BodyObjectFactory() {
        this.world = new World(new Vector2(0, GRAVITY_Y), true);
        this.atlas = new TextureAtlas("Characters.atlas");
        world.setContactListener(new WorldContactListener());
        final TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load("mainNew.tmx");
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
     * @param spawnSpot The spawn spot of the Snowball.
     * @param currentSpawnedSnowballs The current spawned Snowballs in the game.
     * @param currentIndex The current index of the Snowball.
     * @return A new Snowball object.
     */
    public Snowball createSnowball(final Vector2 spawnSpot,
                                   final Array<Snowball> currentSpawnedSnowballs, final int currentIndex) {
        final Snowball snowball = Snowball.snowballFactory(atlas, spawnSpot, currentSpawnedSnowballs, currentIndex);
        initializeB2Body(snowball);
        return snowball;
    }

    private void initializeB2Body(final InitializableB2Body b2Body) {
        Body body = world.createBody(b2Body.getBodyDef());
        Fixture fixture = body.createFixture(b2Body.getFixtureDef());
        fixture.setUserData(b2Body);
        b2Body.setB2body(body);
        b2Body.setFixture(fixture);
    }

    /**
     * Creates a new Bear in the game.
     *
     * @param positionX The x-coordinate of the Bear's position.
     * @param positionY The y-coordinate of the Bear's position.
     * @return A new Bear object.
     */
    public Bear createBear(final float positionX, final float positionY) {
        final Bear bear = Bear.bearFactory(atlas, positionX, positionY);
        initializeB2Body(bear);
        return bear;
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
     * Creates the grounds in the game.
     */
    public void createGrounds() {
        final Array<Ground> groundBodies = new Array<>();
        for (RectangleMapObject object: getMap().getLayers().get(GROUND_LAYER)
                .getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            final Ground ground = new Ground(rect);
            initializeB2Body(ground);
            groundBodies.add(ground);
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
    /**
     * Returns the Box2D world used for physics simulation in the game.
     *
     * @return The Box2D world used for physics simulation in the game.
     */
    public World getWorld() {
        return world;
    }
}
