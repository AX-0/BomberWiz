package bomberwiz;

import java.util.*;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Bomb class. For bombs in the game placed by a BombGuy.
 */
public class Bomb {
    private int x;
    private int y;

    private boolean exploading = false;
    private boolean exploded = false;

    private int tickTimer = 0;

    private List<PImage> bomb = new ArrayList<PImage>();
    private PImage explosionCenter;
    private PImage explosionV;
    private PImage explosionH;

    private PImage currentSprite;


    /**
     * Bomb constructor. Creates the Bomb object.
     * @param x x location
     * @param y y location
     * @param app PApplet object to load sprites
     */
    public Bomb(int x, int y, PApplet app){
        this.x = x;
        this.y = y;
        
        //this.bomb.add(app.loadImage("src/main/resources/bomb/bomb.png"));
        for(int i = 1; i < 9; i++){
            this.bomb.add(app.loadImage(String.format("src/main/resources/bomb/bomb%d.png", i)));
        }

        this.explosionCenter = app.loadImage("src/main/resources/explosion/centre.png");
        this.explosionV = app.loadImage("src/main/resources/explosion/vertical.png");
        this.explosionH = app.loadImage("src/main/resources/explosion/horizontal.png");

        this.currentSprite = this.bomb.get(0);
    }

    /**
     * Returns the exploded status.
     * @return exploded status
     */
    public boolean getExploded(){
        return this.exploded;
    }

    /**
     * Retuns the x location of the Bomb object.
     * @return x location
     */
    public int getX(){
        return this.x;
    }

    /**
     * Retuns the y location of the Bomb object.
     * @return y location
     */
    public int getY(){
        return this.y;
    }

    /**
     * Checks status and computes logic.
     * @param player BombGuy player in the game
     * @param enemies List of enemy in the game
     */
    public void tick(BombWiz player, List<Enemy> enemies){
        if(this.tickTimer % 15 == 0 & this.tickTimer <= 105){
            int index = this.tickTimer/15;
            this.setSprite(this.bomb.get(index));
        }else if(this.tickTimer >= 150){
            this.exploded = true;
        }else if(this.tickTimer > 105 & !this.exploading){
            this.exploading = true;

            this.checkCollisionWithCharacter(this.x, this.y, player, enemies);

            this.currentSprite = this.explosionCenter;
        }

        if(this.tickTimer <= 150){
            this.tickTimer += 1;
        }
    }

    /**
     * Changes the sprite to the provided sprite.
     * @param sprite new sprite
     */
    public void setSprite(PImage sprite){
        this.currentSprite = sprite;
    }

    /**
     * Checks collision with Characters (BombGuy and enemies).
     * @param x x location
     * @param y y location
     * @param player BombGuy player to check collision with
     * @param enemies List of enemy to check collision with
     */
    public void checkCollisionWithCharacter(int x, int y, BombWiz player, List<Enemy> enemies){
        if(player.getX() == x & player.getY() + 16 == y){
            player.loseLife();
        }

        for(Enemy e:enemies){
            if(e.getX() == x & e.getY() + 16 == y){
                e.loseLife();
            }
        }
    }

    private boolean nextVU = true;
    private boolean nextVD = true;
    private boolean nextHR = true;
    private boolean nextHL = true;
    
