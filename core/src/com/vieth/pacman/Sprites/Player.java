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

    private int xPosition;
    private int yPosition;


    public int getXPosition() {
        return xPosition;
    }

    public int getYPosition() {
        return yPosition;
    }


    public float rotation;
    public int texturePositionX;
    public Direction direction;
    public Direction nextdirection;
    public Direction prevdirection;

    public TextureRegion region;

    public Texture texture;
    public Sprite sprite;
    private TiledMapTileLayer layerDots;
    public Hud hud;

    private GameScreen screen;


    public Player(int initX, int initY, GameScreen screen, Hud hud){
        this.direction = Direction.RIGHT;
        this.nextdirection = Direction.RIGHT;
        this.prevdirection = Direction.RIGHT;

        this.xPosition = initX;
        this.yPosition = initY;
        this.rotation = 0;

        this.texture = new Texture("pacman.png");
        region = new TextureRegion(texture);
        region.setRegionX(0);
        region.setRegionY(0);
        region.setRegionWidth(60);
        region.setRegionHeight(60);
        texturePositionX = 0;
        region.flip(true, false);
        this.sprite = new Sprite(region);
        this.sprite.setOrigin(4, 4);
        this.screen = screen;
        this.hud = hud;
        layerDots = (TiledMapTileLayer)screen.map.getLayers().get("Collectables");
    }

    public Tile getCell(){
        Tile tile = screen.tileMatrix[(int) xPosition/8][(int)yPosition/8];
        return tile;
    }

    public Tile getNextCell(Direction dir){
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
            prevdirection = direction;
            if(nextdirection != direction && getNextCell(nextdirection).type != Tile.Type.WALL){
                if(xPosition == getCell().x && yPosition == getCell().y){

                    direction = nextdirection;
                }
            }
            if(getCell().isDot == true){
                layerDots.setCell(getCell().x/8, getCell().y/8, null); //Löscht die Celle aus der Map
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
                        if(prevdirection != direction) {
                            this.rotation = 180;
                        }
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
        hud.lives--;
    }
}
