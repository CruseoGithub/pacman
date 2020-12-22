package com.vieth.pacman.Sprites;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.vieth.pacman.Screens.GameScreen;

public class Player extends Sprite {
    public enum Direction {
            RIGHT, LEFT, UP, DOWN
    };

    public int x;
    public int y;

    public Direction direction;
    public Direction nextdirection;

    private Texture texture;
    public Sprite sprite;
    private TiledMapTileLayer layerDots;

    private GameScreen screen;


    public Player(int initX, int initY, GameScreen screen){
        this.direction = Direction.RIGHT;
        this.nextdirection = Direction.RIGHT;

        this.x = initX;
        this.y = initY;

        this.texture = new Texture("pacman1.png");
        this.sprite = new Sprite(texture,180, 0, 64,64);

        this.screen = screen;
        layerDots = (TiledMapTileLayer)screen.map.getLayers().get("Collectables");
    }

    public void pacmanDir(String pacman, int x, int y){
        this.texture = new Texture(pacman);
        this.sprite = new Sprite(texture,x, y, 64,64);
        this.screen = screen;
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
                this.pacmanDir("pacman1.png", 180 , 0);
                nextCellX = (int) ((x+8) / 8);
                break;
            case LEFT:
                this.pacmanDir("pacman2.png", 60, 0);
                nextCellX = (int) ((x-8) / 8);
                break;
            case UP:
                this.pacmanDir("pacman3.png", 0, 0);
                nextCellY = (int) ((y+8) / 8);
                break;
            case DOWN:
                this.pacmanDir("pacman4.png", 0, 180);
                nextCellY = (int) ((y-8) / 8);
                break;

        }
        tile = screen.tileMatrix[nextCellX][nextCellY];
        return tile;
    }

    public void move(){
        if(x >= 8 && x <= 208){
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
