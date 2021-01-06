package uas.lntv.pacmangame.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import uas.lntv.pacmangame.Scenes.Tile;
import uas.lntv.pacmangame.Screens.GameScreen;

public abstract class Actor {
    public enum Direction {
        RIGHT, LEFT, UP, DOWN
    };
    public enum State {
        RUNNING, STOPPING, EATING, DIEING
    };

    public int xPosition;
    public int yPosition;
    public int getXPosition() {
        return xPosition;
    }
    public int getYPosition() {
        return yPosition;
    }

    protected int tileSize;
    public float rotation;
    public int texturePositionX;

    public Direction direction;
    public Direction nextdirection;
    public Direction prevdirection;

    public State state;

    public Sprite sprite;
    public TextureRegion region;
    public Texture texture;
    protected GameScreen screen;

    public Actor(int initX, int initY, GameScreen screen){
        this.direction = Direction.RIGHT;
        this.nextdirection = Direction.RIGHT;
        this.prevdirection = Direction.RIGHT;
        this.state = State.RUNNING;
        this.xPosition = initX;
        this.yPosition = initY;
        this.rotation = 0;
        this.tileSize = screen.map.tileSize;
        this.screen = screen;
    }
    public void move() {
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
    public abstract void die();
}
