package uas.lntv.pacmangame.Scenes;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import uas.lntv.pacmangame.Maps.Map;
import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Screens.MapScreen;

public abstract class Controller {
    protected MapScreen screen;
    protected Map map;
    protected OrthographicCamera gameCam;
    protected Viewport viewport;
    protected Stage stage;
    protected final int TILE_SIZE;

    protected boolean upPressed, downPressed, leftPressed, rightPressed;

    public Controller(MapScreen screen){
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

