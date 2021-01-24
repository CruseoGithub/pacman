package uas.lntv.pacmangame.Sprites;
import uas.lntv.pacmangame.Screens.MapScreen;
import com.badlogic.gdx.graphics.Texture;
import uas.lntv.pacmangame.Maps.Tile;
import uas.lntv.pacmangame.Screens.MapScreen;
import uas.lntv.pacmangame.Sprites.Actor.State;
import uas.lntv.pacmangame.Sprites.PacMan;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class Animation {
    public Actor actor;
    public float animationSpeed;
    public MapScreen screen;
    private int frameWidth;

    private float tmpTimerAnimation = 0;
    private int tmp = 0;


    public Animation(Actor actor, float animationSpeed, MapScreen screen, int frameCount) {
        this.actor = actor;
        this.animationSpeed = animationSpeed;
        this.screen = screen;
        this.frameWidth = actor.region.getRegionWidth() / frameCount;
    }

    public void animate() {
        if (actor instanceof PacMan) {
            switch (actor.state) {
                case RUNNING:
                    if (actor.texturePositionX <= 128 && actor.mouthOpen == true) {
                        actor.texturePositionX = actor.texturePositionX + frameWidth;
                    } else
                        actor.mouthOpen = false;

                    if (actor.texturePositionX >= frameWidth && actor.mouthOpen == false) {
                        actor.texturePositionX = actor.texturePositionX - frameWidth;
                        if (actor.texturePositionX == 0)
                            actor.mouthOpen = true;
                    } else
                        actor.mouthOpen = true;
                    break;

                case DIEING:
                    actor.texturePositionX = tmp;
                    actor.texture = new Texture("pacman_death.png");
                    actor.rotation = 0;

                    if (this.tmp <= 320) {
                        tmp = tmp + frameWidth;
                        actor.texturePositionX = tmp;
                    } else {
                        actor.texture = new Texture("pacman32.png");
                        actor.state = State.RUNNING;
                        actor.setXPosition(actor.tileSize);
                        actor.setYPosition(17 * actor.tileSize);
                        tmp = 0;
                    }
                    break;
            }
        }

        if (actor instanceof Enemy) {
            switch (actor.direction) {
                case RIGHT:
                    actor.texturePositionY = 0;
                    break;
                case LEFT:
                    actor.texturePositionY = 32;
                    break;
                case UP:
                    actor.texturePositionY = 64;
                    break;
                case DOWN:
                    actor.texturePositionY = 96;
                    break;
            }
            actor.rotation = 0;
            if (actor.texturePositionX <= 64) {
                actor.texturePositionX = actor.texturePositionX + frameWidth;
            } else actor.texturePositionX = 0;
        }
    }

    public void update(float dt) {
        tmpTimerAnimation += dt;
        if (actor instanceof PacMan && actor.state != State.DIEING) {
            this.animationSpeed = 0.005f;
        } else {
            this.animationSpeed = 0.25f;
        }
        if (actor instanceof Enemy && actor.state != State.DIEING) {
            this.animationSpeed = 0.1f;
        }
        if ((screen.map.getTile(actor.xPosition, actor.yPosition, actor.direction).type != Tile.Type.WALL) || (actor.state == State.DIEING)) {
            if (tmpTimerAnimation >= this.animationSpeed) {
                this.animate();
                tmpTimerAnimation = 0;
            }
        }
    }
}