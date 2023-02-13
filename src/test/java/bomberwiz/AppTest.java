package bomberwiz;

import org.junit.jupiter.api.Test;

import processing.core.PApplet;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

public class AppTest {
    App app = new App();
    
    @BeforeEach
    public void setUp(){
        app.noLoop();
        app.setConfig("src/test/resources/config.json");
        PApplet.runSketch(new String[] {"App"}, app);
        app.delay(1000);
        app.setup();
        app.delay(1000);
    }

    // Check if the map (player, enemies, goal) is correctly loaded.
    // Check if pressing a key while another key is not realeased have an action on player.
    @Test 
    public void basicTest() {
        assertEquals(480, App.HEIGHT);
        assertEquals(480, App.WIDTH);

        app.draw();

        app.keyCode = 32;
        app.keyPressed();

        app.draw();

        assertEquals(32, app.getMap().getPlayerLocX());
        assertEquals(80, app.getMap().getPlayerLocY());
        assertEquals(32, app.getPlayer().getX());
        assertEquals(80, app.getPlayer().getY());

        assertEquals(160, app.getMap().getEnemyLocX(0,true));
        assertEquals(336, app.getMap().getEnemyLocY(0, true));
        assertEquals(256, app.getMap().getEnemyLocX(0,false));
        assertEquals(208, app.getMap().getEnemyLocY(0, false));

        assertEquals(416, app.getMap().getGoalX());
        assertEquals(416, app.getMap().getGoalY());

        assertEquals(1, app.getMap().getBombs().size());

        app.keyCode = 39;
        app.keyPressed();

        app.draw();

        assertEquals(32, app.getPlayer().getX());
        assertEquals(80, app.getPlayer().getY());

        app.draw();

        app.keyReleased();
        app.delay(100);
        app.keyCode = 39;
        app.keyPressed();

        app.draw();

        assertEquals(64, app.getPlayer().getX());
        assertEquals(80, app.getPlayer().getY());
    }
}
