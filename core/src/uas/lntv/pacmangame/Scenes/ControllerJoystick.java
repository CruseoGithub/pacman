package uas.lntv.pacmangame.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;

import uas.lntv.pacmangame.Managers.Assets;
import uas.lntv.pacmangame.Screens.MapScreen;
import uas.lntv.pacmangame.Sprites.Joystick;

/**
 * This class creates a joystick controller.
 * the joystick will show itself where ever you touch the screen and draws a circle with a joystick knob.
 * Depending on where the knob is moved it will set a direction for pacman.
 */
public class ControllerJoystick extends Controller {

    /* Fields */

    Vector3 touchDownPos;
    public Joystick joystick;


    /**
     *
     * The joystick knob will move if the touch is dragged to any direction. How ever it wont leave the outer circle.
     * It will set a new direction for pacman if the knob is moved for at least 50 pixels.
     * it will calculate the direction
     *
     * Initializes with the parent constructor and sets the controller layer (grid) visible.
     * It implements methods to process touch inputs:
     *
     * touchDown - gets the touchdown position and sets the position of the joystick circle and knob to this position.
     *
     * touchDragged - checks if the dragged touch position results in a new direction for pacman.
     * Draws the knob inside the circle relative to the angle of the touch position
     *
     * touchUp - checks if the final touch position results in a new direction for pacman.
     * It will activate the pause menu if the touch position is correct.
     *
     * getDirection - two methods:
     * (with boolean touchUp)
     * Checks the type of touch event. In case of a dragged event it will check
     * if the current position moved at least 50 Pixels from the touch down event.
     * In case of a touch up event it will skip this step and get the direction anyway. (efficient for short swipe gestures)
     * (without boolean touchUp)
     * Compares the touch down position to the given position and calculates angle.
     * Depending on the angle it will set a new direction for pacman.
     *
     * @param assets provide the Assetsmanager instance for building the controller
     * @param screen instance of a Screen which will contain the controller
     */
    public ControllerJoystick(Assets assets, MapScreen screen){

        super(assets, screen);
        this.touchEvent = false;
        this.joystick = new Joystick(assets, screen);
        final int joystickZoneRadius = 192 / 2; // 192px x 192px
        final int joystickKnobRadius = 64 / 2; // 64px x 64px

        screen.getMap().layerControlButton.setOpacity(0);
        screen.getMap().layerControlZone.setOpacity(1f);

        Gdx.input.setInputProcessor(new InputAdapter(){
            /* Input Methods */

            /*gets the touchdown position and sets the position of the joystick circle and knob to this position */
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 touch = new Vector3(screenX, screenY, 0);
                gameCam.unproject(touch);

                ready(touch.x, touch.y);

                ControllerJoystick.this.joystick.setXPosition((int)(touch.x-joystickZoneRadius));
                ControllerJoystick.this.joystick.setYPosition((int)(touch.y-joystickZoneRadius));
                ControllerJoystick.this.joystick.setXPositionKnob((int)(touch.x-joystickKnobRadius));
                ControllerJoystick.this.joystick.setYPositionKnob((int)(touch.y-joystickKnobRadius));


                touchEvent = true;
                touchDownPos = new Vector3(touch.x, touch.y, 0);

                return true;
            }

            /*checks if the dragged touch position results in a new direction for pacman.
            Draws the knob inside the circle relative to the angle of the touch position*/
            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                if(touchDownPos == null) {return super.touchDragged(screenX, screenY, pointer);}

                if(touchEvent) getDirection(screenX, screenY, false);

                Vector3 touch = new Vector3(screenX, screenY, 0);
                gameCam.unproject(touch);
                Vector3 current = new Vector3(touch.x, touch.y, 0);
                Vector3 center = new Vector3(touchDownPos.x, touchDownPos.y, 0);
                float distance = current.dst(center); //distance from Center to Knob
                if (distance > joystickZoneRadius) //If the distance is less than the radius, it is already within the center circle.
                {
                    Vector3 fromCenterToKnob = new Vector3(current.x - center.x, current.y - center.y, 0);
                    fromCenterToKnob.x = fromCenterToKnob.x*(joystickZoneRadius / distance); //Multiply by radius //Divide by Distance
                    fromCenterToKnob.y = fromCenterToKnob.y*(joystickZoneRadius / distance); //Multiply by radius //Divide by Distance
                    current.x = center.x + fromCenterToKnob.x; // Generate new current position
                    current.y = center.y + fromCenterToKnob.y; // Generate new current position
                }
                ControllerJoystick.this.joystick.setXPositionKnob((int)(current.x-joystickKnobRadius));
                ControllerJoystick.this.joystick.setYPositionKnob((int)(current.y-joystickKnobRadius));

                return super.touchDragged(screenX, screenY, pointer);
            }

            /* checks if the final touch position results in a new direction for pacman.
            It will activate the pause menu if the touch position is correct. */
            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if(touchEvent) {
                    Vector3 touch = new Vector3(screenX, screenY, 0);
                    gameCam.unproject(touch);
                    setPause(touch.x, touch.y);
                    touchEvent = false;
                    getDirection(screenX, screenY, true);
                }
                return super.touchUp(screenX, screenY, pointer, button);
            }

            /* Checks the type of touch event. In case of a dragged event it will check
            if the current position moved atleast 50 Pixels from the touch down event.
            In case of a touch up event it will skip this step and get the direction anyway. (efficient for short swipe gestures)
             */
            public void getDirection(int screenX, int screenY, boolean touchUp){
                if(!touchUp){
                    int minSwipe = 50;
                    Vector3 touch = new Vector3(screenX, screenY, 0);
                    gameCam.unproject(touch);
                    if((touch.x - touchDownPos.x) > minSwipe ||
                            (touch.x - touchDownPos.x) < -minSwipe ||
                            (touch.y - touchDownPos.y) > minSwipe ||
                            (touch.y - touchDownPos.y) < -minSwipe){
                        getDirection(screenX, screenY);

                    }
                }else{
                    getDirection(screenX, screenY);
                }
            }

            /* compares the touch down position to the given position and calculates angle.
            Depending on the angle it will set a new direction for pacman. */
            public void getDirection(int screenX, int screenY){
                Vector3 touch = new Vector3(screenX, screenY, 0);
                gameCam.unproject(touch);

                double angle = Math.atan2((double) touch.x - touchDownPos.x, (double) touchDownPos.y - touch.y);

                if (angle > -0.5 && angle < 0.5){
                    //unten
                    downPressed = true;
                    joystick.setRotation(0);
                }
                if (angle > 0.5 && angle < 2){
                    //rechts
                    rightPressed = true;
                    joystick.setRotation(90);
                }
                if (angle > 2 || angle < -2.5){
                    //oben
                    upPressed = true;
                    joystick.setRotation(180);
                }
                if (angle > -2.5 && angle < -0.5){
                    //links
                    leftPressed = true;
                    joystick.setRotation(270);
                }
            }
        });
    }
}
