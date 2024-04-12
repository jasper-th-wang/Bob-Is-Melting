package dev.jasper.game.sprites.dynamicSprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import dev.jasper.game.tools.B2BodyObjectFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BearTest {
    // Bear can be created with a TextureAtlas, x and y coordinates
    private Bear bear;
    private TextureAtlas atlas;
    @BeforeEach
    void setUp() {
        this.atlas = new TextureAtlas("Characters.atlas");
        this.bear = Bear.enemyFactory(atlas, .32f, .32f);

    }

    // Bear's velocity can be reversed
    @Test
    public void test_reverseVelocity() {
        Vector2 initialVelocity = bear.getCurrentVelocity();
        bear.reverseVelocity(true, true);
        Vector2 reversedVelocity = bear.getCurrentVelocity();
        assertNotEquals(initialVelocity, reversedVelocity);
    }

    // Bear's current velocity can be retrieved
    @Test
    public void test_retrieve_current_velocity() {
        Vector2 currentVelocity = bear.getCurrentVelocity();
        assertNotNull(currentVelocity);
    }

    // Bear's state can be retrieved
    @Test
    public void test_bear_state_retrieval() {
        bear.getB2body().setLinearVelocity(0, 0);
        assertEquals(DynamicB2BodySprite.State.STANDING, bear.getState());

        bear.getB2body().setLinearVelocity(0, 5);
        assertEquals(DynamicB2BodySprite.State.JUMPING, bear.getState());

        bear.getB2body().setLinearVelocity(5, 0);
        assertEquals(DynamicB2BodySprite.State.RUNNING, bear.getState());

        bear.getB2body().setLinearVelocity(0, -5);
        assertEquals(DynamicB2BodySprite.State.FALLING, bear.getState());
    }

}
