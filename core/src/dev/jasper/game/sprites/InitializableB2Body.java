package dev.jasper.game.sprites;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * The InitializableB2Body interface defines the methods that
 * a class must implement to be considered as a Box2D body in the game.
 * This includes methods for getting and setting the BodyDef, FixtureDef, Body, and Fixture of the Box2D body.
 *
 * @author Jasper Wang
 * @version 2024
 */
public interface InitializableB2Body {

    /**
     * Returns the BodyDef of the Box2D body.
     *
     * @return The BodyDef of the Box2D body.
     */
    BodyDef getBodyDef();

    /**
     * Returns the FixtureDef of the Box2D body.
     *
     * @return The FixtureDef of the Box2D body.
     */
    FixtureDef getFixtureDef();

    /**
     * Returns the Body of the Box2D body.
     *
     * @return The Body of the Box2D body.
     */
    Body getB2body();

    /**
     * Returns the Fixture of the Box2D body.
     *
     * @return The Fixture of the Box2D body.
     */
    Fixture getFixture();

    /**
     * Sets the Body of the Box2D body.
     *
     * @param b2body The new Body of the Box2D body.
     */
    void setB2body(Body b2body);

    /**
     * Sets the Fixture of the Box2D body.
     *
     * @param fixture The new Fixture of the Box2D body.
     */
    void setFixture(Fixture fixture);
}
