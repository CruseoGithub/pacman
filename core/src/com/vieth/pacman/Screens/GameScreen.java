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
import com.vieth.pacman.Sprites.Tile;
import com.vieth.pacman.Sprites.Enemy;


public class GameScreen implements Screen {
    private PacMan game;
    private OrthographicCamera gamecam;
    private Viewport gamePort;
    public Hud hud;
    private TmxMapLoader maploader;
    public TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    Player pacman;
    Enemy ghost;

    private float tmpTimerAnimation = 0;

    public Tile tileMatrix[][];

    public GameScreen(PacMan game){
        this.game = game;

        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(PacMan.V_WIDTH, PacMan.V_HEIGHT, gamecam);

        hud = new Hud(game.batch);

        maploader = new TmxMapLoader();
        map = maploader.load("map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        gamecam.position.set(gamePort.getWorldWidth() / 2,gamePort.getWorldHeight() /2, 0);

        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get(0);
        TiledMapTileLayer layerDots = (TiledMapTileLayer)map.getLayers().get(1);
        tileMatrix = new Tile[PacMan.V_WIDTH/8][PacMan.V_HEIGHT/8];
        for(int x = 0; x < PacMan.V_WIDTH/8; x++){
            for(int y = 0; y < PacMan.V_HEIGHT/8; y++){
                if(layer.getCell(x, y) == null){
                    tileMatrix[x][y] = new Tile(Tile.Type.PATH, ((x*8)), ((y*8)));
                    if(layerDots.getCell(x,y) != null) tileMatrix[x][y].isDot = true;
                }
                else {
                    tileMatrix[x][y] = new Tile(Tile.Type.WALL, ((x*8)), ((y*8)));
                }

            }
        }

        pacman = new Player(8, 136, this);
        ghost = new Enemy(120,224,this);
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

        ghost.nextdirection = Enemy.Direction.getRandomDirection();
        ghost.move();

        //Animation alle 0.5 Sekunden
        if((tmpTimerAnimation+0.5f) <= hud.time) {
            if(pacman.texturePositionX == 0){
                pacman.texturePositionX = 180;
            }else{
                pacman.texturePositionX = 0;
            }
            tmpTimerAnimation = hud.time;
        }

        game.batch.begin();

        //Neuer Draw Befehl, der die Rotation mit berechnet
        game.batch.draw(pacman.texture,pacman.x,pacman.y,pacman.sprite.getOriginX(), pacman.sprite.getOriginY(),
                8,8, pacman.sprite.getScaleX(), pacman.sprite.getScaleY(), pacman.rotation,
                pacman.texturePositionX,0,60,60,true,false);

        game.batch.draw(ghost.sprite, ghost.x , ghost.y , 8, 8);
        game.batch.end();

        renderer.setView(gamecam);
        renderer.render();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.time +=Gdx.graphics.getDeltaTime();
        hud.update();
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
