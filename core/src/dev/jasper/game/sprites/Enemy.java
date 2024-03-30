package dev.jasper.game.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import dev.jasper.game.screens.PlayScreen;

/**
 * The Enemy class serves as an abstract base class that provides a skeletal implementation for the enemies in the game.
 * It extends the Sprite class from the libGDX library, inheriting its properties and methods for graphical representation.
 *
 * @author Jasper Wang
 * @version 2024
 */
public abstract class Enemy extends Sprite {
    protected World world;
    protected PlayScreen screen;
    protected Body b2body;
    protected Vector2 velocity;

    /**
     * Constructs an Enemy instance.
     * @param screen - the game screen
     * @param x - the x-coordinate of the enemy's position
     * @param y - the y-coordinate of the enemy's position
     */
    public Enemy(PlayScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
        velocity = new Vector2(1, 0);
    }
    /**
     * Defines the physical properties of the Enemy character in the game world.
     */
    protected abstract void defineEnemy();
    public void reverseVelocity(boolean x, boolean y) {
        if (x) {
            getVelocity().x = -getVelocity().x;
        }
        if (y) {
            getVelocity().y = -getVelocity().y;
        }
    }

    /**
     * Returns the enemy's Body instance.
     * @return b2body - the Body instance
     */
    public Body getB2body() {
        return b2body;
    }

    /**
     * Returns the enemy's velocity.
     * @return velocity - the velocity
     */
    public Vector2 getVelocity() {
        return velocity;
    }
}
