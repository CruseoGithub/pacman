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

        this.texture = new Texture("pacman32.png");
        region = new TextureRegion(texture);
        region.setRegionX(0);
        region.setRegionY(0);
        region.setRegionWidth(32);
        region.setRegionHeight(32);
        texturePositionX = 0;
        region.flip(true, false);
        this.sprite = new Sprite(region);
        this.sprite.setOrigin(tileSize/2, tileSize/2);
        this.screen = screen;
        this.hud = hud;
    }

    public void move(){
        //if(xPosition >= 8 && xPosition <= 208){
        if(xPosition >= tileSize && xPosition <= 26*tileSize){
            prevdirection = direction;
            if(nextdirection != direction && screen.map.getTile(xPosition, yPosition, nextdirection).type != Tile.Type.WALL){
                if(xPosition == screen.map.getTile(xPosition, yPosition).x && yPosition == screen.map.getTile(xPosition, yPosition).y){

                    direction = nextdirection;
                }
            }

            screen.map.collect(screen.map.getTile(xPosition, yPosition)); //Dots einsammeln

            switch (direction) {
                case RIGHT:
                    if(screen.map.getTile(xPosition, yPosition, direction).type != Tile.Type.WALL) {
                        if(prevdirection != direction) this.rotation = 0;
                        xPosition++;
                    }
                    break;
                case LEFT:
                    if(screen.map.getTile(xPosition, yPosition, direction).type == Tile.Type.WALL) {
                        if(xPosition > screen.map.getTile(xPosition, yPosition).x) xPosition--;
                    }else{
                        if(prevdirection != direction) this.rotation = 180;
                        xPosition--;
                    }
                    break;
                case UP:
                    if(screen.map.getTile(xPosition, yPosition, direction).type != Tile.Type.WALL){
                        if(prevdirection != direction) this.rotation = 90;
                        yPosition++;
                    }
                    break;
                case DOWN:
                    if(screen.map.getTile(xPosition, yPosition, direction).type == Tile.Type.WALL){
                        if(yPosition > screen.map.getTile(xPosition, yPosition).y) yPosition--;
                    }else{
                        if(prevdirection != direction) this.rotation =270;
                        yPosition--;
                    }
                    break;
            }
        }else{
            if(xPosition <= tileSize) xPosition = (((26*tileSize)-1));
            if(xPosition >= (26*tileSize)) xPosition = tileSize+1;
        }

    }

    public void die(){
        xPosition = tileSize;
        yPosition = 17*tileSize;
        if(hud.lives>0) hud.lives--;
        //else {};
    }
}
