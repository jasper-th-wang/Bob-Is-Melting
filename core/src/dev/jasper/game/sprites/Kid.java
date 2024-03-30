package dev.jasper.game.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import dev.jasper.game.BobIsMelting;
import dev.jasper.game.EntityCollisionCategory;
import dev.jasper.game.screens.PlayScreen;


/**
 * Represents the Kid character in the game, extending the Sprite class.
 * It defines the Kid's states, animations, and physical properties in the game world.
 *
 * @author Jasper Wang
 * @version 2024
 */
public class Kid extends Sprite {
        private final World world;;
    private final TextureRegion kidIdle;
    private final Animation<TextureRegion> kidRun;
    private final TextureRegion kidJump;
    private final float invincibleToEnemyDuration = 4f;
    private final float flickerInterval = 0.2f;
    private State currentState;
    private State previousState;
    private Body b2body;
    private Fixture fixture;
    // keep track of amount of time for any given state
    private float stateTimer;
    private boolean isRunningRight;
    private float invincibleToEnemyTimer;
    private float flickerTimer;
    private boolean isInvincibleToEnemy;

    /**
     * Constructs a Kid character for the game.
     *
     * @param screen The PlayScreen instance where the Kid character is displayed and interacts.
     */
    public Kid(PlayScreen screen) {
        super(screen.getAtlas().findRegion("Idle (32 x 32)"));
        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        isRunningRight = true;
        isInvincibleToEnemy = false;
        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("Running (32 x 32)"), i * 32, 0, 32, 32));
//            228,134,128,32
        }
        kidRun = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        kidJump = new TextureRegion(screen.getAtlas().findRegion("Jumping (32 x 32)"), 0, 0, 32, 32);

        defineKid();
        kidIdle = new TextureRegion(screen.getAtlas().findRegion("Idle (32 x 32)"), 0, 0, 32, 32);
        setBounds(0, 0, 32 / BobIsMelting.PPM, 32 / BobIsMelting.PPM);
        setRegion(kidIdle);
    }

    public boolean getIsInvincibleToEnemy() {
        return isInvincibleToEnemy;
    }

    /**
     * Updates the position of the Kid character in the game world.
     *
     * @param dt a float that represents delta time, the amount of time since the last frame was rendered.
     */
    public void update(float dt) {
        setPosition(getB2body().getPosition().x - getWidth() / 2, getB2body().getPosition().y - getHeight() / 4 );
        setRegion(getFrame(dt));
        // Add flicker if hit by enemy
        if (isInvincibleToEnemy) {
            invincibleToEnemyTimer += dt;
            flickerTimer -= dt;

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

    private TextureRegion getFrame(float dt) {
        this.currentState = getState();
        TextureRegion region;
        switch (currentState) {
            case JUMPING:
                region = kidJump;
                break;
            case RUNNING:
                region = kidRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = kidIdle;
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

    /**
     * Defines the physical properties of the Kid character in the game world.
     */
    public void defineKid() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(16 * 8 / BobIsMelting.PPM, 16*4 / BobIsMelting.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(7 / BobIsMelting.PPM);
        fdef.filter.categoryBits = EntityCollisionCategory.KID_BIT;
        fdef.filter.maskBits = EntityCollisionCategory.GROUND_BIT | EntityCollisionCategory.SNOWBALL_BIT | EntityCollisionCategory.OBJECT_BIT | EntityCollisionCategory.ENEMY_BIT;

        fdef.shape = shape;
        fixture = getB2body().createFixture(fdef);
        fixture.setUserData(this);

//        EdgeShape
    }

    public Body getB2body() {
        return b2body;
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

/**
     * Represents the various movement states that the Kid character can be in during the game.
     */
    public enum State {FALLING, JUMPING, STANDING, RUNNING}


}
