package bomberwiz;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import processing.core.PApplet;
import java.util.*;
import org.junit.jupiter.api.*;


public class BombTest {
    App app = new App();
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
    
    // Check if bomb can be placed and hurts player and enemies.
    @Test
    public void bombTestSimple() {
        testMap.placeBomb(testPlayer);

        Enemy testEnemy1 = new YellowEnemy(testPlayer.getX()+32, testPlayer.getY(), app, 1);

        List<Enemy> tEnemies = new ArrayList<Enemy>();
        tEnemies.add(testEnemy1);

        Bomb testBomb = testMap.getBombs().get(0);

        assertFalse(testMap.getBombs().isEmpty());

        for(int i = 0; i <= 120; i++){
            testBomb.tick(testPlayer, tEnemies);
        }

        testBomb.draw(app, testMap, testPlayer, tEnemies);

        for(int i = 0; i <= 30; i++){
            testBomb.tick(testPlayer, tEnemies);
        }

        testBomb.draw(app, testMap, testPlayer, tEnemies);

        testMap.tick(testPlayer);

        assertEquals(32, app.getMap().getPlayerLocX());
        assertEquals(80, app.getMap().getPlayerLocY());
        assertTrue(testMap.getBombs().isEmpty());
        assertEquals(2, testPlayer.getLives());
        assertFalse(tEnemies.get(0).getAlive());
    }

    // Check if bomb explodes.
    @Test
    public void bombTestAllExplosion() {
        testPlayer.changeLoc(96, 144);
        testMap.placeBomb(testPlayer);
        Bomb testBomb2 = testMap.getBombs().get(0);

        testPlayer.changeLoc(192, 144);

        for(int i = 0; i <= 120; i++){
            testBomb2.tick(testPlayer, testEnemies);
        }

        testBomb2.draw(app, testMap, testPlayer, testEnemies);

        for(int i = 0; i <= 30; i++){
            testBomb2.tick(testPlayer, testEnemies);
        }

        testMap.tick(testPlayer);

        assertEquals(3, testPlayer.getLives());

        assertTrue(testMap.getBombs().isEmpty());
    }

    // Check if bomb destroys nearby broken walls.
    @Test
    public void bombTestDestroy1() {
        testPlayer.changeLoc(352, 144);
        testMap.placeBomb(testPlayer);
        Bomb testBomb3 = testMap.getBombs().get(0);

        testPlayer.changeLoc(192, 144);

        for(int i = 0; i <= 130; i++){
            testBomb3.tick(testPlayer, testEnemies);
        }

        testBomb3.draw(app, testMap, testPlayer, testEnemies);

        int xAdjustment = 32;
        List<MapObject> bombSurround = new ArrayList<MapObject>();
        for(int i = 0; i < 2; i++){
            MapObject moH = testMap.getMapObjectByLoc(testBomb3.getX()+xAdjustment, testBomb3.getY());
            MapObject moH2 = testMap.getMapObjectByLoc(testBomb3.getX()-xAdjustment, testBomb3.getY());
            bombSurround.add(moH);
            bombSurround.add(moH2);
            xAdjustment += 32;
        }
        MapObject moV = testMap.getMapObjectByLoc(testBomb3.getX(), testBomb3.getY()+32);
        MapObject moV2 = testMap.getMapObjectByLoc(testBomb3.getX(), testBomb3.getY()-64);
        MapObject moV3 = testMap.getMapObjectByLoc(testBomb3.getX(), testBomb3.getY()-32);
        bombSurround.add(moV);
        bombSurround.add(moV2);
        bombSurround.add(moV3);

        for(int i = 0; i <= 20; i++){
            testBomb3.tick(testPlayer, testEnemies);
        }

        for(MapObject i:bombSurround){
            if(i.getDestructible()){
                assertTrue(i.getDestroyed());
            }
        }

        testMap.tick(testPlayer);

        testPlayer.changeLoc(352, 144);
        testMap.placeBomb(testPlayer);
        Bomb testBomb3_1 = testMap.getBombs().get(0);

        testPlayer.changeLoc(192, 144);

        for(int i = 0; i <= 130; i++){
            testBomb3_1.tick(testPlayer, testEnemies);
        }

        testBomb3_1.draw(app, testMap, testPlayer, testEnemies);
    }

    // Check if bomb destroys farther broken walls.
    @Test
    public void bombTestDestroy2() {
        testPlayer.changeLoc(288, 336);
        testMap.placeBomb(testPlayer);
        Bomb testBomb4 = testMap.getBombs().get(0);

        testPlayer.changeLoc(192, 144);

        for(int i = 0; i <= 130; i++){
            testBomb4.tick(testPlayer, testEnemies);
        }

        testBomb4.draw(app, testMap, testPlayer, testEnemies);

        int xAdjustment = -64;
        int yAdjustment = -64;
        List<MapObject> bombSurround = new ArrayList<MapObject>();
        for(int i = 0; i < 4; i++){
            MapObject moV = testMap.getMapObjectByLoc(testBomb4.getX(), testBomb4.getY()+yAdjustment);
            MapObject moH = testMap.getMapObjectByLoc(testBomb4.getX()+xAdjustment, testBomb4.getY());
            bombSurround.add(moH);

            if(yAdjustment != -64){
                bombSurround.add(moV);
            }
            
            
            xAdjustment += 32;
            yAdjustment += 32;
        }

        for(int i = 0; i <= 20; i++){
            testBomb4.tick(testPlayer, testEnemies);
        }

        for(MapObject i:bombSurround){
            if(i.getDestructible()){
                assertTrue(i.getDestroyed());
            }
        }

        testMap.tick(testPlayer);
    }
}
