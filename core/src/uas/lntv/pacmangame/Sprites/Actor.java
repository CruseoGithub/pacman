package uas.lntv.pacmangame.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Random;

<<<<<<< HEAD:core/src/com/vieth/pacman/Sprites/Player.java
public class Player extends Sprite {
    public enum Direction {
            RIGHT, LEFT, UP, DOWN;
            public static Direction getRandomDirection(){
                Random random = new Random();
                return values()[random.nextInt(values().length)];
            }
=======
import uas.lntv.pacmangame.Maps.Tile;
import uas.lntv.pacmangame.Screens.GameScreen;
import uas.lntv.pacmangame.Screens.MapScreen;

public abstract class Actor {
    public enum Direction {
        RIGHT, LEFT, UP, DOWN;
        public static Direction getRandomDirection(){
            Random random = new Random();
            return values()[random.nextInt(values().length)];
        }
>>>>>>> development_2:core/src/uas/lntv/pacmangame/Sprites/Actor.java
    };
    public enum State {
        RUNNING, STOPPING, EATING, DIEING
    };

    public int xPosition;
    public int yPosition;

    protected int tileSize;
    public float rotation;

    private int speed;
    public int texturePositionX;

    public Direction direction;
    public Direction nextdirection;
    public Direction prevdirection;

    public State state;

    public Sprite sprite;
    public TextureRegion region;
    public Texture texture;
    protected MapScreen screen;

    public int getSpeed() { return speed; }
    public void setSpeed(int speed) { this.speed = speed; }
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

    public Actor(int initX, int initY, MapScreen screen){
        this.direction = Direction.RIGHT;
        this.nextdirection = Direction.RIGHT;
        this.prevdirection = Direction.RIGHT;
        this.state = State.RUNNING;
        this.xPosition = initX;
        this.yPosition = initY;
        this.rotation = 0;
        this.speed = 2; // Values can be {0 == Stop , 1, 2 == Default, 4, 8, 16}
        this.tileSize = screen.map.tileSize;
        this.screen = screen;
    }

<<<<<<< HEAD:core/src/com/vieth/pacman/Sprites/Player.java
    public void move(){
=======
    public void move() {
        //if(xPosition >= 8 && xPosition <= 208){
>>>>>>> development_2:core/src/uas/lntv/pacmangame/Sprites/Actor.java
        if(xPosition >= tileSize && xPosition <= 26*tileSize){
            prevdirection = direction;
            if(nextdirection != direction && screen.map.getTile(xPosition, yPosition, nextdirection).type != Tile.Type.WALL){
                if(xPosition == screen.map.getTile(xPosition, yPosition).getX() && yPosition == screen.map.getTile(xPosition, yPosition).getY()){
<<<<<<< HEAD:core/src/com/vieth/pacman/Sprites/Player.java
=======

>>>>>>> development_2:core/src/uas/lntv/pacmangame/Sprites/Actor.java
                    direction = nextdirection;
                }
            }

            screen.map.collect(screen.map.getTile(xPosition, yPosition)); //Dots einsammeln

            switch (direction) {
                case RIGHT:
                    if(screen.map.getTile(xPosition, yPosition, direction).type != Tile.Type.WALL) {
                        if(prevdirection != direction) this.rotation = 0;
                        xPosition+=speed;
                    }
                    break;
                case LEFT:
                    if(screen.map.getTile(xPosition, yPosition, direction).type == Tile.Type.WALL) {
<<<<<<< HEAD:core/src/com/vieth/pacman/Sprites/Player.java
                        if(xPosition > screen.map.getTile(xPosition, yPosition).getX()) {
                            xPosition--;
                        }
=======
                        if(xPosition > screen.map.getTile(xPosition, yPosition).getX()) xPosition-=speed;
>>>>>>> development_2:core/src/uas/lntv/pacmangame/Sprites/Actor.java
                    }else{
                        if(prevdirection != direction) this.rotation = 180;
                        xPosition-=speed;
                    }
                    break;
                case UP:
                    if(screen.map.getTile(xPosition, yPosition, direction).type != Tile.Type.WALL){
                        if(prevdirection != direction) this.rotation = 90;
                        yPosition+=speed;
                    }
                    break;
                case DOWN:
                    if(screen.map.getTile(xPosition, yPosition, direction).type == Tile.Type.WALL){
<<<<<<< HEAD:core/src/com/vieth/pacman/Sprites/Player.java
                        if(yPosition > screen.map.getTile(xPosition, yPosition).getY()) {
                            yPosition--;
                        }
                    }else{
                        if(prevdirection != direction) this.rotation = 270;
                        yPosition--;
=======
                        if(yPosition > screen.map.getTile(xPosition, yPosition).getY()) yPosition-=speed;
                    }else{
                        if(prevdirection != direction) this.rotation =270;
                        yPosition-=speed;
>>>>>>> development_2:core/src/uas/lntv/pacmangame/Sprites/Actor.java
                    }
                    break;
            }
        }else{
<<<<<<< HEAD:core/src/com/vieth/pacman/Sprites/Player.java
            if(xPosition < tileSize) xPosition = (((26*tileSize)-1));
            if(xPosition > (26*tileSize)) xPosition = tileSize+1;
        }

    }

    public void die(Enemy killer){
        xPosition = tileSize;
        yPosition = 17*tileSize;
        killer.setXPosition(tileSize);
        killer.setYPosition(40*tileSize);
        if(hud.lives>0) hud.lives--;
        //else {};
=======
            if(xPosition <= tileSize) xPosition = (((26*tileSize)-speed));
            if(xPosition >= (26*tileSize)) xPosition = tileSize;
        }
>>>>>>> development_2:core/src/uas/lntv/pacmangame/Sprites/Actor.java
    }
    public abstract void die();
}
