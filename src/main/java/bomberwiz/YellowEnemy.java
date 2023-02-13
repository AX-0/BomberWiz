package bomberwiz;

import processing.core.PApplet;

public class YellowEnemy extends Enemy{
    private int moves = 0;

        /**
     * Constructor for YellowEnemy.
     * @param x x lcoation
     * @param y y location
     * @param app PApplet object to draw on and loads sprite
     * @param lives lives allowed
     */
    public YellowEnemy(int x, int y, PApplet app,int lives) {
        super(x, y, app, lives);

        this.yellow = true;

        String type = "yellow";
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
     * Movement AI for yellow enemies.
     * Move current direction, if did not move change direction clcokwise, move again.
     * @param map Map the enemy is on
     */
    public void movement(Map map){
        int oldX = this.x;
        int oldY = this.y;

        this.walkCurrentDirection(map);
        this.moves += 1;

        if(this.moves >= 5){
            return;
        }else if(oldX == this.x & oldY == this.y){
            if(this.currentDirection == this.down){
                this.currentDirection = this.left;
            }else if(this.currentDirection == this.up){
                this.currentDirection = this.right;
            }else if(this.currentDirection == this.left){
                this.currentDirection = this.up;
            }else if(this.currentDirection == this.right){
                this.currentDirection = this.down;
            }
            movement(map);
        }

        this.moves = 0;
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
