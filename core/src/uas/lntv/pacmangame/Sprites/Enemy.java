package uas.lntv.pacmangame.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import uas.lntv.pacmangame.AI.Pathfinder;
import uas.lntv.pacmangame.Managers.Assets;
import uas.lntv.pacmangame.Maps.Tile;
import uas.lntv.pacmangame.Screens.MapScreen;

/**
 * The ghosts are made of pure evil with the only purpose to chase and kill PacMan.
 */
public class Enemy extends Actor {
    public enum Difficulty{
        RUNAWAY,RANDOM,EASY,MEDIUM,HARD
    }

    private Pathfinder aStar;

    private Difficulty difficulty;
    private Difficulty levelDiff;

    private float boxTimer;
    private final int HOME_X;
    private final int HOME_Y;
    private final Texture LIVING_BODY;

    private boolean home = false;

    public boolean isHome(){ return home; }

    public void notHome(){ home = false; }

    /**
     * Create a new ghost
     * @param initX starting x-coordinate
     * @param initY starting y-coordinate
     * @param screen the screen in which the ghost will be created
     * @param ghost name or path of the png-file that makes the ghost look beautiful
     */
    public Enemy(int initX, int initY, Assets assets, MapScreen screen, Texture ghost){
        super(assets, initX, initY, screen);

        HOME_X = 13 * TILE_SIZE;
        HOME_Y = 33 * TILE_SIZE;


        this.difficulty = Difficulty.EASY;
        this.direction = Direction.DOWN;
        this.nextDirection = Direction.DOWN;
        this.prevDirection = Direction.DOWN;

        LIVING_BODY = ghost;
        this.texture = LIVING_BODY;
        region = new TextureRegion(texture);
        region.setRegionX(0);
        region.setRegionY(0);
        texturePositionX = 0;
        texturePositionY = 0;
        this.animationSpeed = 0.1f;

        animation = new Animation(this, assets, animationSpeed, this.screen,4);

        this.boxTimer = 0;

        region.flip(true, false);
        this.sprite = new Sprite(region);
        this.sprite.setOrigin(TILE_SIZE /2f, TILE_SIZE /2f);
    }

    public Difficulty getDifficulty(){ return this.difficulty; }

    public void setDifficulty(Enemy.Difficulty difficulty){
        if(difficulty == Difficulty.RUNAWAY && this.difficulty != Difficulty.RUNAWAY) levelDiff = this.difficulty;
        this.difficulty = difficulty;
    }

    public void resetDifficulty(){
        setDifficulty(levelDiff);
    }

    public void setBoxTimer(float timer){ this.boxTimer = timer; }

    public float getBoxTimer(){ return this.boxTimer; }

    public void leaveBox(){
        screen.map.getTile(xPosition, yPosition).leave(this);
        if(xPosition < 13 * TILE_SIZE) xPosition += 8;
        else if(xPosition > 13 * TILE_SIZE) xPosition -= 8;
        else if(yPosition < 33 * TILE_SIZE) yPosition += 8;
        else setState(State.RUNNING);
    }

    public void enterBox() {
        if (yPosition > 30 * TILE_SIZE) yPosition -= 8;
        else if (!(screen.map.getTile(12 * TILE_SIZE, 30 * TILE_SIZE).isOccupiedByGhost())){
            if(xPosition > 12 * TILE_SIZE) xPosition -= 8;
            if(xPosition == 12 * TILE_SIZE){
                screen.map.getTile(xPosition, yPosition).enter(this);
                setState(State.BOXED);
                home = false;
                boxTimer = 5;
                texture = LIVING_BODY;
            }
        }
        else if(!(screen.map.getTile(15 * TILE_SIZE, 30 * TILE_SIZE).isOccupiedByGhost())){
            if(xPosition< 15 * TILE_SIZE)xPosition += 8;
            if(xPosition == 15 * TILE_SIZE){
                screen.map.getTile(xPosition, yPosition).enter(this);
                setState(State.BOXED);
                home = false;
                boxTimer = 7.5f;
                texture = LIVING_BODY;
            }
        }
        else if (!(screen.map.getTile(14 * TILE_SIZE, 30 * TILE_SIZE).isOccupiedByGhost())){
            if(xPosition< 14 * TILE_SIZE)xPosition += 8;
            if(xPosition == 14 * TILE_SIZE) {
                screen.map.getTile(xPosition, yPosition).enter(this);
                setState(State.BOXED);
                home = false;
                boxTimer = 10;
                texture = LIVING_BODY;
            }
        }
    }


