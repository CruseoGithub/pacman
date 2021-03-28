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

    protected Assets    assets;
    protected boolean   upPressed, downPressed, leftPressed, rightPressed, touchEvent;
    protected boolean   PauseReady = false;
    protected final int TILE_SIZE;
    protected MapScreen screen;
    protected OrthographicCamera gameCam;
    protected Stage     stage;
    protected Viewport  viewport;

    /* Constructor */

    /**
     * Constructor which initialises all the nessesary variables for the creation of a controller.
     * It stores the currently displayed Mapscreen for its child classes.
     * @param assets provide the Assetsmanager instance for building the controller.
     * @param screen instance of a Screen which will contain the controller
     */
    public Controller(Assets assets, MapScreen screen){
        this.assets = assets;
        this.screen = screen;
        this.TILE_SIZE = Map.getTileSize();
        this.gameCam = new OrthographicCamera();
        this.viewport = new FitViewport(
                Map.getMapWidth() * TILE_SIZE,
                Map.getMapHeight() * TILE_SIZE,
                gameCam);
        this.stage = new Stage(viewport, PacManGame.batch);
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
                    ((GameScreen) screen).setPauseActive(true);
                }
            }
        }
    }

    /**
     * Resets the direction
     */
    public void pulledInput(){
        upPressed = false;
        downPressed = false;
        leftPressed = false;
        rightPressed = false;
    }

    /* Methods */

    /**
     * this will check if the user activates the pause menu.
     * It checks if the touch input correspond with a rectangle zone at the top of the screen.
     * @param x x-value of the touch input
     * @param y y-value of the touch input
     */
    public void ready(float x, float y){
        if (screen instanceof GameScreen) {
            if (y >= 45 * TILE_SIZE && y <= 50 * TILE_SIZE) {
                if (x >= 2 * TILE_SIZE && x <= 26 * TILE_SIZE) {
                    this.PauseReady = true;
                }
            }
        }
    }

    /**
     * disposes the controller
     */
    public void dispose(){
        stage.dispose();
    }

    /**
     * draws the stage
     */
    public void draw(){
        stage.draw();
    }

    /**
     * rizises the viewport of the controller
     * @param width new width of the viewport
     * @param height new height of the viewport
     */
    public void resize(int width, int height){
        viewport.update(width, height);
    }
}

