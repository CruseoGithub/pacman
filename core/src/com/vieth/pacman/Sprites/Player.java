package com.vieth.pacman.Sprites;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.vieth.pacman.Controller;
import com.vieth.pacman.Screens.GameScreen;

public class Player extends Sprite {
    public enum Direction {
            RIGHT, LEFT, UP, DOWN
    };

    public int x;
    public int y;

    public Direction direction;
    public Direction nextdirection;
    public Sprite sprite;
    Texture texture;
    GameScreen screen;

    private TiledMapTileLayer layer;


    public Player(int initX, int initY, GameScreen screen){
        this.direction = Direction.RIGHT;
        this.nextdirection = Direction.RIGHT;
        this.x = initX;
        this.y = initY;
        this.texture = new Texture("pacman.png");
        this.sprite = new Sprite(texture,0, 0, 64,64);
        this.screen = screen;
        this.layer = (TiledMapTileLayer)screen.map.getLayers().get(0);
    }
    public TiledMapTileLayer.Cell getCell(){
        TiledMapTileLayer.Cell cell = layer.getCell((int) x / 8, (int)y/8);
        return cell;
    }
    public TiledMapTileLayer.Cell getNextCell(Direction dir){
        int nextCellX = (int)x/8;
        int nextCellY = (int)y/8;
        TiledMapTileLayer.Cell cell;
        switch (dir) {
            case RIGHT:
                nextCellX = (int) ((x+8) / 8);
                break;
            case LEFT:
                nextCellX = (int) ((x-8) / 8);
                break;
            case UP:
                nextCellY = (int) ((y+8) / 8);
                break;
            case DOWN:
                nextCellY = (int) ((y-8) / 8);
                break;

        }
        return layer.getCell(nextCellX, nextCellY);
    }
    public int getNextCellX(Direction dir){
        int nextCellX = (int)x/8;
        TiledMapTileLayer.Cell cell;
        switch (dir) {
            case RIGHT:
                nextCellX = (int) ((x+8) / 8);
                break;
            case LEFT:
                nextCellX = (int) ((x-8) / 8);
                break;
        }
        return nextCellX;
    }
    public int getNextCellY(Direction dir){
        int nextCellY = (int)y/8;
        TiledMapTileLayer.Cell cell;
        switch (dir) {
            case UP:
                nextCellY = (int) ((y+8) / 8);
                break;
            case DOWN:
                nextCellY = (int) ((y-8) / 8);
                break;

        }
        return nextCellY;
    }

    public void move(){
        if(getNextCell(nextdirection) == null) direction = nextdirection;
        switch (direction) {
            case RIGHT:
                if(getNextCell(direction) == null) x++;
                break;
            case LEFT:
                if(getNextCell(direction) == null) x--;
                else if(x+4 > getNextCellX(direction)+12) x--;
                break;
            case UP:
                if(getNextCell(direction) == null) y++;
                break;
            case DOWN:
                if(getNextCell(direction) == null) y--;
                else if(y > getNextCellY(direction)+8) y--;
                break;
        }
    }

}
