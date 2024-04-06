package dev.jasper.game.sprites.dynamicSprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import dev.jasper.game.BobIsMelting;
import dev.jasper.game.EntityCollisionCategory;

public class Bear extends Enemy {
    private final float positionX;
    private final float positionY;
    protected static final int MAX_RUN_VELOCITY = 2;
    protected static final float DEFAULT_RUN_VELOCITY = 0.05f;
    protected static final float DEFAULT_JUMP_VELOCITY = 3f;
    protected static final float CHANCE_TO_JUMP = 0.5F;
    protected static final float DECIDE_SPECIAL_MOVEMENT_DURATION = 3f;
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> walkAnimation;
    private final float idleDuration = 3f;
    private float idleTimer;

    public Bear(final float x, final float y) {
        super(DEFAULT_RUN_VELOCITY, new Vector2(DEFAULT_RUN_VELOCITY, 0), DEFAULT_JUMP_VELOCITY, CHANCE_TO_JUMP, DECIDE_SPECIAL_MOVEMENT_DURATION, MAX_RUN_VELOCITY);
        this.positionX = x;
        this.positionY = y;
        this.idleTimer = 0;
    }

    public static Bear bearFactory(final TextureAtlas atlas, final float x, final float y) {
        final Bear bear = new Bear(x, y);
        bear.defineDefaultSprite(atlas);
        bear.defineBodyDefPosition();
        bear.defineShape();
        return bear;
    }

    public void update(final float dt) {

        stateTimer += dt;
        decideSpecialMovementTimer += dt;

        applySpecialMovement();

        run();

        setPosition(getB2body().getPosition().x - getWidth() / 2, getB2body().getPosition().y - getHeight() / 3);
        setRegion(getFrame(dt));
    }

//    @Override
//    protected void defineEnemy() {
//
//        BodyDef bdef = new BodyDef();
//        bdef.type = BodyDef.BodyType.DynamicBody;
//        b2body = world.createBody(bdef);
//
//        FixtureDef fdef = new FixtureDef();
////        fdef.filter.categoryBits =
////        fdef.filter.maskBits =
//
//        getB2body().createFixture(fdef).setUserData(this);
//    }
//


//    private TextureRegion getFrame(final float dt) {
//        this.currentState = getState();
//        TextureRegion region;
//        switch (currentState) {
//            case RUNNING:
//                region = walkAnimation.getKeyFrame(stateTime, true);
//                break;
//            case STANDING:
//            default:
//                region = idleAnimation.getKeyFrame(stateTime, true);
//                break;
//        }
//
//        // match jumping, falling and standing sprites to current body direction of running
//        final boolean bodyRunningToLeft = getB2body().getLinearVelocity().x < 0 || !isRunningRight;
//        final boolean bodyRunningToRight = getB2body().getLinearVelocity().x > 0 || isRunningRight;
//        final boolean spriteFacingRight = !region.isFlipX();
//        final boolean spriteFacingLeft = region.isFlipX();
//        if (bodyRunningToLeft && spriteFacingRight) {
//            region.flip(true, false);
//            isRunningRight = false;
//        } else if (bodyRunningToRight && spriteFacingLeft) {
//            region.flip(true, false);
//            isRunningRight = true;
//        }
//
//        stateTime = currentState == previousState ? stateTime + dt : 0;
//        previousState = currentState;
//        return region;
//    }

    @Override
    protected TextureRegion getJumpFrame(float stateTime) {
        return null;
    }

    @Override
    protected TextureRegion getRunFrame(float stateTime) {
        return walkAnimation.getKeyFrame(stateTimer, true);
    }

    @Override
    protected TextureRegion getIdleFrame(float stateTime) {
        return idleAnimation.getKeyFrame(stateTimer, true);
    }

    @Override
    protected void defineDefaultSprite(TextureAtlas atlas) {

        setPosition(positionX, positionY);
        Array<TextureRegion> frames = new Array<>();
        for(int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(atlas.findRegion("bear_polar"), i * 32, 32, 32, 32));
            walkAnimation = new Animation<TextureRegion>(0.1f, frames);
            setBounds(getX(), getY(), 24 / BobIsMelting.PPM, 24 / BobIsMelting.PPM);
        }
        frames.clear();

        for (int i = 0; i < 2; i++) {
            frames.add(new TextureRegion(atlas.findRegion("bear_polar"), i * 32, 0, 32, 32));
            idleAnimation = new Animation<>(1f, frames);
            setBounds(getX(), getY(), 24 / BobIsMelting.PPM, 24 / BobIsMelting.PPM);
        }
        frames.clear();

    }

    @Override
    protected void defineShape() {

        CircleShape shape = new CircleShape();
        shape.setRadius(7 / BobIsMelting.PPM);
        getFixtureDef().shape = shape;
    }

    @Override
    protected void defineBodyDefPosition() {

        getBodyDef().position.set(getX(), getY());
    }
}
