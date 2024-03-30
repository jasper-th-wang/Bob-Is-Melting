package dev.jasper.game.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import dev.jasper.game.screens.PlayScreen;

public abstract class Enemy extends Sprite {
    protected World world;
    protected PlayScreen screen;
    protected Body b2body;
    protected Vector2 velocity;

    public Enemy(PlayScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
        velocity = new Vector2(1, 0);
    }
    protected abstract void defineEnemy();
    public void reverseVelocity(boolean x, boolean y) {
        if (x) {
            getVelocity().x = -getVelocity().x;
        }
        if (y) {
            getVelocity().y = -getVelocity().y;
        }
    }

    public Body getB2body() {
        return b2body;
    }

    public Vector2 getVelocity() {
        return velocity;
    }
}
