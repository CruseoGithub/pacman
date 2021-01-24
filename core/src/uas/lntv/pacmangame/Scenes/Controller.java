package uas.lntv.pacmangame.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import uas.lntv.pacmangame.Maps.Map;
import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Screens.GameScreen;
import uas.lntv.pacmangame.Screens.MapScreen;

public abstract class Controller {
    protected MapScreen screen;
    protected Map map;
    protected OrthographicCamera gamecam;
    protected Viewport viewport;
    protected Stage stage;

    protected int tileSize;

    protected boolean upPressed, downPressed, leftPressed, rightPressed;

    public Controller(MapScreen screen){
        this.screen = screen;
        this.map = screen.map;
        this.tileSize = map.tileSize;
        gamecam = new OrthographicCamera();
        viewport = new FitViewport(map.mapWidth * tileSize,
                map.mapHeight*tileSize,
                gamecam);
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


    public void resize(int width, int height){
        viewport.update(width, height);
    }
}

