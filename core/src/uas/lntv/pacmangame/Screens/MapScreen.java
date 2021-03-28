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
import uas.lntv.pacmangame.Maps.Tile;
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

/**
 * Abstract class for almost every screen, that will be used in our PacManGame.
 * (Except of the SplashScreen)
 */
public abstract class MapScreen implements Screen {

    /* Fields */

    public enum Type {GAME, MENU, SCORE, SETTINGS}

    protected boolean ready = false;
    protected Controller controller;
    protected final ArrayList<Enemy> GHOSTS = new ArrayList<>();
    protected final Assets ASSETS;
    protected final int TILE_SIZE;
    protected final OrthographicCamera GAME_CAM;
    protected final PacManGame GAME;
    protected final Viewport GAME_PORT;
    protected Hud hud;
    protected Map map;
    protected Music music;
    protected Joystick joystick;
    protected PacMan pacman;

    /* Constructor */

    /**
     * Abstract constructor, that creates the screens depending on the type of screen with all
     * the basic features that are needed in every screen.
     * @param game the running PacManGame
     * @param assets the assets manager
     * @param path the path to the map that needs to be loaded for the screen
     * @param type the type of screen
     */
    public MapScreen(PacManGame game, Assets assets, String path, MapScreen.Type type) {
        //sets height and width of the desktop window (16:9-format)
        if (Gdx.app.getType().equals(Application.ApplicationType.Desktop)) {
            Gdx.graphics.setWindowedMode(450, 800);
        }
        this.GAME = game;
        this.ASSETS = assets;

        ArrayList<Music> playlist = new ArrayList<>();
        playlist.add(assets.manager.get(assets.GAME_MUSIC));
        playlist.add(assets.manager.get(assets.GAME_MUSIC_2));
        playlist.add(assets.manager.get(assets.GAME_MUSIC_3));
        playlist.add(assets.manager.get(assets.GAME_MUSIC_4));
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

        this.TILE_SIZE = Map.getTileSize();
        this.GAME_CAM = new OrthographicCamera();
        this.GAME_PORT = new FitViewport(Map.getMapWidth() * TILE_SIZE, Map.getMapHeight() * TILE_SIZE, GAME_CAM);
        this.GAME_CAM.position.set(this.GAME_PORT.getWorldWidth() / 2, this.GAME_PORT.getWorldHeight() / 2, 0);

        if(PrefManager.isJoystick()) this.controller = new ControllerJoystick(assets, this);
        else this.controller = new ControllerButtons(assets,this);
    }

    /* Accessor */

    public ArrayList<Enemy> getGhosts() { return this.GHOSTS; }

    public Hud getHud(){ return this.hud; }

    public Map getMap(){ return this.map; }

    public PacMan getPacman(){ return this.pacman; }

    /* Methods */

    /**
     * Detects, if a key on the keyboard has been pressed (Desktop version) or if the Controller
     * has been used on the touchscreen of the mobile application.
     * @return true if there has been action
     */
    protected boolean handleInput() {
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

    /**
     * Turns hunting music off and starts the game music from the point where it has been stopped,
     * when the hunter item was eaten. This method is called, after the buff time of the hunter
     * item expired.
     */
    protected void switchMusicGame() {
        if(PrefManager.isMusicOn()) {
            ASSETS.manager.get(ASSETS.HUNTING_MUSIC).stop();
            music.play();
        }
    }

    /**
     * Switches the game music to the hunting music. It's called when PacMan ate a cherry-coin.
     */
    protected void switchMusicHunting() {
        if (music.isPlaying() && PrefManager.isMusicOn()) {
            music.pause();
            ASSETS.manager.get(ASSETS.HUNTING_MUSIC).play();
        }
    }

    /**
     * Empty method that's only used in GameScreen
     * @param buffType the type of buff
     */
    public void activateBuff(Tile.Item buffType) { }

    /**
     * This method is called, when the screen is closed. It disposes all disposable instances, that
     * have been created and stops the running music of the current screen.
     */
    @Override
    public void dispose() {
        controller.dispose();
        ASSETS.manager.get(ASSETS.HUNTING_MUSIC).stop();
        music.dispose();
        hud.dispose();
    }

    /**
     * This method is called, when a new level starts or when PacMan dies. It resets the 'home'
     * status of the ghosts and tells the game, that the player is not ready yet until he starts
     * the game by using the controls for moving.
     */
    public void notReady(){
        for(Enemy ghost : GHOSTS) ghost.notHome();
        ready = false;
    }

    /**
     * This method renders the screen. It draws the map, new state of PacMan and the ghosts and
     * also the position of the joystick, if used.
     * @param delta time parameter used by libGDX
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        map.renderer.setView(GAME_CAM);
        map.renderer.render();

        PacManGame.batch.begin();
        pacman.draw();

        for (Enemy ghost : GHOSTS) {
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

    /**
     * Checks the state of the game and updates the textures and positions of the sprites.
     * This method is always called in the render-methods of the child classes.
     * @param dt time parameter used by libGDX
     */
    public void update(float dt) {
        if(handleInput()) ready = true;

        if(!(this instanceof GameScreen) || ready) {
            pacman.update(dt);
            for (Enemy ghost : GHOSTS) {
                ghost.update(dt, pacman);
            }
        }

        GAME_CAM.update();
        map.renderer.setView(GAME_CAM);
    }


    /* Unused methods that needed to be adopted from libGDX' Screen class. */

    @Override
    public void hide() {  }

    @Override
    public void pause() { }

    @Override
    public void resize(int width, int height) {
        GAME_PORT.update(width, height, false);
        GAME_PORT.getCamera().position.set(Map.getMapWidth() * TILE_SIZE / 2f, Map.getMapHeight() * TILE_SIZE / 2f, 0);
        GAME_PORT.getCamera().update();
    }

    @Override
    public void resume() {  }

    @Override
    public void show() {  }

}