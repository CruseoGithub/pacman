package uas.lntv.pacmangame.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import uas.lntv.pacmangame.Managers.Assets;
import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Scenes.Hud;
import uas.lntv.pacmangame.Managers.PrefManager;
import uas.lntv.pacmangame.Screens.MapScreen;
import uas.lntv.pacmangame.Screens.MenuScreen;
import uas.lntv.pacmangame.Screens.ScoreScreen;

public class PacMan extends Actor {
    public Hud hud;
    protected PacManGame game;
    protected Assets assets;

    public PacMan(PacManGame game, Assets assets, int initX, int initY, MapScreen screen, Hud hud){
        super(initX, initY, screen);
        this.direction = Direction.RIGHT;
        this.nextDirection = Direction.RIGHT;
        this.prevDirection = Direction.RIGHT;

        this.game = game;
        this.assets = assets;

        if(!(this instanceof SuperPacMan)) this.texture = assets.manager.get(assets.PAC_MAN);
        else this.texture = assets.manager.get(assets.SUPER_PAC);
        region = new TextureRegion(texture);
        region.setRegionX(0);
        region.setRegionY(0);
        texturePositionX = 0;
        animationSpeed = 0.01f;
        mouthOpen = true;

        animation = new Animation(this, assets, animationSpeed, this.screen,6);

        region.flip(true, false);
        this.sprite = new Sprite(region);
        this.sprite.setOrigin(TILE_SIZE/2f, TILE_SIZE/2f);
        this.hud = hud;
    }

    public PacMan(PacManGame game, Assets assets, int initX, int initY, int speed, MapScreen screen, Hud hud,  Direction now, Direction next, Direction prev){
        super(initX, initY, screen);
        this.direction = now;
        this.nextDirection = next;
        this.prevDirection = prev;
        this.setSpeed(speed);

        this.game = game;
        this.assets = assets;

        if(!(this instanceof SuperPacMan)) this.texture = assets.manager.get(assets.PAC_MAN);
        else this.texture = assets.manager.get(assets.SUPER_PAC);
        region = new TextureRegion(texture);
        region.setRegionX(0);
        region.setRegionY(0);
        texturePositionX = 0;
        animationSpeed = 0.01f;
        mouthOpen = true;

        animation = new Animation(this, assets, animationSpeed, this.screen,6);

        region.flip(true, false);
        this.sprite = new Sprite(region);
        this.sprite.setOrigin(TILE_SIZE/2f, TILE_SIZE/2f);
        this.hud = hud;
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
        if(PrefManager.isSfxOn()) assets.manager.get(assets.DIE).play(0.35f);
        if(PacManGame.getLives() > 1) PacManGame.die();
        else {
            PacManGame.die();
            if(PacManGame.prefManager.addScore(PacManGame.getScore(), "Killed", PacManGame.getLevel() + 1)){
                game.setScreen(new ScoreScreen(game, assets, assets.SCORE_MAP));
            } else {
                game.setScreen(new MenuScreen(game, assets, assets.MENU_MAP));
            }
            super.screen.dispose();
            PacManGame.resetScore();
            PacManGame.resetLives();
            PacManGame.resetLevel();
        }
    }

    @Override
    public void move(){
        super.move();
        screen.map.collect(screen.map.getTile(xPosition, yPosition)); //collect Dots
    }

}