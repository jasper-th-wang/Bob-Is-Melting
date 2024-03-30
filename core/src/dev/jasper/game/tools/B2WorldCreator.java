package dev.jasper.game.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import dev.jasper.game.screens.PlayScreen;
import dev.jasper.game.sprites.Ground;

/**
 * The B2WorldCreator class is responsible for creating the physical world in the game.
 * It uses the Box2D physics engine to create and manage the game world's physical entities.
 *
 * @author Jasper Wang
 * @version 2024
 */
public class B2WorldCreator {

    /**
     * Constructs a B2WorldCreator instance.
     * It creates the non-character bodies and fixtures in the game world.
     * @param screen - the game screen
     */
    public B2WorldCreator(PlayScreen screen) {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        // Create ground bodies and fixtures
        for (MapObject object: map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new Ground(screen, rect);
        }

    }
}