    /**
     * Simply detects if PacMan is above or beneath the ghost.
     * @param distance insert y-distance between ghost and PacMan
     * @return Direction UP or DOWN
     */
    private Direction UpOrDown(int distance){
        if (distance > 0) return Direction.DOWN;
        else return Direction.UP;
    }

    /**
     * Simply detects if PacMan is left or right the ghost.
     * @param distance insert x-distance between ghost and PacMan
     * @return Direction LEFT or RIGHT
     */
    private Direction LeftOrRight(int distance){
        if (distance > 0 ) return Direction.LEFT;
        else return Direction.RIGHT;
    }

    /**
     * If the ghost runs on EASY mode, it runs around randomly until it's close enough to PacMan.
     * In the closer environment of PacMan the ghost calculates the x-/y-distance to PacMan
     * primitively an chooses according to the result in which direction it will walk.
     * It will also detect collisions with PacMan.
     * @param target PacMan
     * @return One of the four directions
     */
    private Direction findNextDirectionEasy(Actor target){
        int distanceX = this.xPosition - target.getXPosition();
        int distanceY = this.yPosition - target.getYPosition();
        if((Math.abs(distanceX) + Math.abs(distanceY)) > 16* TILE_SIZE) return Direction.getRandomDirection();
        if(collisionTest(target)) return Direction.RIGHT;
        if(Math.abs(distanceX) > Math.abs(distanceY)){
            return LeftOrRight(distanceX);
        }
        else return UpOrDown(distanceY);
    }

    /**
     * If the ghost runs on MEDIUM mode, it runs around randomly until it's close enough to PacMan.
     * In the closer environment of PacMan the ghost uses the A*-algorithm to find the shortest
     * path that leads directly to PacMan.
     * It will also detect collisions with PacMan.
     * @param target PacMan
     * @return One of the four directions
     */
    private Direction findNextDirectionMedium(Actor target){
        int distanceX = this.xPosition - target.getXPosition();
        int distanceY = this.yPosition - target.getYPosition();
        if((Math.abs(distanceX)+Math.abs(distanceY)) > 16* TILE_SIZE) return Direction.getRandomDirection();
        return findNextDirectionHard(target);
    }

    /**
     * If the ghost runs on HARD mode, it uses the A*-algorithm to find the shortest path that
     * leads directly to PacMan.
     * It also detects collisions with PacMan.
     * @param target PacMan
     * @return One of the four directions
     */
    private Direction findNextDirectionHard(Actor target){
        if(collisionTest(target)) return Direction.RIGHT;
        aStar = new Pathfinder(screen, this, target);
        Tile temp = aStar.aStarResult();
        if(temp == screen.map.getTile(xPosition, yPosition) || temp == null) return direction;
        if(temp.getY() > this.getYPosition()) return Direction.UP;
        if(temp.getY() < this.getYPosition()) return Direction.DOWN;
        if(temp.getX() > this.getXPosition()) return Direction.RIGHT;
        else return Direction.LEFT;
    }

