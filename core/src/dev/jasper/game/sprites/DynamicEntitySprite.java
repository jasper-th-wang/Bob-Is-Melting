package dev.jasper.game.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class MovingB2Body extends Sprite implements InitializableB2Body {

    protected Body b2body;
    protected State currentState;
    protected State previousState;
    // keep track of amount of time for any given state
    protected float stateTimer;
    protected boolean isRunningRight;

    public MovingB2Body(final TextureRegion defaultTextureRegion) {
        super(defaultTextureRegion);
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        isRunningRight = true;
    }

    /**
     * Determines the current state of the Kid character based on its linear velocity.
     *
     * @return The current state of the Kid character.
     */
    public State getState() {
        if (getB2body().getLinearVelocity().y > 0) {
            return State.JUMPING;
        } else if (getB2body().getLinearVelocity().y < 0) {
            return State.FALLING;
        } else if (getB2body().getLinearVelocity().x != 0) {
            return State.RUNNING;
        } else {
            return State.STANDING;
        }
    }

    public Body getB2body() {
        return b2body;
    }

    protected TextureRegion getFrame(final float dt) {
        this.currentState = getState();
        TextureRegion region;
        switch (currentState) {
            case JUMPING:
                region = getJumpFrame(dt);
                break;
            case RUNNING:
                region = getRunFrame(dt);
                break;
            case FALLING:
            case STANDING:
            default:
                region = getIdleFrame(dt);
                break;
        }

        // match jumping, falling and standing sprites to current body direction of running
        final boolean bodyRunningToLeft = getB2body().getLinearVelocity().x < 0 || !isRunningRight;
        final boolean bodyRunningToRight = getB2body().getLinearVelocity().x > 0 || isRunningRight;
        final boolean spriteFacingRight = !region.isFlipX();
        final boolean spriteFacingLeft = region.isFlipX();
        if (bodyRunningToLeft && spriteFacingRight) {
            region.flip(true, false);
            isRunningRight = false;
        } else if (bodyRunningToRight && spriteFacingLeft) {
            region.flip(true, false);
            isRunningRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

//    protected abstract TextureRegion getJumpFrame();
    protected abstract TextureRegion getJumpFrame(float stateTime);

//    protected abstract TextureRegion getRunFrame();
    protected abstract TextureRegion getRunFrame(float stateTime);

//    protected abstract TextureRegion getIdleFrame();
    protected abstract TextureRegion getIdleFrame(float stateTime);
    /**
     * Represents the various movement states that the Kid character can be in during the game.
     */
    public enum State {FALLING, JUMPING, STANDING, RUNNING}
}
