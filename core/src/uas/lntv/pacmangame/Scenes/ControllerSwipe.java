package uas.lntv.pacmangame.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;

import uas.lntv.pacmangame.Screens.GameScreen;
import uas.lntv.pacmangame.Screens.MapScreen;
import uas.lntv.pacmangame.Screens.PauseScreen;

public class ControllerSwipe extends Controller {
    boolean touchEvent;
    Vector3 touchDownPos;
    Vector3 touchCurrentPos;

    public ControllerSwipe(final MapScreen screen){
        super(screen);
        touchEvent = false;

        Gdx.input.setInputProcessor(new InputAdapter(){
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 touch = new Vector3(screenX, screenY, 0);
                gameCam.unproject(touch);
                //System.out.println("Screen coordinates Swiper: "+ "X: " + touch.x + " Y: " + touch.y);

                ready(touch.x, touch.y);
                touchEvent = true;
                touchDownPos = new Vector3(touch.x, touch.y, 0);

                return true;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                if(touchEvent) getDirection(screenX, screenY, false);
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
                }
                if (angle > 0.5 && angle < 2){
                    //rechts
                    rightPressed = true;
                }
                if (angle > 2 || angle < -2.5){
                    //oben
                    upPressed = true;
                }
                if (angle > -2.5 && angle < -0.5){
                    //links
                    leftPressed = true;
                }
            }
        });
    }
}
