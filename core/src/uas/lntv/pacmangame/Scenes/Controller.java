package uas.lntv.pacmangame.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Screens.GameScreen;
import uas.lntv.pacmangame.Screens.MapScreen;

public class Controller {
    MapScreen screen;
    Viewport viewport;
    Stage stage;
    boolean upPressed, downPressed, leftPressed, rightPressed;
    OrthographicCamera gamecam;
    private int tileSize;

    public Controller(MapScreen screen){
        this.screen = screen;
        this.tileSize = screen.map.tileSize;
        gamecam = new OrthographicCamera();
        viewport = new FitViewport(screen.map.mapWidth*screen.map.tileSize,
                screen.map.mapHeight*screen.map.tileSize,
                (new OrthographicCamera()));
        stage = new Stage(viewport, PacManGame.batch);
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.left().bottom();

        //Creating Up-Arrow
        Image upArrow = new Image(new Texture("UpArrow.png"));
        upArrow.setSize(tileSize*2, tileSize*2);
        upArrow.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = false;
            }
        });

        //Creating Down-Arrow
        Image downArrow = new Image(new Texture("DownArrow.png"));
        downArrow.setSize(tileSize*2, tileSize*2);
        downArrow.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                downPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                downPressed = false;
            }
        });

        //Creating Left-Arrow
        Image leftArrow = new Image(new Texture("LeftArrow.png"));
        leftArrow.setSize(tileSize*2, tileSize*2 );
        leftArrow.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = false;
            }
        });

        //Creating Right-Arrow
        Image rightArrow = new Image(new Texture("RightArrow.png"));
        rightArrow.setSize(tileSize*2, tileSize*2);
        rightArrow.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = false;
            }
        });

        table.add();
        table.add(upArrow).size(upArrow.getWidth(), upArrow.getHeight());
        table.add();
        table.row().pad(5,5,5,5);
        table.add(leftArrow).size(leftArrow.getWidth(), leftArrow.getHeight());
        table.add();
        table.add(rightArrow).size(rightArrow.getWidth(), rightArrow.getHeight());
        table.row().padBottom(5);
        table.add();
        table.add(downArrow).size(downArrow.getWidth(), downArrow.getHeight());
        table.add();

        stage.addActor(table);
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

