package uas.lntv.pacmangame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;

import uas.lntv.pacmangame.Managers.Assets;
import uas.lntv.pacmangame.PacManGame;

/**
 * The LoadingScreen shows the progress of asset loading in a visual way.
 */
public class LoadingScreen implements Screen {
    private final PacManGame GAME;
    private final Assets ASSETS;
    private final OrthographicCamera CAM;
    private final Sprite SPRITE;

    private float checkpoint = 0.04f;
    private int texturePositionX = 0;
    private int texturePositionY = 0;
    private float progress = 0;

    /**
     * Main constructor of the LoadingScreen
     * @param GAME running game
     * @param ASSETS asset management
     */
    public LoadingScreen(final PacManGame GAME, final Assets ASSETS){
        this.GAME = GAME;
        this.ASSETS = ASSETS;
        this.ASSETS.load();
        this.SPRITE = new Sprite(this.ASSETS.manager.get(ASSETS.LOADING));
        this.CAM = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        CAM.translate(CAM.viewportWidth/2, CAM.viewportHeight/2);
    }

    @Override
    public void show() {  }

    /**
     * Checks the progress of the loading process of the asset-manager and changes the shown
     * part of the loading texture
     */
    private void update(){
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
        CAM.update();
    }

    /**
     * Uses the update method to check the progress and draws the loading progress visualization
     * on the screen
     * @param delta time parameter used by libGDX
     */
    @Override
    public void render(float delta) {
        update();


        PacManGame.batch.begin();
        PacManGame.batch.setProjectionMatrix(CAM.combined);
        PacManGame.batch.draw(SPRITE.getTexture(), 100, 300, SPRITE.getOriginX(), SPRITE.getOriginY(),
                256, 256, SPRITE.getScaleX(), SPRITE.getScaleY(), 0,
                texturePositionX, texturePositionY, 256, 256, false, false
        );
        PacManGame.batch.end();
    }

    @Override
    public void resize(int width, int height) {  }

    @Override
    public void pause() {  }

    @Override
    public void resume() {  }

    @Override
    public void hide() {  }

    @Override
    public void dispose() {  }

}
