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

<<<<<<< HEAD
    public enum Difficulty{
        RANDOM,EASY,MEDIUM,HARD;
    };

    public enum Direction {
=======
    /*public enum Direction {
>>>>>>> development_2
        RIGHT, LEFT, UP, DOWN;
        public static Direction getRandomDirection(){
            Random random = new Random();
            return values()[random.nextInt(values().length)];
        }
    };*/

<<<<<<< HEAD
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
=======
    public int xPosition;
    public int yPosition;
    private int tileSize;
>>>>>>> development_2

    public Player.Direction direction;
    public Player.Direction nextdirection;

    private Texture texture;
    public Sprite sprite;
    private Pathfinder aStar;
    private GameScreen screen;
    private Difficulty difficulty;



<<<<<<< HEAD
    public Enemy(int initX, int initY, GameScreen screen, String image, Difficulty difficulty){
        this.direction = Enemy.Direction.RIGHT;
        this.nextdirection = Enemy.Direction.RIGHT;
        this.xPosition = initX;
        this.yPosition = initY;
        this.difficulty = difficulty;
=======
    public Enemy(int initX, int initY, GameScreen screen){
        this.direction = Player.Direction.DOWN;
        this.nextdirection = Player.Direction.DOWN;
        this.xPosition = initX;
        this.yPosition = initY;
>>>>>>> development_2

        this.texture = new Texture(image);
        this.sprite = new Sprite(texture,0, 0, 60,60);
        this.sprite.rotate90(true);
        this.screen = screen;
        this.tileSize = screen.map.tileSize;
    }

<<<<<<< HEAD
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
=======
    public Player.Direction findNextDirection(Player target){
        int distanceX = this.xPosition - target.getXPosition();
        int distanceY = this.yPosition - target.getYPosition();
        if((Math.abs(distanceX) < tileSize) && (Math.abs(distanceY) < tileSize)){
            target.die();
            return Player.Direction.RIGHT;
        }
        if(Math.abs(distanceX) > Math.abs(distanceY)){
            if(distanceX > 0) return Player.Direction.LEFT;
            else return Player.Direction.RIGHT;
        }
        else {
            if (distanceY > 0) return Player.Direction.DOWN;
            else return Player.Direction.UP;
        }
    }

    public void move(){
        if(xPosition >= tileSize && xPosition <= 26*tileSize){
            if(nextdirection != direction && screen.map.getTile(xPosition, yPosition, nextdirection).type != Tile.Type.WALL){
                if(xPosition == screen.map.getTile(xPosition, yPosition).x && yPosition == screen.map.getTile(xPosition, yPosition).y){
>>>>>>> development_2
                    direction = nextdirection;
                }
            }
            switch (direction) {
                case RIGHT:
<<<<<<< HEAD
                    if(getNextCell(direction).type != Tile.Type.WALL) {
=======
                    if(screen.map.getTile(xPosition, yPosition, direction).type != Tile.Type.WALL) {
>>>>>>> development_2
                        xPosition++;
                    }
                    break;
                case LEFT:
<<<<<<< HEAD
                    if(getNextCell(direction).type == Tile.Type.WALL) {
                        if(xPosition > getCell().x) xPosition--;
=======
                    if(screen.map.getTile(xPosition, yPosition, direction).type == Tile.Type.WALL) {
                        if(xPosition > screen.map.getTile(xPosition, yPosition).x) xPosition--;
>>>>>>> development_2
                    }else{
                        xPosition--;
                    }
                    break;
                case UP:
<<<<<<< HEAD
                    if(getNextCell(direction).type != Tile.Type.WALL){
=======
                    if(screen.map.getTile(xPosition, yPosition, direction).type != Tile.Type.WALL){
>>>>>>> development_2
                        yPosition++;
                    }
                    break;
                case DOWN:
<<<<<<< HEAD
                    if(getNextCell(direction).type == Tile.Type.WALL){
                        if(yPosition > getCell().y) yPosition--;
=======
                    if(screen.map.getTile(xPosition, yPosition, direction).type == Tile.Type.WALL){
                        if(yPosition > screen.map.getTile(xPosition, yPosition).y) yPosition--;
>>>>>>> development_2
                    }else{
                        yPosition--;
                    }
                    break;
            }
        }else{
<<<<<<< HEAD
            if(xPosition <= 8) xPosition = 207;
            if(xPosition >= 208) xPosition = 9;
=======
            if(xPosition <= tileSize) xPosition = (((26*tileSize)-1));
            if(xPosition >= (26*tileSize)) xPosition = tileSize+1;
>>>>>>> development_2
        }
    }
}
