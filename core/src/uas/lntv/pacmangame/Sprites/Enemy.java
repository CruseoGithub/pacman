package uas.lntv.pacmangame.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import uas.lntv.pacmangame.Screens.GameScreen;

public class Enemy extends Actor {
    public Enemy(int initX, int initY, GameScreen screen){
        super(initX, initY, screen);
        this.texture = new Texture("enemies.png");
        this.sprite = new Sprite(texture,0, 0, 200,200);
        this.sprite.rotate90(true);
    }

    public Actor.Direction findNextDirection(Actor target){
        int distanceX = this.xPosition - target.getXPosition();
        int distanceY = this.yPosition - target.getYPosition();
        if((Math.abs(distanceX) < tileSize) && (Math.abs(distanceY) < tileSize)){
            target.die();
            return Actor.Direction.RIGHT;
        }
        if(Math.abs(distanceX) > Math.abs(distanceY)){
            if(distanceX > 0) return Actor.Direction.LEFT;
            else return Actor.Direction.RIGHT;
        }
        else {
            if (distanceY > 0) return Actor.Direction.DOWN;
            else return Actor.Direction.UP;
        }
    }

    @Override
    public void die() {

    }
}
