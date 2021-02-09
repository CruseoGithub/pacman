package uas.lntv.pacmangame.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import uas.lntv.pacmangame.AI.Pathfinder;
import uas.lntv.pacmangame.Maps.Tile;
import uas.lntv.pacmangame.Screens.MapScreen;

/**
 * The ghosts are made of pure evil with the only purpose to chase and kill PacMan.
 */
public class Enemy extends Actor {
    public enum Difficulty{
        RUNAWAY,RANDOM,EASY,MEDIUM,HARD;
    };

    private Pathfinder aStar;
    private Difficulty difficulty;
    private Difficulty levelDiff;

    /**
     * Create a new ghost
     * @param initX starting x-coordinate
     * @param initY starting y-coordinate
     * @param screen the screen in which the ghost will be created
     * @param ghost name or path of the png-file that makes the ghost look beautiful
     */
    public Enemy(int initX, int initY, MapScreen screen, String ghost){
        super(initX, initY, screen);

        this.difficulty = Difficulty.EASY;
        this.direction = Direction.DOWN;
        this.nextDirection = Direction.DOWN;
        this.prevDirection = Direction.DOWN;

        this.texture = new Texture(ghost);
        region = new TextureRegion(texture);
        region.setRegionX(0);
        region.setRegionY(0);
        texturePositionX = 0;
        texturePositionY = 0;
        this.animationSpeed = 0.1f;

        animation = new Animation(this,animationSpeed, this.screen,4);

        region.flip(true, false);
        this.sprite = new Sprite(region);
        this.sprite.setOrigin(tileSize/2, tileSize/2);
    }

    public Difficulty getDifficulty(){ return this.difficulty; }

    public void setDifficulty(Enemy.Difficulty difficulty){
        if(difficulty == Difficulty.RUNAWAY && this.difficulty != Difficulty.RUNAWAY) levelDiff = this.difficulty;
        this.difficulty = difficulty;
    }

    public void resetDifficulty(){
        setDifficulty(levelDiff);
    }

    private Direction UpOrDown(int distance){
        if (distance > 0) return Direction.DOWN;
        else return Direction.UP;
    }

    private Direction LeftOrRight(int distance){
        if (distance > 0 ) return Direction.LEFT;
        else return Direction.RIGHT;
    }

    private Direction findNextDirectionEasy(Actor target){
        int distanceX = this.xPosition - target.getXPosition();
        int distanceY = this.yPosition - target.getYPosition();
        if((Math.abs(distanceX) + Math.abs(distanceY)) > 16*tileSize) return Direction.getRandomDirection();
        if(collisionTest(target)) return Direction.RIGHT;
        if(Math.abs(distanceX) > Math.abs(distanceY)){
            return LeftOrRight(distanceX);
        }
        else {
            return UpOrDown(distanceY);
        }
    }

    private Direction findNextDirectionMedium(Actor target){
        int distanceX = this.xPosition - target.getXPosition();
        int distanceY = this.yPosition - target.getYPosition();
        if((Math.abs(distanceX)+Math.abs(distanceY)) > 16*tileSize ) return Direction.getRandomDirection();
        return findNextDirectionHard(target);
    }

    private Direction findNextDirectionHard(Actor target){
        if(collisionTest(target)) return Direction.RIGHT;
        aStar = new Pathfinder(screen, this, target);
        Tile temp = aStar.aStarResult();
        if(temp == screen.map.getTile(xPosition, yPosition) || temp == null) return direction;
        if(temp.getY() > this.getYPosition()) return Direction.UP;
        if(temp.getY() < this.getYPosition()) return Direction.DOWN;
        if(temp.getX() > this.getXPosition()) return Direction.RIGHT;
        else return Direction.LEFT;
    }

    /**
     * This method shows the next tile to visit on the way to the as most secure assumed corner.
     * The map will be divided in four quarters and the targeted tile will be the one in the
     * extreme corner of the opposite quarter in which SuperPacMan is right now.
     * @param hunter RUN! It's SuperPacMan!!
     * @return tile of retreat
     */
    private Direction runAway(Actor hunter){
        if(collisionTest(hunter)){
            this.state = State.KILLED;
            return Direction.RIGHT;
        }
        if(hunter.getXPosition() < 13*tileSize){
            if(hunter.getYPosition() < 30*tileSize) aStar = new Pathfinder(screen, this, 26*tileSize, 43*tileSize, false);
            if(hunter.getYPosition() >= 30*tileSize) aStar = new Pathfinder(screen, this, 26*tileSize, 15*tileSize, false);
        } else{
            if(hunter.getYPosition() < 30*tileSize) aStar = new Pathfinder(screen, this, tileSize, 43*tileSize , false);
            if(hunter.getYPosition() >= 30*tileSize) aStar = new Pathfinder(screen, this, tileSize, 15*tileSize , false);
        }
        Tile temp = aStar.aStarResult();
        if(temp == screen.map.getTile(xPosition, yPosition) || temp == null) return direction;
        if(temp.getY() > this.getYPosition()) return Direction.UP;
        if(temp.getY() < this.getYPosition()) return Direction.DOWN;
        if(temp.getX() > this.getXPosition()) return Direction.RIGHT;
        else return Direction.LEFT;
    }

    private Direction findHome(){
        aStar = new Pathfinder(screen, this, getStartPosX(), getStartPosY(), true);
        Tile temp = aStar.aStarResult();
        if(temp.getY() > this.getYPosition()) return Direction.UP;
        if(temp.getY() < this.getYPosition()) return Direction.DOWN;
        if(temp.getX() > this.getXPosition()) return Direction.RIGHT;
        else return Direction.LEFT;
    }

    public void getHome(){
        correctPosition(direction);
        setSpeed(getSpeed() * 2);
        nextDirection = findHome();
        move();
        setSpeed(getSpeed() / 2);
    }

    public void findNextDirection(Actor target) {
        switch (difficulty) {
            case RANDOM:
                nextDirection = Direction.getRandomDirection();
                break;
            case EASY:
                nextDirection = findNextDirectionEasy(target);
                break;
            case MEDIUM:
                nextDirection = findNextDirectionMedium(target);
                break;
            case HARD:
                nextDirection = findNextDirectionHard(target);
                break;
            case RUNAWAY:
                nextDirection = runAway(target);
                break;
        }
    }

    private boolean collisionTest(Actor target){
        int distanceX = this.xPosition - target.getXPosition();
        int distanceY = this.yPosition - target.getYPosition();
        if((Math.abs(distanceX) < tileSize) && (Math.abs(distanceY) < tileSize)){
            target.collide();
            return true;
        }
        return false;
    }

}
