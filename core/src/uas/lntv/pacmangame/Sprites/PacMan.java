package uas.lntv.pacmangame.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Scenes.Hud;
import uas.lntv.pacmangame.Screens.MapScreen;
import uas.lntv.pacmangame.Screens.MenuScreen;
import uas.lntv.pacmangame.Screens.ScoreScreen;

public class PacMan extends Actor {
    public Hud hud;
    private final Sound SOUND;
    protected PacManGame game;

    public final Sound getSound(){ return SOUND; }

    public PacMan(PacManGame game, int initX, int initY, MapScreen screen, Hud hud){
        super(initX, initY, screen);
        this.direction = Direction.RIGHT;
        this.nextDirection = Direction.RIGHT;
        this.prevDirection = Direction.RIGHT;

        this.game = game;

        if(!(this instanceof SuperPacMan)) this.texture = new Texture("PacMan32.png");
        else this.texture = new Texture("SuperPacMan.png");
        region = new TextureRegion(texture);
        region.setRegionX(0);
        region.setRegionY(0);
        texturePositionX = 0;
        animationSpeed = 0.01f;
        mouthOpen = true;

        animation = new Animation(this, animationSpeed, this.screen,6);

        region.flip(true, false);
        this.sprite = new Sprite(region);
        this.sprite.setOrigin(TILE_SIZE/2f, TILE_SIZE/2f);
        this.hud = hud;
        this.SOUND = Gdx.audio.newSound(Gdx.files.internal("die.wav"));
    }

    public PacMan(PacManGame game, int initX, int initY, int speed, MapScreen screen, Hud hud,  Direction now, Direction next, Direction prev){
        super(initX, initY, screen);
        this.direction = now;
        this.nextDirection = next;
        this.prevDirection = prev;
        this.setSpeed(speed);

        this.game = game;

        if(!(this instanceof SuperPacMan)) this.texture = new Texture("PacMan32.png");
        else this.texture = new Texture("SuperPacMan.png");
        region = new TextureRegion(texture);
        region.setRegionX(0);
        region.setRegionY(0);
        texturePositionX = 0;
        animationSpeed = 0.01f;
        mouthOpen = true;

        animation = new Animation(this, animationSpeed, this.screen,6);

        region.flip(true, false);
        this.sprite = new Sprite(region);
        this.sprite.setOrigin(TILE_SIZE/2f, TILE_SIZE/2f);
        this.hud = hud;
        this.SOUND = Gdx.audio.newSound(Gdx.files.internal("die.wav"));
        switch(direction){
            case RIGHT:
                this.rotation = 0;
                break;
            case UP:
                this.rotation = 90;
                break;
            case LEFT:
                this.rotation = 180;
                break;
            case DOWN:
                this.rotation = 270;
                break;
        }
    }



    public void resetSupStatusTime(){}

    @Override
    public void collide() {
        super.collide();
        SOUND.play(0.35f);
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
        screen.map.collect(screen.map.getTile(xPosition, yPosition)); //collect Dots
    }

}