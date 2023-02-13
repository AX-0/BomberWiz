package bomberwiz;

import static org.junit.jupiter.api.Assertions.*;
import processing.core.PApplet;
import java.util.*;

import org.junit.jupiter.api.*;

public class EnemyTest {
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
        testPlayer = new BombWiz(testMap.getPlayerLocX(), testMap.getPlayerLocY(), app, testMap.getPlayerLives());
    }

    // Check if enemies is loaded and walking into a solid wall will not have any movement.
    @Test
    public void basicTest(){
        testEnemies = testMap.getEnemies();
        List<Enemy> testEnemiesClone = new ArrayList<Enemy>(testMap.getEnemies());

        for(Enemy e: testEnemies){
            if(e.getYellow()){
                e.walkLeft(testMap);
            }else{
                e.walkUp(testMap);
            }
        }

        for(int i = 0; i < testEnemies.size(); i++){
            assertEquals(testEnemiesClone.get(i).getX(), testEnemies.get(i).getX());
            assertEquals(testEnemiesClone.get(i).getY(), testEnemies.get(i).getY());
        }
    }

    // Check yellow enemy's movement AI.
    @Test
    public void yellowMovementTest(){
        int oneMovementTick = 61;
        testEnemies = testMap.getEnemies();

        int yellowIndex = -1;
        for(Enemy e: testEnemies){
            if(e.getYellow()){
                yellowIndex = testEnemies.indexOf(e);
            }
        }

        int initialY = testEnemies.get(yellowIndex).getY();
        int initialX = testEnemies.get(yellowIndex).getX();

        for(int i = 0; i < oneMovementTick; i++){
            testEnemies.get(yellowIndex).tick(testMap, testPlayer);
        }

        assertEquals(initialX, testEnemies.get(yellowIndex).getX());
        assertEquals(initialY+32, testEnemies.get(yellowIndex).getY());
        
        initialY += 32;

        for(int i = 0; i < oneMovementTick+1; i++){
            testEnemies.get(yellowIndex).tick(testMap, testPlayer);
        }

        assertEquals(initialX, testEnemies.get(yellowIndex).getX());
        assertEquals(initialY+32, testEnemies.get(yellowIndex).getY());

        initialY += 32;

        for(int i = 0; i < 4; i++){
            for(int j = 0; j < oneMovementTick+5; j++){
                testEnemies.get(yellowIndex).tick(testMap, testPlayer);
            }
            assertEquals(initialX-32, testEnemies.get(yellowIndex).getX());
            assertEquals(initialY, testEnemies.get(yellowIndex).getY());
    
            initialX -= 32;
        }

        for(int i = 0; i < oneMovementTick+1; i++){
            testEnemies.get(yellowIndex).tick(testMap, testPlayer);
        }

        assertEquals(initialX, testEnemies.get(yellowIndex).getX());
        assertEquals(initialY-32, testEnemies.get(yellowIndex).getY());

        initialY -= 32;

        for(int i = 0; i < oneMovementTick+1; i++){
            testEnemies.get(yellowIndex).tick(testMap, testPlayer);
        }

        assertEquals(initialX, testEnemies.get(yellowIndex).getX());
        assertEquals(initialY-32, testEnemies.get(yellowIndex).getY());

        initialY -= 32;

        for(int i = 0; i < 4; i++){
            for(int j = 0; j < oneMovementTick+5; j++){
                testEnemies.get(yellowIndex).tick(testMap, testPlayer);
            }
            assertEquals(initialX+32, testEnemies.get(yellowIndex).getX());
            assertEquals(initialY, testEnemies.get(yellowIndex).getY());
    
            initialX += 32;
        }

        for(int i = 0; i < oneMovementTick+1; i++){
            testEnemies.get(yellowIndex).tick(testMap, testPlayer);
        }

        assertEquals(initialX, testEnemies.get(yellowIndex).getX());
        assertEquals(initialY+32, testEnemies.get(yellowIndex).getY());

        initialY += 32;
    }

    // Check red enemy's movement AI.
    @Test
    public void redMovementTest(){
        int oneMovementTick = 62;
        testEnemies = testMap.getEnemies();
        

        int redIndex = -1;
        for(Enemy e: testEnemies){
            if(!e.getYellow()){
                redIndex = testEnemies.indexOf(e);
            }
        }

        Enemy red = testEnemies.get(redIndex);

        for(int i = 0; i < oneMovementTick+1; i++){
            red.tick(testMap, testPlayer);
        }

        String direction = red.getDirectionString();

        // Allowable direction at this loc: left or right.
        // If other directions, fail the test.
        if(direction.equals("left")){
            assertEquals("left", direction);
        }else if(direction.equals("right")){
            assertEquals("right", direction);
        }else{
            assertEquals("", direction);;
        }

        red.changeLoc(red.getX(), red.getY()-32);
        for(int i = 0; i < oneMovementTick; i++){
            red.tick(testMap, testPlayer);
        }

        direction = red.getDirectionString();
        if(direction.equals("up")){
            assertEquals("up", direction);
        }else if(direction.equals("down")){
            assertEquals("down", direction);
        }else{
            assertEquals("", direction);;
        }


        for(int i = 0; i < oneMovementTick*15; i++){
            red.tick(testMap, testPlayer);
        }
    }

    // Check if collision with bomb kills the enemy.
    // Check if collision with bombguy make the bombguy loose a life.
    @Test
    public void collisionTest(){
        //int oneMovementTick = 62;
        testEnemies = testMap.getEnemies();

        int yellowIndex = -1;
        for(Enemy e: testEnemies){
            if(e.getYellow()){
                yellowIndex = testEnemies.indexOf(e);
            }
        }

        testEnemies.get(yellowIndex).changeLoc(testPlayer.getX(), testPlayer.getY());

        testEnemies.get(yellowIndex).tick(testMap, testPlayer);

        assertEquals(2, testPlayer.getLives());
        
        //testPlayer.changeLoc(testEnemies.get(yellowIndex).getX()-64, testEnemies.get(yellowIndex).getY());
        testEnemies.get(yellowIndex).changeLoc(testPlayer.getX()+32, testPlayer.getY());
        testMap.placeBomb(testPlayer);

        for(int i = 0; i < 110; i++){
            testMap.getBombs().get(0).tick(testPlayer, testEnemies);
        }

        testMap.getBombs().get(0).draw(app, testMap, testPlayer, testEnemies);

        testEnemies.get(yellowIndex).tick(testMap, testPlayer);

        assertEquals(1, testPlayer.getLives());
        assertEquals(0, testEnemies.get(yellowIndex).getLives());
        assertFalse(testEnemies.get(yellowIndex).getAlive());
    }
}
