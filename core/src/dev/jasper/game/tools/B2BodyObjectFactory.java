package dev.jasper.game.tools;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import dev.jasper.game.sprites.InitializableB2Body;
import dev.jasper.game.sprites.Bear;
import dev.jasper.game.sprites.Bob;
import dev.jasper.game.sprites.Ground;
import dev.jasper.game.sprites.Kid;

/**
 * The B2WorldCreator class is responsible for creating the physical world in the game.
 * It uses the Box2D physics engine to create and manage the game world's physical entities.
 *
 * @author Jasper Wang
 * @version 2024
 */
public final class B2WorldCreator {
    private final World world;

    private final TiledMap map;
    private final TextureAtlas atlas;

    private static final int GRAVITY_Y = -10;

    private static final int SNOWBALL_SPAWN_LAYER = 6;

    private static final int GROUND_LAYER = 5;
//    private final Array<Ground> groundBodies;
//    private final Vector2[] snowballSpawnSpots;

    /**
     * Constructs a B2WorldCreator instance.
     * It creates the non-character bodies and fixtures in the game world.
     * @param screen - the game screen
     */
    public B2WorldCreator(final TextureAtlas atlas) {
        this.world = new World(new Vector2(0, GRAVITY_Y), true);
        this.atlas = atlas;
        world.setContactListener(new WorldContactListener());
        final TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load("mainNew.tmx");
//        World world = screen.getWorld();
//        TiledMap map = screen.getMap();
        // Create ground bodies and fixtures
//        Ground groundBodies = getGround();
//        if (groundBodies != null) {
//            return groundBodies;
//        }

//        snowballSpawnSpots = new Queue<>();
        // Create snowball spawn spots, store it as instance variables
        Array<RectangleMapObject> snowballTileObjects = getMap().getLayers().get(SNOWBALL_SPAWN_LAYER)
                .getObjects().getByType(RectangleMapObject.class);

        final int spotsCount = snowballTileObjects.size;

        snowballSpawnSpots = new Vector2[spotsCount];
        for (int i = 0; i < spotsCount; i++) {
            Rectangle rect = snowballTileObjects.get(i).getRectangle();
            snowballSpawnSpots[i] = new Vector2(rect.getX(), rect.getY());
        }


        // Initialize in game objects
        // TODO: getPlayer, getBob, getBear
        tempBear = new Bear(this, .32f, .32f);

    }
    public Bob getBob() {
        final Bob bob = new Bob(atlas);
        initializeB2Body(bob);
        return bob;
    }
    public Kid getKid() {
        final Kid kid = new Kid(atlas);
        initializeB2Body(kid);
        return kid;
    }

    public Array<Ground> getGround() {
        final Array<Ground> groundBodies = new Array<>();
        for (RectangleMapObject object: getMap().getLayers().get(GROUND_LAYER)
                .getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            final Ground ground = new Ground(rect);
            initializeB2Body(ground);
        }
        return groundBodies;
    }

    /**
     * Returns the array of snowball spawn spots in the game.
     *
     * @return Array of Vector2 objects representing the snowball spawn spots.
     */
    public Vector2[] getSnowballSpawnSpots() {
        return snowballSpawnSpots;
    }

    /**
     * The TiledMap instance used for the game map.
     */
    public TiledMap getMap() {
        return map;
    }

    /**
     * The Box2D world used for physics simulation in the game.
     */
    public World getWorld() {
        return world;
    }

    private void initializeB2Body(final InitializableB2Body b2Body) {
        Body body = world.createBody(b2Body.getBodyDef());
        body.createFixture(b2Body.getFixtureDef()).setUserData(b2Body);
    }
//    public v
}
