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
    public Array<Snowball> initializeSnowballsSpawnSpots(final int snowballCount) {
        final Array<Snowball> snowballs;
        snowballs = new Array<>();
        for (int i = 0; i < snowballCount; i++) {
            snowballs.add(null);
        }
        return snowballs;
    }
    public Snowball createSnowball(Vector2 spawnSpot, Array<Snowball> currentSpawnedSnowballs, int currentIndex) {
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

    public Bear createBear(final float positionX, final float positionY) {
        final Bear bear = Bear.bearFactory(atlas, positionX, positionY);
        initializeB2Body(bear);
        return bear;
    }

    public Bob createBob() {
        final Bob bob = Bob.bobFactory(atlas);
        initializeB2Body(bob);
        return bob;
    }

    public Kid createKid() {
        final Kid kid = Kid.kidFactory(atlas);
        initializeB2Body(kid);
//        kid.setB2body(body);
//        kid.setFixture(body.);
        return kid;
    }

    public Array<Ground> createGrounds() {
        final Array<Ground> groundBodies = new Array<>();
        for (RectangleMapObject object: getMap().getLayers().get(GROUND_LAYER)
                .getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            final Ground ground = new Ground(rect);
            initializeB2Body(ground);
            groundBodies.add(ground);
        }
        return groundBodies;
    }

    /**
     * The TiledMap instance used for the game map.
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
     * The Box2D world used for physics simulation in the game.
     */
    public World getWorld() {
        return world;
    }
}
