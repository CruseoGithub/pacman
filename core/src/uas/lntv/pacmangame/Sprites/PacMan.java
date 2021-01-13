package uas.lntv.pacmangame.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import uas.lntv.pacmangame.Scenes.Hud;
import uas.lntv.pacmangame.Screens.GameScreen;
import uas.lntv.pacmangame.Screens.MapScreen;

public class PacMan extends Actor {
    public Hud hud;
    Texture pac32 = new Texture("pacman32.png");

    public PacMan(int initX, int initY, MapScreen screen, Hud hud){
        super(initX, initY, screen);
        this.direction = Direction.RIGHT;
        this.nextdirection = Direction.UP;
        this.prevdirection = Direction.UP;
        this.texture = pac32;
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
    }

    @Override
    public void die() {
        super.die();
        if(hud.lives>0) hud.lives--;
    }

    @Override
    public void move(){
        super.move();
        screen.map.collect(screen.map.getTile(xPosition, yPosition)); //Dots einsammeln
    }
}
