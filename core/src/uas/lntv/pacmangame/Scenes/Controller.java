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

public abstract class Controller {
    protected MapScreen screen;
    protected OrthographicCamera gameCam;
    protected Viewport viewport;
    protected Stage stage;
    protected final int TILE_SIZE;
    protected Assets assets;


    protected boolean upPressed, downPressed, leftPressed, rightPressed, touchEvent;
    boolean touchpause = true;
    protected boolean PauseReady = false;


    public Controller(Assets assets, MapScreen screen){
        this.assets = assets;
        this.screen = screen;
        this.TILE_SIZE = Map.getTileSize();
        gameCam = new OrthographicCamera();
        viewport = new FitViewport(
                Map.getMapWidth() * TILE_SIZE,
                Map.getMapHeight() * TILE_SIZE,
                gameCam);
        stage = new Stage(viewport, PacManGame.batch);
    }

    public void ready(float x, float y){
        if (screen instanceof GameScreen) {
            if (y >= 45 * TILE_SIZE && y <= 50 * TILE_SIZE) {
                if (x >= 2 * TILE_SIZE && x <= 26 * TILE_SIZE) {
                    PauseReady = true;
                }
            }
        }
    }

    public void setPause(float x, float y){
        if (PauseReady) {
            PauseReady = false;
            if (y >= 45 * TILE_SIZE && y <= 50 * TILE_SIZE) {
                if (x >= 2 * TILE_SIZE && x <= 26 * TILE_SIZE) {
                    ((GameScreen) screen).setPauseActive(true);
                }
            }
        }
    }
    public void dispose(){
        stage.dispose();
    }

    public void draw(){
        stage.draw();
    }

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

    public void pulledInput(){
        upPressed = false;
        downPressed = false;
        leftPressed = false;
        rightPressed = false;
    }

    public void resize(int width, int height){
        viewport.update(width, height);
    }
}

