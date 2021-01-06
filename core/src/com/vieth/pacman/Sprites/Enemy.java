package com.vieth.pacman.Sprites;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.vieth.pacman.Pathfinder;
import com.vieth.pacman.Screens.GameScreen;
import java.util.Random;

public class Enemy extends Sprite{

    public enum Difficulty{
        RANDOM,EASY,MEDIUM,HARD;
    };

/*  public enum Direction {
        RIGHT, LEFT, UP, DOWN;
    };*/

    private int xPosition;
    private int yPosition;

    public int getXPosition() {
        return xPosition;
    }

    public void setXPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public int getYPosition() {
        return yPosition;
    }

    public void setYPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    private int tileSize;

    public Player.Direction direction;
    public Player.Direction nextdirection;

    private Texture texture;
    public Sprite sprite;
    private Pathfinder aStar;
    private GameScreen screen;
    private Difficulty difficulty;


    public Enemy(int initX, int initY, GameScreen screen, String image, Difficulty difficulty) {
        this.direction = Player.Direction.RIGHT;
        this.nextdirection = Player.Direction.RIGHT;
        this.xPosition = initX;
        this.yPosition = initY;
        this.difficulty = difficulty;

        this.texture = new Texture(image);
        this.sprite = new Sprite(texture,0, 0, 60,60);
        this.sprite.rotate90(true);
        this.screen = screen;
        this.tileSize = screen.map.tileSize;
    }

    private Player.Direction UpOrDown(int distance){
        if (distance > 0) return Player.Direction.DOWN;
        else return Player.Direction.UP;
    }

    private Player.Direction LeftOrRight(int distance){
        if (distance > 0 ) return Player.Direction.LEFT;
        else return Player.Direction.RIGHT;
    }

    private Player.Direction findNextDirectionEasy(Player target){
        int distanceX = this.xPosition - target.getXPosition();
        int distanceY = this.yPosition - target.getYPosition();
        if((Math.abs(distanceX)+Math.abs(distanceY)) > 16*tileSize) return Player.Direction.getRandomDirection();
        if((Math.abs(distanceX) < tileSize) && (Math.abs(distanceY) < tileSize)){
            target.die(this);
            return Player.Direction.RIGHT;
        }
        if(Math.abs(distanceX) > Math.abs(distanceY)){
            return LeftOrRight(distanceX);
        }
        else {
            return UpOrDown(distanceY);
        }
    }

    private Player.Direction findNextDirectionMedium(Player target){
        int distanceX = this.xPosition - target.getXPosition();
        int distanceY = this.yPosition - target.getYPosition();
        if((Math.abs(distanceX)+Math.abs(distanceY)) > 16*tileSize ) return Player.Direction.getRandomDirection();
        if((Math.abs(distanceX) < tileSize) && (Math.abs(distanceY) < tileSize)){
            target.die(this);
            return Player.Direction.RIGHT;
        }
        if(Math.abs(distanceX) > Math.abs(distanceY)){
            if(screen.map.getTile(xPosition, yPosition, LeftOrRight(distanceX)).type != Tile.Type.WALL) return LeftOrRight(distanceX);
            else return UpOrDown(distanceY);
        }
        else {
            if(screen.map.getTile(xPosition, yPosition, UpOrDown(distanceY)).type != Tile.Type.WALL) return UpOrDown(distanceY);
            else return LeftOrRight(distanceX);
        }
    }

    private Player.Direction findNextDirectionHard(Tile[][] matrix, Player target){
        int distanceX = this.xPosition - target.getXPosition();
        int distanceY = this.yPosition - target.getYPosition();
        if((Math.abs(distanceX) < tileSize) && (Math.abs(distanceY) < tileSize)){
            target.die(this);
            return Player.Direction.RIGHT;
        }
        aStar = new Pathfinder(screen, this, target, tileSize);
        Tile temp = aStar.aStarResult();
        if(temp.getY() > this.getYPosition()) return Player.Direction.UP;
        if(temp.getY() < this.getYPosition()) return Player.Direction.DOWN;
        if(temp.getX() > this.getXPosition()) return Player.Direction.RIGHT;
        else return Player.Direction.LEFT;
    }

    public void findNextDirection(Tile[][] matrix, Player target) {
        switch (difficulty) {
            case RANDOM:
                nextdirection = Player.Direction.getRandomDirection();
                break;
            case EASY:
                nextdirection = findNextDirectionEasy(target);
                break;
            case MEDIUM:
                nextdirection = findNextDirectionMedium(target);
                break;
            case HARD:
                nextdirection = findNextDirectionHard(matrix, target);
                break;
        }
    }

    public void move(){
        if(xPosition >= tileSize && xPosition <= 26*tileSize){
            if(nextdirection != direction && screen.map.getTile(xPosition, yPosition, nextdirection).type != Tile.Type.WALL){
                if(xPosition == screen.map.getTile(xPosition, yPosition).getX() && yPosition == screen.map.getTile(xPosition, yPosition).getY()){
                    direction = nextdirection;
                }
            }
            switch (direction) {
                case RIGHT:
                    if(screen.map.getTile(xPosition, yPosition, direction).type != Tile.Type.WALL) {
                        xPosition++;
                    }
                    break;
                case LEFT:
                    if(screen.map.getTile(xPosition, yPosition, direction).type == Tile.Type.WALL) {
                        if(xPosition > screen.map.getTile(xPosition, yPosition).getX()) {
                            xPosition--;
                        }
                    }else{
                        xPosition--;
                    }
                    break;
                case UP:
                    if(screen.map.getTile(xPosition, yPosition, direction).type != Tile.Type.WALL){
                        yPosition++;
                    }
                    break;
                case DOWN:
                    if(screen.map.getTile(xPosition, yPosition, direction).type == Tile.Type.WALL){
                        if(yPosition > screen.map.getTile(xPosition, yPosition).getY()) {
                            yPosition--;
                        }
                    }else{
                        yPosition--;
                    }
                    break;
            }
        }else{
            if(xPosition <= tileSize) xPosition = (((26*tileSize)-1));
            if(xPosition >= (26*tileSize)) xPosition = tileSize+1;
        }
    }

}
