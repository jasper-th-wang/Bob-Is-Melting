package dev.jasper.game.sprites.enviromentSprites;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import dev.jasper.game.BobIsMelting;
import dev.jasper.game.EntityCollisionCategory;

/**
 * Represents a Snowball in the game.
 * The Snowball class extends the InteractiveEnviromentSprite class
 * and defines the specific characteristics of a Snowball.
 *
 * @author Jasper Wang
 * @version 2024
 */
public final class Snowball extends InteractiveEnviromentSprite {

    private static final short COLLISION_CATEGORY = EntityCollisionCategory.SNOWBALL_BIT;
    private static final short MASK_BITS = EntityCollisionCategory.GROUND_BIT | EntityCollisionCategory.KID_BIT
            | EntityCollisionCategory.KID_INVINCIBLE_BIT;
    private static final int SPRITE_WIDTH = 16;
    private static final int SPRITE_HEIGHT = 16;
    private static final int SNOWBALL_POLYGON_HALF_WIDTH = 6;
    private static final int SNOWBALL_POLYGON_HALF_HEIGHT = 6;
    private final Array<Snowball> snowballsRef;
    private final int snowballsRefIndex;
    private final Vector2 position;
    private boolean collected;
    private boolean toCollect;

    private Snowball(final Vector2 position, final Array<Snowball> snowballsRef, final int snowballsRefIndex) {
        super(COLLISION_CATEGORY, MASK_BITS);

        this.position = position;
        this.snowballsRef = snowballsRef;
        this.snowballsRefIndex = snowballsRefIndex;
        toCollect = false;
        collected = false;
    }

    /**
     * Factory method to create a new Snowball.
     *
     * @param atlas The TextureAtlas containing the Snowball's textures.
     * @param position The position of the Snowball.
     * @param snowballsRef A reference to the Array of Snowballs.
     * @param snowballsRefIndex The index of this Snowball in the Array.
     * @return A new Snowball.
     */
    public static Snowball snowballFactory(final TextureAtlas atlas, final Vector2 position,
                                           final Array<Snowball> snowballsRef, final int snowballsRefIndex) {
        final Snowball snowball = new Snowball(position, snowballsRef, snowballsRefIndex);
        snowball.defineDefaultSprite(atlas);
        snowball.defineBodyDefPosition();
        snowball.defineShape();
        return snowball;
    }

    @Override
    protected void defineDefaultSprite(final TextureAtlas atlas) {
        TextureRegion snowballSprite = new TextureRegion(atlas.findRegion("snowballs"),
                0, 0, SPRITE_WIDTH, SPRITE_HEIGHT);
        final float spriteBoundHeight = (SPRITE_HEIGHT - 2) / BobIsMelting.PPM;
        setBounds(0, 0, SPRITE_WIDTH / BobIsMelting.PPM, spriteBoundHeight);
        setPosition(position.x / BobIsMelting.PPM, position.y / BobIsMelting.PPM);
        setRegion(snowballSprite);
    }

    @Override
    protected void defineShape() {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(SNOWBALL_POLYGON_HALF_WIDTH / BobIsMelting.PPM,
                SNOWBALL_POLYGON_HALF_HEIGHT / BobIsMelting.PPM);
        getFixtureDef().shape = shape;
    }

    @Override
    protected void defineBodyDefPosition() {
        final float spawnPositionX = this.position.x + 8;
        final float spawnPositionY = this.position.y + 8;
        getBodyDef().position.set(spawnPositionX / BobIsMelting.PPM, spawnPositionY / BobIsMelting.PPM);
    }

    /**
     * Checks if the Snowball has been collected.
     *
     * @return True if the Snowball has been collected, false otherwise.
     */
    public boolean isCollected() {
        return collected;
    }


    /**
     * Marks the Snowball to be collected.
     */
    public void collect() {
        toCollect = true;
    }

    /**
     * Updates the state of the Snowball.
     * This method is called periodically to update the state of the Snowball in the game.
     */
    public void update() {
        if (toCollect && !collected) {
            snowballsRef.set(snowballsRefIndex, null);
            collected = true;
        }
    }

    @Override
    public void draw(final Batch batch) {
        if (!collected) {
            super.draw(batch);
        }
    }

}


