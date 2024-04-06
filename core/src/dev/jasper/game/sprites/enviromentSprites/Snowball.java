package dev.jasper.game.sprites.enviromentSprites;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import dev.jasper.game.BobIsMelting;
import dev.jasper.game.EntityCollisionCategory;
import dev.jasper.game.sprites.dynamicSprites.Kid;

public class Snowball extends InteractiveEnviromentSprite {

    protected static final short COLLISION_CATEGORY = EntityCollisionCategory.SNOWBALL_BIT;
    protected static final short MASK_BITS = EntityCollisionCategory.GROUND_BIT | EntityCollisionCategory.KID_BIT | EntityCollisionCategory.KID_INVINCIBLE_BIT;

    public boolean isCollected() {
        return collected;
    }

    // TODO: simplify these two variables
    private boolean collected;
    private boolean toCollect;
    private final Array<Snowball> snowballsRef;
    private final int snowballsRefIndex;
    private final Vector2 position;

    /**
     * Constructs a Snowball instance.
     */
    private Snowball(final Vector2 position, final Array<Snowball> snowballsRef, final int snowballsRefIndex){
        super(COLLISION_CATEGORY, MASK_BITS);

        this.position = position;
        this.snowballsRef = snowballsRef;
        this.snowballsRefIndex = snowballsRefIndex;
        toCollect = false;
        collected = false;
//
    }
    public static Snowball snowballFactory(final TextureAtlas atlas, final Vector2 position, final Array<Snowball> snowballsRef, final int snowballsRefIndex){
        final Snowball snowball = new Snowball(position, snowballsRef, snowballsRefIndex);
        snowball.defineDefaultSprite(atlas);
        snowball.defineBodyDefPosition();
        snowball.defineShape();
        return snowball;
    }

    @Override
    protected void defineDefaultSprite(TextureAtlas atlas) {
        TextureRegion snowballSprite = new TextureRegion(atlas.findRegion("snowballs"), 0, 0, 16, 16);
        setBounds(0, 0, 16 / BobIsMelting.PPM, 14 / BobIsMelting.PPM);
        setPosition(position.x / BobIsMelting.PPM, position.y / BobIsMelting.PPM);
        setRegion(snowballSprite);
    }

    @Override
    protected void defineShape() {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(6 / BobIsMelting.PPM, 6 / BobIsMelting.PPM);
        getFixtureDef().shape = shape;
    }

    @Override
    protected void defineBodyDefPosition() {
        getBodyDef().position.set((this.position.x + 8) / BobIsMelting.PPM, (this.position.y + 8) / BobIsMelting.PPM);
    }

//    public void collect(final Kid kid) {
//        toCollect = true;
//        kid.collectSnowball();
//    }
    public void collect() {
        toCollect = true;
    }

    public void update(final float dt) {
        if (toCollect && !collected) {
//            world.destroyBody(b2body);
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


