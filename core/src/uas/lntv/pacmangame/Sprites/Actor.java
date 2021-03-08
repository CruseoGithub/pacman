package uas.lntv.pacmangame.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Random;


import uas.lntv.pacmangame.Maps.Tile;
import uas.lntv.pacmangame.Screens.GameScreen;
import uas.lntv.pacmangame.Screens.MapScreen;

public abstract class Actor {
    public enum Direction {
        RIGHT, LEFT, UP, DOWN;

        public static Direction getRandomDirection() {
            Random random = new Random();
            return values()[random.nextInt(values().length)];
        }
    };

    public enum State {
        RUNNING, STOPPING, EATING, DIEING, KILLED
    };

    protected int xPosition;
    private final int START_POS_X;
    protected int yPosition;
    private final int START_POS_Y;

    protected final int tileSize;
    public float rotation;

    private int speed;
    protected int texturePositionX;
    protected int texturePositionY;
    protected Direction direction;
    protected Direction nextDirection;
    protected Direction prevDirection;

    protected State state;

    public Sprite sprite;
    protected TextureRegion region;
    public Texture texture;
    protected MapScreen screen;

    public Animation animation;
    public float animationSpeed;
    boolean mouthOpen;

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getStartPosX() {
        return START_POS_X;
    }

    public int getStartPosY() {
        return START_POS_Y;
    }

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

    public int getTexturePositionX(){ return this.texturePositionX; }

    public int getTexturePositionY(){ return this.texturePositionY; }

    public Direction getNextDirection() { return nextDirection; }

    public Direction getDirection() { return direction; }

    public Direction getPrevDirection() { return prevDirection; }

    public void setNextDirection(Direction nextDirection) {
        this.nextDirection = nextDirection;
    }

    public State getState(){ return this.state; }

    public void setState(State state){ this.state = state; }

    public Actor(int initX, int initY, MapScreen screen) {
        this.state = State.RUNNING;
        this.START_POS_X = initX;
        this.xPosition = initX;
        this.START_POS_Y = initY;
        this.yPosition = initY;
        this.rotation = 0;
        this.speed = 2; // Values can be {0 == Stop , 1, 2 == default, 4, 8, 16}
        this.tileSize = screen.map.getTileSize();
        this.screen = screen;
    }

    public void correctPosition(Direction now) {
        switch (now) {
            case UP:
                if (yPosition % (2 * speed) != 0) yPosition += speed;
                break;
            case DOWN:
                if (yPosition % (2 * speed) != 0) yPosition -= speed;
                break;
            case LEFT:
                if (xPosition % (2 * speed) != 0) xPosition -= speed;
                break;
            case RIGHT:
                if (xPosition % (2 * speed) != 0) xPosition += speed;
                break;
        }
    }

