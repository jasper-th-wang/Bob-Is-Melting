package dev.jasper.game.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import dev.jasper.game.BobIsMelting;
import dev.jasper.game.screens.PlayScreen;


/**
 * Represents the Kid character in the game, extending the Sprite class.
 * It defines the Kid's states, animations, and physical properties in the game world.
 *
 * @author Jasper Wang
 * @version 2024
 */
public class Kid extends Sprite {
    /**
     * Represents the various movement states that the Kid character can be in during the game.
     */
    public enum State {FALLING, JUMPING, STANDING, RUNNING};
    private State currentState;
    private State previousState;
    private final World world;
    private Body b2body;
    private final TextureRegion kidIdle;
    private final Animation<TextureRegion> kidRun;
    private final TextureRegion kidJump;
    // keep track of amount of time for any given state
    private float stateTimer;
    private boolean runningRight;
    private boolean hitByEnemy;

    /**
     * Constructs a Kid character for the game.
     *
     * @param screen The PlayScreen instance where the Kid character is displayed and interacts.
     */
    public Kid(PlayScreen screen) {
        super(screen.getAtlas().createSprite("Idle (32 x 32)"));
        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;
        hitByEnemy = false;
        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(getTexture(), 228 + i * 32, 134, 32, 32));
//            228,134,128,32
        }
        kidRun = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        kidJump = new TextureRegion(getTexture(), 132, 68, 32, 32);

        defineKid();
        kidIdle = new TextureRegion(getTexture(), 388, 392, 32, 32);
        setBounds(0, 0, 32 / BobIsMelting.PPM, 32 / BobIsMelting.PPM);
        setRegion(kidIdle);
    }
    /**
     * Updates the position of the Kid character in the game world.
     *
     * @param dt a float that represents delta time, the amount of time since the last frame was rendered.
     */
    public void update(float dt) {
        setPosition(getB2body().getPosition().x - getWidth() / 2, getB2body().getPosition().y - getHeight() / 4 );
        setRegion(getFrame(dt));
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
        final boolean bodyRunningToLeft = getB2body().getLinearVelocity().x < 0 || !runningRight;
        final boolean bodyRunningToRight = getB2body().getLinearVelocity().x > 0 || runningRight;
        final boolean spriteFacingRight = !region.isFlipX();
        final boolean spriteFacingLeft = region.isFlipX();
        if (bodyRunningToLeft && spriteFacingRight) {
            region.flip(true, false);
            runningRight = false;
        } else if (bodyRunningToRight && spriteFacingLeft) {
            region.flip(true, false);
            runningRight = true;
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
        bdef.position.set(32 / BobIsMelting.PPM, 32 / BobIsMelting.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(7 / BobIsMelting.PPM);
        fdef.filter.categoryBits = BobIsMelting.KID_BIT;
        fdef.filter.maskBits = BobIsMelting.GROUND_BIT | BobIsMelting.SNOWBALL_BIT | BobIsMelting.OBJECT_BIT | BobIsMelting.ENEMY_BIT;

        fdef.shape = shape;
        getB2body().createFixture(fdef).setUserData(this);

//        EdgeShape
    }
    public Body getB2body() {
        return b2body;
    }



}
