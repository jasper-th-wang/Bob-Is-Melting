package dev.jasper.game.tools;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import dev.jasper.game.screens.PlayScreen;
import dev.jasper.game.sprites.Ground;

/**
 * The B2WorldCreator class is responsible for creating the physical world in the game.
 * It uses the Box2D physics engine to create and manage the game world's physical entities.
 *
 * @author Jasper Wang
 * @version 2024
 */
public final class B2WorldCreator {

    /**
     * The constant SNOWBALL_SPAWN_LAYER represents the index of the snowball spawn layer in the game map.
     * This constant is used when iterating over the map layers to create the ground bodies and fixtures.
     * The value is set to 6, which corresponds to the snowball spawn layer in the TiledMap object used in the game.
     */
    private static final int SNOWBALL_SPAWN_LAYER = 6;
    /**
     * The constant GROUND_LAYER represents the index of the ground layer in the game map.
     * The value is set to 5, which corresponds to the ground layer in the TiledMap object used in the game.
     */
    private static final int GROUND_LAYER = 5;
    /**
     * Stores the spawn spots for snowballs in the game.
     */
    private final Vector2[] snowballSpawnSpots;

    /**
     * Constructs a B2WorldCreator instance.
     * It creates the non-character bodies and fixtures in the game world.
     * @param screen - the game screen
     */
    public B2WorldCreator(final PlayScreen screen) {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        // Create ground bodies and fixtures
        for (RectangleMapObject object: map.getLayers().get(GROUND_LAYER)
                .getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            new Ground(screen, rect);
        }

//        snowballSpawnSpots = new Queue<>();
        // Create snowball spawn spots, store it as instance variables
        Array<RectangleMapObject> snowballTileObjects = map.getLayers().get(SNOWBALL_SPAWN_LAYER)
                .getObjects().getByType(RectangleMapObject.class);

        final int spotsCount = snowballTileObjects.size;

        snowballSpawnSpots = new Vector2[spotsCount];
        for (int i = 0; i < spotsCount; i++) {
            Rectangle rect = snowballTileObjects.get(i).getRectangle();
            snowballSpawnSpots[i] = new Vector2(rect.getX(), rect.getY());
        }

    }

    /**
     * Returns the array of snowball spawn spots in the game.
     *
     * @return Array of Vector2 objects representing the snowball spawn spots.
     */
    public Vector2[] getSnowballSpawnSpots() {
        return snowballSpawnSpots;
    }

}
