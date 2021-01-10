package uas.lntv.pacmangame.Screens;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import uas.lntv.pacmangame.Maps.GameMap;
import uas.lntv.pacmangame.Maps.Map;
import uas.lntv.pacmangame.Maps.MenuMap;
import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Scenes.Controller;
import uas.lntv.pacmangame.Scenes.Hud;
import uas.lntv.pacmangame.Sprites.Actor;
import uas.lntv.pacmangame.Sprites.Enemy;
import uas.lntv.pacmangame.Sprites.PacMan;

public abstract class MapScreen implements Screen {
    public enum Type { GAME, MENU};

    protected PacManGame game;
    protected OrthographicCamera gamecam;
    protected Viewport gamePort;

    public Map map;
    public Hud hud;

    public PacMan pacman;
    public Enemy ghost;

    private Controller controller;

    protected float tmpTimerAnimation = 0;

    public MapScreen(PacManGame game, String mapPath, MapScreen.Type type){
        //Setzt Höhe und Breite des Desktopfensters (16:9 Format)
        if (Gdx.app.getType().equals(Application.ApplicationType.Desktop)) {
            Gdx.graphics.setWindowedMode(450, 800);
        }
        this.game = game;
        this.gamecam = new OrthographicCamera();


        switch (type){
            case GAME:
                this.map = new GameMap(mapPath, this);
                break;
            case MENU:
                this.map = new MenuMap(mapPath);
                break;
        }

        this.gamePort = new FitViewport(map.mapWidth*map.tileSize, map.mapHeight*map.tileSize, gamecam);
        this.gamecam.position.set(gamePort.getWorldWidth() / 2,gamePort.getWorldHeight() /2, 0);

        this.controller = new Controller(this);

    }
    @Override
    public void show() {

    }
    public void handleInput(){
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) || controller.isRightPressed()){
            pacman.nextdirection = Actor.Direction.RIGHT;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || controller.isLeftPressed()){
            pacman.nextdirection = Actor.Direction.LEFT;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP) || controller.isUpPressed()){
            pacman.nextdirection = Actor.Direction.UP;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN) || controller.isDownPressed()){
            pacman.nextdirection = Actor.Direction.DOWN;
        }
        pacman.move();
    }

    public void update(float dt){
        handleInput();

        gamecam.update();

        map.renderer.setView(gamecam);
    }
    @Override
    public abstract void render(float delta);

    @Override
    public void resize(int width, int height) {
        gamePort.update(width,height,false);
        gamePort.getCamera().position.set(map.mapWidth*map.tileSize/2f, map.mapHeight*map.tileSize/2f,0);
        gamePort.getCamera().update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        controller.dispose();
        hud.dispose();
        map.dispose();
    }
}
