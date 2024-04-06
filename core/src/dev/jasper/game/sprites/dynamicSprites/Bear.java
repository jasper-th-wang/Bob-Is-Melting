package dev.jasper.game.sprites.dynamicSprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.utils.Array;
import dev.jasper.game.BobIsMelting;

public class Bear extends AbstractEnemy {
    private static final int MAX_RUN_VELOCITY = 2;
    private static final float DEFAULT_RUN_VELOCITY = 0.05f;
    private static final float DEFAULT_JUMP_VELOCITY = 3f;
    private static final float CHANCE_TO_JUMP = 0.5F;
    private static final float DECIDE_SPECIAL_MOVEMENT_DURATION = 3f;
    private final float positionX;
    private final float positionY;
    private final float idleDuration = 3f;
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> walkAnimation;
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

    @Override
    protected TextureRegion getJumpFrame(float stateTime) {
        return null;
    }

    @Override
    protected TextureRegion getRunFrame(float stateTime) {
        return walkAnimation.getKeyFrame(getStateTimer(), true);
    }

    @Override
    protected TextureRegion getIdleFrame(float stateTime) {
        return idleAnimation.getKeyFrame(getStateTimer(), true);
    }

    @Override
    protected void defineDefaultSprite(TextureAtlas atlas) {
        setPosition(positionX, positionY);
        Array<TextureRegion> frames = new Array<>();
        for(int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(atlas.findRegion("bear_polar"), i * 32, 32, 32, 32));
            walkAnimation = new Animation<>(0.1f, frames);
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

    public void update(final float dt) {
        setStateTimer(getStateTimer() + dt);
        setDecideSpecialMovementTimer(getDecideSpecialMovementTimer() + dt);

        applySpecialMovement();

        run();

        setPosition(getB2body().getPosition().x - getWidth() / 2, getB2body().getPosition().y - getHeight() / 3);
        setRegion(getFrame(dt));
    }
}
