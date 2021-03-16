package uas.lntv.pacmangame.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;

import uas.lntv.pacmangame.Managers.Assets;
import uas.lntv.pacmangame.Screens.MapScreen;

public class ControllerButtons extends Controller {
    public ControllerButtons(Assets assets, MapScreen screen){
        super(assets, screen);
        screen.map.layerControlZone.setOpacity(0.5f);

        Gdx.input.setInputProcessor(new InputAdapter(){
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 touch = new Vector3(screenX, screenY, 0);
                gameCam.unproject(touch);

                System.out.println("Screen coordinates Buttons!!!!!!!: "
                        + "X: " + touch.x + " Y: " + touch.y);

                if(touch.x >= 12 * TILE_SIZE && touch.x <= 16 * TILE_SIZE){
                    if(touch.y >= 0 && touch.y <= 4 * TILE_SIZE) downPressed = true;
                    if(touch.y >= 10 * TILE_SIZE && touch.y <= 14 * TILE_SIZE) upPressed = true;
                }

                if(touch.y >= 5 * TILE_SIZE && touch.y <= 9 * TILE_SIZE){
                    if(touch.x >= 7 * TILE_SIZE && touch.x <= 11 * TILE_SIZE) leftPressed = true;
                    if(touch.x >= 17 * TILE_SIZE && touch.x <= 21 * TILE_SIZE) rightPressed = true;
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
