package dev.jasper.game.sprites.dynamicSprites;

/**
 * The InteractableWithEnemy interface represents an object in the game that can interact with enemies.
 * Any class that implements this interface will need to provide an implementation for the onEnemyHit method,
 * which defines the actions to be taken when the object is hit by an enemy.
 *
 * @author Jasper Wang
 * @version 2024
 */
public interface InteractableWithEnemy {
    /**
     * Handles the actions to be taken when the body is hit by an enemy.
     * This method is called when the body collides with an enemy in the game.
     */
    void onEnemyHit();
}
