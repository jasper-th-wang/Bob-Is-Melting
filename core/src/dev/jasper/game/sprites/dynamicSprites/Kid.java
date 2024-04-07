package dev.jasper.game.sprites.dynamicSprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.utils.Array;
import dev.jasper.game.BobIsMelting;
import dev.jasper.game.EntityCollisionCategory;


/**
 * Represents the Kid character in the game, extending the Sprite class.
 * It defines the Kid's states, animations, and physical properties in the game world.
 *
 * @author Jasper Wang
 * @version 2024
 */
public final class Kid extends DynamicEntitySprite {
    private static final short COLLISION_CATEGORY = EntityCollisionCategory.KID_BIT;
    private static final short MASK_BITS = EntityCollisionCategory.GROUND_BIT | EntityCollisionCategory.SNOWBALL_BIT
            | EntityCollisionCategory.OBJECT_BIT | EntityCollisionCategory.ENEMY_BIT;
    private static final float INVINCIBLE_TO_ENEMY_DURATION = 4f;
    private static final int KID_SPRITE_WIDTH = 32;
    private static final int KID_SPRITE_HEIGHT = 32;
    private static final int SNOWBALL_SPRITE_WIDTH = 16;
    private static final int SNOWBALL_SPRITE_HEIGHT = 16;
    private static final int SPAWN_POSITION_X = 16 * 8;
    private static final int SPAWN_POSITION_Y = 16 * 4;
    private static final float ALPHA_INVINCIBLE = .2f;
    private static final float ALPHA_NORMAL = 1f;
    private static final int KID_SHAPE_RADIUS = 7;
    private static Kid kidSingleton;
    private final float flickerInterval = ALPHA_INVINCIBLE;
    private TextureRegion kidIdle;
    private Animation<TextureRegion> kidRun;
    private TextureRegion kidJump;
    private float invincibleToEnemyTimer;
    private float flickerTimer;
    private boolean isInvincibleToEnemy;
    private boolean isCarryingSnowball;
    private Sprite snowballSprite;

    private Kid() {
        super(COLLISION_CATEGORY, MASK_BITS);
        isInvincibleToEnemy = false;
        isCarryingSnowball = false;
    }

    /**
     * Create a singleton instance of the Kid class.
     * This method ensures that only one instance of the Kid class is created throughout the application.
     * If an instance already exists, it returns the existing instance. Otherwise, it creates a new instance.
     *
     * @param atlas The TextureAtlas object that contains the textures for the Kid character.
     * @return The singleton instance of the Kid class.
     */
    public static Kid getInstance(final TextureAtlas atlas) {
        if (kidSingleton != null) {
            return kidSingleton;
        }
        kidSingleton = new Kid();
        kidSingleton.defineDefaultSprite(atlas);
        kidSingleton.defineBodyDefPosition();
        kidSingleton.defineShape();

        return kidSingleton;
    }

    /**
     * Returns the invincibility status of the Kid character.
     * This method is used to check if the Kid character is currently invincible to enemies.
     * The Kid character becomes invincible after being hit by an enemy.
     *
     * @return boolean - true if the Kid character is invincible to enemies, false otherwise.
     */
    public boolean getIsInvincibleToEnemy() {
        return isInvincibleToEnemy;
    }

    @Override
    protected TextureRegion getJumpFrame(final float stateTime) {
        return kidJump;
    }

    @Override
    protected TextureRegion getRunFrame(final float stateTime) {
        return kidRun.getKeyFrame(getStateTimer(), true);
    }

    @Override
    protected TextureRegion getIdleFrame(final float stateTime) {
        return kidIdle;
    }

    @Override
    protected void defineDefaultSprite(final TextureAtlas atlas) {
        kidIdle = new TextureRegion(atlas.findRegion("Idle (32 x 32)"),
                0, 0, KID_SPRITE_WIDTH, KID_SPRITE_HEIGHT);
        setBounds(0, 0, KID_SPRITE_WIDTH / BobIsMelting.PPM, KID_SPRITE_WIDTH / BobIsMelting.PPM);
        setRegion(kidIdle);


        Array<TextureRegion> frames = new Array<>();
        final int frameCount = 4;
        for (int i = 0; i < frameCount; i++) {
            frames.add(new TextureRegion(atlas.findRegion("Running (32 x 32)"),
                    i * KID_SPRITE_WIDTH, 0, KID_SPRITE_WIDTH, KID_SPRITE_HEIGHT));
        }
        final float runFrameDuration = 0.1f;
        kidRun = new Animation<>(runFrameDuration, frames);
        frames.clear();

        kidJump = new TextureRegion(atlas.findRegion("Jumping (32 x 32)"),
                0, 0, KID_SPRITE_WIDTH, KID_SPRITE_HEIGHT);


        snowballSprite = new Sprite(new TextureRegion(atlas.findRegion("snowballs"),
                0, 0, SNOWBALL_SPRITE_WIDTH, SNOWBALL_SPRITE_HEIGHT));
        snowballSprite.setBounds(0, 0, SNOWBALL_SPRITE_WIDTH / BobIsMelting.PPM,
                (SNOWBALL_SPRITE_HEIGHT - 2) / BobIsMelting.PPM);
        snowballSprite.setRegion(snowballSprite);
    }

