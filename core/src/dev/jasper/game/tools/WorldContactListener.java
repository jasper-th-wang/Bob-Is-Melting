package dev.jasper.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import dev.jasper.game.BobIsMelting;
import dev.jasper.game.sprites.Enemy;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            case BobIsMelting.ENEMY_BIT | BobIsMelting.GROUND_BIT:
                Object enemy = fixA.getFilterData().categoryBits == BobIsMelting.ENEMY_BIT ?
                        fixA.getUserData() : fixB.getUserData();
                ((Enemy) enemy).reverseVelocity(true, false);
                break;
            case BobIsMelting.KID_BIT | BobIsMelting.ENEMY_BIT:
                Gdx.app.log("Kid", "Hit");
                break;
        }


    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
