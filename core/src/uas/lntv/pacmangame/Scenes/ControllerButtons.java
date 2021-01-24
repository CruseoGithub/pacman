package uas.lntv.pacmangame.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;

import uas.lntv.pacmangame.Screens.MapScreen;

public class ControllerButtons extends Controller {
    public ControllerButtons(MapScreen screen){
        super(screen);

        Gdx.input.setInputProcessor(new InputAdapter(){
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 touch = new Vector3(screenX, screenY, 0);
                gamecam.unproject(touch);

                System.out.println("Screen coordinates translated to world coordinates: "
                        + "X: " + touch.x + " Y: " + touch.y);

                if(touch.x >= 12 * tileSize && touch.x <= 16 * tileSize){
                    if(touch.y >= 0 && touch.y <= 4 * tileSize) downPressed = true;
                    if(touch.y >= 10 * tileSize && touch.y <= 14 * tileSize) upPressed = true;
                }

                if(touch.y >= 5 * tileSize && touch.y <= 9 * tileSize){
                    if(touch.x >= 7 * tileSize && touch.x <= 11 * tileSize) leftPressed = true;
                    if(touch.x >= 17 * tileSize && touch.x <= 21 * tileSize) rightPressed = true;
                }
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if(isLeftPressed()) leftPressed = false;
                if(isRightPressed()) rightPressed = false;
                if(isUpPressed()) upPressed = false;
                if(isDownPressed()) downPressed = false;
                return super.touchUp(screenX, screenY, pointer, button);
            }

        });
    }
}
