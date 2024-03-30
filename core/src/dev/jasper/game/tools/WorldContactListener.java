package dev.jasper.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import dev.jasper.game.EntityCollisionCategoy;
import dev.jasper.game.sprites.Enemy;

/**
 * This class implements the ContactListener interface from the Box2D physics library.
 * It is used to handle the contact between different game objects in the world.
 *
 * @author Jasper Wang
 * @version 2024
 */
public class WorldContactListener implements ContactListener {
    /**
     * This method is called when two fixtures start to collide.
     * @param contact The contact information about the collision.
     */
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            case EntityCollisionCategoy.ENEMY_BIT | EntityCollisionCategoy.GROUND_BIT:
                Object enemy = fixA.getFilterData().categoryBits == EntityCollisionCategoy.ENEMY_BIT ?
                        fixA.getUserData() : fixB.getUserData();
                ((Enemy) enemy).reverseVelocity(true, false);
                break;
            case EntityCollisionCategoy.KID_BIT | EntityCollisionCategoy.ENEMY_BIT:
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
