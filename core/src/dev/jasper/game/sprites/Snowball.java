package dev.jasper.game.sprites;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import dev.jasper.game.BobIsMelting;
import dev.jasper.game.EntityCollisionCategory;
import dev.jasper.game.screens.PlayScreen;

public class Snowball extends InteractiveEnviromentSprite {

    protected static final short COLLISION_CATEGORY = EntityCollisionCategory.SNOWBALL_BIT;
    protected static final short MASK_BITS = EntityCollisionCategory.GROUND_BIT | EntityCollisionCategory.KID_BIT | EntityCollisionCategory.KID_INVINCIBLE_BIT;
    private World world;
    private TiledMap map;
    private Vector2 position;
    private MapObject object;
    private Body b2body;
    private TextureRegion snowballSprite;
    private boolean collected;
    private boolean toCollect;
    private Array<Snowball> snowballsRef;
    private int snowballsRefIndex;

    /**
     * Constructs a Snowball instance.
     * @param screen - the game screen
     * @param position - the position of the snowball
     */
    public Snowball(final TextureAtlas atlas, final Vector2 position, final Array<Snowball> snowballsRef, final int snowballsRefIndex){
        super(atlas.findRegion("snowballs"), position.x, position.y, COLLISION_CATEGORY, MASK_BITS);
//        this.world = screen.getWorld();
//        this.map = screen.getMap();
//        this.position = position;
        this.snowballsRef = snowballsRef;
        this.snowballsRefIndex = snowballsRefIndex;
        toCollect = false;
        collected = false;

//        defineSnowball();

        snowballSprite = new TextureRegion(atlas.findRegion("snowballs"), 0, 0, 16, 16);
        setBounds(0, 0, 16 / BobIsMelting.PPM, 14 / BobIsMelting.PPM);
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(snowballSprite);
        defineShape();
    }

//    private void defineSnowball() {
//        BodyDef bdef = new BodyDef();
//        FixtureDef fdef = new FixtureDef();
//        PolygonShape shape = new PolygonShape();
//        shape.setAsBox(6 / BobIsMelting.PPM, 6 / BobIsMelting.PPM);
//
//        bdef.position.set((this.position.x + 8) / BobIsMelting.PPM, (this.position.y + 8) / BobIsMelting.PPM);
//        bdef.type = BodyDef.BodyType.StaticBody;
//
//        fdef.filter.categoryBits = COLLISION_CATEGORY;
//        fdef.filter.maskBits = MASK_BITS;
//        fdef.shape = shape;
//
//        b2body = world.createBody(bdef);
//        b2body.createFixture(fdef).setUserData(this);
//    }
    @Override
    protected void defineShape() {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(6 / BobIsMelting.PPM, 6 / BobIsMelting.PPM);
        getFixtureDef().shape = shape;
    }
    public void collect(final Kid kid) {
        toCollect = true;
        kid.collectSnowball();
    }

    public void update(final float dt) {
        if (toCollect && !collected) {
            world.destroyBody(b2body);
            snowballsRef.set(snowballsRefIndex, null);
            collected = true;
        }
    }
    @Override
    public void draw(final Batch batch){
        if (!collected) {
            super.draw(batch);
        }
    }
}


