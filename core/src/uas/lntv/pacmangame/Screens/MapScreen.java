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

import uas.lntv.pacmangame.Assets;
import uas.lntv.pacmangame.Maps.GameMap;
import uas.lntv.pacmangame.Maps.Map;
import uas.lntv.pacmangame.Maps.MenuMap;
import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Scenes.Controller;
import uas.lntv.pacmangame.Scenes.ControllerSwipe;
import uas.lntv.pacmangame.Scenes.Hud;
import uas.lntv.pacmangame.Scenes.PrefManager;
import uas.lntv.pacmangame.Sprites.Actor;
import uas.lntv.pacmangame.Sprites.Enemy;
import uas.lntv.pacmangame.Sprites.PacMan;

public abstract class MapScreen implements Screen {
    public enum Type {GAME, MENU, SCORE, SETTINGS}

    protected PacManGame game;
    protected OrthographicCamera gameCam;
    protected Viewport gamePort;
    protected Music music;
    protected Music huntingMusic;

    protected final Assets assets;

    public Map map;
    public Hud hud;

    public PacMan pacman;
    protected ArrayList<Enemy> ghosts = new ArrayList<>();

    private final Controller CONTROLLER;

<<<<<<< HEAD
    protected Controller controller;
=======
    private final int MAP_WIDTH;
    private final int MAP_HEIGHT;
    protected final int TILE_SIZE;
>>>>>>> dev_Denis

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
                this.music = playlist.get(random.nextInt(4));
                this.music.setVolume(0.3f);
                break;
        }
        this.huntingMusic = assets.manager.get(assets.HUNTING_MUSIC);
        huntingMusic.setVolume(0.25f);
        huntingMusic.setLooping(true);
        music.setLooping(true);
        if(PrefManager.isMusicOn()) music.play();
        this.MAP_WIDTH = this.map.getMapWidth();
        this.MAP_HEIGHT = this.map.getMapHeight();
        this.TILE_SIZE = this.map.getTileSize();

        this.gamePort = new FitViewport(MAP_WIDTH * TILE_SIZE, MAP_HEIGHT * TILE_SIZE, gameCam);
        this.gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        this.CONTROLLER = new ControllerSwipe(this);
    }

    @Override
    public void show() {  }

    public boolean handleInput() {
        boolean action = false;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || CONTROLLER.isRightPressed()) {
            pacman.setNextDirection(Actor.Direction.RIGHT);
            action = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || CONTROLLER.isLeftPressed()) {
            pacman.setNextDirection(Actor.Direction.LEFT);
            action = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || CONTROLLER.isUpPressed()) {
            pacman.setNextDirection(Actor.Direction.UP);
            action = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || CONTROLLER.isDownPressed()) {
            pacman.setNextDirection(Actor.Direction.DOWN);
            action = true;
        }
        CONTROLLER.pulledInput();
        return action;
    }

    public void update(float dt) {
        if(handleInput()) ready = true;

        pacman.update(dt);
        for (Enemy ghost : ghosts) {
            ghost.update(dt);
        }

        if(!(this instanceof GameScreen) || ready) {
            if (pacman.getState() != Actor.State.DIEING) {
                pacman.move();
                for (Enemy ghost : ghosts) {
                    if (ghost.getState() != Actor.State.KILLED) {
                        ghost.findNextDirection(pacman);
                        ghost.move();
                    } else {
                        if (map.getTile(ghost.getXPosition(), ghost.getYPosition()) != map.getTile(ghost.getStartPosX(), ghost.getStartPosY())) {
                            ghost.texture = assets.manager.get(assets.BLUE_DEAD);
                            ghost.getHome();
                        } else {
                            if (ghost == getGhosts().get(0)) {
                                ghost.texture = assets.manager.get(assets.GHOST_1);
                            }
                            if (getGhosts().size() > 1) {
                                if (ghost == getGhosts().get(1)) {
                                    ghost.texture = assets.manager.get(assets.GHOST_2);
                                }
                                if (getGhosts().size() > 2) {
                                    if (ghost == getGhosts().get(2)) {
                                        ghost.texture = assets.manager.get(assets.GHOST_3);
                                    }
                                }
                            }
                            ghost.setState(Actor.State.RUNNING);
                        }
                    }
                }
            } else {
                for (Enemy ghost : ghosts) {
                    if (map.getTile(ghost.getXPosition(), ghost.getYPosition()) != map.getTile(ghost.getStartPosX(), ghost.getStartPosY())) {
                        ghost.getHome();
                    }
                }
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

        PacManGame.batch.draw(pacman.texture, pacman.getXPosition(), pacman.getYPosition(), pacman.sprite.getOriginX(), pacman.sprite.getOriginY(),
                TILE_SIZE, TILE_SIZE, pacman.sprite.getScaleX(), pacman.sprite.getScaleY(), pacman.rotation,
                pacman.getTexturePositionX(), 0, 32, 32, false, false
        );

        for (Enemy ghost : ghosts) {
            PacManGame.batch.draw(ghost.texture, ghost.getXPosition(), ghost.getYPosition(), ghost.sprite.getOriginX(), ghost.sprite.getOriginY(),
                    TILE_SIZE, TILE_SIZE, ghost.sprite.getScaleX(), ghost.sprite.getScaleY(), ghost.rotation,
                    ghost.getTexturePositionX(), ghost.getTexturePositionY(), 32, 32, false, false
            );
        }

        PacManGame.batch.end();

        PacManGame.batch.setProjectionMatrix(hud.stage.getCamera().combined);
    }

    public ArrayList<Enemy> getGhosts() { return ghosts; }

    public void evolvePacMan() { }

    public void shrinkPacMan() { }

    public void notReady(){ ready = false; }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height, false);
        gamePort.getCamera().position.set(MAP_WIDTH * TILE_SIZE / 2f, MAP_HEIGHT * TILE_SIZE / 2f, 0);
        gamePort.getCamera().update();
    }

    public void switchMusicHunting() {
        if (music.isPlaying() && PrefManager.isMusicOn()) {
            music.pause();
            huntingMusic.play();
        }
    }

    public void switchMusicGame() {
        if(PrefManager.isMusicOn()) {
            huntingMusic.stop();
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
        CONTROLLER.dispose();
        music.dispose();
        huntingMusic.dispose();
    }

}