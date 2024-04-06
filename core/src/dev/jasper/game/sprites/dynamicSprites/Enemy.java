package dev.jasper.game.sprites.dynamicSprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import dev.jasper.game.EntityCollisionCategory;
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
    protected static final short COLLISION_CATEGORY = EntityCollisionCategory.ENEMY_BIT;
    protected static final short MASK_BITS = EntityCollisionCategory.GROUND_BIT | EntityCollisionCategory.OBJECT_BIT | EntityCollisionCategory.KID_BIT | EntityCollisionCategory.KID_CARRY_SNOWBALL_BIT;
    protected final int maxRunVelocity;
    protected final float defaultRunVelocity;
    protected final float defaultJumpVelocity;
    protected final float chanceToJump;
    protected final float decideSpecialMovementDuration;

//    protected World world;
//    protected PlayScreen screen;
//    protected Body b2body;
    protected float decideSpecialMovementTimer;
//    protected State currentState;
//    protected State previousState;
//    protected float stateTime;
//    protected boolean isRunningRight;
    protected Vector2 currentVelocity;
    /**
     * Constructs an Enemy instance.
     * @param defaultRunVelocity
     * @param currentVelocity
     * @param defaultJumpVelocity
     * @param chanceToJump
     * @param decideSpecialMovementDuration
     * @param maxRunVelocity
     */
    public Enemy(float defaultRunVelocity, Vector2 currentVelocity, float defaultJumpVelocity, float chanceToJump, float decideSpecialMovementDuration, int maxRunVelocity) {
        super(COLLISION_CATEGORY, MASK_BITS);
//        this.world = screenInstance.getWorld();
//        this.screen = screenInstance;
//        defineEnemy();

        this.defaultRunVelocity = defaultRunVelocity;
        this.currentVelocity = currentVelocity;
        this.defaultJumpVelocity = defaultJumpVelocity;
        this.chanceToJump = chanceToJump;
        decideSpecialMovementTimer = 0;
        this.decideSpecialMovementDuration = decideSpecialMovementDuration;
        this.maxRunVelocity = maxRunVelocity;
    }

    private void jump() {
        b2body.applyLinearImpulse(new Vector2(0, getDefaultJumpVelocity()), b2body.getWorldCenter(), true);
        Gdx.app.log("Enemy", "jump!");
    }

    private void idle() {
        setCurrentVelocity(new Vector2(0, 0));
    }

    protected final void run() {
        if (Math.abs(getB2body().getLinearVelocity().x) <= getMaxRunVelocity()) {
            getB2body().applyLinearImpulse(getCurrentVelocity(), getB2body().getWorldCenter(), true);
        }
    }

    protected final void applySpecialMovement() {
        if (decideSpecialMovementTimer >= getDecideSpecialMovementDuration()) {
            if (MathUtils.randomBoolean(getChanceToJump())) {
                jump();
                setCurrentVelocity(new Vector2(getDefaultRunVelocity(), 0));
            } else {
                idle();
            }
            decideSpecialMovementTimer = 0;
        }
    }

//    /**
//     * Defines the physical properties of the Enemy character in the game world.
//     */
//    protected abstract void defineEnemy();

    /**
     * Reverses the enemy's velocity in the x and/or y direction.
     * @param x - if true, reverse the x-component of the velocity
     * @param y - if true, reverse the y-component of the velocity
     */
    public void reverseVelocity(final boolean x, final boolean y) {
        if (x) {
            getCurrentVelocity().x = -getCurrentVelocity().x;
        }
        if (y) {
            getCurrentVelocity().y = -getCurrentVelocity().y;
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
    public Vector2 getCurrentVelocity() {
        return currentVelocity;
    }

    /**
     * Sets the velocity of the enemy.
     *
     * @param newVelocity - The new velocity to be set for the enemy.
     *                    This is a Vector2 object which contains the x and y components of the velocity.
     */
    public final void setCurrentVelocity(final Vector2 newVelocity) {
        this.currentVelocity = newVelocity;
    }

    /**
     * Determines the current state of the Kid character based on its linear velocity.
     *
     * @return The current state of the Kid character.
     */
    @Override
    public State getState() {
        if (getB2body().getLinearVelocity().x != 0) {
            return State.RUNNING;
        } else {
            return State.STANDING;
        }
    }
    protected float getDefaultRunVelocity() {
        return this.defaultRunVelocity;
    }

    protected float getDefaultJumpVelocity() {
        return this.defaultJumpVelocity;
    }

    protected float getChanceToJump() {
        return this.chanceToJump;
    }

    protected float getDecideSpecialMovementDuration() {
        return this.decideSpecialMovementDuration;
    }

    protected int getMaxRunVelocity() {
        return this.maxRunVelocity;
    }

//    /**
//     * The State enum represents the possible states of an Enemy.
//     * STANDING: The enemy is standing still.
//     * RUNNING: The enemy is running.
//     */
//    public enum State { STANDING, RUNNING }
}
