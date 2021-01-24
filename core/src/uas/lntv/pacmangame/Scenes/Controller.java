package uas.lntv.pacmangame.Scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.PauseableThread;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import uas.lntv.pacmangame.Maps.Map;
import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Screens.GameScreen;
import uas.lntv.pacmangame.Screens.MapScreen;
import uas.lntv.pacmangame.Scenes.Hud;
import uas.lntv.pacmangame.Screens.ScoreScreen;

public class Controller {
    private MapScreen screen;
    private Map map;
    private OrthographicCamera gamecam;
    private Viewport viewport;
    private Stage stage;

    private int tileSize;

    boolean upPressed, downPressed, leftPressed, rightPressed;
     boolean touchpause = true;


    public Controller(final MapScreen screen){
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
                if(touch.y >= 45 * tileSize && touch.y <= 50 * tileSize){
                    if(touch.x >= 2 * tileSize && touch.x <= 26 * tileSize) {
                        if(touchpause == true) {
                            System.out.println("Pausebutton Controller Layer");
                            //game.setScreen(new ScoreScreen(game, "HighScoreList.tmx"));
                            screen.pause();
                            screen.dispose();
                        }
                    }
                }
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if(isLeftPressed()) leftPressed = false;
                if(isRightPressed()) rightPressed = false;
                if(isUpPressed()) upPressed = false;
                if(isDownPressed()) downPressed = false;
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

    public void resize(int width, int height){
        viewport.update(width, height);
    }
}

