package dev.jasper.game.sprites.dynamicSprites;

/**
 * The SnowballCarrier interface represents an object in the game that is capable of handling a snowball.
 * This includes collecting a snowball, dropping off a snowball, and checking if it's currently carrying a snowball.
 * Any class that implements this interface will need to provide implementations for these methods.
 *
 * @author Jasper Wang
 * @version 2024
 */
public interface SnowballCarrier {
    /**
     * Handles the actions to be taken when collecting a snowball.
     * This method is called when the body collides with a snowball in the game.
     */
    void collectSnowball();

    /**
     * Handles the actions to be taken when dropping off a snowball.
     * This method is called when the body collides with Bob in the game.
     */
    void dropoffSnowball();

    /**
     * Returns the carrying snowball status of the body.
     * This method is used to check if the body is currently carrying a snowball.
     *
     * @return boolean - true if the body is carrying a snowball, false otherwise.
     */
    boolean getIsCarryingSnowball();

    /**
     * Sets the carrying snowball status of the body.
     * This method is used to change the carrying snowball status of the body.
     *
     * @param carryingSnowball - the new carrying snowball status of the body.
     */
    void setIsCarryingSnowball(boolean carryingSnowball);
}
