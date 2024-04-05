package dev.jasper.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import dev.jasper.game.BobIsMelting;
import dev.jasper.game.EntityCollisionCategory;
import dev.jasper.game.screens.PlayScreen;

public class Bob extends InteractiveEnviromentSprite {

    protected static final short COLLISION_CATEGORY = EntityCollisionCategory.BOB_BIT;
    protected static final int MASK_BITS = EntityCollisionCategory.KID_BIT | EntityCollisionCategory.KID_CARRY_SNOWBALL_BIT;
    private World world;
    private Body b2body;
    private Fixture fixture;
    private TextureRegion fullHealth;
    private TextureRegion subHealth;
    private TextureRegion badHealth;
    private TextureRegion dying;


    public Bob(final PlayScreen screen) {
        super(screen.getAtlas().findRegion("Snowman-tileset"));
        this.world = screen.getWorld();
        fullHealth = new TextureRegion(screen.getAtlas().findRegion("Snowman-tileset"), 1, -1, 16, 24);
        // TODO: add other textures
//        defineBob();
        setBounds(0, 0, 16 / BobIsMelting.PPM, 24 / BobIsMelting.PPM);
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2 + 0.02f);
        setRegion(fullHealth);
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
    @Override
    protected void defineShape() {
        final CircleShape shape = new CircleShape();
        shape.setRadius(10 / BobIsMelting.PPM);
        getFixtureDef().shape = shape;
    }
}
