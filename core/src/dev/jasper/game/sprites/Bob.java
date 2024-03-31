package dev.jasper.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import dev.jasper.game.BobIsMelting;
import dev.jasper.game.EntityCollisionCategory;
import dev.jasper.game.screens.PlayScreen;

public class Bob extends Sprite {

    private World world;
    private Body b2body;
    private Fixture fixture;
    private TextureRegion fullHealth;
    private TextureRegion subHealth;
    private TextureRegion badHealth;
    private TextureRegion dying;


    public Bob(PlayScreen screen) {
        super(screen.getAtlas().findRegion("Snowman-tileset"));
        this.world = screen.getWorld();
        fullHealth = new TextureRegion(screen.getAtlas().findRegion("Snowman-tileset"), 1, -1, 16, 24);
        // TODO: add other textures
        defineBob();
        setBounds(0, 0, 16 / BobIsMelting.PPM, 24 / BobIsMelting.PPM);
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2 + 0.02f);
        setRegion(fullHealth);
    }
    /**
     * Updates the position of the Bob character in the game world.
     *
     * @param dt a float that represents delta time, the amount of time since the last frame was rendered.
     */
    public void update(float dt) {
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
    public void defineBob() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(16 * 7 / BobIsMelting.PPM, 16*4 / BobIsMelting.PPM + .1f);
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(10 / BobIsMelting.PPM);
        fdef.filter.categoryBits = EntityCollisionCategory.BOB_BIT;
        fdef.filter.maskBits = EntityCollisionCategory.KID_BIT;

        fdef.shape = shape;
        fixture = b2body.createFixture(fdef);
        fixture.setUserData(this);
    }
}
