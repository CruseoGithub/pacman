package uas.lntv.pacmangame.Screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import uas.lntv.pacmangame.Managers.Assets;
import uas.lntv.pacmangame.PacManGame;


/**
 * The SplashScreen is the first thing shown when the application starts.
 */
public class SplashScreen implements Screen {

    /* Fields */

    private boolean touchEvent = false;
    private final Assets ASSETS;
    private final OrthographicCamera CAM;
    private final OrthogonalTiledMapRenderer renderer;
    private final PacManGame GAME;
    private final Sprite SPRITE;
    private final Viewport gamePort;
    private final int MAP_HEIGHT;
    private final int MAP_WIDTH;
    private final int TILE_SIZE;
    private final TiledMapTileLayer layerGDX;
    private final TiledMapTileLayer layerLNTV;
    private float alpha = 0;
    private float checkpoint = 0.04f;
    private float progress = 0;
    private float timer = 0;
    private float time = 0;
    private int texturePositionX = 0;
    private int texturePositionY = 0;
    private TiledMapTileLayer visibleLayer;

    /* Constructor */

    /**
     * Main constructor of the SplashScreen
     * @param GAME running game
     * @param ASSETS asset management
     */
    public SplashScreen(final PacManGame GAME, final Assets ASSETS){
        //sets height and width of the desktop window (16:9-format)
        if (Gdx.app.getType().equals(Application.ApplicationType.Desktop)) {
            Gdx.graphics.setWindowedMode(450, 800);
        }

        this.GAME = GAME;
        this.ASSETS = ASSETS;

        TmxMapLoader tmxMapLoader = new TmxMapLoader();
        TiledMap TMX_MAP = tmxMapLoader.load(ASSETS.SPLASH);

        this.MAP_WIDTH = Integer.parseInt(TMX_MAP.getProperties().get("width").toString());
        this.MAP_HEIGHT = Integer.parseInt(TMX_MAP.getProperties().get("height").toString());
        this.TILE_SIZE = Integer.parseInt(TMX_MAP.getProperties().get("tilewidth").toString());
        this.renderer = new OrthogonalTiledMapRenderer(TMX_MAP);
        this.CAM = new OrthographicCamera();
        this.gamePort = new FitViewport(MAP_WIDTH * TILE_SIZE, MAP_HEIGHT * TILE_SIZE, CAM);
        this.CAM.position.set((MAP_WIDTH * TILE_SIZE) / 2f, (MAP_WIDTH * TILE_SIZE) / 2f, 0);

        this.layerLNTV = (TiledMapTileLayer) TMX_MAP.getLayers().get("Walls");
        this.layerGDX = (TiledMapTileLayer) TMX_MAP.getLayers().get("Path");
        this.visibleLayer = layerLNTV;

        this.layerLNTV.setOpacity(0f);

        this.layerLNTV.setVisible(true);

        this.ASSETS.manager.get(this.ASSETS.DIAL_UP).play(0.4f);

        this.ASSETS.load();
        this.SPRITE = new Sprite(this.ASSETS.manager.get(ASSETS.LOADING));

        Gdx.input.setInputProcessor(new InputAdapter(){
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if(touchEvent) {
                    timer = 10;
                    ASSETS.manager.get(ASSETS.DIAL_UP).stop();
                }
                else touchEvent = true;
                return true;
            }
        });

    }

    /* Methods */

    /**
     * Uses the update method to check the time and draws the logo on the screen.
     * @param delta time parameter used by libGDX
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.setView(CAM);
        renderer.render();

        update();


    }

    /**
     * rizises the splashscreen to the new values
     * @param width new width of the screen
     * @param height new height of the screen
     */
    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height, false);
        gamePort.getCamera().position.set(MAP_WIDTH * TILE_SIZE / 2f, MAP_HEIGHT * TILE_SIZE / 2f, 0);
        gamePort.getCamera().update();
    }

    /**
     * displays the the logos and then moves on to displaying the loading pacman icon
     */
    private void update(){
        time = Gdx.graphics.getDeltaTime();
        timer += time;
        if(timer < 10) updateSplash();
        else updateLoading();
    }

    /**
     * Displays the the loading pacman icon in the middle of the screen.
     * The Icon shows the progess toward loading the game. When finished it changes to the menu screen.
     */
    private void updateLoading(){
        visibleLayer.setVisible(false);

        //Grabing the center point of the Screen
        Vector3 center = new Vector3((float)(Gdx.graphics.getWidth()) / 2, (float)(Gdx.graphics.getHeight()) / 2, 0);
        CAM.unproject(center);

        //Move the center of Sprite from left/bottom corner to center
        center.x -= 256 / 2f;
        center.y -= 256 / 2f;

        PacManGame.batch.begin();
        PacManGame.batch.setProjectionMatrix(CAM.combined);

        //Draws the Pacman icon on the screen
        PacManGame.batch.draw(SPRITE.getTexture(), center.x, center.y, SPRITE.getOriginX(), SPRITE.getOriginY(),
                256, 256, SPRITE.getScaleX(), SPRITE.getScaleY(), 0,
                texturePositionX, texturePositionY, 256, 256, false, false
        );
        PacManGame.batch.end();

        //lerps to the next texture position. when progress is finished it loads the menu screen.
        progress = MathUtils.lerp(progress, ASSETS.manager.getProgress(), 0.025f);
        if(progress > checkpoint){
            checkpoint += 0.04f;
            if(texturePositionX < 1024) texturePositionX += 256;
            else{
                texturePositionX = 0;
                texturePositionY += 256;
            }
        }
        if(ASSETS.manager.update() && progress > 0.99f){
            this.dispose();
            GAME.setScreen(new MenuScreen(GAME, ASSETS, ASSETS.MENU_MAP));
        }

    }

    /**
     * Checks the time, the SplashScreen is shown yet and reacts according to it.
     * It changes the used logo and simulates the fade-effects.
     */
    private void updateSplash() {
        if(timer>=5 && visibleLayer == layerLNTV){
            visibleLayer.setVisible(false);
            visibleLayer = layerGDX;
            visibleLayer.setOpacity(0f);
            visibleLayer.setVisible(true);
        }

        visibleLayer.setOpacity(alpha);

        if(timer < 2) alpha += time / 2;
        if(timer > 2 && timer < 3) alpha = 1;
        if(timer > 3 && timer < 5) alpha -= time / 2;
        if(timer > 5 && timer < 7) alpha +=  time / 2;
        if(timer > 7 && timer < 8) alpha = 1;
        if(timer > 8 && timer < 10) alpha -= time / 2;
    }

    /* Unused methods that needed to be adopted from libGDX' Screen class. */

    @Override
    public void dispose() {  }

    @Override
    public void hide() {  }

    @Override
    public void pause() {  }

    @Override
    public void resume() {  }

    @Override
    public void show() { }

}
