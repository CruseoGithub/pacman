package uas.lntv.pacmangame.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Random;

import uas.lntv.pacmangame.Managers.Assets;
import uas.lntv.pacmangame.Maps.Map;
import uas.lntv.pacmangame.Maps.Tile;
import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Screens.GameScreen;
import uas.lntv.pacmangame.Screens.MapScreen;

public abstract class Actor {

    /* Fields */

    public enum Direction {
        RIGHT, LEFT, UP, DOWN;
        public static Direction getRandomDirection() {
            Random random = new Random();
            return values()[random.nextInt(values().length)];
        }
    }

    public enum State { RUNNING, DIEING, HOMING, BOXED }

    protected Animation animation;
    protected Assets assets;
    protected boolean mouthOpen;
    protected Direction direction, nextDirection, prevDirection;
    protected final int TILE_SIZE;
    protected final Map MAP;
    protected final MapScreen SCREEN;
    protected float animationSpeed;
    protected float rotation;
    protected int homeX, homeY;
    protected int speed;
    protected int texturePositionX, texturePositionY;
    protected int xPosition, yPosition;
    protected State state;
    protected Sprite sprite;
    protected Texture texture;
    protected TextureRegion region;


    /* Constructor */

    public Actor(Assets assets, int initX, int initY, MapScreen screen) {
        this.assets = assets;
        this.state = State.RUNNING;
        this.xPosition = initX;
        this.yPosition = initY;
        this.rotation = 0;
        this.speed = 2; // Values can be {0 == Stop , 1, 2 == default, 4, 8, 16}
        this.TILE_SIZE = Map.getTileSize();
        this.SCREEN = screen;
        this.MAP = SCREEN.getMap();
    }

    /* Accessors */

    public Animation getAnimation(){ return animation; }

    public Direction getDirection() { return direction; }

    public int getHomeX(){ return homeX; }

    public int getHomeY(){ return homeY; }

    public int getSpeed() { return speed; }

    /**
     * Divides the pixel-coordinate x-position by the TILE_SIZE of the map.
     * @return x-tile-coordinate
     */
    public int getXCoordinate(){ return xPosition / TILE_SIZE; }

    public int getXPosition() { return xPosition; }

    /**
     * Divides the pixel-coordinate y-position by the TILE_SIZE of the map.
     * @return y-tile-coordinate
     */
    public int getYCoordinate() { return yPosition / TILE_SIZE; }

    public int getYPosition() { return yPosition; }

    public State getState(){ return state; }

    /* Mutators */

    /**
     * Resets the texturePosition to (0|0). This is needed for the lives in the HUD, when PacMan
     * dies and regains a life in the same level.
     */
    public void resetTexturePosition() {
        this.texturePositionX = 0;
        this.texturePositionY = 0;
    }

    public void setNextDirection(Direction nextDirection) { this.nextDirection = nextDirection; }

    public void setRotation(int rotation) { this.rotation = rotation; }

    public void setSpeed(int speed) { this.speed = speed; }

    public void setState(State state){ this.state = state; }

    public void setTexture(Texture texture){ this.texture = texture; }

    public void setXPosition(int xPosition) { this.xPosition = xPosition; }

    public void setYPosition(int yPosition) { this.yPosition = yPosition; }

    /* Methods */

    /**
     * This is highly necessary, when you want to double the speed of an Actor as result of an
     * action on the field. This usually happens, when one step on the tile has been made.
     * If double the speed then without this correction, your Actor will not be able to detect
     * WALLS anymore, because you won't end up moving on a multiple of TILE_SIZE.
     * @param now the direction in which the Actor is moving right now.
     */
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

    /**
     * Let the Actors collide with each other. If PacMan is not buffed, he will die, lose a life
     * and the Ghosts will return to their place.
     */
    public void collide() {
        this.state = State.DIEING;
        for(Enemy ghost : SCREEN.getGhosts()){
            if (ghost.getState() != State.BOXED) {
                ghost.setState(State.HOMING);
                MAP.getTile(ghost.xPosition, ghost.yPosition).leave(ghost);
            }
        }
    }

    /**
     * General method to draw the Actor on the screen.
     * The SpriteBatch must begin in instance.
     */
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

