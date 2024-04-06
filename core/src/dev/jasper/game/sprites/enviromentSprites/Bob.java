package dev.jasper.game.sprites.enviromentSprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import dev.jasper.game.BobIsMelting;
import dev.jasper.game.EntityCollisionCategory;

public class Bob extends InteractiveEnviromentSprite {

    private static Bob bobSingleton;
    private final static float POSITION_X = 16 * 7;
    private final static float POSITION_Y = 16 * 4;
    protected static final short COLLISION_CATEGORY = EntityCollisionCategory.BOB_BIT;
    protected static final short MASK_BITS = EntityCollisionCategory.KID_BIT | EntityCollisionCategory.KID_CARRY_SNOWBALL_BIT;
    private TextureRegion fullHealth;
    private TextureRegion subHealth;
    private TextureRegion badHealth;
    private TextureRegion dying;


    private Bob() {
        super(COLLISION_CATEGORY, MASK_BITS);
        // TODO: add other textures
    }
    public static Bob bobFactory(final TextureAtlas atlas) {
        if (bobSingleton != null) {
            return bobSingleton;
        }
        bobSingleton = new Bob();
        bobSingleton.defineDefaultSprite(atlas);
        bobSingleton.defineBodyDefPosition();
        bobSingleton.defineShape();

        return bobSingleton;
    }

    @Override
    protected void defineShape() {
        final CircleShape shape = new CircleShape();
        shape.setRadius(10 / BobIsMelting.PPM);
        getFixtureDef().shape = shape;
    }
    @Override
    protected void defineDefaultSprite(TextureAtlas atlas) {
        fullHealth = new TextureRegion(atlas.findRegion("Snowman-tileset"), 1, -1, 16, 24);
        setBounds(0, 0, 16 / BobIsMelting.PPM, 24 / BobIsMelting.PPM);
        setPosition(POSITION_X / BobIsMelting.PPM - getWidth() / 2, POSITION_Y / BobIsMelting.PPM);
        setRegion(fullHealth);
    }

    @Override
    protected void defineBodyDefPosition() {
//        setPosition(POSITION_X - getWidth() / 2, POSITION_Y - getHeight() / 2 + 0.02f);
        getBodyDef().position.set(POSITION_X / BobIsMelting.PPM, POSITION_Y / BobIsMelting.PPM + .1f);
    }
    /**
     * Updates the position of the Bob character in the game world.
     *
     * @param dt a float that represents delta time, the amount of time since the last frame was rendered.
     */
    public void update(final float dt) {
        setRegion(getFrame());
    }
    private TextureRegion getFrame() {
        // stub implementation for now
        // calculation should be based on health
        return fullHealth;
    }

    /**
     * Defines the physical properties of the Bob character in the game world.
     */
//    public void defineBob() {
//        BodyDef bdef = new BodyDef();
//        FixtureDef fdef = new FixtureDef();
//
//        bdef.position.set(16 * 7 / BobIsMelting.PPM, 16*4 / BobIsMelting.PPM + .1f);
//        bdef.type = BodyDef.BodyType.StaticBody;
//
//        CircleShape shape = new CircleShape();
//        shape.setRadius(10 / BobIsMelting.PPM);
//        fdef.filter.categoryBits = COLLISION_CATEGORY;
//        fdef.filter.maskBits = MASK_BITS;
//        fdef.shape = shape;
//
//        b2body = world.createBody(bdef);
//        fixture = b2body.createFixture(fdef);
//        fixture.setUserData(this);
//    }
}
