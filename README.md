# BomberWiz
To play the game, run *gradle build run* on your terminal.

## Control
The character is controlled with up, down, left, and right arrow keys for movement and space key to drop a bomb.

## Mechanism
- Each game a player gets 3 lives, the player looses a live when it is hit by a bomb or interacted with an enemy.
- Only brick blocks can be destroyed by a bomb.
- The player looses when:
  - The timer counts down to 0, where the timer starts with 180 seconds.
  - All lives are lost.
- Player progresses to the next level by reaching the goal block of the current level.
- The player wins by reaching the goal block of the last level.  
- There are currently 2 levels.
- There are two enemies: Red and Yellow enemy. They can be identified by the color of their "core" and the orbs
surrounding them. The enemies can be killed by bombs.
  - Red enemy moves to a random direction when it hits a wall.
  - Yellow enemy moves in a clockwise fashion. 

## Issues
- Tests are disabled for the *BombWiz* class because they are acting weird.

# Future Plans
- Fix the testing and use Mockito. Need sometime to comprehend the code written by my dumb first-year self...
- Apply design patterns, the current structure is tedious.
- Better graphic and design.
- Add new features:
  - More levels
  - Be able to restart
  - Have a backstory
  - Enemies be more threatening
  - An UI
