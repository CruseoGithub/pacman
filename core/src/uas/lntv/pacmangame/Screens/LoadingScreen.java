package uas.lntv.pacmangame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;

import uas.lntv.pacmangame.Assets;
import uas.lntv.pacmangame.PacManGame;

public class LoadingScreen implements Screen {
    private final PacManGame GAME;
    private final Assets ASSETS;
    private final OrthographicCamera CAM;

    private final Sprite SPRITE;

    private float checkpoint = 0.04f;
    private int texturePositionX = 0;
    private int texturePositionY = 0;
    private float progress = 0;

    public LoadingScreen(final PacManGame GAME, final Assets ASSETS){
        this.GAME = GAME;
        this.ASSETS = ASSETS;
        this.ASSETS.load();
        this.SPRITE = new Sprite(new Texture("setup/LoadingPacMan.png"));
        this.CAM = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        CAM.translate(CAM.viewportWidth/2, CAM.viewportHeight/2);
    }

    @Override
    public void show() {  }

    private void update(){
        progress = MathUtils.lerp(progress, ASSETS.manager.getProgress(), 0.05f);
        if(progress > checkpoint){
            checkpoint += 0.04f;
            if(texturePositionX < 128) texturePositionX += 32;
            else{
                texturePositionX = 0;
                texturePositionY += 32;
            }
        }
        if(ASSETS.manager.update() && progress > 0.99f){
            GAME.setScreen(new MenuScreen(GAME, ASSETS, ASSETS.MENU_MAP));
            this.dispose();
        }
        CAM.update();
    }

    @Override
    public void render(float delta) {
        update();


        PacManGame.batch.begin();
        PacManGame.batch.setProjectionMatrix(CAM.combined);
        PacManGame.batch.draw(SPRITE.getTexture(), 300, 300, SPRITE.getOriginX(), SPRITE.getOriginY(),
                96, 96, SPRITE.getScaleX(), SPRITE.getScaleY(), 0,
                texturePositionX, texturePositionY, 32, 32, false, false
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
    public void dispose() {
        SPRITE.getTexture().dispose();
    }

}
