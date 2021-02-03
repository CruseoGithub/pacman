package uas.lntv.pacmangame.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Random;


import uas.lntv.pacmangame.Maps.Tile;
import uas.lntv.pacmangame.Scenes.Hud;
import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Screens.GameScreen;
import uas.lntv.pacmangame.Screens.MapScreen;
import uas.lntv.pacmangame.Screens.ScoreScreen;

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

    public int xPosition;
    public int yPosition;

    protected int tileSize;
    public float rotation;

    private int speed;
    public int texturePositionX;
    public int texturePositionY;
    public Direction direction;
    public Direction nextdirection;
    public Direction prevdirection;

    public State state;

    public Sprite sprite;
    public TextureRegion region;
    public Texture texture;
    protected MapScreen screen;
    private int startPosX;
    private int startPosY;

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
        return startPosX;
    }

    public int getStartPosY() {
        return startPosY;
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

    public Actor(int initX, int initY, MapScreen screen) {
        this.state = State.RUNNING;
        this.startPosX = initX;
        this.xPosition = initX;
        this.startPosY = initY;
        this.yPosition = initY;
        this.rotation = 0;
        this.speed = 4; // Values can be {0 == Stop , 1, 2, 4 == default, 8, 16}
        this.tileSize = screen.map.tileSize;
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
            prevdirection = direction;
            if (nextdirection != direction && screen.map.getTile(xPosition, yPosition, nextdirection).type != Tile.Type.WALL) {
                if (xPosition == screen.map.getTile(xPosition, yPosition).getX() && yPosition == screen.map.getTile(xPosition, yPosition).getY()) {
                    direction = nextdirection;
                }
            }

            switch (direction) {
                case RIGHT:
                    if (screen.map.getTile(xPosition, yPosition, direction).type != Tile.Type.WALL) {
                        if (prevdirection != direction && !(this instanceof Enemy)) this.rotation = 0;
                        int temp = xPosition;
                        xPosition += speed;
                        if (temp / tileSize != xPosition / tileSize) {
                            screen.map.getTile(temp, yPosition).leave(this);
                            screen.map.getTile(xPosition, yPosition).enter(this);
                        }
                    }
                    break;
                case LEFT:
                    if (screen.map.getTile(xPosition, yPosition, direction).type == Tile.Type.WALL) {
                        if (xPosition > screen.map.getTile(xPosition, yPosition).getX()) {
                            int temp = xPosition;
                            xPosition -= speed;
                            if (temp / tileSize != xPosition / tileSize) {
                                screen.map.getTile(temp, yPosition).leave(this);
                                screen.map.getTile(xPosition, yPosition).enter(this);
                            }
                        }
                    } else {
                        if (prevdirection != direction && !(this instanceof Enemy)) this.rotation = 180;
                        int temp = xPosition;
                        xPosition -= speed;
                        if (temp / tileSize != xPosition / tileSize) {
                            screen.map.getTile(temp, yPosition).leave(this);
                            screen.map.getTile(xPosition, yPosition).enter(this);
                        }
                    }
                    break;
                case UP:
                    if (screen.map.getTile(xPosition, yPosition, direction).type != Tile.Type.WALL) {
                        if (prevdirection != direction && !(this instanceof Enemy)) this.rotation = 90;
                        int temp = yPosition;
                        yPosition += speed;
                        if (temp / tileSize != yPosition / tileSize) {
                            screen.map.getTile(xPosition, temp).leave(this);
                            screen.map.getTile(xPosition, yPosition).enter(this);
                        }
                    }
                    break;
                case DOWN:
                    if (screen.map.getTile(xPosition, yPosition, direction).type == Tile.Type.WALL) {
                        if (yPosition > screen.map.getTile(xPosition, yPosition).getY()) {
                            int temp = yPosition;
                            yPosition -= speed;
                            if (temp / tileSize != yPosition / tileSize) {
                                screen.map.getTile(xPosition, temp).leave(this);
                                screen.map.getTile(xPosition, yPosition).enter(this);
                            }
                        }
                    } else {
                        if (prevdirection != direction && !(this instanceof Enemy)) this.rotation = 270;
                        int temp = yPosition;
                        yPosition -= speed;
                        if (temp / tileSize != yPosition / tileSize) {
                            screen.map.getTile(xPosition, temp).leave(this);
                            screen.map.getTile(xPosition, yPosition).enter(this);
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