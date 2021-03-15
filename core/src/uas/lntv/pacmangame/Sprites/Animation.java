package uas.lntv.pacmangame.Sprites;

import uas.lntv.pacmangame.Assets;
import uas.lntv.pacmangame.Screens.MapScreen;
import uas.lntv.pacmangame.Maps.Tile;
import uas.lntv.pacmangame.Sprites.Actor.State;


public class Animation {
    private final Actor ACTOR;
    private final Assets ASSETS;
    private float animationSpeed;
    private final MapScreen SCREEN;
    private final int FRAME_WIDTH;

    private float tmpTimerAnimation = 0;
    private int tmp = 0;


    public Animation(Actor actor, final Assets ASSETS, float animationSpeed, MapScreen screen, int frameCount) {
        this.ACTOR = actor;
        this.ASSETS = ASSETS;
        this.animationSpeed = animationSpeed;
        this.SCREEN = screen;
        this.FRAME_WIDTH = actor.region.getRegionWidth() / frameCount;
    }

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
                        ACTOR.setXPosition(14 * ACTOR.TILE_SIZE);
                        ACTOR.setYPosition(21 * ACTOR.TILE_SIZE);
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
        if ((SCREEN.map.getTile(ACTOR.xPosition, ACTOR.yPosition, ACTOR.direction).type != Tile.Type.WALL) || (ACTOR.state == State.DIEING)) {
            if (tmpTimerAnimation >= this.animationSpeed) {
                this.animate();
                tmpTimerAnimation = 0;
            }
        }
    }
}