package uas.lntv.pacmangame.Screens;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import uas.lntv.pacmangame.Maps.GameMap;
import uas.lntv.pacmangame.Maps.Map;
import uas.lntv.pacmangame.Maps.MenuMap;
import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Scenes.Controller;
import uas.lntv.pacmangame.Scenes.ControllerSwipe;
import uas.lntv.pacmangame.Scenes.Hud;
import uas.lntv.pacmangame.Sprites.Actor;
import uas.lntv.pacmangame.Sprites.Enemy;
import uas.lntv.pacmangame.Sprites.PacMan;

public abstract class MapScreen implements Screen {
    public enum Type {GAME, MENU, SCORE};

    protected PacManGame game;
    protected OrthographicCamera gameCam;
    protected Viewport gamePort;
    protected Music music;
    protected Music huntingMusic;

    public Map map;
    public Hud hud;

    public PacMan pacman;
    protected ArrayList<Enemy> ghosts = new ArrayList<Enemy>();

    private Controller controller;

    private final int mapWidth;
    private final int mapHeight;
    protected final int tileSize;

    public MapScreen(PacManGame game, String mapPath, MapScreen.Type type) {
        //Setzt Höhe und Breite des Desktopfensters (16:9 Format)
        if (Gdx.app.getType().equals(Application.ApplicationType.Desktop)) {
            Gdx.graphics.setWindowedMode(450, 800);
        }
        this.game = game;
        this.gameCam = new OrthographicCamera();
        switch (type) {
            case GAME:
                this.map = new GameMap(game, mapPath, this);
                if (((int) (game.getLevel() / 5)) % 2 == 0) {
                    this.music = Gdx.audio.newMusic(Gdx.files.internal("GameMusic.mp3"));
                    music.setVolume(0.2f);
                } else {
                    this.music = Gdx.audio.newMusic(Gdx.files.internal("AmazingHorse.mp3"));
                    music.setVolume(0.4f);
                }
                this.huntingMusic = Gdx.audio.newMusic(Gdx.files.internal("hunting.mp3"));
                huntingMusic.setVolume(0.25f);
                huntingMusic.setLooping(true);
                break;
            case MENU:
                this.map = new MenuMap(mapPath);
                this.music = Gdx.audio.newMusic(Gdx.files.internal("MenuMusic.MP3"));
                music.setVolume(0.3f);
                break;
            case SCORE:
                this.map = new MenuMap(mapPath);
                this.music = Gdx.audio.newMusic(Gdx.files.internal("HeartOfCourage.mp3"));
                music.setVolume(0.4f);
                break;
        }
        music.setLooping(true);
        music.play();
        this.mapWidth = map.getMapWidth();
        this.mapHeight = map.getMapHeight();
        this.tileSize = map.getTileSize();

        this.gamePort = new FitViewport(mapWidth * tileSize, mapHeight * tileSize, gameCam);
        this.gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        this.controller = new ControllerSwipe(this);
    }

    @Override
    public void show() {

    }

    public void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || controller.isRightPressed()) {
            pacman.nextDirection = Actor.Direction.RIGHT;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || controller.isLeftPressed()) {
            pacman.nextDirection = Actor.Direction.LEFT;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || controller.isUpPressed()) {
            pacman.nextDirection = Actor.Direction.UP;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || controller.isDownPressed()) {
            pacman.nextDirection = Actor.Direction.DOWN;
        }
        controller.pulledInput();
    }

    public void update(float dt) {
        handleInput();

        pacman.update(dt);
        for (Enemy ghost : ghosts) {
            ghost.update(dt);
        }

        if (pacman.state != Actor.State.DIEING) {
            pacman.move();
            for (Enemy ghost : ghosts) {
                if (ghost.state != Actor.State.KILLED) {
                    ghost.findNextDirection(pacman);
                    ghost.move();
                } else {
                    if (map.getTile(ghost.getXPosition(), ghost.getYPosition()) != map.getTile(ghost.getStartPosX(), ghost.getStartPosY())) {
                        ghost.texture = new Texture("blue.png");
                        ghost.getHome();
                    } else {
                        if (ghost == getGhosts().get(0)) {
                            ghost.texture = new Texture("redghost.png");
                        }
                        if (game.getLevel() >= 2) {
                            if (ghost == getGhosts().get(1)) {
                                ghost.texture = new Texture("orange.png");
                            }
                            if (game.getLevel() >= 4) {
                                if (ghost == getGhosts().get(2)) {
                                    ghost.texture = new Texture("pinky.png");
                                }
                            }
                        }
                        ghost.state = Actor.State.RUNNING;
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

        gameCam.update();

        map.renderer.setView(gameCam);
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        map.renderer.setView(gameCam);
        map.renderer.render();

        game.batch.begin();

        game.batch.draw(pacman.texture, pacman.getXPosition(), pacman.getYPosition(), pacman.sprite.getOriginX(), pacman.sprite.getOriginY(),
                tileSize, tileSize, pacman.sprite.getScaleX(), pacman.sprite.getScaleY(), pacman.rotation,
                pacman.texturePositionX, 0, 32, 32, false, false
        );

        for (Enemy ghost : ghosts) {
            game.batch.draw(ghost.texture, ghost.xPosition, ghost.yPosition, ghost.sprite.getOriginX(), ghost.sprite.getOriginY(),
                    tileSize, tileSize, ghost.sprite.getScaleX(), ghost.sprite.getScaleY(), ghost.rotation,
                    ghost.texturePositionX, ghost.texturePositionY, 32, 32, false, false
            );
        }

        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.time -= Gdx.graphics.getDeltaTime();
    }

    public ArrayList<Enemy> getGhosts() { return ghosts; }

    public void evolvePacMan() { }

    public void shrinkPacMan() { }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height, false);
        gamePort.getCamera().position.set(mapWidth * tileSize / 2f, mapHeight * tileSize / 2f, 0);
        gamePort.getCamera().update();
    }

    public void switchMusicHunting() {
        if (music.isPlaying()) {
            music.pause();
            huntingMusic.play();
        }
    }

    public void switchMusicGame() {
        huntingMusic.stop();
        music.play();
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
        music.dispose();
        if(this instanceof GameScreen) huntingMusic.dispose();
    }

}