package dev.jasper.game.sprites;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import dev.jasper.game.BobIsMelting;

/**
 * Represents a tile body in the game.
 * The TileB2Body class implements the InitializableB2Body interface
 * and defines the common characteristics and behaviors of a tile body.
 *
 * @author Jasper Wang
 * @version 2024
 */
public abstract class TileB2Body implements InitializableB2Body {
    private final BodyDef bodyDef;
    private final FixtureDef fixtureDef;
    private Body b2body;
    private Fixture fixture;

    /**
     * Constructs a TileB2Body with the specified bounds and collision category.
     *
     * @param bounds The bounds of the tile body.
     * @param collisionCategory The collision category of the tile body.
     */
    public TileB2Body(final Rectangle bounds, final short collisionCategory) {
        final float positionX = bounds.getX() + bounds.getWidth() / 2;
        final float positionY = bounds.getY() + bounds.getHeight() / 2;

        final PolygonShape shape = new PolygonShape();
        shape.setAsBox(bounds.getWidth() / 2 / BobIsMelting.PPM, bounds.getHeight() / 2 / BobIsMelting.PPM);

        bodyDef = new BodyDef();
        getBodyDef().position.set(positionX / BobIsMelting.PPM,
                positionY / BobIsMelting.PPM);
        getBodyDef().type = BodyDef.BodyType.StaticBody;

        fixtureDef = new FixtureDef();
        getFixtureDef().filter.categoryBits = collisionCategory;
        getFixtureDef().shape = shape;
    }

    @Override
    public final BodyDef getBodyDef() {
        return bodyDef;
    }

    @Override
    public final FixtureDef getFixtureDef() {
        return fixtureDef;
    }

    @Override
    public final Body getB2body() {
        return b2body;
    }

    @Override
    public final Fixture getFixture() {
        return fixture;
    }

    @Override
    public final void setFixture(final Fixture fixture) {
        this.fixture = fixture;
    }

    @Override
    public final void setB2body(final Body b2body) {
        this.b2body = b2body;
    }

}
