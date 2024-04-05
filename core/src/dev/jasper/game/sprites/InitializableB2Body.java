package dev.jasper.game.sprites;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public interface InitializableB2Body {
    BodyDef getBodyDef();

    FixtureDef getFixtureDef();
}
