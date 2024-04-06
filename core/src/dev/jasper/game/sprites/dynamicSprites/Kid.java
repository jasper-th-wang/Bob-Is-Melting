package dev.jasper.game.sprites.dynamicSprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
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
public class Kid extends DynamicEntitySprite {
    protected static final short COLLISION_CATEGORY = EntityCollisionCategory.KID_BIT;
    protected static final short MASK_BITS = EntityCollisionCategory.GROUND_BIT | EntityCollisionCategory.SNOWBALL_BIT | EntityCollisionCategory.OBJECT_BIT | EntityCollisionCategory.ENEMY_BIT;
    private static Kid kidSingleton;
    private final float invincibleToEnemyDuration = 4f;
    private final float flickerInterval = 0.2f;
    private TextureRegion kidIdle;
    private Animation<TextureRegion> kidRun;
    private TextureRegion kidJump;
    private float invincibleToEnemyTimer;
    private float flickerTimer;
    private boolean isInvincibleToEnemy;
    private boolean isCarryingSnowball;
    // TODO: testing
    private Sprite snowballSprite;

    private Kid() {
        super(COLLISION_CATEGORY, MASK_BITS);
        isInvincibleToEnemy = false;
        isCarryingSnowball = false;
    }

    public static Kid kidFactory(final TextureAtlas atlas) {
        if (kidSingleton != null) {
            return kidSingleton;
        }
        kidSingleton = new Kid();
        kidSingleton.defineDefaultSprite(atlas);
        kidSingleton.defineBodyDefPosition();
        kidSingleton.defineShape();

        return kidSingleton;
    }

    public boolean getIsCarryingSnowball() {
        return isCarryingSnowball;
    }

    public void setIsCarryingSnowball(final boolean carryingSnowball) {
        isCarryingSnowball = carryingSnowball;
    }

    public boolean getIsInvincibleToEnemy() {
        return isInvincibleToEnemy;
    }

    /**
     * Updates the position of the Kid character in the game world.
     *
     * @param dt a float that represents delta time, the amount of time since the last frame was rendered.
     */
    public void update(final float dt) {
        setPosition(getB2body().getPosition().x - getWidth() / 2, getB2body().getPosition().y - getHeight() / 4 );
        setRegion(getFrame(dt));
        updateCollisionState(dt);

        if (isCarryingSnowball) {
            snowballSprite.setPosition(getB2body().getPosition().x - snowballSprite.getWidth() / 2, getB2body().getPosition().y + snowballSprite.getHeight() / 2);
            snowballSprite.setRegion(snowballSprite);
        }
    }

    private void updateCollisionState(final float dt) {
        if (isInvincibleToEnemy) {
            invincibleToEnemyTimer += dt;
            flickerTimer -= dt;

            // Add flicker if hit by enemy
            if (flickerTimer <= 0) {
                final float newAlpha = this.getColor().a == .2f ? 1f : .2f;
                this.setAlpha(newAlpha);
                flickerTimer = flickerInterval;
            }

            if (invincibleToEnemyTimer >= invincibleToEnemyDuration) {
                isInvincibleToEnemy = false;
                this.setAlpha(1f);
                resetCollisionCategory();
            }
        }
    }

    @Override
    protected TextureRegion getJumpFrame(final float stateTime) {
        return kidJump;
    }

    @Override
    protected TextureRegion getRunFrame(final float stateTime) {
        return kidRun.getKeyFrame(stateTimer, true);
    }

    @Override
    protected TextureRegion getIdleFrame(final float stateTime) {
        return kidIdle;
    }


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
        fixture.setFilterData(filter);
    }

    private void resetCollisionCategory() {
        Filter filter = new Filter();
        filter.categoryBits = EntityCollisionCategory.KID_BIT;
        filter.maskBits = EntityCollisionCategory.GROUND_BIT | EntityCollisionCategory.SNOWBALL_BIT | EntityCollisionCategory.OBJECT_BIT | EntityCollisionCategory.ENEMY_BIT;
        fixture.setFilterData(filter);
    }


    public void collectSnowball() {
        // change collision category
        Filter filter = new Filter();
        filter.categoryBits = EntityCollisionCategory.KID_CARRY_SNOWBALL_BIT;
        filter.maskBits = EntityCollisionCategory.GROUND_BIT | EntityCollisionCategory.BOB_BIT | EntityCollisionCategory.ENEMY_BIT;
        fixture.setFilterData(filter);
        // add snowball sprite on top of Kid
        setIsCarryingSnowball(true);
    }
    public void dropoffSnowball() {
        // change collision category
        resetCollisionCategory();
        // remove snowball sprite on top of Kid
        setIsCarryingSnowball(false);
    }

    @Override
    public void draw(final Batch batch){
        super.draw(batch);
        if (getIsCarryingSnowball()) {
            snowballSprite.draw(batch);
        }
    }

    @Override
    protected void defineDefaultSprite(TextureAtlas atlas) {
        kidIdle = new TextureRegion(atlas.findRegion("Idle (32 x 32)"), 0, 0, 32, 32);
        setBounds(0, 0, 32 / BobIsMelting.PPM, 32 / BobIsMelting.PPM);
        setRegion(kidIdle);


        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(atlas.findRegion("Running (32 x 32)"), i * 32, 0, 32, 32));
//            228,134,128,32
        }
        kidRun = new Animation<>(0.1f, frames);
        frames.clear();

        kidJump = new TextureRegion(atlas.findRegion("Jumping (32 x 32)"), 0, 0, 32, 32);


        snowballSprite = new Sprite(new TextureRegion(atlas.findRegion("snowballs"), 0, 0, 16, 16));
        snowballSprite.setBounds(0, 0, 16 / BobIsMelting.PPM, 14 / BobIsMelting.PPM);
        snowballSprite.setRegion(snowballSprite);
    }

    @Override
    protected void defineShape() {
        CircleShape shape = new CircleShape();
        shape.setRadius(7 / BobIsMelting.PPM);
        getFixtureDef().shape = shape;
    }

    @Override
    protected void defineBodyDefPosition() {
        getBodyDef().position.set(16 * 8 / BobIsMelting.PPM, 16*4 / BobIsMelting.PPM);
    }
}
