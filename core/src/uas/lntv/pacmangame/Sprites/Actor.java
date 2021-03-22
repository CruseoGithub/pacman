package uas.lntv.pacmangame.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Random;

import uas.lntv.pacmangame.Managers.Assets;
import uas.lntv.pacmangame.Maps.Tile;
import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Screens.GameScreen;
import uas.lntv.pacmangame.Screens.MapScreen;

public abstract class Actor {
    public enum Direction {
        RIGHT, LEFT, UP, DOWN;

        public static Direction getRandomDirection() {
            Random random = new Random();
            return values()[random.nextInt(values().length)];
        }
    }

    public enum State {
        RUNNING, DIEING, HOMING, BOXED
    }

    protected Assets assets;

    protected int xPosition;
    protected int yPosition;
    protected int homeX;
    protected int homeY;

    protected final int TILE_SIZE;
    protected float rotation;

    protected int speed;
    protected int texturePositionX;
    protected int texturePositionY;
    protected Direction direction;
    protected Direction nextDirection;
    protected Direction prevDirection;

    protected State state;

    protected Sprite sprite;
    protected TextureRegion region;
    protected Texture texture;
    protected MapScreen screen;

    protected Animation animation;
    protected float animationSpeed;
    protected boolean mouthOpen;

    public Animation getAnimation(){ return this.animation; }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getHomeX(){ return homeX; }

    public int getHomeY(){ return homeY; }

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

    public void setTexture(Texture texture){ this.texture = texture; }

    public void resetTexturePosition() {
        this.texturePositionX = 0;
        this.texturePositionY = 0;
    }

    public Direction getDirection() { return direction; }

    public void setNextDirection(Direction nextDirection) {
        this.nextDirection = nextDirection;
    }

    public void setRotation(int rotation) { this.rotation = rotation; }

    public State getState(){ return this.state; }

    public void setState(State state){ this.state = state; }

