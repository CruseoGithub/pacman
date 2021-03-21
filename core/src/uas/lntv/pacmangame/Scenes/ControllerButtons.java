package uas.lntv.pacmangame.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;

import uas.lntv.pacmangame.Managers.Assets;
import uas.lntv.pacmangame.Screens.MapScreen;

public class ControllerButtons extends Controller {
    public ControllerButtons(Assets assets, MapScreen screen){
        super(assets, screen);
        screen.map.layerControlZone.setOpacity(1f);
        screen.map.layerControlButton.setOpacity(1f);

        Gdx.input.setInputProcessor(new InputAdapter(){
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 touch = new Vector3(screenX, screenY, 0);
                gameCam.unproject(touch);

                ready(touch.x, touch.y);

                /*System.out.println("Screen coordinates an angle from center: "
                        + "X: " + touch.x + " Y: " + touch.y + " Angle: " + getAngleFromCenter(touch));*/

                float angle = getAngleFromCenter(touch);
                if(angle > 45 && angle < 135)       upPressed = true;
                else if(angle > 135 && angle < 225) leftPressed = true;
                else if(angle > 225 && angle < 315) downPressed = true;
                else if(angle > 315 || angle < 45)  rightPressed = true;

                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                Vector3 touch = new Vector3(screenX, screenY, 0);
                gameCam.unproject(touch);
                setPause(touch.x, touch.y);
                return super.touchUp(screenX, screenY, pointer, button);
            }
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
