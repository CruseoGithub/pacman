package uas.lntv.pacmangame.Sprites;

import uas.lntv.pacmangame.Managers.Assets;
import uas.lntv.pacmangame.Maps.Map;
import uas.lntv.pacmangame.Screens.MapScreen;
import uas.lntv.pacmangame.Maps.Tile;
import uas.lntv.pacmangame.Sprites.Actor.State;


public class Animation {

    /* Fields */

    private final Actor ACTOR;
    private final Assets ASSETS;
    private final int FRAME_WIDTH;
    private final MapScreen SCREEN;
    private float animationSpeed;
    private float tmpTimerAnimation = 0;
    private int tmp = 0;

    /* Constructor */

    /**
     * Create a new animation
     * @param actor the actor
     * @param ASSETS asset management
     * @param animationSpeed the speed for the animation
     * @param screen the screen in which the animation will be created
     * @param frameCount number of frames of a sprite
     */
    public Animation(Actor actor, final Assets ASSETS, float animationSpeed, MapScreen screen, int frameCount) {
        this.ACTOR = actor;
        this.ASSETS = ASSETS;
        this.animationSpeed = animationSpeed;
        this.SCREEN = screen;
        this.FRAME_WIDTH = actor.region.getRegionWidth() / frameCount;
    }

    /* Methods */

    /**
     * Animates the actors Enemy and the pacman depending on his current state
     * The method takes their sprite and draws a rectangle around the desired frame
     * After that it goes to next one and repeats that
     */
    public void animate() {
        if (ACTOR instanceof PacMan) {
            switch (ACTOR.state) {
                case RUNNING:
                    if (ACTOR.texturePositionX <= 128 && ACTOR.mouthOpen) {
                        ACTOR.texturePositionX = ACTOR.texturePositionX + FRAME_WIDTH;
                    } else
                        ACTOR.mouthOpen = false;

                    if (ACTOR.texturePositionX >= FRAME_WIDTH && !ACTOR.mouthOpen) {
                        ACTOR.texturePositionX = ACTOR.texturePositionX - FRAME_WIDTH;
                        if (ACTOR.texturePositionX == 0)
                            ACTOR.mouthOpen = true;
                    } else
                        ACTOR.mouthOpen = true;
                    break;

                case DIEING:
                    ACTOR.texturePositionX = tmp;
                    ACTOR.texture = ASSETS.manager.get(ASSETS.DEATH_PAC);
                    ACTOR.rotation = 0;

                    if (this.tmp <= 320) {
                        tmp += FRAME_WIDTH;
                        ACTOR.texturePositionX = tmp;
                    } else {
                        ACTOR.texture = ASSETS.manager.get(ASSETS.PAC_MAN);
                        ACTOR.state = State.RUNNING;
                        ACTOR.texturePositionX = 0;
                        switch(ACTOR.direction) {
                            case RIGHT:
                                ACTOR.rotation = 0;
                                break;
                            case UP:
                                ACTOR.rotation = 90;
                                break;
                            case LEFT:
                                ACTOR.rotation = 180;
                                break;
                            case DOWN:
                                ACTOR.rotation = 270;
                                break;
                        }
                        ACTOR.setXPosition(ACTOR.getHomeX());
                        ACTOR.setYPosition(ACTOR.getHomeY());
                        SCREEN.notReady();
                        tmp = 0;
                    }
                    break;
            }
        }

        if (ACTOR instanceof Enemy) {
            switch (ACTOR.direction) {
                case RIGHT:
                    ACTOR.texturePositionY = 0;
                    break;
                case LEFT:
                    ACTOR.texturePositionY = 32;
                    break;
                case UP:
                    ACTOR.texturePositionY = 64;
                    break;
                case DOWN:
                    ACTOR.texturePositionY = 96;
                    break;
            }
            ACTOR.rotation = 0;
            if (ACTOR.texturePositionX <= 64) {
                ACTOR.texturePositionX = ACTOR.texturePositionX + FRAME_WIDTH;
            } else ACTOR.texturePositionX = 0;
        }
    }

    public void resetTmp(){ this.tmp = 0; }

    /**
     * This method adjusts the speed for the animation depending on whose sprite is chosen
     * @param dt time parameter used by libGDX
     */
    public void update(float dt) {
        tmpTimerAnimation += dt;
        if (ACTOR instanceof PacMan && ACTOR.state != State.DIEING) {
            this.animationSpeed = 0.005f;
        } else {
            this.animationSpeed = 0.25f;
        }
        if (ACTOR instanceof Enemy && ACTOR.state != State.DIEING) {
            this.animationSpeed = 0.1f;
        }
        if ((SCREEN.getMap().getTile(ACTOR.xPosition, ACTOR.yPosition, ACTOR.direction).getType() != Tile.Type.WALL)
                || ACTOR.xPosition%Map.getTileSize() != 0
                || ACTOR.yPosition%Map.getTileSize() != 0
                || ACTOR.state == State.DIEING
        ) {
            if (tmpTimerAnimation >= this.animationSpeed) {
                this.animate();
                tmpTimerAnimation = 0;
            }
        }
    }
}