    @Override
    protected void defineShape() {
        CircleShape shape = new CircleShape();
        shape.setRadius(KID_SHAPE_RADIUS / BobIsMelting.PPM);
        getFixtureDef().shape = shape;
    }

    @Override
    protected void defineBodyDefPosition() {
        getBodyDef().position.set(SPAWN_POSITION_X / BobIsMelting.PPM, SPAWN_POSITION_Y / BobIsMelting.PPM);
    }

    /**
     * Updates the position of the Kid character in the game world.
     *
     * @param dt a float that represents delta time, the amount of time since the last frame was rendered.
     */
    public void update(final float dt) {
        final float xPositionOffset = getB2body().getPosition().x - getWidth() / 2;
        final float yPositionOffset = getB2body().getPosition().y - getHeight() / 4;
        setPosition(xPositionOffset, yPositionOffset);
        setRegion(getFrame(dt));
        updateCollisionState(dt);

        if (isCarryingSnowball) {
            snowballSprite.setPosition(getB2body().getPosition().x - snowballSprite.getWidth() / 2,
                    getB2body().getPosition().y + snowballSprite.getHeight() / 2);
            snowballSprite.setRegion(snowballSprite);
        }
    }

    private void updateCollisionState(final float dt) {
        if (isInvincibleToEnemy) {
            invincibleToEnemyTimer += dt;
            flickerTimer -= dt;

            // Add flicker if hit by enemy
            if (flickerTimer <= 0) {
                final float newAlpha;
                if (this.getColor().a == ALPHA_INVINCIBLE) {
                    newAlpha = ALPHA_NORMAL;
                } else {
                    newAlpha = ALPHA_INVINCIBLE;
                }
                this.setAlpha(newAlpha);
                flickerTimer = flickerInterval;
            }

            if (invincibleToEnemyTimer >= INVINCIBLE_TO_ENEMY_DURATION) {
                isInvincibleToEnemy = false;
                this.setAlpha(ALPHA_NORMAL);
                resetCollisionCategory();
            }
        }
    }

    private void resetCollisionCategory() {
        Filter filter = new Filter();
        filter.categoryBits = EntityCollisionCategory.KID_BIT;
        filter.maskBits = EntityCollisionCategory.GROUND_BIT | EntityCollisionCategory.SNOWBALL_BIT
                | EntityCollisionCategory.OBJECT_BIT | EntityCollisionCategory.ENEMY_BIT;
        getFixture().setFilterData(filter);
    }

    /**
     * Handles the actions to be taken when the Kid character is hit by an enemy.
     * This method is called when the Kid character collides with an enemy in the game.
     * It sets the Kid character to be invincible to enemies, preventing further effect from enemy collisions.
     */
    public void onEnemyHit() {
        setInvincibleToEnemy();
        isInvincibleToEnemy = true;
        flickerTimer = flickerInterval;
        invincibleToEnemyTimer = 0;
    }

    private void setInvincibleToEnemy() {
        Filter filter = new Filter();
        filter.categoryBits = EntityCollisionCategory.KID_INVINCIBLE_BIT;
        filter.maskBits = EntityCollisionCategory.GROUND_BIT;
        getFixture().setFilterData(filter);
    }

    /**
     * Handles the actions to be taken when the Kid character collects a snowball.
     * This method is called when the Kid character collides with a snowball in the game.
     * It changes the collision category of the Kid character and sets the carrying snowball status to true.
     */
    public void collectSnowball() {
        // change collision category
        Filter filter = new Filter();
        filter.categoryBits = EntityCollisionCategory.KID_CARRY_SNOWBALL_BIT;
        filter.maskBits = EntityCollisionCategory.GROUND_BIT
                | EntityCollisionCategory.BOB_BIT | EntityCollisionCategory.ENEMY_BIT;
        getFixture().setFilterData(filter);
        // add snowball sprite on top of Kid
        setIsCarryingSnowball(true);
    }

    /**
     * Handles the actions to be taken when the Kid character drops off a snowball.
     * This method is called when the Kid character collides with Bob in the game.
     * It resets the collision category of the Kid character and sets the carrying snowball status to false.
     */
    public void dropoffSnowball() {
        // change collision category
        resetCollisionCategory();
        // remove snowball sprite on top of Kid
        setIsCarryingSnowball(false);
    }

    @Override
    public void draw(final Batch batch) {
        super.draw(batch);
        if (getIsCarryingSnowball()) {
            snowballSprite.draw(batch);
        }
    }

    /**
     * Returns the carrying snowball status of the Kid character.
     * This method is used to check if the Kid character is currently carrying a snowball.
     *
     * @return boolean - true if the Kid character is carrying a snowball, false otherwise.
     */
    public boolean getIsCarryingSnowball() {
        return isCarryingSnowball;
    }

    /**
     * Sets the carrying snowball status of the Kid character.
     * This method is used to change the carrying snowball status of the Kid character.
     *
     * @param carryingSnowball - the new carrying snowball status of the Kid character.
     */
    public void setIsCarryingSnowball(final boolean carryingSnowball) {
        isCarryingSnowball = carryingSnowball;
    }
}
