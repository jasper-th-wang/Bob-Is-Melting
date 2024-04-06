package dev.jasper.game.sprites;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public interface InitializableB2Body {
    BodyDef getBodyDef();

    FixtureDef getFixtureDef();

    Body getB2body();
    Fixture getFixture();

    void setB2body(Body b2body);

    void setFixture(Fixture fixture);
}
