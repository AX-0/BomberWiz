package bomberwiz;

import java.io.*;
import java.util.*;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.*;

/**
 * Map class, read config, stores MapObject, Enemy, Bomb, and checks for conditions.
 * Map of the game.
 */
public class Map {
    private List<List<MapObject>> mapObjects = new ArrayList<List<MapObject>>();

    private List<String> levels = new ArrayList<String>();
    private List<Integer> times = new ArrayList<Integer>();

    private int currentLevel = 0;

    private int time;
    private int tickTimer = 0;

    private List<Bomb> bombs = new ArrayList<Bomb>();
    private List<Enemy> enemies = new ArrayList<Enemy>();

    private PApplet app;

    private PImage wall;
    private PImage empty;
    private PImage broken;
    private PImage goal;

    private int goalX;
    private int goalY;

    private int playerX = 0;
    private int playerY = 0;
    private int playerLives;

    private HashMap<Integer, List<Integer>> redEnemiesLoc = new HashMap<Integer, List<Integer>>();
    private HashMap<Integer, List<Integer>> yellowEnemiesLoc = new HashMap<Integer, List<Integer>>();

    private boolean playerWin = false;
    private boolean playerContinue = true;

    /**
     * Constructor for Map object.
     * @param app app object to load image on
     */
    public Map(PApplet app){
        this.app = app;

        this.wall = app.loadImage("src/main/resources/wall/solid.png");
        this.empty = app.loadImage("src/main/resources/empty/empty.png");
        this.broken = app.loadImage("src/main/resources/broken/broken.png");
        this.goal = app.loadImage("src/main/resources/goal/goal.png");
    }

    /**
     * Reads config and gets player lives, and levels' path and time limit information.
     * @param path path to read the config from
     */
    public void readConfig(String path){
        Reader reader = null;
        try {
            reader = new FileReader(path);
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }

        JSONObject config = new JSONObject(reader);
        JSONArray configLevels = config.getJSONArray("levels");
        for(int i = 0; i < configLevels.size(); i++){
            JSONObject configLevel = configLevels.getJSONObject(i);
            this.levels.add(configLevel.getString("path"));
            this.times.add(configLevel.getInt("time"));
        }
        this.playerLives = config.getInt("lives");
    }

    /**
     * Sets the map objects with the given level path and time limit.
     * Generates a 2D array of MapObjects and stores in this Map object.
     * @param path path to the level file
     * @param time time limit of the level
     */
    public void setMap(String path, int time){
        List<String> mapProfile = new ArrayList<String>();

        File f = new File(path);

        try{
            Scanner sc = new Scanner(f);

            while(sc.hasNextLine()){
                String line = sc.nextLine();
                mapProfile.add(line);
            }

            sc.close();
        }catch(FileNotFoundException e){
            System.out.println(e);
            return;
        }

        int y = 64;

        for(String line: mapProfile){
            
            ArrayList<MapObject> col = new ArrayList<MapObject>();
            MapObject object = null;

            

            int x = 0;

            for(int index = 0; index < line.length(); index++){
                line = line.toLowerCase();

                if(line.charAt(index) == 'w'){
                    object = new MapObject(x, y, wall, false);
                }else if(line.charAt(index) == 'b'){
                    object = new MapObject(x, y, broken, true);
                }else if(line.charAt(index) == 'g'){
                    this.goalX = x;
                    this.goalY = y;
                    object = new MapObject(x, y, goal, false, true);

                }else if(line.charAt(index) == 'p'){
                    this.playerX = x;
                    this.playerY = y-16;
                    object = new MapObject(x, y, empty, false, true);
                }else if(line.charAt(index) == 'y'){
                    List<Integer> enemyLoc = new ArrayList<Integer>();

                    enemyLoc.add(x);
                    enemyLoc.add(y);

                    yellowEnemiesLoc.put(yellowEnemiesLoc.size(), enemyLoc);
                    object = new MapObject(x, y, empty, false, true);
                }else if(line.charAt(index) == 'r'){
                    List<Integer> enemyLoc = new ArrayList<Integer>();

                    enemyLoc.add(x);
                    enemyLoc.add(y);

                    redEnemiesLoc.put(redEnemiesLoc.size(), enemyLoc);
                    object = new MapObject(x, y, empty, false, true);
                }else{
                    object = new MapObject(x, y, empty, false, true);
                }

                col.add(object);
                x += 32;
            }
            
            this.mapObjects.add(col);
            y += 32;
        }

        this.time = time;

        this.generateEnemies();
    }

