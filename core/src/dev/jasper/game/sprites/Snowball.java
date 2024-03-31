package dev.jasper.game.sprites;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import dev.jasper.game.BobIsMelting;
import dev.jasper.game.EntityCollisionCategory;
import dev.jasper.game.screens.PlayScreen;

public class Snowball extends Sprite {

    private World world;
    private TiledMap map;
    private Vector2 position;
    private MapObject object;
    private Body b2body;
    private TextureRegion snowballSprite;
    private boolean collected;
    private boolean toCollect;

    /**
     * Constructs a Snowball instance.
     * @param screen - the game screen
     * @param position - the position of the snowball
     */
    public Snowball(PlayScreen screen, Vector2 position){
        super(screen.getAtlas().findRegion("snowballs"));
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.position = position;
        toCollect = false;
        collected = false;

        defineSnowball();

        snowballSprite = new TextureRegion(screen.getAtlas().findRegion("snowballs"), 0, 0, 16, 16);
        setBounds(0, 0, 16 / BobIsMelting.PPM, 16 / BobIsMelting.PPM);
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(snowballSprite);
    }

    private void defineSnowball() {
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();


        fdef.filter.categoryBits = EntityCollisionCategory.SNOWBALL_BIT;
        fdef.filter.maskBits = EntityCollisionCategory.GROUND_BIT | EntityCollisionCategory.KID_BIT | EntityCollisionCategory.KID_INVINCIBLE_BIT;

        bdef.position.set((this.position.x + 8) / BobIsMelting.PPM, (this.position.y + 8) / BobIsMelting.PPM);
        bdef.type = BodyDef.BodyType.StaticBody;

        this.b2body = world.createBody(bdef);

        shape.setAsBox(6 / BobIsMelting.PPM, 6 / BobIsMelting.PPM);
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }
    public void collect(Kid kid) {
        toCollect = true;
//        kid.collectSnow();
    }

    public void update(float dt) {
        if (toCollect && !collected) {
            world.destroyBody(b2body);
            collected = true;
        }
    }
    public void draw(Batch batch){
        if (!collected) {
            super.draw(batch);
        }
    }
}


