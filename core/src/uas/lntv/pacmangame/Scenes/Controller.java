package uas.lntv.pacmangame.Scenes;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import uas.lntv.pacmangame.Maps.Map;
import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Screens.GameScreen;
import uas.lntv.pacmangame.Screens.MapScreen;
<<<<<<< HEAD
import uas.lntv.pacmangame.Scenes.Hud;
import uas.lntv.pacmangame.Screens.PauseScreen;
import uas.lntv.pacmangame.Screens.ScoreScreen;
=======
import uas.lntv.pacmangame.Assets;
>>>>>>> development_2

public abstract class Controller {
    protected MapScreen screen;
    protected Map map;
    protected OrthographicCamera gameCam;
    protected Viewport viewport;
    protected Stage stage;
    protected final int TILE_SIZE;
    protected Assets assets;

<<<<<<< HEAD


    protected boolean upPressed, downPressed, leftPressed, rightPressed;
    boolean touchpause = true;
    private boolean PauseReady = false;

=======
    protected boolean upPressed, downPressed, leftPressed, rightPressed, touchEvent;
>>>>>>> development_2

    public Controller(Assets assets, MapScreen screen){
        this.assets = assets;
        this.screen = screen;
        this.map = screen.map;
        this.TILE_SIZE = screen.map.getTileSize();
        gameCam = new OrthographicCamera();
        viewport = new FitViewport(
                map.getMapWidth() * TILE_SIZE,
                map.getMapHeight() * TILE_SIZE,
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

