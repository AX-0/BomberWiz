package bomberwiz;

import processing.core.*;

/**
 * The game's driver class. Sets config, draws graphics.
 */
public class App extends PApplet {
    /**
     * Width of the game window
     */
    public static final int WIDTH = 480;
    /**
     * Height of the game window
     */
    public static final int HEIGHT = 480;
    /**
     * Frames per second of the game
     */
    public static final int FPS = 60;

    private PFont font;
    private boolean keyHold = false;
    private String configPath = "config.json";

    private UI ui;
    private Map map;
    private BombWiz player;
    
    /**
     * Constructor for an App object.
     */
    public App() { 
    }

    /**
     * Returns the BombGuy player.
     * @return BombGuy player
     */
    public BombWiz getPlayer(){
        return this.player;
    }

    /**
     * Returns the Map object.
     * @return Map map
     */
    public Map getMap(){
        return this.map;
    }

    /**
     * Set to read the config in another path other than root for later config parsing in the Map object.
     * Ignore if root is wanted.
     * @param path the path
     */
    public void setConfig(String path){
        this.configPath = path;
    }

    /**
     * Settings
     */
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Sets the App up.
     * Sets FPS, loads map, create player, create font, create UI before loop start.
     */
    public void setup() {
        frameRate(FPS);
        
        this.map = new Map(this);
        this.map.readConfig(this.configPath);
        this.map.setMap(this.map.getLevels().get(0), this.map.getTimes().get(0));

        this.player = new BombWiz(this.map.getPlayerLocX(), this.map.getPlayerLocY(), this, this.map.getPlayerLives());

        this.font = this.createFont("src/main/resources/PressStart2P-Regular.ttf", 25);
        this.textFont(this.font);
        this.ui = new UI(this, this.font);
    }

    /**
     * Draws the graphics at each frame.
     */
    public void draw() {
        background(68,179,224);

        this.map.tick(this.player);
        this.player.tick(this.map, null);     
        
        this.map.draw(this);
        
        for(Bomb b: this.map.getBombs()){
            b.draw(this, this.map, this.player, this.map.getEnemies());
            b.tick(this.player, this.map.getEnemies());
        }

        for(Enemy enemy:this.map.getEnemies()){
            enemy.tick(this.map, this.player);
            enemy.draw(this);
        }

        if(this.map.getPlayerContinue()){
            this.player.draw(this);
        }

        this.ui.draw(this, this.map, this.player);
    }

    /**
     * Registers when a key is released.
     * @see #keyPressed()
     */
    public void keyReleased(){
        if(this.keyHold){
            this.keyHold = false;
        }
    }

    /**
     * Registers when a key is pressed.
     * Passes keycode to move method in BombGuy player.
     * @see #keyReleased()
     */
    public void keyPressed(){
        if(!keyHold & this.map.getPlayerContinue()){
            this.player.move(this.keyCode, this.map);
        }  
        this.keyHold = true;
    }

    public static void main(String[] args) {
        PApplet.main("bomberwiz.App");
    }
}