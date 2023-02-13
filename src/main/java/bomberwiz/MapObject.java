package bomberwiz;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Map object class. A class for each block in the map.
 */
public class MapObject{
    private int x;
    private int y;

    private PImage sprite;
    
    private boolean destroyed;
    private boolean destructible;
    private boolean accessible;

    /**
     * MapObject constructor, creates MapObject object. For solid walls or any non-accessible object.
     * @param x x location
     * @param y y location
     * @param sprite PImage object with the sprite for this block
     * @param destructible destructible by bombs - true, not destructible - false
     */
    public MapObject(int x, int y, PImage sprite, boolean destructible){
        this.x = x;
        this.y = y;

        this.sprite = sprite;
        this.destructible = destructible;
        this.accessible = false;
        this.destroyed = false;
    }

    /**
     * MapObject constructor, creates MapObject object. For empty object or any accessible object.
     * @param x x location
     * @param y y location
     * @param sprite PImage object with the sprite for this block
     * @param destructible destructible by bombs - true, not destructible - false
     * @param accessible accessible by Character - true, not acessible by Character - false
     */
    public MapObject(int x, int y, PImage sprite, boolean destructible, boolean accessible){
        this.x = x;
        this.y = y;

        this.sprite = sprite;
        this.destructible = destructible;
        this.accessible = accessible;
        this.destroyed = false;
    }

    /**
     * Destroys the object. Sets destroyed and accessible to true.
     */
    public void destroy(){
        if(destructible){
            this.destroyed =  true;
            this.accessible = true;
        }
    }

    /**
     * Returns destroyed status.
     * @return destroyed status
     */
    public boolean getDestroyed(){
        return this.destroyed;
    }

    /**
     * Returns accessible
     * @return accessible
     */
    public boolean getAccessible(){
        return this.accessible;
    }

    /**
     * Retuns destructible.
     * @return destructible
     */
    public boolean getDestructible(){
        return this.destructible;
    }

    /**
     * Returns the object's x value.
     * @return x location
     */
    public int getX(){
        return this.x;
    }

        /**
     * Returns the object's y value.
     * @return y location
     */
    public int getY(){
        return this.y;
    }

    /**
     * Sets a new PImage sprite for the object.
     * @param sprite PImage object
     */
    public void changeSprite(PImage sprite){
        this.sprite = sprite;
    }

    /**
     * Draws graphics.
     * @param app PApplet object to draw on
     */
    public void draw(PApplet app){
        app.image(this.sprite, this.x, this.y);
    }
}
