package bomberwiz;

import processing.core.PApplet;

import java.util.HashMap;
import java.util.Random;

public class RedEnemy extends Enemy{
    private Random nextDirection = new Random();
    private HashMap<String, Integer> directionCounter = new HashMap<String, Integer>();

    /**
     * Constructor for RedEnemy.
     * @param x x lcoation
     * @param y y location
     * @param app PApplet object to draw on and loads sprite
     * @param lives lives allowed
     */
    public RedEnemy(int x, int y, PApplet app,int lives) {
        super(x, y, app, lives);

        this.yellow = false;

        String type = "red";
        for(int i = 1; i < 5; i++){
            this.left.add(app.loadImage(String.format("src/main/resources/%s_enemy/%s_left%d.png",type, type,i)));
        }

        for(int i = 1; i < 5; i++){
            this.right.add(app.loadImage(String.format("src/main/resources/%s_enemy/%s_right%d.png",type, type,i)));
        }

        for(int i = 1; i < 5; i++){
            this.up.add(app.loadImage(String.format("src/main/resources/%s_enemy/%s_up%d.png",type, type,i)));
        }

        for(int i = 1; i < 5; i++){
            this.down.add(app.loadImage(String.format("src/main/resources/%s_enemy/%s_down%d.png",type, type,i)));
        }

        this.currentSprite = this.down.get(0);
        this.currentDirection = this.down;
    }

    /**
     * Changes current direction to a random direction.
     */
    public void randDirection(){
        int direction = nextDirection.nextInt(4); // 0 = Left, 1 = Up, 2 = Right, 3 = Down

        if(direction == 0){
            this.currentDirection = this.left;
        }else if(direction == 1){
            this.currentDirection = this.up;
        }else if(direction == 2){
            this.currentDirection = this.right;
        }else if(direction == 3){
            this.currentDirection = this.down;
        }
    }
    
    /**
     * Movement AI for red enemies.
     * Move current direction, if did not move, new direction selected randomly, move again.
     * @param map Map the enemy is on
     */
    public void movement(Map map){
        int oldX = this.x;
        int oldY = this.y;

        this.walkCurrentDirection(map);

        int blockCounter = 0;
        for(int i: this.directionCounter.values()){
            if(i == 1){
                blockCounter += 1;
            }
        }

        if(blockCounter >= 4){
            return;
        }else if(oldX == this.x & oldY == this.y){
            this.directionCounter.put(this.getDirectionString(), 1);
            this.randDirection();
            this.movement(map);
        }

        this.directionCounter.clear();
    }

    /**
     * Checks status and computes logic.
     * @param map Map object the Character is on
     * @param player the Character/BombGuy player in the game
     */
    public void tick(Map map, Character player) {
        if(!this.getAlive()){
            return;
        }
        
        this.checkCollisionWithBombGuy(player);

        if(this.tickTimerMovement == 60){
                this.movement(map);
        }

        if(this.tickTimerMovement > 60){
            this.tickTimerMovement = 0;
        }else{
            this.tickTimerMovement += 1;
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
