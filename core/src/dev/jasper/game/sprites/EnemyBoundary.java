package dev.jasper.game.sprites;

import com.badlogic.gdx.math.Rectangle;
import dev.jasper.game.EntityCollisionCategory;

public class EnemyBoundary extends TileB2Body {
    private static final short COLLISION_CATEGORY = EntityCollisionCategory.ENEMY_BOUNDARY_BIT;
    /**
     * Constructs a TileB2Body with the specified bounds and collision category.
     *
     * @param bounds            The bounds of the tile body.
     */
    public EnemyBoundary(final Rectangle bounds) {
        super(bounds, COLLISION_CATEGORY);
    }
}
