package uas.lntv.pacmangame.Screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import uas.lntv.pacmangame.Managers.Assets;
import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Managers.PrefManager;

/**
 * The SplashScreen is the first thing shown when the application starts.
 */
public class SplashScreen implements Screen {
    private final PacManGame GAME;
    private final Assets ASSETS;

    private final OrthographicCamera CAM;
    private final Viewport gamePort;

    private final OrthogonalTiledMapRenderer renderer;
    private final TiledMap TMX_MAP;

    private final int MAP_WIDTH;
    private final int MAP_HEIGHT;
    private final int TILE_SIZE;

    private final TiledMapTileLayer layerLNTV;
    private final TiledMapTileLayer layerGDX;
    private TiledMapTileLayer visibleLayer;

    private float timer = 0;
    private float alpha = 0;

    /**
     * Main constructor of the SplashScreen
     * @param GAME running game
     * @param ASSETS asset management
     */
    public SplashScreen(final PacManGame GAME, final Assets ASSETS){
        this.GAME = GAME;
        this.ASSETS = ASSETS;

        TmxMapLoader tmxMapLoader = new TmxMapLoader();
        this.TMX_MAP = tmxMapLoader.load("maps/splash.tmx");

        this.MAP_WIDTH = Integer.parseInt(TMX_MAP.getProperties().get("width").toString());
        this.MAP_HEIGHT = Integer.parseInt(TMX_MAP.getProperties().get("height").toString());
        this.TILE_SIZE = Integer.parseInt(TMX_MAP.getProperties().get("tilewidth").toString());
        this.renderer = new OrthogonalTiledMapRenderer(TMX_MAP);
        this.CAM = new OrthographicCamera();
        this.gamePort = new FitViewport(MAP_WIDTH * TILE_SIZE, MAP_HEIGHT * TILE_SIZE, CAM);
        this.CAM.position.set((float)(MAP_WIDTH * TILE_SIZE) / 2, (float)(MAP_WIDTH * TILE_SIZE) / 2, 0);

        this.layerLNTV = (TiledMapTileLayer) TMX_MAP.getLayers().get("Walls");
        this.layerGDX = (TiledMapTileLayer) TMX_MAP.getLayers().get("Path");
        this.visibleLayer = layerLNTV;

        this.layerLNTV.setOpacity(0f);
        this.layerLNTV.setVisible(true);
    }

    @Override
    public void show() { }

    /**
     * Checks the time, the SplashScreen is shown yet and reacts according to it.
     * It changes the used logo and simulates the fade-effects.
     */
    private void update(){
        float time = Gdx.graphics.getDeltaTime();
        timer += time;

        if(timer>=5 && visibleLayer == layerLNTV){
            visibleLayer.setVisible(false);
            visibleLayer = layerGDX;
            visibleLayer.setOpacity(0f);
            visibleLayer.setVisible(true);
        }
        else if(timer > 10) {
            this.dispose();
            GAME.setScreen(new LoadingScreen(GAME, ASSETS));
        }

        visibleLayer.setOpacity(alpha);

        if(timer < 2) alpha += time / 2;
        if(timer > 2 && timer < 3) alpha = 1;
        if(timer > 3 && timer < 5) alpha -= time / 2;
        if(timer > 5 && timer < 7) alpha +=  time / 2;
        if(timer > 7 && timer < 8) alpha = 1;
        if(timer > 8 && timer < 10) alpha -= time / 2;
    }

    /**
     * Uses the update method to check the time and draws the logo on the screen.
     * @param delta time parameter used by libGDX
     */
    @Override
    public void render(float delta) {
        update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.setView(CAM);
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height, false);
        gamePort.getCamera().position.set(MAP_WIDTH * TILE_SIZE / 2f, MAP_HEIGHT * TILE_SIZE / 2f, 0);
        gamePort.getCamera().update();
    }

    @Override
    public void pause() {  }

    @Override
    public void resume() {  }

    @Override
    public void hide() {  }

    @Override
    public void dispose() {  }

}
