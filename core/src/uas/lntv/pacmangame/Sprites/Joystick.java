package uas.lntv.pacmangame.Sprites;

import com.badlogic.gdx.graphics.Texture;

import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Screens.MapScreen;
import uas.lntv.pacmangame.Managers.Assets;

public class Joystick extends Actor {
    public Texture textureKnob;
    protected int xPositionKnob, yPositionKnob;

    public void setXPositionKnob(int xPositionKnob) { this.xPositionKnob = xPositionKnob; }
    public void setYPositionKnob(int yPositionKnob) { this.yPositionKnob = yPositionKnob; }
    public int getXPositionKnob() { return xPositionKnob; }
    public int getYPositionKnob() { return yPositionKnob; }

    public Joystick(Assets assets, MapScreen screen){
        super(assets,0,0,screen);
        this.texture = assets.manager.get(assets.JOYSTICK_ZONE);
        textureKnob = assets.manager.get(assets.JOYSTICK_KNOB);
    }

    @Override
    public void draw() {
        PacManGame.batch.draw(texture, getXPosition(), getYPosition(), 96, 96,
                192, 192, 1, 1, rotation,
                texturePositionX, texturePositionY, 192, 192, false, false
        );
        PacManGame.batch.draw(textureKnob, xPositionKnob, yPositionKnob);
    }
}
