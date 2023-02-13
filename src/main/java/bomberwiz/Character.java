package bomberwiz;

import java.util.*;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * Character class. For chracters (enemies and bombguy) in the game.
 * Parent class of Enemy and BombGuy.
 */
public abstract class Character {
    
    /**
     * Down facing sprites.
     */
    protected List<PImage> down = new ArrayList<PImage>();

     /**
     * Left facing sprites.
     */
    protected List<PImage> left = new ArrayList<PImage>();

     /**
     * Right facing sprites.
     */
    protected List<PImage> right = new ArrayList<PImage>();

     /**
     * Up facing sprites.
     */
    protected List<PImage> up = new ArrayList<PImage>();

    /**
     * Currently drawing sprite.
     */
    protected PImage currentSprite;

    /**
     * Timer for sprite changing. Increment at every frame.
     */
    protected int tickTimer = 0;

    /**
     * Currently facing direction sprites.
     */
    protected List<PImage> currentDirection;

    /**
     * x location.
     */
    protected int x;

    /**
     * y location.
     */
    protected int y;

    /**
     * Allowed lives.
     */
    protected int lives;

    /**
     * Alive status.
     */
    protected boolean alive;

    /**
     * Constructor for Character class.
     * @param x x location
     * @param y y location
     * @param app PApplet object to draw on and loads sprite
     * @param lives Lives allowed
     */
    public Character(int x, int y, PApplet app, int lives){
        this.x = x;
        this.y = y;

        this.lives = lives;
        this.alive = true;
    }

    /**
     * Draws the graphics (sprites).
     * @param app PApplet object to draw on
     */
    public void draw(PApplet app){
        if(this.getAlive()){
            app.image(this.currentSprite, this.x, this.y);
        }
    }

    /**
     * Checks status and computes logic.
     * @param map Map object the Character is on
     * @param c Other Character
     */
    public abstract void tick(Map map, Character c);

    /**
     * Retuns the object's x location.
     * @return x location
     */
    public int getX(){
        return this.x;
    }

    /**
     * Retuns the object's y location.
     * @return y location
     */
    public int getY(){
        return this.y;
    }

    /**
     * Retuns the current direction in string.
     * @return current direction in string
     */
    public String getDirectionString(){
        if(this.currentDirection == this.down){
            return "down";
        }else if(this.currentDirection == this.left){
            return "left";
        }else if(this.currentDirection == this.up){
            return "up";
        }else{
            return "right";
        }
    }

    /**
     * Returns remaining lives.
     * @return remaining lives
     */
    public int getLives(){
        return this.lives;
    }

    /**
     * Changes the location of the object.
     * @param x x location
     * @param y y location
     */
    public void changeLoc(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * Moves the object 32 pixels up if that blcok is accessible.
     * @param map Map object that the object is on
     */
    public void walkUp(Map map){
        if(map.getMapObjectByLoc(this.x, this.y - 16).getAccessible()){
            this.y -= 32;

            if(this.currentDirection != this.up){
                this.currentSprite = up.get(0);
                this.currentDirection = this.up;
            }
        }
    }

    /**
     * Moves the object 32 pixels down if that blcok is accessible.
     * @param map Map object that the object is on
     */
    public void walkDown(Map map){
        if(map.getMapObjectByLoc(this.x, this.y + 48).getAccessible()){
            this.y += 32;

            if(this.currentDirection != this.down){
                this.currentSprite = down.get(0);
                this.currentDirection = this.down;
            }
        }
    }

    /**
     * Moves the object 32 pixels left if that blcok is accessible.
     * @param map Map object that the object is on
     */
    public void walkLeft(Map map){
        if(map.getMapObjectByLoc(this.x - 32, this.y + 16).getAccessible()){
            this.x -= 32;

            if(this.currentDirection != this.left){
                this.currentSprite = left.get(0);
                this.currentDirection = this.left;
            }
        }
    }

    /**
     * Moves the object 32 pixels right if that blcok is accessible.
     * @param map Map object that the object is on
     */
    public void walkRight(Map map){
        if(map.getMapObjectByLoc(this.x + 32, this.y + 16).getAccessible()){
            this.x += 32;

            if(this.currentDirection != this.right){
                this.currentSprite = right.get(0);
                this.currentDirection = this.right;
            }
        }
    }

    /**
     * Decrements the object's remaining lives.
     * If new lives is 0, object dies.
     * @see #die()
     */
    public void loseLife(){
        if(this.lives - 1 == 0){
            this.lives -= 1;
            this.die();
        }else{
            this.lives -= 1;
        }
    }

    /**
     * Returns the object's alive status.
     * @return boolean alive
     */
    public boolean getAlive(){
        return this.alive;
    }

    /**
     * Kills the object.
     * Sets lives to 0.
     */
    public void die(){
        this.lives = 0;
        this.alive = false;
    }

    /**
     * Changes current sprite to provided sprite.
     * @param newSprite new sprite
     */
    public void setSprite(PImage newSprite){
        this.currentSprite = newSprite;
    }
}
