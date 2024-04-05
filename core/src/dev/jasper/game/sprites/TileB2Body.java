package dev.jasper.game.sprites;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import dev.jasper.game.BobIsMelting;

public abstract class TileB2Body implements InitializableB2Body {
    private final BodyDef bodyDef;
    private final FixtureDef fixtureDef;

    public TileB2Body(final Rectangle bounds, final short collisionCategory) {
        final float positionX = bounds.getX() + bounds.getWidth() / 2;
        final float positionY = bounds.getY() + bounds.getHeight() / 2;
        final PolygonShape shape = new PolygonShape();

        bodyDef = new BodyDef();
        getBodyDef().position.set(positionX / BobIsMelting.PPM,
                positionY / BobIsMelting.PPM);
        getBodyDef().type = BodyDef.BodyType.StaticBody;

        fixtureDef = new FixtureDef();
        getFixtureDef().filter.categoryBits = collisionCategory;
        getFixtureDef().shape = shape;
    }

    @Override
    public BodyDef getBodyDef() {
        return bodyDef;
    }

    @Override
    public FixtureDef getFixtureDef() {
        return fixtureDef;
    }
}
