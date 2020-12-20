package com.vieth.pacman.Screens;

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
import com.vieth.pacman.Scenes.Hud;
import com.vieth.pacman.PacMan;
import com.vieth.pacman.Sprites.Player;


public class GameScreen implements Screen {
    private PacMan game;
    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Hud hud;
    private TmxMapLoader maploader;
    public TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private Box2DDebugRenderer b2dr;

    private Texture pacManTex;
    private Sprite player;
    private int playerX;
    private int playerY;
    Player pacman;

    public GameScreen(PacMan game){
        this.game = game;

        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(PacMan.V_WIDTH, PacMan.V_HEIGHT, gamecam);

        hud = new Hud(game.batch);

        maploader = new TmxMapLoader();
        map = maploader.load("map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        gamecam.position.set(gamePort.getWorldWidth() / 2,gamePort.getWorldHeight() /2, 0);


        world = new World(new Vector2(0,0), true);
        b2dr = new Box2DDebugRenderer();

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        pacman = new Player(8, 8, this);

    }
    @Override
    public void show() {

    }
    public void handleInput(float dt){
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            pacman.nextdirection = Player.Direction.RIGHT;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            pacman.nextdirection = Player.Direction.LEFT;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            pacman.nextdirection = Player.Direction.UP;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            pacman.nextdirection = Player.Direction.DOWN;
        }
        pacman.move();
    }
    public void update(float dt){
        handleInput(dt);

        gamecam.update();

        renderer.setView(gamecam);
    }
    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(pacman.sprite, pacman.x , pacman.y , 8, 8);
        game.batch.end();

        renderer.setView(gamecam);
        renderer.render();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();


    }

    @Override
    public void resize(int width, int height) {
        //gamePort.update(width, height);
        gamePort.update(width,height,false);
        gamePort.getCamera().position.set(PacMan.V_WIDTH/2f,PacMan.V_HEIGHT/2f,0);
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

    }
}
