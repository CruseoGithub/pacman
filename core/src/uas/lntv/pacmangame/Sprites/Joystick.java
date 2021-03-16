package uas.lntv.pacmangame.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import uas.lntv.pacmangame.Screens.MapScreen;
import uas.lntv.pacmangame.Assets;

public class Joystick extends Actor {
    protected Assets assets;
    public Texture textureKnob;
    protected int xPositionKnob, yPositionKnob;

    public void setXPositionKnob(int xPositionKnob) { this.xPositionKnob = xPositionKnob; }
    public void setYPositionKnob(int yPositionKnob) { this.yPositionKnob = yPositionKnob; }
    public int getXPositionKnob() { return xPositionKnob; }
    public int getYPositionKnob() { return yPositionKnob; }

    public Joystick(Assets assets, MapScreen screen){
        super(0,0,screen);
        this.assets = assets;
        this.texture = assets.manager.get(assets.JOYSTICK_ZONE);
        textureKnob = assets.manager.get(assets.JOYSTICK_KNOB);
    }

}
