package uas.lntv.pacmangame.Screens;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
    public enum Type { GAME, MENU, SCORE};

    protected PacManGame game;
    protected OrthographicCamera gamecam;
    protected Viewport gamePort;
    protected Music music;

    public Map map;
    public Hud hud;

    public PacMan pacman;
    public Enemy ghost;

    protected Controller controller;

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
                this.map = new GameMap(game, mapPath, this);
                if(((int)(game.getLevel()/5))%2 == 0) {
                    this.music = Gdx.audio.newMusic(Gdx.files.internal("GameMusic.mp3"));
                    music.setVolume(0.2f);
                } else{
                    this.music = Gdx.audio.newMusic(Gdx.files.internal("AmazingHorse.mp3"));
                    music.setVolume(0.4f);
                }
                music.setLooping(true);
                music.play();
                break;
            case MENU:
                this.map = new MenuMap(mapPath);
                this.music = Gdx.audio.newMusic(Gdx.files.internal("MenuMusic.wav"));
                music.setVolume(0.3f);
                music.setLooping(true);
                music.play();
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
    public void render(float delta){
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        ghost.findNextDirection(pacman);
        ghost.move();

        //Animation alle 0.5 Sekunden
        if ((tmpTimerAnimation + 0.5f) <= hud.animationTime) {
            if (pacman.texturePositionX == 0) {
                pacman.texturePositionX = 96;
            } else {
                pacman.texturePositionX = 0;
            }
            tmpTimerAnimation = hud.animationTime;
        }

        map.renderer.setView(gamecam);
        map.renderer.render();

        game.batch.begin();

        //Neuer Draw Befehl, der die Rotation mit berechnet
        /*game.batch.draw(pacman.texture,pacman.getXPosition(),pacman.getYPosition(),pacman.sprite.getOriginX(), pacman.sprite.getOriginY(),
                map.tileSize,map.tileSize, pacman.sprite.getScaleX(), pacman.sprite.getScaleY(), pacman.rotation,
                pacman.texturePositionX,0,60,60,true,false);*/
        game.batch.draw(pacman.texture, pacman.getXPosition(), pacman.getYPosition(), pacman.sprite.getOriginX(), pacman.sprite.getOriginY(),
                map.tileSize, map.tileSize, pacman.sprite.getScaleX(), pacman.sprite.getScaleY(), pacman.rotation,
                pacman.texturePositionX, 0, 32, 32, false, false);

        game.batch.draw(ghost.sprite, ghost.xPosition, ghost.yPosition, map.tileSize, map.tileSize);
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.time -= Gdx.graphics.getDeltaTime();
        hud.animationTime += Gdx.graphics.getDeltaTime();
    }

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
        music.dispose();
    }
}
