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

public class Controller {
    private MapScreen screen;
    private Map map;
    private OrthographicCamera gamecam;
    private Viewport viewport;
    private Stage stage;

    private float x;
    private float y;

    private int tileSize;

    boolean upPressed, downPressed, leftPressed, rightPressed;

    public Controller(MapScreen screen){
        this.screen = screen;
        this.map = screen.map;
        this.tileSize = map.tileSize;
        gamecam = new OrthographicCamera();
        viewport = new FitViewport(map.mapWidth * tileSize,
                map.mapHeight*tileSize,
                gamecam);
        stage = new Stage(viewport, PacManGame.batch);

        Gdx.input.setInputProcessor(new InputAdapter(){
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 touch = new Vector3(screenX, screenY, 0);
                gamecam.unproject(touch);

                x = touch.x;
                y = touch.y;
                System.out.println("Screen coordinates translated to world coordinates: "
                        + "X: " + touch.x + " Y: " + touch.y);

                if(touch.x >= 12 * tileSize && touch.x <= 16 * tileSize){
                    if(touch.y >= 0 && touch.y <= 4 * tileSize) downPressed = true;
                    if(touch.y >= 10 * tileSize && touch.y <= 14 * tileSize) upPressed = true;
                }

                if(touch.y >= 5 * tileSize && touch.y <= 9 * tileSize){
                    if(touch.x >= 7 * tileSize && touch.x <= 11 * tileSize) leftPressed = true;
                    if(touch.x >= 17 * tileSize && touch.x <= 21 * tileSize) rightPressed = true;
                }
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                Vector3 touch = new Vector3(screenX, screenY, 0);
                gamecam.unproject(touch);
                float compX = x - touch.x;
                float compY = y - touch.y;
                if(Math.abs(compX) <= Math.abs(compY)){
                    if(compY > 50)  downPressed = true;
                    if(compY < -50) upPressed = true;
                } else{
                    if(compX > 50) leftPressed = true;
                    if(compX < -50) rightPressed = true;
                }

                return super.touchUp(screenX, screenY, pointer, button);
            }

        });

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

    public void pullInput(){
        leftPressed = false;
        rightPressed = false;
        upPressed = false;
        downPressed = false;
    }

    public void resize(int width, int height){
        viewport.update(width, height);
    }
}