    /**
     * Generates red and yellow enemies of the current level using setting read from setMap();
     */
    public void generateEnemies(){
        //List<Enemy> enemies = new ArrayList<Enemy>();
        for(int j = 0; j < this.getYellowEnemyAmount(); j++){
            int x = this.getEnemyLocX(j, true);
            int y = this.getEnemyLocY(j, true);
            Enemy yEnemy = new YellowEnemy(x, y, this.app, 1);
            this.enemies.add(yEnemy);
        }

        for(int j = 0; j < this.getRedEnemyAmount(); j++){
            int x = this.getEnemyLocX(j, false);
            int y = this.getEnemyLocY(j, false);
            Enemy rEnemy = new RedEnemy(x, y, this.app, 1);
            this.enemies.add(rEnemy);
        }

        //return enemies;
    }

    /**
     * Changes the level to wanted level.
     * @param level ;evel to change to
     */
    public void changeLevel(int level){
        if(!this.redEnemiesLoc.isEmpty() || !this.yellowEnemiesLoc.isEmpty()){
            this.redEnemiesLoc.clear();
            this.yellowEnemiesLoc.clear();
        }

        this.mapObjects =  new ArrayList<List<MapObject>>();

        this.bombs.clear();
        this.enemies.clear();

        this.setMap(this.levels.get(level), this.times.get(level));
    }

    /**
     * Resets the map.
     * @param player BombGuy object in the game
     */
    public void reset(BombWiz player){
        this.changeLevel(this.currentLevel);
        this.bombs.clear();
        player.changeLoc(this.playerX, this.playerY);
    }

    /**
     * Clears the map.
     */
    public void clear(){
        this.mapObjects =  new ArrayList<List<MapObject>>();
        this.enemies.clear();
        this.bombs.clear();
        this.playerContinue = false;
    }

    /**
     * Changes the destroyed MapObject objects within the map to empty.
     * @param mapObjects 2D array of MapObject
     * @param empty empty sprite
     */
    public void checkClearDestroyed(List<List<MapObject>> mapObjects, PImage empty){
        for(List<MapObject> moList: mapObjects){
            for(MapObject mo:moList){
                if(mo.getDestroyed()){
                    mo.changeSprite(empty);
                }
            }
        }
    }

    /**
     * Returns the MapObject object at provided x and y.
     * @param x x-axis value
     * @param y y-axis value
     * @return MapObject object at the x and y
     */
    public MapObject getMapObjectByLoc(int x, int y){
        for(List<MapObject> moList: mapObjects){
            for(MapObject mo:moList){
                int mo_x = mo.getX();
                int mo_y = mo.getY();

                if(mo_x == x & mo_y == y){
                    return mo;
                }
            }
        }

        return null;
    }

    /**
     * Actions to the map when game ended.
     * Clears map, change background, display text.
     */
    public void gameOver(){
        this.clear();
        this.app.background(68,179,224);
        this.app.fill(0);
        this.app.text("GAME OVER", 120, 240);
    }

    /**
     * Actions to the map when game is won.
     * Clears map, change background, display text.
     */
    public void gameWin(){
        this.clear();
        this.app.background(68,179,224);
        this.app.fill(0);
        this.app.text("YOU WIN", 150, 240);
    }

    /**
     * Checks status.
     * @param player BombGuy player on the map
     */
    public void tick(BombWiz player){
        if(this.playerWin & this.currentLevel < this.levels.size()-1){
            this.currentLevel += 1;

            this.changeLevel(this.currentLevel);

            player.changeLoc(getPlayerLocX(),getPlayerLocY());

            this.playerWin = false;

        }else if(this.playerWin & this.currentLevel == this.levels.size()-1){
            this.gameWin();
        }

        if(!player.getAlive()){
            this.gameOver();
        }

        if(this.tickTimer == 59){
            this.time -= 1;
        }

        if(time == 0){
            player.die();
        }

        if(this.tickTimer == 59){
            this.tickTimer = 0;
        }else{
            this.tickTimer += 1;
        }

        int bombRemvoeIndex = -1;
        for(Bomb b: this.bombs){
            if(b.getExploded()){
                bombRemvoeIndex = this.bombs.indexOf(b);
            }
        }

        if(bombRemvoeIndex != -1){
            this.bombs.remove(this.bombs.get(bombRemvoeIndex));
        }

        this.checkClearDestroyed(this.mapObjects, this.empty);
    }

