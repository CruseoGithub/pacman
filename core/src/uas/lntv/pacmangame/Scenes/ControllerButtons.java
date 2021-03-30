package uas.lntv.pacmangame.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;

import uas.lntv.pacmangame.Managers.Assets;
import uas.lntv.pacmangame.Screens.MapScreen;

/**
 * This class creates a on screen controller.
 * Depending on the touch down position it will set a direction for pacman
 */
public class ControllerButtons extends Controller {

    /* Constructor */

    /**
     * Initializes with the parent constructor and sets the controller layers (grid and buttons) visible.
     * It implements methods to process touch inputs:
     *
     * touchDown - gets the touchdown position. Depending on the angle from the center of the button layout
     * it sets a direction for pacman.
     *
     * touchUp - it will activate the pause menu if the touch position is correct
     *
     * getAngleFromCenter - calculates the angle from the center of the button layout to a touch position.
     *
     * @param assets provide the Assetsmanager instance for building the controller
     * @param screen instance of a Screen which will contain the controller
     */
    public ControllerButtons(Assets assets, MapScreen screen){
        super(assets, screen);
        screen.getMap().getLayerControlZone().setOpacity(1f);
        screen.getMap().getLayerControlButton().setOpacity(1f);

        Gdx.input.setInputProcessor(new InputAdapter(){

            /* Input Methods */

            /*gets the touchdown position and depending on the angle from the center of the button layout
            it sets a direction for pacman. */
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 touch = new Vector3(screenX, screenY, 0);
                GAME_CAM.unproject(touch);

                ready(touch.x, touch.y);


                if(!PauseReady){
                    float angle = getAngleFromCenter(touch);
                    if(angle > 45 && angle < 135)       upPressed = true;
                    else if(angle > 135 && angle < 225) leftPressed = true;
                    else if(angle > 225 && angle < 315) downPressed = true;
                    else if(angle > 315 || angle < 45)  rightPressed = true;
                }
                return true;
            }

            /* it will activate the pause menu if the touch position is correct */
            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                Vector3 touch = new Vector3(screenX, screenY, 0);
                GAME_CAM.unproject(touch);
                setPause(touch.x, touch.y);
                return super.touchUp(screenX, screenY, pointer, button);
            }

            /* calculates the angle from the center of the button layout to given touch position. */
            public float getAngleFromCenter(Vector3 target) {
                //Center { x = 14, y = 7 }
                float angle = (float) Math.toDegrees(Math.atan2(target.y - (7 * TILE_SIZE), target.x - (14 * TILE_SIZE)));
                if(angle < 0){
                    angle += 360;
                }
                return angle;
            }
        });
    }

}
