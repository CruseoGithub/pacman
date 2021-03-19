package uas.lntv.pacmangame.Screens;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Random;

import uas.lntv.pacmangame.Managers.Assets;
import uas.lntv.pacmangame.Maps.GameMap;
import uas.lntv.pacmangame.Maps.Map;
import uas.lntv.pacmangame.Maps.MenuMap;
import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Scenes.Controller;
import uas.lntv.pacmangame.Scenes.ControllerButtons;
import uas.lntv.pacmangame.Scenes.ControllerJoystick;
import uas.lntv.pacmangame.Scenes.Hud;
import uas.lntv.pacmangame.Managers.PrefManager;
import uas.lntv.pacmangame.Sprites.Actor;
import uas.lntv.pacmangame.Sprites.Enemy;
import uas.lntv.pacmangame.Sprites.Joystick;
import uas.lntv.pacmangame.Sprites.PacMan;

public abstract class MapScreen implements Screen {
    public enum Type {GAME, MENU, SCORE, SETTINGS}

    protected PacManGame game;
    protected OrthographicCamera gameCam;
    protected Viewport gamePort;
    protected Music music;

    protected final Assets assets;

    public Map map;
    public Hud hud;

    public PacMan pacman;
    protected ArrayList<Enemy> ghosts = new ArrayList<>();


    protected  Controller controller;
    protected Joystick joystick;


    private final int MAP_WIDTH;
    private final int MAP_HEIGHT;
    protected final int TILE_SIZE;


    protected boolean ready = false;

    public MapScreen(PacManGame game, Assets assets, String path, MapScreen.Type type) {
        //Setzt Höhe und Breite des Desktopfensters (16:9 Format)
        if (Gdx.app.getType().equals(Application.ApplicationType.Desktop)) {
            Gdx.graphics.setWindowedMode(450, 800);
        }
        this.assets = assets;
        ArrayList<Music> playlist = new ArrayList<>();
        playlist.add(assets.manager.get(assets.GAME_MUSIC));
        playlist.add(assets.manager.get(assets.GAME_MUSIC_2));
        playlist.add(assets.manager.get(assets.GAME_MUSIC_3));
        playlist.add(assets.manager.get(assets.GAME_MUSIC_4));
        this.game = game;
        this.gameCam = new OrthographicCamera();
        Random random = new Random();
        switch (type) {
            case GAME:
                this.map = new GameMap(assets, path, this);
                this.music = playlist.get(random.nextInt(4));
                this.music.setVolume(0.3f);
                break;
            case MENU:
                this.map = new MenuMap(path, assets);
                this.music = assets.manager.get(assets.MENU_MUSIC);
                music.setVolume(0.3f);
                break;
            case SCORE:
                this.map = new MenuMap(path, assets);
                this.music = assets.manager.get(assets.SCORE_MUSIC);
                music.setVolume(0.4f);
                break;
            case SETTINGS:
                this.map = new MenuMap(path, assets);
                this.music = assets.manager.get(assets.SETTINGS_MUSIC);
                this.music.setVolume(0.3f);
                break;
        }
        assets.manager.get(assets.HUNTING_MUSIC).setVolume(0.25f);
        assets.manager.get(assets.HUNTING_MUSIC).setLooping(true);
        music.setLooping(true);
        if(PrefManager.isMusicOn()) music.play();
        this.MAP_WIDTH = this.map.getMapWidth();
        this.MAP_HEIGHT = this.map.getMapHeight();
        this.TILE_SIZE = this.map.getTileSize();

        this.gamePort = new FitViewport(MAP_WIDTH * TILE_SIZE, MAP_HEIGHT * TILE_SIZE, gameCam);
        this.gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        
        if(PrefManager.isJoystick()) this.controller = new ControllerJoystick(assets, this);
        else this.controller = new ControllerButtons(assets,this);

    }


    @Override
    public void show() {  }

    public boolean handleInput() {
        boolean action = false;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || controller.isRightPressed()) {
            pacman.setNextDirection(Actor.Direction.RIGHT);
            action = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || controller.isLeftPressed()) {
            pacman.setNextDirection(Actor.Direction.LEFT);
            action = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || controller.isUpPressed()) {
            pacman.setNextDirection(Actor.Direction.UP);
            action = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || controller.isDownPressed()) {
            pacman.setNextDirection(Actor.Direction.DOWN);
            action = true;
        }
        controller.pulledInput();
        return action;
    }

    public void update(float dt) {
        if(handleInput()) ready = true;

        if(!(this instanceof GameScreen) || ready) {
            pacman.update(dt);
            for (Enemy ghost : ghosts) {
                ghost.update(dt, pacman);
            }
        }

        gameCam.update();
        map.renderer.setView(gameCam);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        map.renderer.setView(gameCam);
        map.renderer.render();

        PacManGame.batch.begin();
        pacman.draw();

        for (Enemy ghost : ghosts) {
            ghost.draw();
        }

        if(controller instanceof ControllerJoystick){
            if(controller.isTouchEvent()){
                joystick = ((ControllerJoystick) controller).joystick;
                joystick.draw();
            }
        }

        PacManGame.batch.end();

        PacManGame.batch.setProjectionMatrix(hud.stage.getCamera().combined);
    }

    public ArrayList<Enemy> getGhosts() { return ghosts; }

    public void evolvePacMan() { }

    public void shrinkPacMan() { }

    public void notReady(){
        for(Enemy ghost : ghosts) ghost.notHome();
        ready = false;
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height, false);
        gamePort.getCamera().position.set(MAP_WIDTH * TILE_SIZE / 2f, MAP_HEIGHT * TILE_SIZE / 2f, 0);
        gamePort.getCamera().update();
    }

    public void switchMusicHunting() {
        if (music.isPlaying() && PrefManager.isMusicOn()) {
            music.pause();
            assets.manager.get(assets.HUNTING_MUSIC).play();
        }
    }

    public void switchMusicGame() {
        if(PrefManager.isMusicOn()) {
            assets.manager.get(assets.HUNTING_MUSIC).stop();
            music.play();
        }
    }


    @Override
    public void pause() { }

    @Override
    public void resume() {  }

    @Override
    public void hide() {  }

    @Override
    public void dispose() {
        controller.dispose();
        assets.manager.get(assets.HUNTING_MUSIC).stop();
        music.dispose();
    }

}