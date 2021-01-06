package com.vieth.pacman.Screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.*;
import com.vieth.pacman.Controller;
import com.vieth.pacman.Pathfinder;
import com.vieth.pacman.Scenes.Hud;
import com.vieth.pacman.PacMan;
import com.vieth.pacman.Scenes.Map;
import com.vieth.pacman.Sprites.Player;
import com.vieth.pacman.Sprites.Tile;
import com.vieth.pacman.Sprites.Enemy;


public class GameScreen implements Screen {

    private PacMan game;
    private OrthographicCamera gamecam;
    private Viewport gamePort;

    public Map map;
    public Hud hud;
    public Controller controller;
    Player pacman;
    Enemy blinky;
/*  Enemy clyde;
    Enemy inky;
    Enemy pinky;
*/

    private float tmpTimerAnimation = 0;

    public GameScreen(PacMan game){
        //Setzt Höhe und Breite des Desktopfensters (16:9 Format)
        if (Gdx.app.getType().equals(Application.ApplicationType.Desktop)) {
            Gdx.graphics.setWindowedMode(450, 800);
        }
        this.game = game;
        this.gamecam = new OrthographicCamera();
        this.map = new Map("map5.tmx", this);

        this.gamePort = new FitViewport(map.mapWidth*map.tileSize, map.mapHeight*map.tileSize, gamecam);
        this.gamecam.position.set(gamePort.getWorldWidth() / 2,gamePort.getWorldHeight() /2, 0);

        this.hud = new Hud(game.batch, this);
        this.controller = new Controller();

        this.pacman = new Player(map.tileSize, 17*map.tileSize, this, hud);
        this.blinky = new Enemy(map.tileSize, 40*map.tileSize, this, "blinky.png", Enemy.Difficulty.HARD);
/*        clyde = new Enemy(map.tileSize, 40*map.tileSize, this, "clyde.png", Enemy.Difficulty.MEDIUM);
        inky = new Enemy(map.tileSize, 40*map.tileSize, this, "inky.png", Enemy.Difficulty.EASY);
        pinky = new Enemy(map.tileSize, 40*map.tileSize, this, "pinky.png", Enemy.Difficulty.EASY);*/
    }

    @Override
    public void show() {
    }

    public void handleInput(float dt){
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) || controller.isRightPressed()){
            pacman.nextdirection = Player.Direction.RIGHT;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || controller.isLeftPressed()){
            pacman.nextdirection = Player.Direction.LEFT;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP) || controller.isUpPressed()){
            pacman.nextdirection = Player.Direction.UP;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN) || controller.isDownPressed()){
            pacman.nextdirection = Player.Direction.DOWN;
        }
        pacman.move();
    }

    public void update(float dt){
        handleInput(dt);

        gamecam.update();

        map.renderer.setView(gamecam);
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        blinky.findNextDirection(map.matrix, pacman);
        blinky.move();
/*        clyde.findNextDirection(tileMatrix, pacman);
        clyde.move();
        inky.findNextDirection(tileMatrix, pacman);
        inky.move();
        pinky.findNextDirection(tileMatrix, pacman);
        pinky.move();*/

        //Animation alle 0.5 Sekunden
        if ((tmpTimerAnimation + 0.5f) <= hud.time) {
            if (pacman.texturePositionX == 0) {
                pacman.texturePositionX = 96;

            } else {
                pacman.texturePositionX = 0;
            }
            tmpTimerAnimation = hud.time;
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


        game.batch.draw(blinky.sprite, blinky.getXPosition(), blinky.getYPosition(), map.tileSize, map.tileSize);
/*        game.batch.draw(clyde.sprite, clyde.getXPosition() , clyde.getYPosition() , map.tileSize, map.tileSize);
        game.batch.draw(inky.sprite, inky.getXPosition() , inky.getYPosition() , map.tileSize, map.tileSize);
        game.batch.draw(pinky.sprite, pinky.getXPosition() , pinky.getYPosition() , map.tileSize, map.tileSize); */
        game.batch.end();

        controller.draw();


        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.time += Gdx.graphics.getDeltaTime();
        hud.update();
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        //gamePort.update(width, height);
        gamePort.update(width,height,false);
        gamePort.getCamera().position.set(map.mapWidth*map.tileSize/2f, map.mapHeight*map.tileSize/2f,0);
        gamePort.getCamera().update();
        //controller.resize(width, height);
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() { }

}
