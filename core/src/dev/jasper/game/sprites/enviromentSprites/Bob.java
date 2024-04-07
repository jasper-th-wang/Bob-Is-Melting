package dev.jasper.game.sprites.enviromentSprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.CircleShape;
import dev.jasper.game.BobIsMelting;
import dev.jasper.game.EntityCollisionCategory;

/**
 * Represents the Bob character in the game.
 * The Bob class extends the InteractiveEnviromentSprite class
 * and defines the specific characteristics of the Bob character.
 * It also implements the Singleton pattern to ensure that there is only one instance of Bob in the game.
 *
 * @author Jasper Wang
 * @version 2024
 */
public final class Bob extends InteractiveEnviromentSprite {

    private static final short COLLISION_CATEGORY = EntityCollisionCategory.BOB_BIT;
    private static final short MASK_BITS = EntityCollisionCategory.KID_BIT
            | EntityCollisionCategory.KID_CARRY_SNOWBALL_BIT;
    private static final int SPRITE_WIDTH = 16;
    private static final float POSITION_X = SPRITE_WIDTH * 7;
    private static final float POSITION_Y = SPRITE_WIDTH * 4;
    private static final int SPRITE_HEIGHT = 24;
    private static final int SPAWN_POSITION_X = 1;
    private static final int SPAWN_POSITION_Y = -1;
    private static final int BOB_SPHERE_RADIUS = 10;
    private static Bob bobSingleton;
    private TextureRegion fullHealth;
    private TextureRegion subHealth;
    private TextureRegion badHealth;
    private TextureRegion dying;

    private Bob() {
        super(COLLISION_CATEGORY, MASK_BITS);
        // TODO: add other textures
    }

    /**
     * Create a new Bob.
     * This method implements the Singleton pattern to ensure that there is only one instance of Bob in the game.
     *
     * @param atlas The TextureAtlas containing the Bob's textures.
     * @return The single instance of Bob.
     */
    public static Bob getInstance(final TextureAtlas atlas) {
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
    protected void defineDefaultSprite(final TextureAtlas atlas) {
        fullHealth = new TextureRegion(atlas.findRegion("Snowman-tileset"),
                SPAWN_POSITION_X, SPAWN_POSITION_Y, SPRITE_WIDTH, SPRITE_HEIGHT);
        setBounds(0, 0, SPRITE_WIDTH / BobIsMelting.PPM, SPRITE_HEIGHT / BobIsMelting.PPM);
        setPosition(POSITION_X / BobIsMelting.PPM - getWidth() / 2, POSITION_Y / BobIsMelting.PPM);
        setRegion(fullHealth);
    }

    @Override
    protected void defineShape() {
        final CircleShape shape = new CircleShape();
        shape.setRadius(BOB_SPHERE_RADIUS / BobIsMelting.PPM);
        getFixtureDef().shape = shape;
    }

    @Override
    protected void defineBodyDefPosition() {
        final float yOffset = .1f;
        getBodyDef().position.set(POSITION_X / BobIsMelting.PPM, POSITION_Y / BobIsMelting.PPM + yOffset);
    }

    /**
     * Updates the position of the Bob character in the game world.
     */
    public void update() {
        setRegion(getFrame());
    }

    private TextureRegion getFrame() {
        // stub implementation for now
        // calculation should be based on health
        return fullHealth;
    }
}
