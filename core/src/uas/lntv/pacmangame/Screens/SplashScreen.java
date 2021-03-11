package uas.lntv.pacmangame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import uas.lntv.pacmangame.Assets;
import uas.lntv.pacmangame.PacManGame;

public class SplashScreen implements Screen {
    private final PacManGame GAME;
    private final Assets ASSETS;
    private final OrthographicCamera CAM;
    private final Texture libGDX;
    private final Sprite SPLASHY;
    private float timer = 0;
    private float alpha = 0;

    public SplashScreen(final PacManGame GAME, final Assets ASSETS){
        this.GAME = GAME;
        this.ASSETS = ASSETS;
        this.CAM = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        CAM.translate(CAM.viewportWidth/2, CAM.viewportHeight/2);
        Texture logo = new Texture("setup/LNTVLogo.png");
        this.libGDX = new Texture("setup/libGDXLogoDark.png");
        this.SPLASHY = new Sprite(logo);
        this.SPLASHY.setSize(200, 200);
        Sound dialUp = Gdx.audio.newSound(Gdx.files.internal("setup/DialUp.mp3"));
        dialUp.play(0.6f);
    }

    @Override
    public void show() { }

    private void update(){
        float time = Gdx.graphics.getDeltaTime();
        timer += time;

        if(timer > 8 && SPLASHY.getTexture() != libGDX) {
            alpha = 0;
            SPLASHY.getTexture().dispose();
            SPLASHY.setTexture(libGDX);
            SPLASHY.setSize(390, 65);
        }

        if(timer > 14) {
            GAME.setScreen(new LoadingScreen(GAME, ASSETS));
            this.dispose();
        }

        if(timer < 3) alpha += time / 3;
        if(timer > 3 && timer < 5) alpha = 1;
        if(timer > 5 && timer < 8) alpha -= time / 3;
        if(timer > 8 && timer < 11) alpha +=  time / 3;
        if(timer > 11 && timer < 12 ) alpha = 1;
        if(timer > 12 && timer < 14) alpha -= time / 2;

        CAM.update();
    }

    @Override
    public void render(float delta) {
        update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        PacManGame.batch.begin();
        PacManGame.batch.setProjectionMatrix(CAM.combined);
        SPLASHY.draw(PacManGame.batch, alpha);
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
        libGDX.dispose();
    }

}
