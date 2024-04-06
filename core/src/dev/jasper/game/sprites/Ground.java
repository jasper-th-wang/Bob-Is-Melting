package dev.jasper.game.sprites;

import com.badlogic.gdx.math.Rectangle;
import dev.jasper.game.EntityCollisionCategory;

/**
 * The Ground class represents the ground in the game.
 * It defines the physical properties of the ground in the game world.
 *
 * @author Jasper Wang
 * @version 2024
 */
public final class Ground extends TileB2Body {
    private static final short COLLISION_CATEGORY = EntityCollisionCategory.GROUND_BIT;

    /**
     * Constructs a Ground instance.
     * @param bounds - the bounds of the ground
     */
    public Ground(final Rectangle bounds) {
        super(bounds, COLLISION_CATEGORY);
    }

}
