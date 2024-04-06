package dev.jasper.game.sprites.enviromentSprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import dev.jasper.game.sprites.InitializableB2Body;


/**
 * Represents an interactive environment sprite in the game.
 * The InteractiveEnviromentSprite class extends the Sprite class and implements the InitializableB2Body interface.
 * It defines the common characteristics and behaviors of an interactive environment sprite.
 *
 * @author Jasper Wang
 * @version 2024
 */
public abstract class InteractiveEnviromentSprite extends Sprite implements InitializableB2Body {
    private final BodyDef bodyDef;
    private final FixtureDef fixtureDef;
    private Body b2body;
    private Fixture fixture;

    /**
     * Constructs an InteractiveEnviromentSprite with the specified collision category and mask bits.
     *
     * @param collisionCategory The collision category of the sprite.
     * @param collisionMaskBits The collision mask bits of the sprite.
     */
    public InteractiveEnviromentSprite(final short collisionCategory, final short collisionMaskBits) {
        super();

        bodyDef = new BodyDef();
        getBodyDef().type = BodyDef.BodyType.StaticBody;

        fixtureDef = new FixtureDef();
        getFixtureDef().filter.categoryBits = collisionCategory;
        getFixtureDef().filter.maskBits = collisionMaskBits;

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
        return this.b2body;
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

    protected abstract void defineDefaultSprite(TextureAtlas atlas);

    protected abstract void defineShape();

    protected abstract void defineBodyDefPosition();
}
