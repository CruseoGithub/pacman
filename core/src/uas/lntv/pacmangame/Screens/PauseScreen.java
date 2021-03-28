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
 * The PauseScreen is an extension of the MapScreen class, it's shown when the game gets paused.
 * It will pop up, if you are inside a game and click in the area of the HUD (top).
 * When in this screen, the game is paused and can be resumed by selecting 'CONTINUE'.
 * Some options for sound and control can also be changed in here, you can also return to MENU.
 */
public class PauseScreen extends MapScreen {

    /* Fields */

    private boolean controllerSet = false;
    private final BitmapFont FONT;
    private final GameScreen SCREEN;

    /* Constructor */

    /**
     * Main constructor of the PauseScreen
     * @param game running game
     * @param assets asset management
     * @param mapPath the path where the map is stored
     * @param hud hud from the game paused
     */
    public PauseScreen(PacManGame game, Assets assets, String mapPath, GameScreen screen, Hud hud){
        super(game, assets, mapPath, Type.MENU);
        this.hud = hud;
        this.pacman = new PacMan(game, assets, 25*TILE_SIZE, 43*TILE_SIZE, this);
        this.pacman.setSpeed(8);
        GHOSTS.add(new Enemy(25*TILE_SIZE, 3*TILE_SIZE, assets, this, assets.manager.get(assets.BLUE_DEAD)));
        this.FONT = new BitmapFont();
        FONT.getData().setScale(FONT.getScaleX()*2);
        this.SCREEN = screen;
    }

    /* Methods */

    /**
     * Draws labels on the screen, where options.
     * @param delta time value
     */
    @Override
    public void render(float delta) {

        super.render(delta);
        update(delta);
        hud.stage.draw();
        hud.update();

        PacManGame.batch.begin();
            FONT.draw(PacManGame.batch,
                    "CONTINUE",
                    TILE_SIZE + 15,
                    44* TILE_SIZE - 6);

            FONT.draw(PacManGame.batch,
                    "RETURN TO MENU",
                    2 * TILE_SIZE,
                    17* TILE_SIZE);

            FONT.draw(PacManGame.batch,
                    "MUSIC",
                    3 * TILE_SIZE + 20,
                    40* TILE_SIZE);

            FONT.draw(PacManGame.batch,
                    "ON",
                    2 * TILE_SIZE,
                    35* TILE_SIZE -5);

            FONT.draw(PacManGame.batch,
                    "OFF",
                    7 * TILE_SIZE,
                    35* TILE_SIZE -5);

            FONT.draw(PacManGame.batch,
                    "SOUND",
                    13 * TILE_SIZE +13 ,
                    35* TILE_SIZE);

            FONT.draw(PacManGame.batch,
                    "ON",
                    12 * TILE_SIZE,
                    30* TILE_SIZE -5);

            FONT.draw(PacManGame.batch,
                    "OFF",
                    17 * TILE_SIZE,
                    30* TILE_SIZE -5);

            FONT.draw(PacManGame.batch,
                    "CONTROLLER",
                    6 * TILE_SIZE,
                    24* TILE_SIZE );

            FONT.draw(PacManGame.batch,
                    "JOYSTICK",
                    4 * TILE_SIZE,
                    27* TILE_SIZE -4);

            FONT.draw(PacManGame.batch,
                    "BUTTONS",
                    4 * TILE_SIZE,
                    21* TILE_SIZE -4);

        PacManGame.batch.end();
    }

    /**
     * Checks the position of PacMan for changing settings, returning to or exiting the game.
     * @param dt time parameter used by libGDX
     */
    @Override
    public void update(float dt){
        boolean moving = false;
        if(handleInput()) moving = true;
        if(
                !(pacman.getXPosition() == 25 * TILE_SIZE
                        &&(pacman.getYPosition() == 39 * TILE_SIZE
                        || pacman.getYPosition() == 34 * TILE_SIZE
                        || pacman.getYPosition() == 23 * TILE_SIZE)
                )
                        && !(pacman.getYPosition() == 39 * TILE_SIZE && pacman.getXPosition() == 7 * TILE_SIZE)
                        && !(pacman.getYPosition() == 34 * TILE_SIZE && pacman.getXPosition() == 17 * TILE_SIZE)
                        && !(pacman.getYPosition() == 23 * TILE_SIZE && pacman.getXPosition() == 4 * TILE_SIZE)
                        || moving
        ){
            pacman.update(dt);
            pacman.move();
        }

        //RETURN TO GAME
        if(pacman.getYPosition() == 43 * TILE_SIZE){
            if(pacman.getXPosition() <= TILE_SIZE) {
                this.dispose();
                GAME.setScreen(SCREEN);
            }
        }

        //TO MENU
        if(pacman.getYPosition() == 16*TILE_SIZE){
            if(pacman.getXPosition() <= 2*TILE_SIZE) {
                this.dispose();
                SCREEN.dispose();
                if(PacManGame.prefManager.addScore(PacManGame.getScore(), "Forfeited", PacManGame.getLevel() + 1)){
                    GAME.setScreen(new ScoreScreen(GAME, ASSETS, ASSETS.SCORE_MAP));
                } else {
                    GAME.setScreen(new MenuScreen(GAME, ASSETS, ASSETS.MENU_MAP));
                }
                PacManGame.resetLives();
                PacManGame.resetScore();
                PacManGame.resetLevel();
            }
        }

        //MUSIC
        if(pacman.getYPosition() == 34*TILE_SIZE){
            if(pacman.getXPosition() == 2*TILE_SIZE) {
                PrefManager.setMusicOn(true);
                if(!music.isPlaying()) music.play();
            }
            if(pacman.getXPosition() == 7*TILE_SIZE) {
                PrefManager.setMusicOn(false);
                ASSETS.manager.get(ASSETS.HUNTING_MUSIC).stop();
                music.stop();
            }
            PrefManager.savePrefs();
        }

        //SOUND SFX
        if(pacman.getYPosition() == 29*TILE_SIZE){
            if(pacman.getXPosition() == 12*TILE_SIZE) {
                PrefManager.setSfxOn(true);
            }
            if(pacman.getXPosition() == 17*TILE_SIZE) {
                PrefManager.setSfxOn(false);
            }
            PrefManager.savePrefs();
        }

        if(pacman.getXPosition() == 8 *TILE_SIZE){
            if(pacman.getYPosition() == 26 * TILE_SIZE && !controllerSet){
                PrefManager.setJoystick(true);
                PrefManager.savePrefs();
                controller.dispose();
                controller = new ControllerJoystick(ASSETS,this);
                controllerSet = true;
            }
            if(pacman.getYPosition() == 20 * TILE_SIZE && !controllerSet){
                PrefManager.setJoystick(false);
                PrefManager.savePrefs();
                controller.dispose();
                controller = new ControllerButtons(ASSETS, this);
                controllerSet = true;
            }
        }
        if(pacman.getYPosition() == 23 * TILE_SIZE) controllerSet = false;

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
    }

}