package bomberwiz;

import processing.core.PApplet;

/**
 * BombGuy class. For the bombguy player in the game.
 * A child class of Character.
 */
public class BombWiz extends Character{
    private int oldLives;

    /**
     * Constructor for BOmbGuy object.
     * @param x x location
     * @param y y location
     * @param app PApplet object to load sprites
     * @param lives lives allowed
     */
    public BombWiz(int x, int y, PApplet app, int lives) {
        super(x, y, app, lives);

        for(int i = 1; i < 5; i++){
            this.left.add(app.loadImage(String.format("src/main/resources/player/player_left%d.png", i)));
        }

        for(int i = 1; i < 5; i++){
            this.right.add(app.loadImage(String.format("src/main/resources/player/player_right%d.png", i)));
        }

        for(int i = 1; i < 5; i++){
            this.up.add(app.loadImage(String.format("src/main/resources/player/player_up%d.png", i)));
        }

        for(int i = 1; i < 5; i++){
            this.down.add(app.loadImage(String.format("src/main/resources/player/player%d.png", i)));
        }

        this.currentSprite = this.down.get(0);
        this.currentDirection = this.down;

        this.oldLives = lives;
    }

    /**
     * Check if BombGuy on goal block.
     * @param goalX goal block x location
     * @param goalY goal block y location
     * @return BombGuy on goal block - true, else - false
     */
    public boolean checkWin(int goalX, int goalY){
        if(this.x == goalX & (this.y + 16) == goalY){
            return true;
        }
        return false;
    }

    /**
     * Moves the BombGuy by 32 pixels or place a bomb according the key code.
     * @param keyCode key code received: 37 - left, 38 - up, 39 - right, 40 - down, 32 - place bomb
     * @param map Map the BombGuy is on
     */
    public void move(int keyCode, Map map){
        if (keyCode == 37){
            this.walkLeft(map);
        }else if(keyCode == 38){
            this.walkUp(map);
        }else if(keyCode == 39){
            this.walkRight(map);
        }else if(keyCode == 40){
            this.walkDown(map);
        }else if(keyCode == 32){
            map.placeBomb(this);
        }
    }

    /**
     * Checks status and computes logic.
     * @param map Map object the Character is on
     * @param c null
     */
    public void tick(Map map, Character c) {
        c = null;
               
        if(checkWin(map.getGoalX(), map.getGoalY())){
            map.setPlayerWin(true);
        }

        if(this.oldLives > this.lives){
            map.reset(this);
            this.oldLives = this.lives;
        }

        if(this.tickTimer % 12 == 0 & this.tickTimer <= 36){
            int index = this.tickTimer/12;
            if(this.tickTimer <= 24){
                index = index + 1;
                this.setSprite(this.currentDirection.get(index));
            }else if(this.tickTimer == 36){
                index = index - 1;
                this.setSprite(this.currentDirection.get(index));
            }
        }

        if(this.tickTimer >= 48){
            this.tickTimer = 0;
        }else{
            this.tickTimer += 1;
        }     
    }
}
