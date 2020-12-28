package com.vieth.pacman.Sprites;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.vieth.pacman.Screens.GameScreen;
import java.util.Random;

public class Enemy extends Sprite{

    public enum Direction {
        RIGHT, LEFT, UP, DOWN;
        public static Direction getRandomDirection(){
            Random random = new Random();
            return values()[random.nextInt(values().length)];
        }
    };

    public int x;
    public int y;
    private int tileSize;

    public Enemy.Direction direction;
    public Enemy.Direction nextdirection;

    private Texture texture;
    public Sprite sprite;

    private GameScreen screen;


    public Enemy(int initX, int initY, GameScreen screen){
        this.direction = Enemy.Direction.RIGHT;
        this.nextdirection = Enemy.Direction.RIGHT;
        this.x = initX;
        this.y = initY;

        this.texture = new Texture("enemies.png");
        this.sprite = new Sprite(texture,0, 0, 200,200);
        this.sprite.rotate90(true);
        this.screen = screen;
        this.tileSize = screen.map.tileSize;
    }

    public Direction findNextDirection(Player target){
        int distanceX = this.x - target.getXPosition();
        int distanceY = this.y - target.getYPosition();
        if((Math.abs(distanceX) < tileSize) && (Math.abs(distanceY) < tileSize)){
            target.die();
            return Direction.RIGHT;
        }
        if(Math.abs(distanceX) > Math.abs(distanceY)){
            if(distanceX > 0) return Direction.LEFT;
            else return Direction.RIGHT;
        }
        else {
            if (distanceY > 0) return Direction.DOWN;
            else return Direction.UP;
        }
    }

    public Tile getCell(){
        Tile tile = screen.map.matrix[(int) x/tileSize][(int)y/tileSize];
        return tile;
    }

    public Tile getNextCell(Enemy.Direction dir){
        int nextCellX = (int) ((x/tileSize));
        int nextCellY = (int) ((y/tileSize));
        Tile tile;
        switch (dir) {
            case RIGHT:
                nextCellX = (int) ((x+tileSize) / tileSize);
                break;
            case LEFT:
                nextCellX = (int) ((x-tileSize) / tileSize);
                break;
            case UP:
                nextCellY = (int) ((y+tileSize) / tileSize);
                break;
            case DOWN:
                nextCellY = (int) ((y-tileSize) / tileSize);
                break;

        }
        tile = screen.map.matrix[nextCellX][nextCellY];
        return tile;
    }

    public void move(){
        if(x >= 8 && x <= 208){
            if(nextdirection != direction && getNextCell(nextdirection).type != Tile.Type.WALL){
                if(x == getCell().x && y == getCell().y){
                    direction = nextdirection;
                }
            }

            switch (direction) {
                case RIGHT:
                    if(getNextCell(direction).type != Tile.Type.WALL) {
                        x++;
                    }
                    break;
                case LEFT:
                    if(getNextCell(direction).type == Tile.Type.WALL) {
                        if(x > getCell().x) x--;
                    }else{
                        x--;
                    }
                    break;
                case UP:
                    if(getNextCell(direction).type != Tile.Type.WALL){
                        y++;
                    }
                    break;
                case DOWN:
                    if(getNextCell(direction).type == Tile.Type.WALL){
                        if(y > getCell().y) y--;
                    }else{
                        y--;
                    }
                    break;
            }
        }else{
            if(x<=8) x=207;
            if(x>=208) x=9;
        }

    }

}