    public void move() {
        if (xPosition >= tileSize && xPosition <= 26 * tileSize && yPosition >= 15 * tileSize && yPosition <= 44 * tileSize || !(screen instanceof GameScreen)) {
            prevDirection = direction;
            if (nextDirection != direction && screen.map.getTile(xPosition, yPosition, nextDirection).type != Tile.Type.WALL) {
                if (xPosition == screen.map.getTile(xPosition, yPosition).getX() && yPosition == screen.map.getTile(xPosition, yPosition).getY()) {
                    direction = nextDirection;
                }
            }

            Tile tempTile = screen.map.getTile(xPosition, yPosition, direction);
            switch (direction) {
                case RIGHT:
                    if (tempTile.type != Tile.Type.WALL) {
                        if(!(this instanceof Enemy) || !(tempTile.isOccupiedByGhost())){
                            if (prevDirection != direction && !(this instanceof Enemy))
                                this.rotation = 0;
                            int temp = xPosition;
                            xPosition += speed;
                            if (temp / tileSize != xPosition / tileSize && this.state != State.KILLED) {
                                screen.map.getTile(temp, yPosition).leave(this);
                                screen.map.getTile(xPosition, yPosition).enter(this);
                            }
                        }
                    }
                    break;
                case LEFT:
                    if (tempTile.type == Tile.Type.WALL) {
                        if(!(this instanceof Enemy) || !(tempTile.isOccupiedByGhost())) {
                            if (xPosition > screen.map.getTile(xPosition, yPosition).getX()) {
                                int temp = xPosition;
                                xPosition -= speed;
                                if (temp / tileSize != xPosition / tileSize && this.state != State.KILLED) {
                                    screen.map.getTile(temp, yPosition).leave(this);
                                    screen.map.getTile(xPosition, yPosition).enter(this);
                                }
                            }
                        }
                    } else {
                        if(!(this instanceof Enemy) || !(tempTile.isOccupiedByGhost())) {
                            if (prevDirection != direction && !(this instanceof Enemy))
                                this.rotation = 180;
                            int temp = xPosition;
                            xPosition -= speed;
                            if (temp / tileSize != xPosition / tileSize && this.state != State.KILLED) {
                                screen.map.getTile(temp, yPosition).leave(this);
                                screen.map.getTile(xPosition, yPosition).enter(this);
                            }
                        }
                    }
                    break;
                case UP:
                    if (tempTile.type != Tile.Type.WALL) {
                        if(!(this instanceof Enemy) || !(tempTile.isOccupiedByGhost())) {
                            if (prevDirection != direction && !(this instanceof Enemy))
                                this.rotation = 90;
                            int temp = yPosition;
                            yPosition += speed;
                            if (temp / tileSize != yPosition / tileSize && this.state != State.KILLED) {
                                screen.map.getTile(xPosition, temp).leave(this);
                                screen.map.getTile(xPosition, yPosition).enter(this);
                            }
                        }
                    }
                    break;
                case DOWN:
                    if (tempTile.type == Tile.Type.WALL) {
                        if(!(this instanceof Enemy) || !(tempTile.isOccupiedByGhost())) {
                            if (yPosition > screen.map.getTile(xPosition, yPosition).getY()) {
                                int temp = yPosition;
                                yPosition -= speed;
                                if (temp / tileSize != yPosition / tileSize && this.state != State.KILLED) {
                                    screen.map.getTile(xPosition, temp).leave(this);
                                    screen.map.getTile(xPosition, yPosition).enter(this);
                                }
                            }
                        }
                    } else {
                        if(!(this instanceof Enemy) || !(tempTile.isOccupiedByGhost())) {
                            if (prevDirection != direction && !(this instanceof Enemy))
                                this.rotation = 270;
                            int temp = yPosition;
                            yPosition -= speed;
                            if (temp / tileSize != yPosition / tileSize && this.state != State.KILLED) {
                                screen.map.getTile(xPosition, temp).leave(this);
                                screen.map.getTile(xPosition, yPosition).enter(this);
                            }
                        }
                    }
                    break;
            }
        } else if (screen instanceof GameScreen) {
            if (xPosition < tileSize) {
                int temp = xPosition;
                xPosition = 26 * tileSize - speed;
                screen.map.getTile(temp, yPosition).leave(this);
                screen.map.getTile(xPosition, yPosition).enter(this);
            }
            if (xPosition > 26 * tileSize) {
                int temp = xPosition;
                xPosition = tileSize + speed;
                screen.map.getTile(temp, yPosition).leave(this);
                screen.map.getTile(xPosition, yPosition).enter(this);
            }
            if (yPosition < 15 * tileSize) {
                int temp = yPosition;
                yPosition = 44 * tileSize - speed;
                screen.map.getTile(xPosition, temp).leave(this);
                screen.map.getTile(xPosition, yPosition).enter(this);
            }
            if (yPosition > 44 * tileSize) {
                int temp = yPosition;
                yPosition = 15 * tileSize + speed;
                screen.map.getTile(xPosition, temp).leave(this);
                screen.map.getTile(xPosition, yPosition).enter(this);
            }
        }
    }

    public void collide() {
        this.state = State.DIEING;
    }

    public void update(float dt){ animation.update(dt); }

}