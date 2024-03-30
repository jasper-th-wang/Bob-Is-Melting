package dev.jasper.game.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import dev.jasper.game.BobIsMelting;
import dev.jasper.game.EntityCollisionCategoy;
import dev.jasper.game.screens.PlayScreen;

public class Bear extends Enemy {
    private float stateTime;
    private Animation<TextureRegion> walkAnimation;
    private Array<TextureRegion> frames;

    public Bear(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        for(int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("bear_polar"), i * 32, 32, 32, 32));
            walkAnimation = new Animation<TextureRegion>(0.1f, frames);
            setBounds(getX(), getY(), 24 / BobIsMelting.PPM, 24 / BobIsMelting.PPM);
        }
    }

    public void update(float dt) {
        stateTime += dt;
        getB2body().setLinearVelocity(getVelocity());
        setPosition(getB2body().getPosition().x - getWidth() / 2, getB2body().getPosition().y - getHeight() / 3);
        setRegion(walkAnimation.getKeyFrame(stateTime, true));
    }

    @Override
    protected void defineEnemy() {

        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(7 / BobIsMelting.PPM);
        fdef.filter.categoryBits = EntityCollisionCategoy.ENEMY_BIT;
        fdef.filter.maskBits = EntityCollisionCategoy.GROUND_BIT | EntityCollisionCategoy.ENEMY_BIT | EntityCollisionCategoy.OBJECT_BIT | EntityCollisionCategoy.KID_BIT;

        fdef.shape = shape;
        getB2body().createFixture(fdef).setUserData(this);
    }
}
