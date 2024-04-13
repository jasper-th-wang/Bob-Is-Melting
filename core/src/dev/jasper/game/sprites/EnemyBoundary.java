package dev.jasper.game.sprites;

import com.badlogic.gdx.math.Rectangle;
import dev.jasper.game.EntityCollisionCategory;

/**
 * The EnemyBoundary class represents a boundary for enemies in the game.
 * It extends the TileB2Body class and defines the collision category for enemy boundaries.
 * This class is used to create physical boundaries in the game world that enemies cannot cross.
 *
 * @author Jasper Wang
 * @version 2024
 */
public class EnemyBoundary extends TileB2Body {
    private static final short COLLISION_CATEGORY = EntityCollisionCategory.ENEMY_BOUNDARY_BIT;

    /**
     * Constructs a TileB2Body with the specified bounds and collision category.
     *
     * @param bounds The bounds of the tile body.
     */
    public EnemyBoundary(final Rectangle bounds) {
        super(bounds, COLLISION_CATEGORY);
    }
}
