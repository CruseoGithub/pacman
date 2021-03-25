package uas.lntv.pacmangame.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import uas.lntv.pacmangame.AI.Pathfinder;
import uas.lntv.pacmangame.Managers.Assets;
import uas.lntv.pacmangame.Maps.Map;
import uas.lntv.pacmangame.Maps.Tile;
import uas.lntv.pacmangame.Screens.MapScreen;

/**
 * The ghosts are made of pure evil with the only purpose to chase and kill PacMan.
 */
public class Enemy extends Actor {
    public enum Difficulty{
        RUNAWAY,RANDOM,EASY,MEDIUM,HARD
    }

    private Difficulty difficulty;
    private Difficulty levelDiff;
    private Pathfinder aStar;
    private final Texture LIVING_BODY;

    private float boxTimer;

    private boolean home = false;

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

        homeX = 13 * TILE_SIZE;
        homeY = 33 * TILE_SIZE;


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

        animation = new Animation(this, assets, animationSpeed, this.SCREEN,4);

        this.boxTimer = 0;

        region.flip(true, false);
        this.sprite = new Sprite(region);
        this.sprite.setOrigin(TILE_SIZE /2f, TILE_SIZE /2f);
    }

    public Difficulty getDifficulty(){ return this.difficulty; }


    /**
     * In addition to normal setter methods, this one can keep the difficulty of the current level.
     * @param difficulty the difficulty that the ghost is supposed to adopt
     */
    public void setDifficulty(Enemy.Difficulty difficulty){
        if(difficulty == Difficulty.RUNAWAY && this.difficulty != Difficulty.RUNAWAY) levelDiff = this.difficulty;
        this.difficulty = difficulty;
    }

    /**
     * This method is needed, when SuperPacMan shrinks down to PacMan and the ghost needs to
     * turn back to his assigned difficulty depending on the level.
     */
    public void resetDifficulty(){
        setDifficulty(levelDiff);
    }

    public void setBoxTimer(float timer){ this.boxTimer = timer; }

    /**
     * Moves the ghost according to its current position until it has reached the HOME-position
     * outside of the box.
     */
    public void leaveBox(){
        MAP.getTile(xPosition, yPosition).leave(this);
        if(xPosition < 13 * TILE_SIZE) xPosition += 8;
        else if(xPosition > 13 * TILE_SIZE) xPosition -= 8;
        else if(yPosition < 33 * TILE_SIZE) yPosition += 8;
        else setState(State.RUNNING);
    }

    /**
     * Make the ghost find his spot in the box.
     */
    public void enterBox() {
        if (yPosition > 30 * TILE_SIZE) yPosition -= 8;
        else if (!(MAP.getTile(12 * TILE_SIZE, 30 * TILE_SIZE).isOccupiedByGhost())){
            if(xPosition > 12 * TILE_SIZE) xPosition -= 8;
            if(xPosition == 12 * TILE_SIZE){
                MAP.getTile(xPosition, yPosition).enter(this);
                setState(State.BOXED);
                home = false;
                boxTimer = 5;
                texture = LIVING_BODY;
            }
        }
        else if(!(MAP.getTile(15 * TILE_SIZE, 30 * TILE_SIZE).isOccupiedByGhost())){
            if(xPosition< 15 * TILE_SIZE)xPosition += 8;
            if(xPosition == 15 * TILE_SIZE){
                MAP.getTile(xPosition, yPosition).enter(this);
                setState(State.BOXED);
                home = false;
                boxTimer = 7.5f;
                texture = LIVING_BODY;
            }
        }
        else if (!(MAP.getTile(14 * TILE_SIZE, 30 * TILE_SIZE).isOccupiedByGhost())){
            if(xPosition < 14 * TILE_SIZE)xPosition += 8;
            if(xPosition > 14 * TILE_SIZE) xPosition -= 8;  //this could happen if the second and third ghost enter the box in short distance to each other (< TILE_SIZE)
            if(xPosition == 14 * TILE_SIZE) {
                MAP.getTile(xPosition, yPosition).enter(this);
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
        if(state != State.BOXED && (Math.abs(distanceX) + Math.abs(distanceY)) > 16* TILE_SIZE) return Direction.getRandomDirection();
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
        aStar = new Pathfinder(SCREEN, this, target);
        Tile temp = aStar.aStarResult();
        if(temp == MAP.getTile(xPosition, yPosition) || temp == null) return direction;
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
     * @return Direction to the tile of retreat
     */
    private Direction runAway(Actor hunter){
        if(collisionTest(hunter)){
            this.state = State.HOMING;
            this.texture = assets.manager.get(assets.BLUE_DEAD);
            home = false;
            MAP.getTile(xPosition, yPosition).leave(this);
            return Direction.RIGHT;
        }
        if(hunter.getXPosition() < 13* TILE_SIZE){
            if(hunter.getYPosition() < 30* TILE_SIZE) aStar = new Pathfinder(SCREEN, this, 26* TILE_SIZE, 43* TILE_SIZE);
            if(hunter.getYPosition() >= 30* TILE_SIZE) aStar = new Pathfinder(SCREEN,this, 26* TILE_SIZE, 15* TILE_SIZE);
        } else{
            if(hunter.getYPosition() < 30* TILE_SIZE) aStar = new Pathfinder(SCREEN, this, TILE_SIZE, 43* TILE_SIZE);
            if(hunter.getYPosition() >= 30* TILE_SIZE) aStar = new Pathfinder(SCREEN, this, TILE_SIZE, 15* TILE_SIZE);
        }
        Tile temp = aStar.aStarResult();
        if(temp == MAP.getTile(xPosition, yPosition) || temp == null) return direction;
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
        aStar = new Pathfinder(SCREEN, this, homeX, homeY);
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
        if(MAP.getTile(xPosition, yPosition) == MAP.getTile(homeX, homeY)){
            xPosition = homeX;
            yPosition = homeY;
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

    /**
     * This update method checks the current state of the game to determine what each ghost needs to
     * do next.
     * @param dt time parameter used by libGDX
     * @param pacman PacMan
     */
    public void update(float dt, PacMan pacman) {
        super.update(dt);
        if(pacman.getState() == State.DIEING){
            if(state != Actor.State.BOXED) {
                if (!home) getHome();
                else if (LIVING_BODY == assets.manager.get(assets.GHOST_1)) setState(Actor.State.RUNNING);
                else enterBox();
            }
        } else{
            if(boxTimer > 0){
                boxTimer -= Gdx.graphics.getDeltaTime();
                Direction temp = findNextDirectionEasy(pacman);
                switch(temp){
                    case RIGHT:
                        texturePositionY = 0;
                        break;
                    case LEFT:
                        texturePositionY = 32;
                        break;
                    case UP:
                        texturePositionY = 64;
                        break;
                    case DOWN:
                        texturePositionY = 96;
                        break;
                }
            }
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
