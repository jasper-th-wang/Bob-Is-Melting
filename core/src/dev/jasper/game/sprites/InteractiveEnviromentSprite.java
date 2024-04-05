package dev.jasper.game.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import dev.jasper.game.BobIsMelting;

public abstract class InteractiveEnviromentSprite extends Sprite implements InitializableB2Body {
    private final BodyDef bodyDef;
    private final FixtureDef fixtureDef;

    public InteractiveEnviromentSprite(final TextureRegion defaultTextureRegion, final float positionX, final float positionY, final short collisionCategory, final short collisionMaskBits) {
        super(defaultTextureRegion);

        bodyDef = new BodyDef();
        getBodyDef().position.set(positionX / BobIsMelting.PPM,
                positionY / BobIsMelting.PPM);
        getBodyDef().type = BodyDef.BodyType.StaticBody;

        fixtureDef = new FixtureDef();
        getFixtureDef().filter.categoryBits = collisionCategory;
        getFixtureDef().filter.maskBits = collisionMaskBits;
    }

    protected abstract void defineShape();
    @Override
    public BodyDef getBodyDef() {
        return bodyDef;
    }

    @Override
    public FixtureDef getFixtureDef() {
        return fixtureDef;
    }
}
