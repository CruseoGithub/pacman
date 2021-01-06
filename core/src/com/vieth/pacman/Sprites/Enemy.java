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

    public enum Direction {
        RIGHT, LEFT, UP, DOWN;
        public static Direction getRandomDirection(){
            Random random = new Random();
            return values()[random.nextInt(values().length)];
        }
    };

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

    private int xPosition;
    private int yPosition;

    public Enemy.Direction direction;
    public Enemy.Direction nextdirection;

    private Texture texture;
    public Sprite sprite;
    private Pathfinder aStar;
    private GameScreen screen;
    private Difficulty difficulty;



    public Enemy(int initX, int initY, GameScreen screen, String image, Difficulty difficulty){
        this.direction = Enemy.Direction.RIGHT;
        this.nextdirection = Enemy.Direction.RIGHT;
        this.xPosition = initX;
        this.yPosition = initY;
        this.difficulty = difficulty;

        this.texture = new Texture(image);
        this.sprite = new Sprite(texture,0, 0, 60,60);
        this.sprite.rotate90(true);
        this.screen = screen;
    }

    private Direction UpOrDown(int distance){
        if (distance > 0) return Direction.DOWN;
        else return Direction.UP;
    }

    private Direction LeftOrRight(int distance){
        if (distance > 0 ) return Direction.LEFT;
        else return Direction.RIGHT;
    }

    private Direction findNextDirectionEasy(Player target){
        int distanceX = this.xPosition - target.getXPosition();
        int distanceY = this.yPosition - target.getYPosition();
        if((Math.abs(distanceX)+Math.abs(distanceY)) > 128) return Direction.getRandomDirection();
        if((Math.abs(distanceX) < 8) && (Math.abs(distanceY) < 8)){
            target.die(this);
            return Direction.RIGHT;
        }
        if(Math.abs(distanceX) > Math.abs(distanceY)){
            return LeftOrRight(distanceX);
        }
        else {
            return UpOrDown(distanceY);
        }
    }

    private Direction findNextDirectionMedium(Player target){
        int distanceX = this.xPosition - target.getXPosition();
        int distanceY = this.yPosition - target.getYPosition();
        if((Math.abs(distanceX)+Math.abs(distanceY)) > 128) return Direction.getRandomDirection();
        if((Math.abs(distanceX) < 8) && (Math.abs(distanceY) < 8)){
            target.die(this);
            return Direction.RIGHT;
        }
        if(Math.abs(distanceX) > Math.abs(distanceY)){
            if(getNextCell(LeftOrRight(distanceX)).type != Tile.Type.WALL) return LeftOrRight(distanceX);
            else return UpOrDown(distanceY);
        }
        else {
            if(getNextCell(UpOrDown(distanceY)).type != Tile.Type.WALL) return UpOrDown(distanceY);
            else return LeftOrRight(distanceX);
        }
    }

    private Direction findNextDirectionHard(Tile[][] matrix, Player target){
        int distanceX = this.xPosition - target.getXPosition();
        int distanceY = this.yPosition - target.getYPosition();
        if((Math.abs(distanceX) < 8) && (Math.abs(distanceY) < 8)){
            target.die(this);
            return Direction.RIGHT;
        }
        aStar = new Pathfinder(matrix, this, target);
        Tile temp = aStar.aStarResult();
        if(temp.getY() > this.getYPosition()) return Direction.UP;
        if(temp.getY() < this.getYPosition()) return Direction.DOWN;
        if(temp.getX() > this.getXPosition()) return Direction.RIGHT;
        else return Direction.LEFT;
    }

    public void findNextDirection(Tile[][] matrix, Player target) {
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
                nextdirection = findNextDirectionHard(matrix, target);
                nextdirection = findNextDirectionHard(matrix, target);
                break;
        }
    }

    public Tile getCell(){
        Tile tile = screen.tileMatrix[(int) xPosition/8][(int)yPosition/8];
        return tile;
    }

    public Tile getNextCell(Enemy.Direction dir){
        int nextCellX = (int) ((xPosition/8));
        int nextCellY = (int) ((yPosition/8));
        Tile tile;
        switch (dir) {
            case RIGHT:
                nextCellX = (int) ((xPosition+8) / 8);
                break;
            case LEFT:
                nextCellX = (int) ((xPosition-8) / 8);
                break;
            case UP:
                nextCellY = (int) ((yPosition+8) / 8);
                break;
            case DOWN:
                nextCellY = (int) ((yPosition-8) / 8);
                break;

        }
        tile = screen.tileMatrix[nextCellX][nextCellY];
        return tile;
    }

    public void move(){
        if(xPosition >= 8 && xPosition <= 208){
            if(nextdirection != direction && getNextCell(nextdirection).type != Tile.Type.WALL){
                if(xPosition == getCell().x && yPosition == getCell().y){
                    direction = nextdirection;
                }
            }
            switch (direction) {
                case RIGHT:
                    if(getNextCell(direction).type != Tile.Type.WALL) {
                        xPosition++;
                    }
                    break;
                case LEFT:
                    if(getNextCell(direction).type == Tile.Type.WALL) {
                        if(xPosition > getCell().x) xPosition--;
                    }else{
                        xPosition--;
                    }
                    break;
                case UP:
                    if(getNextCell(direction).type != Tile.Type.WALL){
                        yPosition++;
                    }
                    break;
                case DOWN:
                    if(getNextCell(direction).type == Tile.Type.WALL){
                        if(yPosition > getCell().y) yPosition--;
                    }else{
                        yPosition--;
                    }
                    break;
            }
        }else{
            if(xPosition <= 8) xPosition = 207;
            if(xPosition >= 208) xPosition = 9;
        }
    }
}
