package dev.jasper.game.sprites;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import dev.jasper.game.BobIsMelting;
import dev.jasper.game.EntityCollisionCategory;
import dev.jasper.game.screens.PlayScreen;

/**
 * The Ground class represents the ground in the game.
 * It defines the physical properties of the ground in the game world.
 *
 * @author Jasper Wang
 * @version 2024
 */
public class Ground {
    private World world;
    private TiledMap map;
    private Rectangle bounds;
    private MapObject object;
    private Body body;

    /**
     * Constructs a Ground instance.
     * @param screen - the game screen
     * @param bounds - the bounds of the ground
     */
    public Ground(PlayScreen screen, Rectangle bounds){
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.bounds = bounds;

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        fdef.filter.categoryBits = EntityCollisionCategory.GROUND_BIT;
//        fdef.filter.maskBits = EntityCollisionCategoy.ENEMY_BIT | EntityCollisionCategoy.OBJECT_BIT | EntityCollisionCategoy.KID_BIT;

        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / BobIsMelting.PPM, (bounds.getY() + bounds.getHeight() / 2) / BobIsMelting.PPM);
        bdef.type = BodyDef.BodyType.StaticBody;

        this.body = world.createBody(bdef);

        shape.setAsBox(bounds.getWidth() / 2 / BobIsMelting.PPM, bounds.getHeight() / 2 / BobIsMelting.PPM);
        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
    }
}
