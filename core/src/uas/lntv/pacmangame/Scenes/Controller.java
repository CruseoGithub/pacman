package uas.lntv.pacmangame.Scenes;

<<<<<<< HEAD
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
=======
>>>>>>> dev_Denis
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
<<<<<<< HEAD
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.PauseableThread;
=======
>>>>>>> dev_Denis
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import uas.lntv.pacmangame.Maps.Map;
import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Screens.MapScreen;
import uas.lntv.pacmangame.Scenes.Hud;
import uas.lntv.pacmangame.Screens.PauseScreen;
import uas.lntv.pacmangame.Screens.ScoreScreen;

public abstract class Controller {
    protected MapScreen screen;
    protected Map map;
    protected OrthographicCamera gameCam;
    protected Viewport viewport;
    protected Stage stage;
    protected final int TILE_SIZE;

<<<<<<< HEAD
    private int tileSize;

    boolean upPressed, downPressed, leftPressed, rightPressed;
    boolean touchpause = true;
    private boolean PauseReady = false;
=======
    protected boolean upPressed, downPressed, leftPressed, rightPressed;
>>>>>>> dev_Denis

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
<<<<<<< HEAD

        Gdx.input.setInputProcessor(new InputAdapter(){
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 touch = new Vector3(screenX, screenY, 0);
                gamecam.unproject(touch);

                System.out.println("Screen coordinates translated to world coordinates: "
                        + "X: " + touch.x + " Y: " + touch.y);

                if (touch.x >= 12 * tileSize && touch.x <= 16 * tileSize) {
                    if (touch.y >= 0 && touch.y <= 4 * tileSize) downPressed = true;
                    if (touch.y >= 10 * tileSize && touch.y <= 14 * tileSize) upPressed = true;
                }

                if (touch.y >= 5 * tileSize && touch.y <= 9 * tileSize) {
                    if (touch.x >= 7 * tileSize && touch.x <= 11 * tileSize) leftPressed = true;
                    if (touch.x >= 17 * tileSize && touch.x <= 21 * tileSize) rightPressed = true;
                }
                ready(touch.x, touch.y);

                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                Vector3 touch = new Vector3(screenX, screenY, 0);
                gamecam.unproject(touch);
                if(isLeftPressed()) leftPressed = false;
                if(isRightPressed()) rightPressed = false;
                if(isUpPressed()) upPressed = false;
                if(isDownPressed()) downPressed = false;
                setPause(touch.x, touch.y);
                return super.touchUp(screenX, screenY, pointer, button);

            }

        });

=======
>>>>>>> dev_Denis
    }

    public void ready(float x, float y){
        if (screen instanceof GameScreen) {
            if (y >= 45 * tileSize && y <= 50 * tileSize) {
                if (x >= 2 * tileSize && x <= 26 * tileSize) {
                    PauseReady = true;
                }
            }
        }
    }
    public void setPause(float x, float y){
        if (PauseReady) {
            PauseReady = false;
            if (y >= 45 * tileSize && y <= 50 * tileSize) {
                if (x >= 2 * tileSize && x <= 26 * tileSize) {
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

