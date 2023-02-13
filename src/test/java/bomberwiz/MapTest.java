package bomberwiz;

import static org.junit.jupiter.api.Assertions.*;
import processing.core.PApplet;
import java.util.*;

import org.junit.jupiter.api.*;

public class MapTest {
    App app = new App();;
    Map testMap;
    List<Enemy> testEnemies;
    BombWiz testPlayer;

    @BeforeEach
    public void setUp(){
        app.noLoop();
        app.setConfig("src/test/resources/config.json");
        PApplet.runSketch(new String[] {"App"}, app);

        testMap = new Map(app);
        testMap.readConfig("src/test/resources/config.json");
        testMap.setMap(testMap.getLevels().get(0), testMap.getTimes().get(0));
        testMap.generateEnemies();
        testEnemies = testMap.getEnemies();
        testPlayer = new BombWiz(testMap.getPlayerLocX(), testMap.getPlayerLocY(), app, testMap.getPlayerLives());
    }

    // Tests if the level changes after bombguy is on the goal block.
    @Test
    public void changeLevelTest(){
        testPlayer.changeLoc(testMap.getGoalX(), testMap.getGoalY()-16);

        assertTrue(testPlayer.checkWin(testMap.getGoalX(), testMap.getGoalY()));

        testPlayer.tick(testMap, null);
        testMap.tick(testPlayer);

        assertEquals(1, testMap.getCurrentLevel());
    }

    // Check if map reset and clear works.
    @Test
    public void resetClearTest(){
        testMap.placeBomb(testPlayer);

        for(int i = 0; i < 120; i++){
            testMap.getBombs().get(0).tick(testPlayer, testEnemies);
        }

        testPlayer.tick(testMap, null);
        testMap.tick(testPlayer);

        assertTrue(testMap.getBombs().isEmpty());

        testPlayer.die();

        testPlayer.tick(testMap, null);
        testMap.tick(testPlayer);

        assertTrue(testMap.getEnemies().isEmpty());
        assertTrue(testMap.getBombs().isEmpty());
        assertFalse(testMap.getPlayerContinue());
    }

    // Check if winning and losing status is detected and correctly executed.
    @Test
    public void winLoseTest(){
        testPlayer.changeLoc(testMap.getGoalX(), testMap.getGoalY()-16);

        testPlayer.tick(testMap, null);
        testMap.tick(testPlayer);

        testPlayer.changeLoc(testMap.getGoalX(), testMap.getGoalY()-16);

        testPlayer.tick(testMap, null);
        testMap.tick(testPlayer);

        assertEquals(1, testMap.getCurrentLevel());

        assertTrue(testMap.getEnemies().isEmpty());
        assertTrue(testMap.getBombs().isEmpty());
        assertFalse(testMap.getPlayerContinue());
    }

    // Check if the timer is working (i.e. bombguy lose after the time limit).
    @Test
    public void timerTest(){
        int testTime = 180;
        for(int i = 0; i < testMap.getTimes().get(0)*60; i++){
            testMap.tick(testPlayer);

            if(i%60 == 0 & i != 0){
                testTime -= 1;
                assertEquals(testTime, testMap.getTime());
                
            }
        }

        testMap.tick(testPlayer);

        assertFalse(testPlayer.getAlive());
    }

    // Check if get map object method gets null with (1000, 1000) (i.e. not within the map).
    // Check that bombs cannot be placed on the same spot.
    @Test
    public void otherMapTest(){
        assertNull(testMap.getMapObjectByLoc(1000, 1000));

        testMap.placeBomb(testPlayer);
        testMap.placeBomb(testPlayer);

        assertEquals(1, testMap.getBombs().size());
    }
}