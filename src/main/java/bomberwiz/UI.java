package bomberwiz;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

/**
 * UI class, displays timer, remaining lives and their logo.
 */
public class UI {
    private PImage timeSPrite;
    private PImage livesSprite;

    /**
     * Constructor for UI object.
     * @param app App object
     * @param font Font for texts
     */
    public UI(PApplet app, PFont font){
        this.timeSPrite = app.loadImage("src/main/resources/icons/clock.png");
        this.livesSprite = app.loadImage("src/main/resources/icons/player.png");
    }

    /**
     * Draws the clock and player logos along with their corresponding value (time left, lives).
     * @param app PApplet object to draw on
     * @param map Map object with time
     * @param player BombGuy object with lives
     */
    public void draw(PApplet app, Map map, BombWiz player){
        if(map.getPlayerContinue()){
            app.fill(0);
            app.image(this.livesSprite, 100 , 16);
            app.text(player.getLives(), 140, 48);
    
            app.image(this.timeSPrite, 260 , 16);
            app.text(map.getTime(), 300, 48);
        }
    }
}
