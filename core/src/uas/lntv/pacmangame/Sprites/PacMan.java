package uas.lntv.pacmangame.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Scenes.Hud;
import uas.lntv.pacmangame.Screens.GameScreen;
import uas.lntv.pacmangame.Screens.MapScreen;
import uas.lntv.pacmangame.Screens.MenuScreen;
import uas.lntv.pacmangame.Screens.ScoreScreen;

public class PacMan extends Actor {
    public Hud hud;
    Texture pac32 = new Texture("pacman32.png");
    private Sound sound;
    private PacManGame game;


    public PacMan(PacManGame game, int initX, int initY, MapScreen screen, Hud hud){

        super(initX, initY, screen);
        this.direction = Direction.RIGHT;
        this.nextdirection = Direction.UP;
        this.prevdirection = Direction.UP;
        this.texture = pac32;



        this.game = game;

        this.texture = new Texture("pacman32.png");

        region = new TextureRegion(texture);

        region.setRegionX(0);
        region.setRegionY(0);
        texturePositionX = 0;
        animationSpeed = 0.01f;
        mouthOpen = true;

        animation = new Animation(this,animationSpeed, this.screen,6);

        region.flip(true, false);
        this.sprite = new Sprite(region);
        this.sprite.setOrigin(tileSize/2, tileSize/2);
        this.hud = hud;
        this.sound = Gdx.audio.newSound(Gdx.files.internal("die.wav"));
    }

    @Override
    public void die() {

        super.die();
        if(game.getLives()>0) game.reduceLive(game.getLives());
        sound.play(0.35f);
        if(game.getLives() > 1) game.die();
        else {
            game.die();
            if(game.highScore.addScore(game.getScore())){
                game.setScreen(new ScoreScreen(game, "HighScoreList.tmx"));
            } else {
                game.setScreen(new MenuScreen(game, "mainMenu.tmx"));
            }
            super.screen.dispose();
            game.resetScore();
            game.resetLives();
            game.resetLevel();
        }
    }

    @Override
    public void move(){
        super.move();
        screen.map.collect(screen.map.getTile(xPosition, yPosition)); //Dots einsammeln
    }

}