    /**
     * This method shows the next tile to visit on the way to the as most secure assumed corner.
     * The map will be divided in four quarters and the targeted tile will be the one in the
     * extreme corner of the opposite quarter in which SuperPacMan is right now.
     * @param hunter RUN! It's SuperPacMan!!
     * @return tile of retreat
     */
    private Direction runAway(Actor hunter){
        if(collisionTest(hunter)){
            this.state = State.HOMING;
            this.texture = assets.manager.get(assets.BLUE_DEAD);
            home = false;
            screen.map.getTile(xPosition, yPosition).leave(this);
            return Direction.RIGHT;
        }
        if(hunter.getXPosition() < 13* TILE_SIZE){
            if(hunter.getYPosition() < 30* TILE_SIZE) aStar = new Pathfinder(screen, this, 26* TILE_SIZE, 43* TILE_SIZE, false);
            if(hunter.getYPosition() >= 30* TILE_SIZE) aStar = new Pathfinder(screen, this, 26* TILE_SIZE, 15* TILE_SIZE, false);
        } else{
            if(hunter.getYPosition() < 30* TILE_SIZE) aStar = new Pathfinder(screen, this, TILE_SIZE, 43* TILE_SIZE, false);
            if(hunter.getYPosition() >= 30* TILE_SIZE) aStar = new Pathfinder(screen, this, TILE_SIZE, 15* TILE_SIZE, false);
        }
        Tile temp = aStar.aStarResult();
        if(temp == screen.map.getTile(xPosition, yPosition) || temp == null) return direction;
        if(temp.getY() > this.getYPosition()) return Direction.UP;
        if(temp.getY() < this.getYPosition()) return Direction.DOWN;
        if(temp.getX() > this.getXPosition()) return Direction.RIGHT;
        else return Direction.LEFT;
    }

    /**
     * The ghost never forgets where it's home is.
     * If it killed PacMan or if it got killed, it will use the A*-algorithm to rush back home.
     * @return One of the four directions
     */
    private Direction findHome(){
        aStar = new Pathfinder(screen, this, HOME_X, HOME_Y, true);
        Tile temp = aStar.aStarResult();
        if(temp.getY() > this.getYPosition()) return Direction.UP;
        if(temp.getY() < this.getYPosition()) return Direction.DOWN;
        if(temp.getX() > this.getXPosition()) return Direction.RIGHT;
        else return Direction.LEFT;
    }

    /**
     * Increase the ghosts speed and find it's way home.
     */
    public void getHome(){
        correctPosition(direction);
        setSpeed(getSpeed() * 2);
        correctPosition(direction);
        setSpeed(getSpeed() * 2);
        nextDirection = findHome();
        move();
        setSpeed(getSpeed() / 2);
        setSpeed(getSpeed() / 2);
        if(screen.map.getTile(xPosition, yPosition) == screen.map.getTile(HOME_X, HOME_Y)){
            xPosition = HOME_X;
            yPosition = HOME_Y;
            home = true;
        }
    }

    /**
     * Decide which method will be used to find the next direction to move
     * depending on the difficulty.
     * @param target PacMan
     */
    public void findNextDirection(Actor target) {
        switch (difficulty) {
            case RANDOM:
                nextDirection = Direction.getRandomDirection();
                break;
            case EASY:
                nextDirection = findNextDirectionEasy(target);
                break;
            case MEDIUM:
                nextDirection = findNextDirectionMedium(target);
                break;
            case HARD:
                nextDirection = findNextDirectionHard(target);
                break;
            case RUNAWAY:
                nextDirection = runAway(target);
                break;
        }
    }

    public void update(float dt, PacMan pacman) {
        super.update(dt);
        if(pacman.getState() == State.DIEING){
            if(state != Actor.State.BOXED) {
                if (!home) getHome();
                else if (LIVING_BODY == assets.manager.get(assets.GHOST_1)) setState(Actor.State.RUNNING);
                else enterBox();
            }
        } else{
            if(boxTimer > 0) boxTimer -= Gdx.graphics.getDeltaTime();
            else if(state == State.BOXED) leaveBox();
            else if(state == State.HOMING) {
                if(!home) getHome();
                else enterBox();
            }
            else{
                findNextDirection(pacman);
                move();
            }
        }

    }

    /**
     * Calculates if the ghost is close enough to PacMan to detect a collision
     * @param target PacMan
     * @return true in case of a hit
     */
    private boolean collisionTest(Actor target){
        int distanceX = this.xPosition - target.getXPosition();
        int distanceY = this.yPosition - target.getYPosition();
        if((Math.abs(distanceX) < TILE_SIZE) && (Math.abs(distanceY) < TILE_SIZE)){
            target.collide();
            return true;
        }
        return false;
    }

}
