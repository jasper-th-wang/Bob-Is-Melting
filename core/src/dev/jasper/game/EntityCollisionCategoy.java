package dev.jasper.game;

public class EntityCollisionCategoy {
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
     * Bit value representing destroyed objects in the game's physics engine.
     */
    public static final short DESTROYED_BIT = 8;
    /**
     * Bit value representing generic objects in the game's physics engine.
     */
    public static final short OBJECT_BIT = 16;
    /**
     * Bit value representing enemies in the game's physics engine.
     */
    public static final short ENEMY_BIT = 32;
    public static final short KID_INVINCIBLE_BIT = 64;
}