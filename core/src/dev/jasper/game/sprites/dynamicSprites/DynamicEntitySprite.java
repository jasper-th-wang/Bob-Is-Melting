package dev.jasper.game.sprites.dynamicSprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import dev.jasper.game.sprites.InitializableB2Body;

/**
 * This abstract class represents a dynamic (cna move around in the world) entity sprite in the game.
 * It extends the Sprite class and implements the InitializableB2Body interface.
 * It contains methods for getting and setting the body and fixture of the sprite,
 * as well as methods for getting the current state of the sprite and updating it.
 *
 * @author Jasper Wang
 * @version 2024
 */
public abstract class DynamicEntitySprite extends Sprite implements InitializableB2Body {

    private final BodyDef bodyDef;
    private final FixtureDef fixtureDef;
    private Body b2body;
    private Fixture fixture;
    private State currentState;
    private State previousState;
    private float stateTimer;
    private boolean isRunningRight;
    /**
     * Constructor for the DynamicEntitySprite class.
     * Initializes the body and fixture definitions, and sets the initial state to STANDING.
     *
     * @param collisionCategory The category of the collision.
     * @param collisionMaskBits The mask bits of the collision.
     */
    public DynamicEntitySprite(final short collisionCategory, final short collisionMaskBits) {
        super();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        isRunningRight = true;


        bodyDef = new BodyDef();
        getBodyDef().type = BodyDef.BodyType.DynamicBody;

        fixtureDef = new FixtureDef();
        getFixtureDef().filter.categoryBits = collisionCategory;
        getFixtureDef().filter.maskBits = collisionMaskBits;
    }

    @Override
    public final BodyDef getBodyDef() {
        return bodyDef;
    }

    @Override
    public final FixtureDef getFixtureDef() {
        return fixtureDef;
    }

    @Override
    public final Body getB2body() {
        return b2body;
    }

    @Override
    public final Fixture getFixture() { return fixture; }

    @Override
    public final void setFixture(Fixture fixture) {
        this.fixture = fixture;
    }

    @Override
    public final void setB2body(final Body b2body) {
        this.b2body = b2body;
    }

    protected final TextureRegion getFrame(final float dt) {
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

        matchSpriteRegionToBodyDirection(region);

        stateTimer = currentState == previousState ? getStateTimer() + dt : 0;
        previousState = currentState;
        return region;
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

    protected abstract TextureRegion getJumpFrame(float stateTime);

    protected abstract TextureRegion getRunFrame(float stateTime);

    protected abstract TextureRegion getIdleFrame(float stateTime);

    private void matchSpriteRegionToBodyDirection(final TextureRegion region) {
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
    }

    protected final float getStateTimer() {
        return stateTimer;
    }

    protected final void setStateTimer(final float stateTimer) {
        this.stateTimer = stateTimer;
    }

    protected abstract void defineDefaultSprite(TextureAtlas atlas);

    protected abstract void defineShape();

    protected abstract void defineBodyDefPosition();

    /**
     * Updates the state of the dynamic entity sprite.
     * This method should be overridden in subclasses to provide specific update logic.
     * It is typically used to update the position, velocity,
     * or other properties of the sprite based on the elapsed time.
     *
     * @param dt The amount of time that has passed since the last update, typically the time between frames.
     */
    public abstract void update(float dt);

    /**
     * Represents the various movement states that a Dynamic Entity can be in during the game.
     */
    public enum State { FALLING, JUMPING, STANDING, RUNNING }
}
