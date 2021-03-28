package uas.lntv.pacmangame.Screens;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

import uas.lntv.pacmangame.Managers.Assets;
import uas.lntv.pacmangame.Maps.MenuMap;
import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Scenes.ControllerButtons;
import uas.lntv.pacmangame.Scenes.ControllerJoystick;
import uas.lntv.pacmangame.Scenes.Hud;
import uas.lntv.pacmangame.Managers.PrefManager;
import uas.lntv.pacmangame.Sprites.Enemy;
import uas.lntv.pacmangame.Sprites.PacMan;

/**
 * In the SettingsScreen you can edit your settings or view the scoreboard and the credits.
 */
public class SettingsScreen extends MapScreen {

    /* Fields */

    private boolean controllerSet = false;
    private final BitmapFont FONT;

    /* Constructor */

    /**
     * Main constructor of the SettingsScreen
     * @param game the running game
     * @param assets the asset management
     * @param path the path where the needed map is located in the assets
     */
    public SettingsScreen(PacManGame game, Assets assets, String path){
        super(game, assets, path, Type.SETTINGS);
        this.pacman = new PacMan(game, assets, 2 * TILE_SIZE, 33 * TILE_SIZE, this);
        this.pacman.setSpeed(8);
        this.GHOSTS.add(new Enemy(25 * TILE_SIZE, 3 * TILE_SIZE,  assets, this, assets.manager.get(assets.GHOST_1)));
        this.hud = new Hud(game, assets,this, false);
        this.FONT = new BitmapFont();
        FONT.getData().setScale(FONT.getScaleX()*2);
    }

    /* Methods */

    /**
     * In addition to the render method of the abstract MapScreen class, this render method also
     * draws words on the screen, so that the player knows which option he can use in which place
     * and it uses the classes own update method to check PacMan's place.
     * @param delta time parameter used by libGDX
     * @see MapScreen
     */
    @Override
    public void render(float delta) {
        update(delta);
        super.render(delta);

        PacManGame.batch.begin();
        FONT.draw(
                PacManGame.batch,
                "MUSIC",
                20 * TILE_SIZE,
                46 * TILE_SIZE
        );
        FONT.draw(
                PacManGame.batch,
                "ON",
                18 * TILE_SIZE,
                42 * TILE_SIZE
        );
        FONT.draw(
                PacManGame.batch,
                "OFF",
                22 * TILE_SIZE,
                42 * TILE_SIZE
        );
        FONT.draw(
                PacManGame.batch,
                "SOUND",
                12 * TILE_SIZE,
                42 * TILE_SIZE
        );
        FONT.draw(
                PacManGame.batch,
                "ON",
                10 * TILE_SIZE,
                38 * TILE_SIZE
        );
        FONT.draw(
                PacManGame.batch,
                "OFF",
                14 * TILE_SIZE,
                38 * TILE_SIZE
        );
        FONT.draw(
                PacManGame.batch,
                "JOYSTICK",
                19 * TILE_SIZE,
                38 * TILE_SIZE
        );
        FONT.draw(
                PacManGame.batch,
                "CONTROL",
                19 * TILE_SIZE,
                34 * TILE_SIZE
        );
        FONT.draw(
                PacManGame.batch,
                "BUTTONS",
                19 * TILE_SIZE,
                30 * TILE_SIZE
        );
        FONT.draw(
                PacManGame.batch,
                "USER",
                13 * TILE_SIZE,
                30 * TILE_SIZE
        );
        FONT.draw(
                PacManGame.batch,
                "SCORES",
                19 * TILE_SIZE,
                26 * TILE_SIZE
        );
        FONT.draw(
                PacManGame.batch,
                "CREDITS",
                19 * TILE_SIZE,
                22 * TILE_SIZE
        );
        FONT.draw(
                PacManGame.batch,
                "RETURN",
                19 * TILE_SIZE,
                18 * TILE_SIZE
        );
        PacManGame.batch.end();
    }