    /**
     * Draws the graphics (bomb sprite and explosion sprites when exploding).
     * @param app PApplet object to draw on
     * @param map Map object the bomb is on
     * @param player BombGuy player in the game - BombGuy player to check collision with
     * @param enemies List of enemy in the game - List of enemy to check collision with
     */
    public void draw(PApplet app, Map map, BombWiz player, List<Enemy> enemies){
        app.image(this.currentSprite, this.x, this.y);

        if(exploading){
            if(map.getMapObjectByLoc(this.x, this.y-32).getAccessible() || map.getMapObjectByLoc(this.x, this.y-32).getDestructible()){

                app.image(explosionV, this.x, this.y-32);

                this.checkCollisionWithCharacter(this.x, this.y-32, player, enemies);

                if(map.getMapObjectByLoc(this.x, this.y-32).getDestructible() & !map.getMapObjectByLoc(this.x, this.y-32).getDestroyed()){
                    map.getMapObjectByLoc(this.x, this.y-32).destroy();
                    this.nextVU = false;
                }

                if((!map.getMapObjectByLoc(this.x, this.y-32).getDestroyed() || this.nextVU) & 
                (map.getMapObjectByLoc(this.x, this.y-64).getAccessible() || map.getMapObjectByLoc(this.x, this.y-64).getDestructible())){
                    
                    app.image(explosionV, this.x, this.y-64);

                    this.checkCollisionWithCharacter(this.x, this.y-64, player, enemies);

                    if(map.getMapObjectByLoc(this.x, this.y-64).getDestructible()){
                        map.getMapObjectByLoc(this.x, this.y-64).destroy();
                    }
                }
            }

            if(map.getMapObjectByLoc(this.x, this.y+32).getAccessible() || map.getMapObjectByLoc(this.x, this.y+32).getDestructible()){
                app.image(explosionV, this.x, this.y+32);

                this.checkCollisionWithCharacter(this.x, this.y+32, player, enemies);

                if(map.getMapObjectByLoc(this.x, this.y+32).getDestructible() & !map.getMapObjectByLoc(this.x, this.y+32).getDestroyed()){
                    map.getMapObjectByLoc(this.x, this.y+32).destroy();
                    this.nextVD = false;
                }

                if((!map.getMapObjectByLoc(this.x, this.y+32).getDestroyed() || this.nextVD) & 
                (map.getMapObjectByLoc(this.x, this.y+64).getAccessible() || map.getMapObjectByLoc(this.x, this.y+64).getDestructible())){

                    app.image(explosionV, this.x, this.y+64);

                    this.checkCollisionWithCharacter(this.x, this.y+64, player, enemies);

                    if(map.getMapObjectByLoc(this.x, this.y+64).getDestructible()){
                        map.getMapObjectByLoc(this.x, this.y+64).destroy();
                    }
                }
            }

            if(map.getMapObjectByLoc(this.x-32, this.y).getAccessible() || map.getMapObjectByLoc(this.x-32, this.y).getDestructible()){
                app.image(explosionH, this.x-32, this.y);

                this.checkCollisionWithCharacter(this.x-32, this.y, player, enemies);

                if(map.getMapObjectByLoc(this.x-32, this.y).getDestructible() & !map.getMapObjectByLoc(this.x-32, this.y).getDestroyed()){
                    map.getMapObjectByLoc(this.x-32, this.y).destroy();
                    this.nextHL = false;
                }

                if((!map.getMapObjectByLoc(this.x-32, this.y).getDestroyed() || this.nextHL) &
                (map.getMapObjectByLoc(this.x-64, this.y).getAccessible() || map.getMapObjectByLoc(this.x-64, this.y).getDestructible())){

                    app.image(explosionH, this.x-64, this.y);

                    this.checkCollisionWithCharacter(this.x-64, this.y, player, enemies);

                    if(map.getMapObjectByLoc(this.x-64, this.y).getDestructible()){
                        map.getMapObjectByLoc(this.x-64, this.y).destroy();
                    }

                }
            }

            if(map.getMapObjectByLoc(this.x+32, this.y).getAccessible() || map.getMapObjectByLoc(this.x+32, this.y).getDestructible()){
                app.image(explosionH, this.x+32, this.y);

                this.checkCollisionWithCharacter(this.x+32, this.y, player, enemies);

                if(map.getMapObjectByLoc(this.x+32, this.y).getDestructible() & !map.getMapObjectByLoc(this.x+32, this.y).getDestroyed()){
                    map.getMapObjectByLoc(this.x+32, this.y).destroy();
                    this.nextHR = false;
                }

                if((!map.getMapObjectByLoc(this.x+32, this.y).getDestroyed() || this.nextHR) & 
                (map.getMapObjectByLoc(this.x+64, this.y).getAccessible() || map.getMapObjectByLoc(this.x+64, this.y).getDestructible())){

                    app.image(explosionH, this.x+64, this.y);

                    this.checkCollisionWithCharacter(this.x+64, this.y, player, enemies);

                    if(map.getMapObjectByLoc(this.x+64, this.y).getDestructible()){
                        map.getMapObjectByLoc(this.x+64, this.y).destroy();
                    }

                }
            }
        }
    }
}
