package uas.lntv.pacmangame.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import uas.lntv.pacmangame.AI.Pathfinder;
import uas.lntv.pacmangame.Maps.Tile;
import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Screens.GameScreen;
import uas.lntv.pacmangame.Screens.MapScreen;

public class Enemy extends Actor {
    public enum Difficulty{
        RUNAWAY,RANDOM,EASY,MEDIUM,HARD;
    };

    private Pathfinder aStar;
    private Difficulty difficulty ;

    public Enemy(int initX, int initY, MapScreen screen, Difficulty difficulty){
        super(initX, initY, screen);
        this.texture = new Texture("enemies.png");
        this.sprite = new Sprite(texture,0, 0, 200,200);
        this.sprite.rotate90(true);
        this.difficulty = difficulty;
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
        if((Math.abs(distanceX)+Math.abs(distanceY)) > 16*tileSize) return Direction.getRandomDirection();
        if((Math.abs(distanceX) < tileSize) && (Math.abs(distanceY) < tileSize)){
            target.die();
            this.die();
            return Direction.RIGHT;
        }
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
        int distanceX = this.xPosition - target.getXPosition();
        int distanceY = this.yPosition - target.getYPosition();
        if((Math.abs(distanceX) < tileSize) && (Math.abs(distanceY) < tileSize)){
            target.die();
            this.die();
            return Direction.RIGHT;
        }
        aStar = new Pathfinder(screen, this, target, tileSize);
        Tile temp = aStar.aStarResult();
        if(temp.getY() > this.getYPosition()) return Direction.UP;
        if(temp.getY() < this.getYPosition()) return Direction.DOWN;
        if(temp.getX() > this.getXPosition()) return Direction.RIGHT;
        else return Direction.LEFT;
    }

    public void findNextDirection(Actor target) {
        switch (difficulty) {
            case RANDOM:
                nextdirection = Direction.getRandomDirection();
                break;
            case EASY:
                nextdirection = findNextDirectionEasy(target);
                break;
            case MEDIUM:
                nextdirection = findNextDirectionMedium(target);
                break;
            case HARD:
                nextdirection = findNextDirectionHard(target);
                break;
        }
    }

    @Override
    public void die() {
        this.setXPosition(tileSize);
        this.setYPosition(40*tileSize);
    }
}
