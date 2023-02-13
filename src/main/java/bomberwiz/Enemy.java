package bomberwiz;

import processing.core.PApplet;

/**
 * Enemy class. For enemies in the game.
 * A child class of Character.
 */
public abstract class Enemy extends Character{
    /**
     * Yellow enemy identifier.
     * YellowEnemy - true, RedEnemy - false
     */
    protected boolean yellow;

    /**
     * Tick timer for movement changing. Increment at every frame.
     */
    protected int tickTimerMovement = 0;

    /**
     * Constructor for Enemy.
     * @param x x lcoation
     * @param y y location
     * @param app PApplet object to draw on and loads sprite
     * @param lives lives allowed
     */
    public Enemy(int x, int y, PApplet app,int lives) {
        super(x, y, app, lives);
    }

    /**
     * Returns the yellow status. True for yellow enemy and false for red enemy.
     * @return yellow - true for yellow enemy and false for red enemy.
     */
    public boolean getYellow(){
        return this.yellow;
    }

    /**
     * Move 32 pixels up/down/left/right according to current direction.
     * @param map Map the enemy is on
     * @see #walkDown(Map)
     * @see #walkUp(Map)
     * @see #walkLeft(Map)
     * @see #walkRight(Map)
     */
    public void walkCurrentDirection(Map map){
        if(this.currentDirection == this.down){
            this.walkDown(map);
        }else if(this.currentDirection == this.up){
            this.walkUp(map);
        }else if(this.currentDirection == this.left){
            this.walkLeft(map);
        }else if(this.currentDirection == this.right){
            this.walkRight(map);
        }
    }

    /**
     * Movement AI for enemy.
     * @param map Map the enemy is on
     */
    public abstract void movement(Map map);

    /**
     * Checks collision with BombGuy.
     * If collided, BombGuy loses life.
     * @param player the BombGuy player to check collision with
     */
    public void checkCollisionWithBombGuy(Character player){
        if(this.x == player.getX() & this.y == player.getY()){
            player.loseLife();
        }
    }

    /**
     * Checks status and computes logic.
     * @param map Map object the Character is on
     * @param player the Character/BombGuy player in the game
     */
    public abstract void tick(Map map, Character player);   
}
