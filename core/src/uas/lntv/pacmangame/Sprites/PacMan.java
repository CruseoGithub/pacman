package uas.lntv.pacmangame.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import uas.lntv.pacmangame.Scenes.Hud;
import uas.lntv.pacmangame.Screens.GameScreen;

public class PacMan extends Actor {
    Hud hud;
    public PacMan(int initX, int initY, GameScreen screen, Hud hud){
        super(initX, initY, screen);
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
        if(hud.lives>0) hud.lives--;
        //else {};
    }
}
