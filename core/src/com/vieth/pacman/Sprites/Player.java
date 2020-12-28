package com.vieth.pacman.Sprites;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.vieth.pacman.Controller;
import com.vieth.pacman.Screens.GameScreen;
import com.vieth.pacman.Scenes.Hud;

public class Player extends Sprite {
    public enum Direction {
            RIGHT, LEFT, UP, DOWN
    };
    public enum State {
            RUNNING, STOPPING, EATING, DIEING
    };

    private int xPosition;
    private int yPosition;


    public int getXPosition() {
        return xPosition;
    }

    public int getYPosition() {
        return yPosition;
    }

    private int tileSize;
    public float rotation;
    public int texturePositionX;

    public Direction direction;
    public Direction nextdirection;
    public Direction prevdirection;

    public State state;

    public Sprite sprite;
    public TextureRegion region;
    public Texture texture;

    private TiledMapTileLayer layerDots;
    public Hud hud;

    private GameScreen screen;


    public Player(int initX, int initY, GameScreen screen, Hud hud){
        this.direction = Direction.RIGHT;
        this.nextdirection = Direction.RIGHT;
        this.prevdirection = Direction.RIGHT;

        this.state = State.RUNNING;

        this.xPosition = initX;
        this.yPosition = initY;
        this.rotation = 0;
        this.tileSize = screen.map.tileSize;

        this.texture = new Texture("pacman.png");
        region = new TextureRegion(texture);
        region.setRegionX(0);
        region.setRegionY(0);
        region.setRegionWidth(60);
        region.setRegionHeight(60);
        texturePositionX = 0;
        region.flip(true, false);
        this.sprite = new Sprite(region);
        this.sprite.setOrigin(tileSize/2, tileSize/2);
        this.screen = screen;
        this.hud = hud;
    }

    public Tile getCell(){
        Tile tile = screen.map.matrix[(int) xPosition/tileSize][(int)yPosition/tileSize];
        return tile;
    }

    public Tile getNextCell(Direction dir){
        int nextCellX = (int) ((xPosition/tileSize));
        int nextCellY = (int) ((yPosition/tileSize));
        Tile tile;
        switch (dir) {
            case RIGHT:
                nextCellX = (int) ((xPosition+tileSize) / tileSize);
                break;
            case LEFT:
                nextCellX = (int) ((xPosition-tileSize) / tileSize);
                break;
            case UP:
                nextCellY = (int) ((yPosition+tileSize) / tileSize);
                break;
            case DOWN:
                nextCellY = (int) ((yPosition-tileSize) / tileSize);
                break;

        }
        tile = screen.map.matrix[nextCellX][nextCellY];
        return tile;
    }

    public void move(){
        if(xPosition >= 8 && xPosition <= 208){
            prevdirection = direction;
            if(nextdirection != direction && getNextCell(nextdirection).type != Tile.Type.WALL){
                if(xPosition == getCell().x && yPosition == getCell().y){

                    direction = nextdirection;
                }
            }
            if(getCell().isDot == true){
                screen.map.layerCollect.setCell(getCell().x/tileSize, getCell().y/tileSize, null); //Löscht die Celle aus der Map
                screen.hud.score++;
                screen.hud.update();
                getCell().isDot = false;
            }

            switch (direction) {
                case RIGHT:
                    if(getNextCell(direction).type != Tile.Type.WALL) {
                        if(prevdirection != direction) this.rotation = 0;
                        xPosition++;
                    }
                    break;
                case LEFT:
                    if(getNextCell(direction).type == Tile.Type.WALL) {
                        if(xPosition > getCell().x) xPosition--;
                    }else{
                        if(prevdirection != direction) this.rotation = 180;
                        xPosition--;
                    }
                    break;
                case UP:
                    if(getNextCell(direction).type != Tile.Type.WALL){
                        if(prevdirection != direction) this.rotation = 90;
                        yPosition++;
                    }
                    break;
                case DOWN:
                    if(getNextCell(direction).type == Tile.Type.WALL){
                        if(yPosition > getCell().y) yPosition--;
                    }else{
                        if(prevdirection != direction) this.rotation =270;
                        yPosition--;
                    }
                    break;
            }
        }else{
            if(xPosition <= 8) xPosition = 207;
            if(xPosition >= 208) xPosition = 9;
        }

    }

    public void die(){
        xPosition = 8;
        yPosition = 136;
        if(hud.lives>0) hud.lives--;
        //else {};
    }
}
