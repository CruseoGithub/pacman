package uas.lntv.pacmangame.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;

<<<<<<< HEAD:core/src/uas/lntv/pacmangame/Scenes/ControllerSwipe.java
import uas.lntv.pacmangame.Screens.GameScreen;
import uas.lntv.pacmangame.Screens.MapScreen;
import uas.lntv.pacmangame.Screens.PauseScreen;
=======
import uas.lntv.pacmangame.Assets;
import uas.lntv.pacmangame.Screens.MapScreen;
import uas.lntv.pacmangame.Sprites.Joystick;
>>>>>>> development_2:core/src/uas/lntv/pacmangame/Scenes/ControllerJoystick.java

public class ControllerJoystick extends Controller {
    Vector3 touchDownPos;
    Vector3 touchCurrentPos;
    Joystick joystick;

<<<<<<< HEAD:core/src/uas/lntv/pacmangame/Scenes/ControllerSwipe.java
    public ControllerSwipe(final MapScreen screen){
        super(screen);
=======


    public ControllerJoystick(final Joystick joystick, Assets assets, MapScreen screen){
        super(assets, screen);
>>>>>>> development_2:core/src/uas/lntv/pacmangame/Scenes/ControllerJoystick.java
        touchEvent = false;
        this.joystick = joystick;
        final int joystickZoneRadius = 192 / 2; // 192px x 192px
        final int joystickKnobRadius = 64 / 2; // 64px x 64px

        screen.map.layerControlButton.setOpacity(0);

        Gdx.input.setInputProcessor(new InputAdapter(){
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 touch = new Vector3(screenX, screenY, 0);
                gameCam.unproject(touch);
<<<<<<< HEAD:core/src/uas/lntv/pacmangame/Scenes/ControllerSwipe.java
                //System.out.println("Screen coordinates Swiper: "+ "X: " + touch.x + " Y: " + touch.y);

                ready(touch.x, touch.y);
=======
                ControllerJoystick.this.joystick.setXPosition((int)(touch.x-joystickZoneRadius));
                ControllerJoystick.this.joystick.setYPosition((int)(touch.y-joystickZoneRadius));
                ControllerJoystick.this.joystick.setXPositionKnob((int)(touch.x-joystickKnobRadius));
                ControllerJoystick.this.joystick.setYPositionKnob((int)(touch.y-joystickKnobRadius));

>>>>>>> development_2:core/src/uas/lntv/pacmangame/Scenes/ControllerJoystick.java
                touchEvent = true;
                touchDownPos = new Vector3(touch.x, touch.y, 0);

                return true;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                if(touchDownPos == null) {return super.touchDragged(screenX, screenY, pointer);}

                if(touchEvent) getDirection(screenX, screenY, false);

                Vector3 touch = new Vector3(screenX, screenY, 0);
                gameCam.unproject(touch);
                Vector3 current = new Vector3(touch.x, touch.y, 0);
                Vector3 center = new Vector3(touchDownPos.x, touchDownPos.y, 0);
                float distance = current.dst(center); //distance from Center to Knob
                if (distance > joystickZoneRadius) //If the distance is less than the radius, it is already within the circle.
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
            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if(touchEvent) {
                    //System.out.println("touch-up");
                    Vector3 touch = new Vector3(screenX, screenY, 0);
                    gameCam.unproject(touch);
                    setPause(touch.x, touch.y);
                    touchEvent = false;
                    getDirection(screenX, screenY, true);
                }
                return super.touchUp(screenX, screenY, pointer, button);
            }

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
            public void getDirection(int screenX, int screenY){
                Vector3 touch = new Vector3(screenX, screenY, 0);
                gameCam.unproject(touch);
                //System.out.println("x:"+ touch.x+ "y:"+ touch.y);

                double angle = Math.atan2((double) touch.x - touchDownPos.x, (double) touchDownPos.y - touch.y);
                //System.out.println("Winkel: "+ angle);

                if (angle > -0.5 && angle < 0.5){
                    //unten
                    downPressed = true;
                    joystick.rotation = 0;
                }
                if (angle > 0.5 && angle < 2){
                    //rechts
                    rightPressed = true;
                    joystick.rotation = 90;
                }
                if (angle > 2 || angle < -2.5){
                    //oben
                    upPressed = true;
                    joystick.rotation = 180;
                }
                if (angle > -2.5 && angle < -0.5){
                    //links
                    leftPressed = true;
                    joystick.rotation = 270;
                }
            }
        });
    }
}