    public Actor(Assets assets, int initX, int initY, MapScreen screen) {
        this.assets = assets;
        this.state = State.RUNNING;
        this.xPosition = initX;
        this.yPosition = initY;
        this.rotation = 0;
        this.speed = 2; // Values can be {0 == Stop , 1, 2 == default, 4, 8, 16}
        this.TILE_SIZE = screen.map.getTileSize();
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
        if (xPosition >= TILE_SIZE
                && xPosition <= 26 * TILE_SIZE
                && yPosition >= 15 * TILE_SIZE
                && yPosition <= 44 * TILE_SIZE
                || !(screen instanceof GameScreen)
        ){
            prevDirection = direction;
            if (nextDirection != direction
                    && screen.map.getTile(xPosition, yPosition, nextDirection).type != Tile.Type.WALL
                    && !( this instanceof Enemy && screen.map.getTile(xPosition, yPosition, nextDirection).isOccupiedByGhost() )
            ) {
                if (xPosition == screen.map.getTile(xPosition, yPosition).getX() && yPosition == screen.map.getTile(xPosition, yPosition).getY()) {
                    direction = nextDirection;
                }
            }

            Tile tempTile = screen.map.getTile(xPosition, yPosition, direction);
            switch (direction) {
                case RIGHT:
                    if (tempTile.type != Tile.Type.WALL) {
                        if (prevDirection != direction && !(this instanceof Enemy))
                            this.rotation = 0;
                        int temp = xPosition;
                        xPosition += speed;
                        if (temp / TILE_SIZE != xPosition / TILE_SIZE && this.state != State.HOMING) {
                            screen.map.getTile(temp, yPosition).leave(this);
                            screen.map.getTile(xPosition, yPosition).enter(this);
                        }
                    }
                    break;
                case LEFT:
                    if (tempTile.type == Tile.Type.WALL) {
                        if (xPosition > screen.map.getTile(xPosition, yPosition).getX()) {
                            int temp = xPosition;
                            xPosition -= speed;
                            if (temp / TILE_SIZE != xPosition / TILE_SIZE && this.state != State.HOMING) {
                                screen.map.getTile(temp, yPosition).leave(this);
                                screen.map.getTile(xPosition, yPosition).enter(this);
                            }
                        }
                    } else {
                        if (prevDirection != direction && !(this instanceof Enemy))
                            this.rotation = 180;
                        int temp = xPosition;
                        xPosition -= speed;
                        if (temp / TILE_SIZE != xPosition / TILE_SIZE && this.state != State.HOMING) {
                            screen.map.getTile(temp, yPosition).leave(this);
                            screen.map.getTile(xPosition, yPosition).enter(this);
                        }
                    }
                    break;
                case UP:
                    if (tempTile.type != Tile.Type.WALL) {
                        if (prevDirection != direction && !(this instanceof Enemy))
                            this.rotation = 90;
                        int temp = yPosition;
                        yPosition += speed;
                        if (temp / TILE_SIZE != yPosition / TILE_SIZE && this.state != State.HOMING) {
                            screen.map.getTile(xPosition, temp).leave(this);
                            screen.map.getTile(xPosition, yPosition).enter(this);
                        }
                    }
                    break;
                case DOWN:
                    if (tempTile.type == Tile.Type.WALL) {
                        if (yPosition > screen.map.getTile(xPosition, yPosition).getY()) {
                            int temp = yPosition;
                            yPosition -= speed;
                            if (temp / TILE_SIZE != yPosition / TILE_SIZE && this.state != State.HOMING) {
                                screen.map.getTile(xPosition, temp).leave(this);
                                screen.map.getTile(xPosition, yPosition).enter(this);
                            }
                        }
                    } else {
                        if (prevDirection != direction && !(this instanceof Enemy))
                            this.rotation = 270;
                        int temp = yPosition;
                        yPosition -= speed;
                        if (temp / TILE_SIZE != yPosition / TILE_SIZE && this.state != State.HOMING) {
                            screen.map.getTile(xPosition, temp).leave(this);
                            screen.map.getTile(xPosition, yPosition).enter(this);
                        }
                    }
                    break;
            }
        } else {
            if (xPosition < TILE_SIZE) {
                int temp = xPosition;
                xPosition = 26 * TILE_SIZE - speed;
                screen.map.getTile(temp, yPosition).leave(this);
                screen.map.getTile(xPosition, yPosition).enter(this);
            }
            if (xPosition > 26 * TILE_SIZE) {
                int temp = xPosition;
                xPosition = TILE_SIZE + speed;
                screen.map.getTile(temp, yPosition).leave(this);
                screen.map.getTile(xPosition, yPosition).enter(this);
            }
            if (yPosition < 15 * TILE_SIZE) {
                int temp = yPosition;
                yPosition = 44 * TILE_SIZE - speed;
                screen.map.getTile(xPosition, temp).leave(this);
                screen.map.getTile(xPosition, yPosition).enter(this);
            }
            if (yPosition > 44 * TILE_SIZE) {
                int temp = yPosition;
                yPosition = 15 * TILE_SIZE + speed;
                screen.map.getTile(xPosition, temp).leave(this);
                screen.map.getTile(xPosition, yPosition).enter(this);
            }
        }
    }

    public void draw(){
        PacManGame.batch.draw(texture,
                xPosition, yPosition,
                sprite.getOriginX(), sprite.getOriginY(),
                TILE_SIZE, TILE_SIZE,
                sprite.getScaleX(), sprite.getScaleY(),
                rotation,
                texturePositionX, texturePositionY,
                32, 32, false, false
        );
    }

    public void collide() {
        this.state = State.DIEING;
        for(Enemy ghost : screen.getGhosts()){
            if (ghost.getState() != State.BOXED) {
                ghost.setState(State.HOMING);
                screen.map.getTile(ghost.xPosition, ghost.yPosition).leave(ghost);
            }
        }
    }

    public void update(float dt){ animation.update(dt); }

}