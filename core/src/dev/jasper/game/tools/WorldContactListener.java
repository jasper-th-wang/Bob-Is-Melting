/**
 * This package contains tools and utilities for the game.
 *
 * @author Jasper Wang
 * @version 2024
 */
package dev.jasper.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import dev.jasper.game.EntityCollisionCategory;
import dev.jasper.game.sprites.dynamicSprites.AbstractEnemy;
import dev.jasper.game.sprites.dynamicSprites.AbstractPlayer;
import dev.jasper.game.sprites.dynamicSprites.SnowballCarrier;
import dev.jasper.game.sprites.enviromentSprites.Snowball;

import static com.badlogic.gdx.Gdx.app;

/**
 * This class implements the ContactListener interface from the Box2D physics library.
 * It is used to handle the contact between different game objects in the world.
 *
 * @author Jasper Wang
 * @version 2024
 */
public final class WorldContactListener implements ContactListener {
    private final GameStateManager gameStateManager;

    /**
     * Constructs a WorldContactListener instance with the specified GameStateManager.
     *
     * @param gameStateManager The GameStateManager instance used to manage the game state.
     */
    public WorldContactListener(final GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
    }

    /**
     * This method is called when two fixtures start to collide.
     *
     * @param contact The contact information about the collision.
     */
    @Override
    public void beginContact(final Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            case EntityCollisionCategory.ENEMY_BIT | EntityCollisionCategory.GROUND_BIT:
            case EntityCollisionCategory.ENEMY_BIT | EntityCollisionCategory.ENEMY_BOUNDARY_BIT:
                handleEnemyGroundCollision(fixA, fixB);
                break;
            case EntityCollisionCategory.KID_BIT | EntityCollisionCategory.ENEMY_BIT:
            case EntityCollisionCategory.KID_CARRY_SNOWBALL_BIT | EntityCollisionCategory.ENEMY_BIT:
                handleKidEnemyCollision(fixA, fixB);
                break;
            case EntityCollisionCategory.KID_BIT | EntityCollisionCategory.SNOWBALL_BIT:
                handleKidSnowballCollision(fixA, fixB);
                break;
            case EntityCollisionCategory.KID_CARRY_SNOWBALL_BIT | EntityCollisionCategory.BOB_BIT:
                handleKidBobCollision(fixA, fixB);
                break;
            default:
                break;
        }

    }

    private void handleEnemyGroundCollision(final Fixture fixA, final Fixture fixB) {
        AbstractEnemy enemy;
        if (fixA.getFilterData().categoryBits == EntityCollisionCategory.ENEMY_BIT) {
            enemy = (AbstractEnemy) fixA.getUserData();
        } else {
            enemy = (AbstractEnemy) fixB.getUserData();
        }
        final boolean toReverseVelocity = MathUtils.randomBoolean(0.8F);
        enemy.reverseVelocity(toReverseVelocity, false);
    }

    private void handleKidEnemyCollision(final Fixture fixA, final Fixture fixB) {
        AbstractPlayer abstractPlayer;
        if (fixA.getFilterData().categoryBits != EntityCollisionCategory.ENEMY_BIT) {
            abstractPlayer = (AbstractPlayer) fixA.getUserData();
        } else {
            abstractPlayer = (AbstractPlayer) fixB.getUserData();
        }
        abstractPlayer.onEnemyHit();
        abstractPlayer.setIsCarryingSnowball(false);
        Gdx.app.log("Kid", "Hit");
    }

    private void handleKidSnowballCollision(final Fixture fixA, final Fixture fixB) {
        Snowball snowball;
        if (fixA.getFilterData().categoryBits == EntityCollisionCategory.SNOWBALL_BIT) {
            snowball = (Snowball) fixA.getUserData();
        } else {
            snowball = (Snowball) fixB.getUserData();
        }
        SnowballCarrier theKid;
        if (fixA.getFilterData().categoryBits != EntityCollisionCategory.SNOWBALL_BIT) {
            theKid = (SnowballCarrier) fixA.getUserData();
        } else {
            theKid = (SnowballCarrier) fixB.getUserData();
        }

        snowball.collect();
        theKid.collectSnowball();
        app.log("Kid", "got snow!");
    }

    private void handleKidBobCollision(final Fixture fixA, final Fixture fixB) {
        SnowballCarrier snowballCarrier;

        if (fixA.getFilterData().categoryBits == EntityCollisionCategory.KID_CARRY_SNOWBALL_BIT) {
            snowballCarrier = (SnowballCarrier) fixA.getUserData();
        } else {
            snowballCarrier = (SnowballCarrier) fixB.getUserData();
        }

        snowballCarrier.dropoffSnowball();
        gameStateManager.addSnowball();
        app.log("Kid", "drop the snow!");
    }

    @Override
    public void endContact(final Contact contact) {

    }

    @Override
    public void preSolve(final Contact contact, final Manifold oldManifold) {

    }

    @Override
    public void postSolve(final Contact contact, final ContactImpulse impulse) {

    }
}