    /**
     * Returns levels' path read from config.
     * @return list of string with the paths
     * @see #readConfig(String)
     */
    public List<String> getLevels(){
        return this.levels;
    }

    /**
     * Returns the time limits read from config.
     * @return list of integer with the time limits.
     * @see #readConfig(String)
     */
    public List<Integer> getTimes(){
        return this.times;
    }

    /**
     * Returns player's intial x location.
     * @return x location
     */
    public int getPlayerLocX(){
        return this.playerX;
    }

    /**
     * Returns player's intial y location.
     * @return y location
     */
    public int getPlayerLocY(){
        return this.playerY;
    }

    /**
     * Returns player's initial lives.
     * @return player's inital lives
     */
    public int getPlayerLives(){
        return this.playerLives;
    }

    /**
     * Returns the amount of red enemies in the map.
     * @return red enemies amount
     */
    public int getRedEnemyAmount(){
        return redEnemiesLoc.size();
    }

    /**
     * Returns the amount of yellow enemies in the map,
     * @return yellow enemies amount
     */
    public int getYellowEnemyAmount(){
        return yellowEnemiesLoc.size();
    }

    /**
     * Returns a selected enemy's x location.
     * @param enemyNum red/yellow enemy's index number
     * @param yellow for yellow enemy - true, for red enemy - false 
     * @return  x location
     */
    public int getEnemyLocX(int enemyNum, boolean yellow){
        if(yellow){
            return yellowEnemiesLoc.get(enemyNum).get(0);
        }else{
            return redEnemiesLoc.get(enemyNum).get(0);
        }
        
    }

    /**
     * Returns a selected enemy's y location.
     * @param enemyNum red/yellow enemy's index number
     * @param yellow for yellow enemy - true, for red enemy - false 
     * @return  y location
     */
    public int getEnemyLocY(int enemyNum, boolean yellow){
        if(yellow){
            return yellowEnemiesLoc.get(enemyNum).get(1)-16;
        }else{
            return redEnemiesLoc.get(enemyNum).get(1)-16;
        }
    }

    /**
     * Returns the list of enemies in the map.
     * @return list of enemies
     */
    public List<Enemy> getEnemies(){
        return this.enemies;
    }

    /**
     * Returns the x location of goal block.
     * @return x location
     */
    public int getGoalX(){
        return this.goalX;
    }

    /**
     * Returns the y location of goal block.
     * @return y location
     */
    public int getGoalY(){
        return this.goalY;
    }

    /**
     * Returns current remaining time.
     * @return remaining time limit
     */
    public int getTime(){
        return this.time;
    }

    /**
     * Returns a list of bombs that currently exists.
     * @return list of Bomb objects.
     */
    public List<Bomb> getBombs(){
        return this.bombs;
    }

    /**
     * Returns playerContinue.
     * @return playerContinue
     */
    public boolean getPlayerContinue(){
        return this.playerContinue;
    }

    /**
     * Returns current level.
     * @return  current level
     */
    public int getCurrentLevel(){
        return this.currentLevel;
    }

    /**
     * Sets playerwin to a boolean value
     * @param win boolean value
     */
    public void setPlayerWin(boolean win){
        this.playerWin = win;
    }

    /**
     * Places a bomb at player's location.
     * @param player BombGuy player in the map
     */
    public void placeBomb(BombWiz player){
        boolean bombExist = false;

        for(Bomb b: this.bombs){
            if(b.getX() == player.getX() & b.getY() == player.getY() + 16){
                bombExist = true;
            }
        }

        if(!bombExist){
            Bomb bomb = new Bomb(player.getX(), player.getY() + 16, app);
            this.bombs.add(bomb);
        }
    }

    /**
     * Draws the illustrations, map objects.
     * @param app PApplet object to draw on
     */
    public void draw(PApplet app){
        for(List<MapObject> row : this.mapObjects){
            for(MapObject col : row){
                col.draw(app);
            }
        }
    }
}
