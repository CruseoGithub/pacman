package com.vieth.pacman.Sprites;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.vieth.pacman.Screens.GameScreen;
import java.util.Random;

public class Enemy extends Sprite{

    /*public enum Direction {
        RIGHT, LEFT, UP, DOWN;
        public static Direction getRandomDirection(){
            Random random = new Random();
            return values()[random.nextInt(values().length)];
        }
    };*/

    public int xPosition;
    public int yPosition;
    private int tileSize;

    public Player.Direction direction;
    public Player.Direction nextdirection;

    private Texture texture;
    public Sprite sprite;

    private GameScreen screen;


    public Enemy(int initX, int initY, GameScreen screen){
        this.direction = Player.Direction.DOWN;
        this.nextdirection = Player.Direction.DOWN;
        this.xPosition = initX;
        this.yPosition = initY;

        this.texture = new Texture("enemies.png");
        this.sprite = new Sprite(texture,0, 0, 200,200);
        this.sprite.rotate90(true);
        this.screen = screen;
        this.tileSize = screen.map.tileSize;
    }

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
                        if(xPosition > screen.map.getTile(xPosition, yPosition).x) xPosition--;
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
                        if(yPosition > screen.map.getTile(xPosition, yPosition).y) yPosition--;
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
