package uas.lntv.pacmangame.Scenes;



import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import uas.lntv.pacmangame.Maps.Map;
import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Screens.GameScreen;
import uas.lntv.pacmangame.Screens.MapScreen;
import uas.lntv.pacmangame.Managers.Assets;

/**
 * This class creates a on screen controller.
 * Depending on the child class this can be on screen buttons or a Joystick which is visible on touchdown
 */
public abstract class Controller {

    /* Fields */

    protected boolean upPressed, downPressed, leftPressed, rightPressed, touchEvent;
    protected boolean PauseReady = false;
    protected final Assets ASSETS;
    protected final int TILE_SIZE;
    protected final MapScreen SCREEN;
    protected final OrthographicCamera GAME_CAM;
    protected final Stage STAGE;
    protected final Viewport VIEWPORT;

    /* Constructor */

    /**
     * Constructor which initialises all the nessesary variables for the creation of a controller.
     * It stores the currently displayed Mapscreen for its child classes.
     * @param assets provide the Assetsmanager instance for building the controller.
     * @param screen instance of a Screen which will contain the controller
     */
    public Controller(Assets assets, MapScreen screen){
        this.ASSETS = assets;
        this.SCREEN = screen;
        this.TILE_SIZE = Map.getTileSize();
        this.GAME_CAM = new OrthographicCamera();
        this.VIEWPORT = new FitViewport(
                Map.getMapWidth() * TILE_SIZE,
                Map.getMapHeight() * TILE_SIZE,
                GAME_CAM);
        this.STAGE = new Stage(VIEWPORT, PacManGame.batch);
    }

    /* Accessors */

    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isTouchEvent() {
        return touchEvent;
    }

    /* Mutators */

    /**
     * If the top of the screen is touched the pause menu will activate.
     * It will check if PauseReady() determined a touch input at the top of the screen and double checks it again.
     * if everything is true the pause menu will be activated
     * @param x x-value of the touch input
     * @param y y-value of the touch input
     */
    public void setPause(float x, float y){
        if (this.PauseReady) {
            this.PauseReady = false;
            if (y >= 45 * TILE_SIZE && y <= 50 * TILE_SIZE) {
                if (x >= 2 * TILE_SIZE && x <= 26 * TILE_SIZE) {
                    ((GameScreen) SCREEN).setPauseActive(true);
                }
            }
        }
    }

    /**
     * Resets the direction. Avoids handing over the same direction a several times.
     */
    public void pulledInput(){
        upPressed = false;
        downPressed = false;
        leftPressed = false;
        rightPressed = false;
    }

    /* Methods */

    /**
     * This will check if the user activates the pause menu.
     * It checks if the touch input correspond with a rectangle zone at the top of the screen.
     * @param x x-value of the touch input
     * @param y y-value of the touch input
     */
    public void ready(float x, float y){
        if (SCREEN instanceof GameScreen) {
            if (y >= 45 * TILE_SIZE && y <= 50 * TILE_SIZE) {
                if (x >= 2 * TILE_SIZE && x <= 26 * TILE_SIZE) {
                    this.PauseReady = true;
                }
            }
        }
    }

    /**
     * Disposes the controller.
     */
    public void dispose(){
        STAGE.dispose();
    }

    /**
     * Resize the viewport of the controller
     * @param width new width of the viewport
     * @param height new height of the viewport
     */
    public void resize(int width, int height){
        VIEWPORT.update(width, height);
    }
}

