package dev.jasper.game.sprites.enviromentSprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import dev.jasper.game.sprites.InitializableB2Body;

public abstract class InteractiveEnviromentSprite extends Sprite implements InitializableB2Body {
    private final BodyDef bodyDef;
    private final FixtureDef fixtureDef;
    private Body b2body;
    private Fixture fixture;

    public InteractiveEnviromentSprite(final short collisionCategory, final short collisionMaskBits) {
        super();

        bodyDef = new BodyDef();
        getBodyDef().type = BodyDef.BodyType.StaticBody;

        fixtureDef = new FixtureDef();
        getFixtureDef().filter.categoryBits = collisionCategory;
        getFixtureDef().filter.maskBits = collisionMaskBits;

    }

    @Override
    public BodyDef getBodyDef() {
        return bodyDef;
    }

    @Override
    public FixtureDef getFixtureDef() {
        return fixtureDef;
    }

    @Override
    public Body getB2body() {
        return this.b2body;
    }

    @Override
    public Fixture getFixture() {
        return fixture;
    }

    @Override
    public void setFixture(Fixture fixture) {
        this.fixture = fixture;
    }

    @Override
    public void setB2body(Body b2body) {
        this.b2body = b2body;
    }

    protected abstract void defineDefaultSprite(TextureAtlas atlas);

    protected abstract void defineShape();

    protected abstract void defineBodyDefPosition();
}
