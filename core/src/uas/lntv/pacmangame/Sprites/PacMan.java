package uas.lntv.pacmangame.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Scenes.Hud;
import uas.lntv.pacmangame.Screens.GameScreen;
import uas.lntv.pacmangame.Screens.MapScreen;
import uas.lntv.pacmangame.Screens.MenuScreen;

public class PacMan extends Actor {
    Hud hud;
    private PacManGame game;
    public PacMan(PacManGame game, int initX, int initY, MapScreen screen, Hud hud){
        super(initX, initY, screen);
        this.game = game;
        this.texture = new Texture("pacman32.png");
        region = new TextureRegion(texture);
        region.setRegionX(0);
        region.setRegionY(0);
        region.setRegionWidth(32);
        region.setRegionHeight(32);
        texturePositionX = 0;
        region.flip(true, false);
        this.sprite = new Sprite(region);
        this.sprite.setOrigin(tileSize/2, tileSize/2);
        this.hud = hud;
    }

    @Override
    public void die() {
        this.setXPosition(tileSize);
        this.setYPosition(17*tileSize);
        if(game.getLives() > 1) game.die();
        else {
            game.die();
            game.setScreen(new MenuScreen(game, "mainMenu.tmx"));
            game.resetScore();
            game.resetLives();
            game.resetLevel();
        };
    }

    @Override
    public void move(){
        super.move();
        screen.map.collect(screen.map.getTile(xPosition, yPosition)); //Dots einsammeln
    }

}
