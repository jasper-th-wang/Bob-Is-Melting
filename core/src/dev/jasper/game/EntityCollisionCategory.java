package dev.jasper.game;

/**
 * This class represents the different categories of entities that can collide in the game.
 * Each category is represented by a bit value. These bit values are used in the game's physics engine
 * to handle collisions between different types of entities.
 *
 * @author Jasper Wang
 * @version 2024
 */
public final class EntityCollisionCategory {
    /**
     * Bit value representing the ground in the game's physics engine.
     */
    public static final short GROUND_BIT = 1;
    /**
     * Bit value representing the kid character in the game's physics engine.
     */
    public static final short KID_BIT = 2;
    /**
     * Bit value representing the snowball in the game's physics engine.
     */
    public static final short SNOWBALL_BIT = 4;
    /**
     * Bit value representing the boundary of enemies in the game's physics engine.
     */
    public static final short ENEMY_BOUNDARY_BIT = 8;
    /**
     * Bit value representing generic objects in the game's physics engine.
     */
    public static final short OBJECT_BIT = 16;
    /**
     * Bit value representing enemies in the game's physics engine.
     */
    public static final short ENEMY_BIT = 32;
    /**
     * Bit value representing the invincible state of the kid character in the game's physics engine.
     */
    public static final short KID_INVINCIBLE_BIT = 64;
    /**
     * Bit value representing the Bob character in the game's physics engine.
     */
    public static final short BOB_BIT = 128;
    /**
     * Bit value representing the state of the kid character carrying a snowball in the game's physics engine.
     */
    public static final short KID_CARRY_SNOWBALL_BIT = 256;
    private EntityCollisionCategory() {
        throw new UnsupportedOperationException("Utility class");
    }
}
