package dev.jasper.game.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import dev.jasper.game.BobIsMelting;
import dev.jasper.game.EntityCollisionCategory;
import dev.jasper.game.screens.PlayScreen;

public class Bear extends Enemy {
    protected final int maxRunVelocity = 2;
    protected final float defaultRunVelocity = 0.05f;
    protected final float defaultJumpVelocity = 3f;
    protected final float chanceToJump = 0.5F;
    protected final float decideSpecialMovementDuration = 3f;
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> walkAnimation;
    private final float idleDuration = 3f;
    private float idleTimer;

    public Bear(final PlayScreen screen, final float x, final float y) {
        super(screen, x, y);
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("bear_polar"), i * 32, 32, 32, 32));
            walkAnimation = new Animation<TextureRegion>(0.1f, frames);
            setBounds(getX(), getY(), 24 / BobIsMelting.PPM, 24 / BobIsMelting.PPM);
        }
        frames.clear();

        for (int i = 0; i < 2; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("bear_polar"), i * 32, 0, 32, 32));
            idleAnimation = new Animation<>(1f, frames);
            setBounds(getX(), getY(), 24 / BobIsMelting.PPM, 24 / BobIsMelting.PPM);
        }
        frames.clear();

        idleTimer = 0;
    }

    public void update(final float dt) {

        stateTime += dt;
        decideSpecialMovementTimer += dt;

        applySpecialMovement();

        run();

        setPosition(getB2body().getPosition().x - getWidth() / 2, getB2body().getPosition().y - getHeight() / 3);
        setRegion(getFrame(dt));
    }

    @Override
    protected void defineEnemy() {

        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(7 / BobIsMelting.PPM);
        fdef.filter.categoryBits = EntityCollisionCategory.ENEMY_BIT;
        fdef.filter.maskBits = EntityCollisionCategory.GROUND_BIT | EntityCollisionCategory.OBJECT_BIT | EntityCollisionCategory.KID_BIT | EntityCollisionCategory.KID_CARRY_SNOWBALL_BIT;

        fdef.shape = shape;
        getB2body().createFixture(fdef).setUserData(this);
    }

    @Override
    protected float getDefaultRunVelocity() {
        return this.defaultRunVelocity;
    }

    @Override
    protected float getDefaultJumpVelocity() {
        return this.defaultJumpVelocity;
    }

    @Override
    protected float getChanceToJump() {
        return this.chanceToJump;
    }

    @Override
    protected float getDecideSpecialMovementDuration() {
        return this.decideSpecialMovementDuration;
    }

    @Override
    protected int getMaxRunVelocity() {
        return this.maxRunVelocity;
    }

    /**
     * Determines the current state of the Kid character based on its linear velocity.
     *
     * @return The current state of the Kid character.
     */
    public State getState() {
        if (getB2body().getLinearVelocity().x != 0) {
            return State.RUNNING;
        } else {
            return State.STANDING;
        }
    }
    private TextureRegion getFrame(final float dt) {
        this.currentState = getState();
        TextureRegion region;
        switch (currentState) {
            case RUNNING:
                region = walkAnimation.getKeyFrame(stateTime, true);
                break;
            case STANDING:
            default:
                region = idleAnimation.getKeyFrame(stateTime, true);
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

        stateTime = currentState == previousState ? stateTime + dt : 0;
        previousState = currentState;
        return region;
    }
}