    /**
     * Update checks the position of PacMan, so find out if any of the options were chosen by
     * the player.
     * @param dt time parameter used by libGDX
     */
    @Override
    public void update(float dt){
        boolean moving = false;
        if(handleInput()) moving = true;

        //Stopping points, to make the movement through the menu easier
        if(
                !(pacman.getXPosition() == 4 * TILE_SIZE &&
                        (pacman.getYPosition() == 21 * TILE_SIZE
                                || pacman.getYPosition() == 25 * TILE_SIZE
                                || pacman.getYPosition() == 29 * TILE_SIZE
                                || pacman.getYPosition() == 33 * TILE_SIZE
                                || pacman.getYPosition() == 41 * TILE_SIZE)
                )
                        && !(pacman.getYPosition() == 41 * TILE_SIZE && pacman.getXPosition() == 11 * TILE_SIZE)
                        && !(pacman.getYPosition() == 45 * TILE_SIZE && pacman.getXPosition() == 19 * TILE_SIZE)
                        && !(pacman.getYPosition() == 33 * TILE_SIZE && pacman.getXPosition() == 23 * TILE_SIZE)
                        || moving
        ){
            pacman.update(dt);
            pacman.move();
        }

        //If PacMan reaches the threshold of action, it depends in which corridor he is
        if(pacman.getXPosition() == 19 *TILE_SIZE){
            //RETURN TO  MAIN MENU
            if(pacman.getYPosition() == 17 * TILE_SIZE){
                this.dispose();
                GAME.setScreen(new MenuScreen(GAME, ASSETS, ASSETS.MENU_MAP));
            }
            //ENTER SCORE SCREEN
            if(pacman.getYPosition() == 25 * TILE_SIZE){
                this.dispose();
                GAME.setScreen(new ScoreScreen(GAME, ASSETS, ASSETS.SCORE_MAP));
            }
            //SET CONTROLLER TO JOYSTICK
            if(pacman.getYPosition() == 37 * TILE_SIZE && !controllerSet){
                PrefManager.setJoystick(true);
                PrefManager.savePrefs();
                controller.dispose();
                controller = new ControllerJoystick(ASSETS,this);
                controllerSet = true;
            }
            //SET CONTROLLER TO BUTTONS
            if(pacman.getYPosition() == 29 * TILE_SIZE && !controllerSet){
                PrefManager.setJoystick(false);
                PrefManager.savePrefs();
                controller.dispose();
                controller = new ControllerButtons(ASSETS, this);
                controllerSet = true;
            }
        }

        //preventing infinite dispose/create loop
        if(pacman.getYPosition() == 33 * TILE_SIZE) controllerSet = false;

        //MUSIC ON/OFF
        if(pacman.getYPosition() == 41 * TILE_SIZE){
            if(pacman.getXPosition() == 19 * TILE_SIZE){
                PrefManager.setMusicOn(true);
                if(!music.isPlaying()) music.play();
            }
            if(pacman.getXPosition() == 23 * TILE_SIZE){
                PrefManager.setMusicOn(false);
                music.stop();
            }
            PrefManager.savePrefs();
        }

        //SFX ON/OFF
        if(pacman.getYPosition() == 37 * TILE_SIZE){
            if(pacman.getXPosition() == 11 * TILE_SIZE) PrefManager.setSfxOn(true);
            if(pacman.getXPosition() == 15 * TILE_SIZE) PrefManager.setSfxOn(false);
            PrefManager.savePrefs();
        }

        //CHANGE USER NAME
        if(pacman.getYPosition() == 29 * TILE_SIZE) {
            if (pacman.getXPosition() == 14 * TILE_SIZE) {
                PrefManager.setNameSet(true);
            }
            if (pacman.getXPosition() == 15 * TILE_SIZE) {
                PrefManager.setNameSet(false);
            }
            if (pacman.getXPosition() == 16 * TILE_SIZE) {
                if (PrefManager.noNameSet()) PrefManager.setName();
                PrefManager.savePrefs();
            }
        }

        //CHECK FOR HIGHLIGHTING
        if(PrefManager.isMusicOn()) {
            ((MenuMap)map).getHighlightLayers().get(0).setVisible(true);
            ((MenuMap)map).getHighlightLayers().get(1).setVisible(false);
        }
        else{
            ((MenuMap)map).getHighlightLayers().get(0).setVisible(false);
            ((MenuMap)map).getHighlightLayers().get(1).setVisible(true);
        }
        if(PrefManager.isSfxOn()){
            ((MenuMap)map).getHighlightLayers().get(2).setVisible(true);
            ((MenuMap)map).getHighlightLayers().get(3).setVisible(false);
        }
        else {
            ((MenuMap)map).getHighlightLayers().get(2).setVisible(false);
            ((MenuMap)map).getHighlightLayers().get(3).setVisible(true);
        }
        if(PrefManager.isJoystick()) {
            ((MenuMap)map).getHighlightLayers().get(4).setVisible(true);
            ((MenuMap)map).getHighlightLayers().get(5).setVisible(false);
        }
        else{
            ((MenuMap)map).getHighlightLayers().get(4).setVisible(false);
            ((MenuMap)map).getHighlightLayers().get(5).setVisible(true);
        }

        GHOSTS.get(0).update(dt);
        GHOSTS.get(0).move();
        GAME_CAM.update();
        map.renderer.setView(GAME_CAM);
    }

}
