package com.vieth.pacman.Sprites;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.vieth.pacman.Screens.GameScreen;

public class Player extends Sprite {
    public enum Direction {
            RIGHT, LEFT, UP, DOWN
    };

    public int x;
    public int y;

    public float rotation;
    public int texturePositionX;
    public Direction direction;
    public Direction nextdirection;
    public Direction prevdirection;

    public TextureRegion region;

    public Texture texture;
    public Sprite sprite;
    private TiledMapTileLayer layerDots;

    private GameScreen screen;


    public Player(int initX, int initY, GameScreen screen){
        this.direction = Direction.RIGHT;
        this.nextdirection = Direction.RIGHT;
        this.prevdirection = Direction.RIGHT;

        this.x = initX;
        this.y = initY;
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
        layerDots = (TiledMapTileLayer)screen.map.getLayers().get("Collectables");
    }

    public Tile getCell(){
        Tile tile = screen.tileMatrix[(int) x/8][(int)y/8];
        return tile;
    }
    public Tile getNextCell(Direction dir){
        int nextCellX = (int) ((x/8));
        int nextCellY = (int) ((y/8));
        Tile tile;
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
        tile = screen.tileMatrix[nextCellX][nextCellY];
        return tile;
    }

    public void move(){
        if(x >= 8 && x <= 208){
            prevdirection = direction;
            if(nextdirection != direction && getNextCell(nextdirection).type != Tile.Type.WALL){
                if(x == getCell().x && y == getCell().y){

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
                        x++;
                    }
                    break;
                case LEFT:
                    if(getNextCell(direction).type == Tile.Type.WALL) {
                        if(x > getCell().x) x--;
                    }else{
                        if(prevdirection != direction) {
                            this.rotation = 180;
                        }
                        x--;
                    }
                    break;
                case UP:
                    if(getNextCell(direction).type != Tile.Type.WALL){
                        if(prevdirection != direction) this.rotation = 90;
                        y++;
                    }
                    break;
                case DOWN:
                    if(getNextCell(direction).type == Tile.Type.WALL){
                        if(y > getCell().y) y--;
                    }else{
                        if(prevdirection != direction) this.rotation =270;
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
