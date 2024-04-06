package dev.jasper.game.sprites.dynamicSprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import dev.jasper.game.EntityCollisionCategory;

/**
 * The Enemy class serves as an abstract base class that provides a skeletal implementation for the enemies in the game.
 * It extends the Sprite class from the libGDX library,
 * inheriting its properties and methods for graphical representation.
 *
 * @author Jasper Wang
 * @version 2024
 */
public abstract class Enemy extends DynamicEntitySprite {
    private static final short COLLISION_CATEGORY = EntityCollisionCategory.ENEMY_BIT;
    private static final short MASK_BITS = EntityCollisionCategory.GROUND_BIT | EntityCollisionCategory.OBJECT_BIT | EntityCollisionCategory.KID_BIT | EntityCollisionCategory.KID_CARRY_SNOWBALL_BIT;
    private final int maxRunVelocity;
    private final float defaultRunVelocity;
    private final float defaultJumpVelocity;
    private final float chanceToJump;
    private final float decideSpecialMovementDuration;

    private float decideSpecialMovementTimer;
    private Vector2 currentVelocity;

    /**
     * Constructs an Enemy instance.
     *
     * @param defaultRunVelocity
     * @param currentVelocity
     * @param defaultJumpVelocity
     * @param chanceToJump
     * @param decideSpecialMovementDuration
     * @param maxRunVelocity
     */
    public Enemy(float defaultRunVelocity, Vector2 currentVelocity, float defaultJumpVelocity, float chanceToJump, float decideSpecialMovementDuration, int maxRunVelocity) {
        super(COLLISION_CATEGORY, MASK_BITS);

        this.defaultRunVelocity = defaultRunVelocity;
        this.currentVelocity = currentVelocity;
        this.defaultJumpVelocity = defaultJumpVelocity;
        this.chanceToJump = chanceToJump;
        this.decideSpecialMovementTimer = 0;
        this.decideSpecialMovementDuration = decideSpecialMovementDuration;
        this.maxRunVelocity = maxRunVelocity;
    }

    protected final void run() {
        if (Math.abs(getB2body().getLinearVelocity().x) <= getMaxRunVelocity()) {
            getB2body().applyLinearImpulse(getCurrentVelocity(), getB2body().getWorldCenter(), true);
        }
    }

    protected int getMaxRunVelocity() {
        return this.maxRunVelocity;
    }

    /**
     * Returns the enemy's velocity.
     *
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

    protected final void applySpecialMovement() {
        if (getDecideSpecialMovementTimer() >= getDecideSpecialMovementDuration()) {
            if (MathUtils.randomBoolean(getChanceToJump())) {
                jump();
                setCurrentVelocity(new Vector2(getDefaultRunVelocity(), 0));
            } else {
                idle();
            }
            setDecideSpecialMovementTimer(0);
        }
    }

    protected float getDecideSpecialMovementTimer() {
        return decideSpecialMovementTimer;
    }

    protected float getDecideSpecialMovementDuration() {
        return this.decideSpecialMovementDuration;
    }

    protected float getChanceToJump() {
        return this.chanceToJump;
    }

    private void jump() {
        getB2body().applyLinearImpulse(new Vector2(0, getDefaultJumpVelocity()), getB2body().getWorldCenter(), true);
    }

    protected float getDefaultRunVelocity() {
        return this.defaultRunVelocity;
    }

    private void idle() {
        setCurrentVelocity(new Vector2(0, 0));
    }

    protected float getDefaultJumpVelocity() {
        return this.defaultJumpVelocity;
    }

    protected void setDecideSpecialMovementTimer(float decideSpecialMovementTimer) {
        this.decideSpecialMovementTimer = decideSpecialMovementTimer;
    }

    /**
     * Reverses the enemy's velocity in the x and/or y direction.
     *
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
}
