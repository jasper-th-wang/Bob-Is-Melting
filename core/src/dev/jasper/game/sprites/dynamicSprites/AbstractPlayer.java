package dev.jasper.game.sprites.dynamicSprites;


/**
 * AbstractPlayer is an abstract class that extends DynamicB2BodySprite
 * and implements SnowballCarrier and InteractableWithEnemy interfaces.
 * This class is used to define the common properties and behaviors of all types of players in the game.
 * It provides a method to check if the player is invincible to enemies.
 *
 * @author Jasper Wang
 * @version 2024
 */
public abstract class AbstractPlayer extends DynamicB2BodySprite implements SnowballCarrier, InteractableWithEnemy {
    /**
     * Constructor for the AbstractPlayer class.
     * It initializes the player with the given collision category and mask bits.
     *
     * @param collisionCategory The collision category of the player.
     * @param maskBits          The mask bits of the player.
     */
    public AbstractPlayer(final short collisionCategory, final short maskBits) {
        super(collisionCategory, maskBits);
    }

    /**
     * Returns the invincibility status of the player.
     *
     * @return boolean - true if the player is invincible to enemies, false otherwise.
     */
    public abstract boolean getIsInvincibleToEnemy();

}
