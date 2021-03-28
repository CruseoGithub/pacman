package uas.lntv.pacmangame.Sprites;

import com.badlogic.gdx.graphics.Texture;

import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Screens.MapScreen;
import uas.lntv.pacmangame.Managers.Assets;

/**
 * This class draws the joystick (circle and knob) on the screen
 */
public class Joystick extends Actor {

    /* Fields */

    private final Texture TEXTURE_KNOB;
    protected int xPositionKnob, yPositionKnob;

    /* Constructor */

    /**
     * The constructor initializes the textures for the joystick circle and knob
     * @param assets provide the assetsmanager instance
     * @param screen instance of a screen which will contain the controller
     */
    public Joystick(Assets assets, MapScreen screen){
        super(assets,0,0,screen);
        this.texture = assets.manager.get(assets.JOYSTICK_ZONE);
        TEXTURE_KNOB = assets.manager.get(assets.JOYSTICK_KNOB);
    }

    /* Mutators */

    public void setXPositionKnob(int xPositionKnob) { this.xPositionKnob = xPositionKnob; }

    public void setYPositionKnob(int yPositionKnob) { this.yPositionKnob = yPositionKnob; }

    /* Methods */

    /**
     * draws the controller circle and knob on the screen
     */
    @Override
    public void draw() {
        PacManGame.batch.draw(texture, getXPosition(), getYPosition(), 96, 96,
                192, 192, 1, 1, rotation,
                texturePositionX, texturePositionY, 192, 192, false, false
        );
        PacManGame.batch.draw(TEXTURE_KNOB, xPositionKnob, yPositionKnob);
    }
}
