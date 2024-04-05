package dev.jasper.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import dev.jasper.game.screens.PlayScreen;

/**
 * The Enemy class serves as an abstract base class that provides a skeletal implementation for the enemies in the game.
 * It extends the Sprite class from the libGDX library,
 * inheriting its properties and methods for graphical representation.
 *
 * @author Jasper Wang
 * @version 2024
 */
public abstract class Enemy extends DynamicEntitySprite {
    /**
     * The maximum running velocity of the enemy.
     */
    protected final int maxRunVelocity;

    /**
     * The default running velocity of the enemy.
     */
    protected final float defaultRunVelocity;

    /**
     * The default jumping velocity of the enemy.
     */
    protected final float defaultJumpVelocity;

    /**
     * The chance for the enemy to jump.
     */
    protected final float chanceToJump;

    /**
     * The duration for the enemy to decide special movement.
     */
    protected final float decideSpecialMovementDuration;

    /**
     * The world in which the enemy exists.
     */
    protected World world;

    /**
     * The screen on which the enemy is displayed.
     */
    protected PlayScreen screen;

    /**
     * The body of the enemy, used for physics calculations.
     */
    protected Body b2body;

    /**
     * The timer for the enemy to decide special movement.
     */
    protected float decideSpecialMovementTimer;

    /**
     * The current state of the enemy.
     */
    protected State currentState;

    /**
     * The previous state of the enemy.
     */
    protected State previousState;

    /**
     * The time the enemy has been in its current state.
     */
    protected float stateTime;

    /**
     * A flag indicating whether the enemy is running to the right.
     */
    protected boolean isRunningRight;

    /**
     * The velocity of the enemy.
     */
    protected Vector2 velocity;
    /**
     * Constructs an Enemy instance.
     * @param screenInstance - the game screen
     * @param x - the x-coordinate of the enemy's position
     * @param y - the y-coordinate of the enemy's position
     */
    public Enemy(final PlayScreen screenInstance, final float x, final float y) {
        this.world = screenInstance.getWorld();
        this.screen = screenInstance;
        setPosition(x, y);
        defineEnemy();

        defaultRunVelocity = getDefaultRunVelocity();
        velocity = new Vector2(getDefaultRunVelocity(), 0);
        defaultJumpVelocity = getDefaultJumpVelocity();
        chanceToJump = getChanceToJump();
        decideSpecialMovementTimer = 0;
        decideSpecialMovementDuration = getDecideSpecialMovementDuration();
        maxRunVelocity = getMaxRunVelocity();
    }

    private void jump() {
        b2body.applyLinearImpulse(new Vector2(0, getDefaultJumpVelocity()), b2body.getWorldCenter(), true);
        Gdx.app.log("Enemy", "jump!");
    }

    private void idle() {
        setVelocity(new Vector2(0, 0));
    }

    protected final void run() {
        if (Math.abs(getB2body().getLinearVelocity().x) <= getMaxRunVelocity()) {
            getB2body().applyLinearImpulse(getVelocity(), getB2body().getWorldCenter(), true);
        }

    }

    protected final void applySpecialMovement() {
        if (decideSpecialMovementTimer >= getDecideSpecialMovementDuration()) {
            if (MathUtils.randomBoolean(getChanceToJump())) {
                jump();
                setVelocity(new Vector2(getDefaultRunVelocity(), 0));
            } else {
                idle();
            }
            decideSpecialMovementTimer = 0;
        }
    }

    /**
     * Defines the physical properties of the Enemy character in the game world.
     */
    protected abstract void defineEnemy();

    /**
     * Reverses the enemy's velocity in the x and/or y direction.
     * @param x - if true, reverse the x-component of the velocity
     * @param y - if true, reverse the y-component of the velocity
     */
    public void reverseVelocity(final boolean x, final boolean y) {
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

    /**
     * Sets the velocity of the enemy.
     *
     * @param newVelocity - The new velocity to be set for the enemy.
     *                    This is a Vector2 object which contains the x and y components of the velocity.
     */
    public final void setVelocity(final Vector2 newVelocity) {
        this.velocity = newVelocity;
    }

    protected abstract float getDefaultRunVelocity();

    protected abstract float getDefaultJumpVelocity();

    protected abstract float getChanceToJump();
    protected abstract float getDecideSpecialMovementDuration();

    protected abstract int getMaxRunVelocity();

    /**
     * The State enum represents the possible states of an Enemy.
     * STANDING: The enemy is standing still.
     * RUNNING: The enemy is running.
     */
    public enum State { STANDING, RUNNING }
}
