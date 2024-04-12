package dev.jasper.game.sprites.dynamicSprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.utils.Array;
import dev.jasper.game.BobIsMelting;

/**
 * Represents a Bear enemy in the game.
 * The Bear class extends the AbstractEnemy class and defines the specific characteristics of a Bear enemy.
 *
 * @author Jasper Wang
 * @version 2024
 */
public final class Bear extends AbstractEnemy {
    private static final int MAX_RUN_VELOCITY = 2;
    private static final float DEFAULT_RUN_VELOCITY = 0.05f;
    private static final float DEFAULT_JUMP_VELOCITY = 3f;
    private static final float CHANCE_TO_JUMP = 0.5F;
    private static final float MAX_DECIDE_SPECIAL_MOVEMENT_DURATION = 4f;
    private static final float MIN_DECIDE_SPECIAL_MOVEMENT_DURATION = 1f;
    private static final int BEAR_SPHERE_RADIUS = 7;
    private final float positionX;
    private final float positionY;
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> walkAnimation;

    private Bear(final float x, final float y, final float specialMovementDuration) {
        super(DEFAULT_RUN_VELOCITY, new Vector2(DEFAULT_RUN_VELOCITY, 0), DEFAULT_JUMP_VELOCITY, CHANCE_TO_JUMP,
                specialMovementDuration, MAX_RUN_VELOCITY);
        this.positionX = x;
        this.positionY = y;
    }

    /**
     * Factory method to create a new Bear.
     *
     * @param atlas The TextureAtlas containing the Bear's textures.
     * @param x The x-coordinate of the Bear's position.
     * @param y The y-coordinate of the Bear's position.
     * @return A new Bear.
     */
    public static Bear enemyFactory(final TextureAtlas atlas, final float x, final float y) {
        final float specialMovementDuration = MathUtils.random(MAX_DECIDE_SPECIAL_MOVEMENT_DURATION) + MIN_DECIDE_SPECIAL_MOVEMENT_DURATION;
        final Bear bear = new Bear(x, y, specialMovementDuration);
        bear.defineDefaultSprite(atlas);
        bear.defineBodyDefPosition();
        bear.defineShape();
        return bear;
    }

    @Override
    protected TextureRegion getJumpFrame(final float stateTime) {
        return null;
    }

    @Override
    protected TextureRegion getRunFrame(final float stateTime) {
        return walkAnimation.getKeyFrame(getStateTimer(), true);
    }

    @Override
    protected TextureRegion getIdleFrame(final float stateTime) {
        return idleAnimation.getKeyFrame(getStateTimer(), true);
    }

    @Override
    protected void defineDefaultSprite(final TextureAtlas atlas) {
        setPosition(positionX, positionY);
        final float spriteBoundWidth = 24 / BobIsMelting.PPM;
        final float spriteBoundHeight = 24 / BobIsMelting.PPM;
        final float walkFrameDuration = 0.2f;
        final float idleFrameDuration = 1f;
        final int frameCount = 4;
        final int spriteWidth = 32;
        final int spriteHeight = 32;

        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < frameCount; i++) {
            frames.add(new TextureRegion(atlas.findRegion("bear_polar"),
                    i * spriteWidth, spriteHeight, spriteWidth, spriteHeight));
            walkAnimation = new Animation<>(walkFrameDuration, frames);
            setBounds(getX(), getY(), spriteBoundWidth, spriteBoundHeight);
        }
        frames.clear();

        for (int i = 0; i < 2; i++) {
            frames.add(new TextureRegion(atlas.findRegion("bear_polar"), i * spriteWidth, 0,
                    spriteWidth, spriteHeight));
            idleAnimation = new Animation<>(idleFrameDuration, frames);
            setBounds(getX(), getY(), spriteBoundWidth, spriteBoundHeight);
        }
        frames.clear();
    }

    @Override
    protected void defineShape() {
        CircleShape shape = new CircleShape();
        shape.setRadius(BEAR_SPHERE_RADIUS / BobIsMelting.PPM);
        getFixtureDef().shape = shape;
    }

    @Override
    protected void defineBodyDefPosition() {
        getBodyDef().position.set(getX(), getY());
    }
}
