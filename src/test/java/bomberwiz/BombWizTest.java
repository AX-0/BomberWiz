package bomberwiz;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.*;

public class BombWizTest {
    App app = new App();
    Map testMap;
    List<Enemy> testEnemies;
    BombWiz testPlayer;

    private static BombWiz test2;
    private static App app2 = new App();

    @BeforeAll
    public static void setup() {
        test2 = new BombWiz(10, 10, app2, 10);
    }

    @Test
    public void movementTest2(){
        assertEquals(10, test2.getX());
    }

    // Test bombguy movement with keys.
//    @Test
//    public void movementTest(){
//        testPlayer.move(39, testMap); //Right
//        //32 80
//        assertEquals(64, testPlayer.getX());
//        assertEquals(80, testPlayer.getY());
//        assertEquals("right", testPlayer.getDirectionString());
//
//        testPlayer.move(39, testMap); //Right
//
//        testPlayer.move(40, testMap); //Down
//        assertEquals(96, testPlayer.getX());
//        assertEquals(112, testPlayer.getY());
//        assertEquals("down", testPlayer.getDirectionString());
//
//        testPlayer.move(40, testMap); //Down
//
//        testPlayer.move(37, testMap); //Left
//        assertEquals(64, testPlayer.getX());
//        assertEquals(144, testPlayer.getY());
//        assertEquals("left", testPlayer.getDirectionString());
//
//        testPlayer.move(37, testMap); //Left
//        assertEquals("left", testPlayer.getDirectionString());
//
//        testPlayer.move(38, testMap); //Up
//        assertEquals(32, testPlayer.getX());
//        assertEquals(112, testPlayer.getY());
//        assertEquals("up", testPlayer.getDirectionString());
//
//        for(int i = 0; i < 120; i++){
//            testPlayer.tick(testMap, null);
//        }
//    }
//
//    @BeforeEach
//    public void setUp(){
//        app.noLoop();
//        app.setConfig("src/test/resources/config.json");
//        PApplet.runSketch(new String[] {"App"}, app);
//
//        testMap = new Map(app);
//        testMap.readConfig("src/test/resources/config.json");
//        testMap.setMap(testMap.getLevels().get(0), testMap.getTimes().get(0));
//        testMap.generateEnemies();
//        testEnemies = testMap.getEnemies();
//        testPlayer = new BombGuy(testMap.getPlayerLocX(), testMap.getPlayerLocY(), app, testMap.getPlayerLives());
//    }
}
