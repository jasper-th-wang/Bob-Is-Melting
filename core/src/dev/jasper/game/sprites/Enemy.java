package dev.jasper.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import dev.jasper.game.screens.PlayScreen;

/**
 * The Enemy class serves as an abstract base class that provides a skeletal implementation for the enemies in the game.
 * It extends the Sprite class from the libGDX library, inheriting its properties and methods for graphical representation.
 *
 * @author Jasper Wang
 * @version 2024
 */
public abstract class Enemy extends Sprite {
    protected static final Vector2 DEFAULT_VELOCITY = new Vector2(0.05f, 0);
    protected final float decideSpecialMovementDuration = 3f;
    protected World world;
    protected PlayScreen screen;
    protected Body b2body;
    protected float decideSpeicalMovementTimer;
    protected Bear.State currentState;
    protected Bear.State previousState;
    protected float stateTime;
    protected boolean isRunningRight;

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }
    protected Vector2 velocity;

    /**
     * Constructs an Enemy instance.
     * @param screen - the game screen
     * @param x - the x-coordinate of the enemy's position
     * @param y - the y-coordinate of the enemy's position
     */
    public Enemy(PlayScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
        velocity = new Vector2(0.05f, 0);
        decideSpeicalMovementTimer = 0;
    }

    private void jump() {
        b2body.applyLinearImpulse(new Vector2(0, 3f), b2body.getWorldCenter(), true);
        Gdx.app.log("Enemy", "jump!");
    }

    private void idle() {
        setVelocity(new Vector2(0, 0));
    }

    protected void run() {
        if (Math.abs(getB2body().getLinearVelocity().x) <= 2) {
            getB2body().applyLinearImpulse(getVelocity(), getB2body().getWorldCenter(), true);
        }

    }

    protected void applySpecialMovement() {
        if (decideSpeicalMovementTimer >= decideSpecialMovementDuration) {
            if (MathUtils.randomBoolean(0.5F)) {
                jump();
                setVelocity(DEFAULT_VELOCITY);
            } else {
                idle();
            }
            decideSpeicalMovementTimer = 0;
        }
    }

    /**
     * Defines the physical properties of the Enemy character in the game world.
     */
    protected abstract void defineEnemy();
    /**
     * Reverses the enemy's velocity in the x and/or y direction.
     * @param x - if true, reverse the x-component of the velocity
     * @param y - if true, reverse the y-component of the velocity
     */
    public void reverseVelocity(boolean x, boolean y) {
        if (x) {
            getVelocity().x = -getVelocity().x;
        }
        if (y) {
            getVelocity().y = -getVelocity().y;
        }
    }

    /**
     * Returns the enemy's Body instance.
     * @return b2body - the Body instance
     */
    public Body getB2body() {
        return b2body;
    }

    /**
     * Returns the enemy's velocity.
     * @return velocity - the velocity
     */
    public Vector2 getVelocity() {
        return velocity;
    }
}
