package dev.jasper.game.sprites.dynamicSprites;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import dev.jasper.game.EntityCollisionCategory;

/**
 * The AbstractEnemy class that provides a skeletal implementation for the enemies in the game.
 * It extends the Sprite class from the libGDX library,
 * inheriting its properties and methods for graphical representation.
 *
 * @author Jasper Wang
 * @version 2024
 */
public abstract class AbstractEnemy extends DynamicB2BodySprite {
    private static final short COLLISION_CATEGORY = EntityCollisionCategory.ENEMY_BIT;
    private static final short MASK_BITS = EntityCollisionCategory.GROUND_BIT | EntityCollisionCategory.OBJECT_BIT
            | EntityCollisionCategory.KID_BIT | EntityCollisionCategory.KID_CARRY_SNOWBALL_BIT
            | EntityCollisionCategory.ENEMY_BOUNDARY_BIT;
    private final float maxRunVelocity;
    private final float defaultRunVelocity;
    private final float defaultJumpVelocity;
    private final float chanceToJump;
    private final float decideSpecialMovementDuration;

    private float decideSpecialMovementTimer;
    private Vector2 currentVelocity;

    /**
     * Constructs an AbstractEnemy with the specified parameters.
     *
     * @param defaultRunVelocity            The default running velocity of the enemy.
     * @param currentVelocity               The current velocity of the enemy.
     * @param defaultJumpVelocity           The default jumping velocity of the enemy.
     * @param chanceToJump                  The chance for the enemy to jump.
     * @param decideSpecialMovementDuration The duration to decide the special movement of the enemy.
     * @param maxRunVelocity                The maximum running velocity of the enemy.
     */
    public AbstractEnemy(final float defaultRunVelocity, final Vector2 currentVelocity,
                         final float defaultJumpVelocity, final float chanceToJump,
                         final float decideSpecialMovementDuration, final float maxRunVelocity) {
        super(COLLISION_CATEGORY, MASK_BITS);

        this.defaultRunVelocity = defaultRunVelocity;
        this.currentVelocity = currentVelocity;
        this.defaultJumpVelocity = defaultJumpVelocity;
        this.chanceToJump = chanceToJump;
        this.decideSpecialMovementTimer = 0;
        this.decideSpecialMovementDuration = decideSpecialMovementDuration;
        this.maxRunVelocity = maxRunVelocity;
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

    /**
     * Updates the state of the enemy sprite and body.
     * This method is called periodically to update the state of the enemy in the game.
     * It increments the state timer and the special movement decision timer,
     * applies any special movement, and updates the enemy's position.
     *
     * @param dt The time delta, representing the amount of time passed since the last update.
     */
    public void update(final float dt) {
        setStateTimer(getStateTimer() + dt);
        setDecideSpecialMovementTimer(getDecideSpecialMovementTimer() + dt);

        applySpecialMovement();

        run();

        final float xPositionOffset = getB2body().getPosition().x - getWidth() / 2;
        final float yPositionOffset = getB2body().getPosition().y - getHeight() / 3;
        setPosition(xPositionOffset, yPositionOffset);
        setRegion(getFrame(dt));
    }

    protected final float getDecideSpecialMovementTimer() {
        return decideSpecialMovementTimer;
    }

    protected final void applySpecialMovement() {
//        // Half the time the enemy will not apply special movemen
//        if (MathUtils.randomBoolean(.5f)) {
//            return;
//        }
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

    protected final void run() {
        if (Math.abs(getB2body().getLinearVelocity().x) <= getMaxRunVelocity()) {
            getB2body().applyLinearImpulse(getCurrentVelocity(), getB2body().getWorldCenter(), true);
        }
    }

    protected final float getDecideSpecialMovementDuration() {
        return this.decideSpecialMovementDuration;
    }

    protected final float getChanceToJump() {
        return this.chanceToJump;
    }

    private void jump() {
        getB2body().applyLinearImpulse(new Vector2(0, getDefaultJumpVelocity()), getB2body().getWorldCenter(), true);
    }

    protected final float getDefaultRunVelocity() {
        return this.defaultRunVelocity;
    }

    private void idle() {
        setCurrentVelocity(new Vector2(0, 0));
    }

    protected final float getMaxRunVelocity() {
        return this.maxRunVelocity;
    }

    protected final float getDefaultJumpVelocity() {
        return this.defaultJumpVelocity;
    }

    protected final void setDecideSpecialMovementTimer(final float decideSpecialMovementTimer) {
        this.decideSpecialMovementTimer = decideSpecialMovementTimer;
    }
}
