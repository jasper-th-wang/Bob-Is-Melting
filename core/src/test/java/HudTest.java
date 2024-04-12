import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dev.jasper.game.scenes.Hud;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
//
//public class HudTest {
//
//
//    // Hud instance is constructed successfully
//    @Test
//    public void test_hud_instance_construction() {
//        SpriteBatch batch = new SpriteBatch();
//        Hud hud = new Hud(batch);
//        assertNotNull(hud);
//    }
//
//    // Hud health is initialized to 100
//    @Test
//    public void test_hud_health_initialization() {
//        SpriteBatch batch = new SpriteBatch();
//        Hud hud = new Hud(batch);
//        assertEquals(100, hud.getHealth());
//    }
//
//    // Hud health is updated correctly when addSnowball() is called
//    @Test
//    public void test_hud_health_update_with_addSnowball() {
//        SpriteBatch batch = new SpriteBatch();
//        Hud hud = new Hud(batch);
//        int initialHealth = hud.getHealth();
//        Hud.addSnowball();
//        assertEquals(initialHealth + 10, hud.getHealth());
//    }
//
//    // Hud health is not set to negative values
//    @Test
//    public void test_hud_health_not_negative() {
//        SpriteBatch batch = new SpriteBatch();
//        Hud hud = new Hud(batch);
//        Hud.setHealth(-10);
//        assertEquals(0, hud.getHealth());
//    }
//
//    // Hud health is not set to values greater than 100
//    @Test
//    public void test_hud_health_not_greater_than_100() {
//        SpriteBatch batch = new SpriteBatch();
//        Hud hud = new Hud(batch);
//        Hud.setHealth(200);
//        assertEquals(100, hud.getHealth());
//    }
//}