    /**
     * This method moves the Actor as long as it's way isn't blocked.
     * It also turns the Actor if it changes direction.
     * If the Actor reaches the end of the transport-pipe on the GameScreen, it will also warp to
     * the other side of the screen.
     */
    public void move() {
        if (xPosition >= TILE_SIZE
                && xPosition <= 26 * TILE_SIZE
                && yPosition >= 15 * TILE_SIZE
                && yPosition <= 44 * TILE_SIZE
                || !(SCREEN instanceof GameScreen)
        ){
            prevDirection = direction;
            if (nextDirection != direction
                    && MAP.getTile(xPosition, yPosition, nextDirection).getType() != Tile.Type.WALL
                    && !( this instanceof Enemy && MAP.getTile(xPosition, yPosition, nextDirection).isOccupiedByGhost() )
            ) {
                if (xPosition == MAP.getTile(xPosition, yPosition).getX() && yPosition == MAP.getTile(xPosition, yPosition).getY()) {
                    direction = nextDirection;
                }
            }

            Tile tempTile = MAP.getTile(xPosition, yPosition, direction);
            switch (direction) {
                case RIGHT:
                    if (tempTile.getType() != Tile.Type.WALL) {
                        if (prevDirection != direction && !(this instanceof Enemy))
                            this.rotation = 0;
                        int temp = xPosition;
                        xPosition += speed;
                        if (temp / TILE_SIZE != xPosition / TILE_SIZE && this.state != State.HOMING) {
                            MAP.getTile(temp, yPosition).leave(this);
                            MAP.getTile(xPosition, yPosition).enter(this);
                        }
                    }
                    break;
                case LEFT:
                    if (tempTile.getType() == Tile.Type.WALL) {
                        if (xPosition > MAP.getTile(xPosition, yPosition).getX()) {
                            int temp = xPosition;
                            xPosition -= speed;
                            if (temp / TILE_SIZE != xPosition / TILE_SIZE && this.state != State.HOMING) {
                                MAP.getTile(temp, yPosition).leave(this);
                                MAP.getTile(xPosition, yPosition).enter(this);
                            }
                        }
                    } else {
                        if (prevDirection != direction && !(this instanceof Enemy))
                            this.rotation = 180;
                        int temp = xPosition;
                        xPosition -= speed;
                        if (temp / TILE_SIZE != xPosition / TILE_SIZE && this.state != State.HOMING) {
                            MAP.getTile(temp, yPosition).leave(this);
                            MAP.getTile(xPosition, yPosition).enter(this);
                        }
                    }
                    break;
                case UP:
                    if (tempTile.getType() != Tile.Type.WALL) {
                        if (prevDirection != direction && !(this instanceof Enemy))
                            this.rotation = 90;
                        int temp = yPosition;
                        yPosition += speed;
                        if (temp / TILE_SIZE != yPosition / TILE_SIZE && this.state != State.HOMING) {
                            MAP.getTile(xPosition, temp).leave(this);
                            MAP.getTile(xPosition, yPosition).enter(this);
                        }
                    }
                    break;
                case DOWN:
                    if (tempTile.getType() == Tile.Type.WALL) {
                        if (yPosition > MAP.getTile(xPosition, yPosition).getY()) {
                            int temp = yPosition;
                            yPosition -= speed;
                            if (temp / TILE_SIZE != yPosition / TILE_SIZE && this.state != State.HOMING) {
                                MAP.getTile(xPosition, temp).leave(this);
                                MAP.getTile(xPosition, yPosition).enter(this);
                            }
                        }
                    } else {
                        if (prevDirection != direction && !(this instanceof Enemy))
                            this.rotation = 270;
                        int temp = yPosition;
                        yPosition -= speed;
                        if (temp / TILE_SIZE != yPosition / TILE_SIZE && this.state != State.HOMING) {
                            MAP.getTile(xPosition, temp).leave(this);
                            MAP.getTile(xPosition, yPosition).enter(this);
                        }
                    }
                    break;
            }
        } else {
            if (xPosition < TILE_SIZE) {
                int temp = xPosition;
                xPosition = 26 * TILE_SIZE - speed;
                MAP.getTile(temp, yPosition).leave(this);
                MAP.getTile(xPosition, yPosition).enter(this);
            }
            if (xPosition > 26 * TILE_SIZE) {
                int temp = xPosition;
                xPosition = TILE_SIZE + speed;
                MAP.getTile(temp, yPosition).leave(this);
                MAP.getTile(xPosition, yPosition).enter(this);
            }
            if (yPosition < 15 * TILE_SIZE) {
                int temp = yPosition;
                yPosition = 44 * TILE_SIZE - speed;
                MAP.getTile(xPosition, temp).leave(this);
                MAP.getTile(xPosition, yPosition).enter(this);
            }
            if (yPosition > 44 * TILE_SIZE) {
                int temp = yPosition;
                yPosition = 15 * TILE_SIZE + speed;
                MAP.getTile(xPosition, temp).leave(this);
                MAP.getTile(xPosition, yPosition).enter(this);
            }
        }
    }

    /**
     * Updates the animation of the Actor.
     * @param dt Time parameter used by libGDX
     */
    public void update(float dt){ animation.update(dt); }

}