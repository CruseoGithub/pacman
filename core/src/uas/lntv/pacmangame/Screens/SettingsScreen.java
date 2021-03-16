package uas.lntv.pacmangame.Screens;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

import uas.lntv.pacmangame.Managers.Assets;
import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Scenes.ControllerButtons;
import uas.lntv.pacmangame.Scenes.ControllerJoystick;
import uas.lntv.pacmangame.Scenes.Hud;
import uas.lntv.pacmangame.Managers.PrefManager;
import uas.lntv.pacmangame.Sprites.Enemy;
import uas.lntv.pacmangame.Sprites.PacMan;

public class SettingsScreen extends MapScreen {

    private final BitmapFont FONT;
    private boolean controllerSet = false;

    public SettingsScreen(PacManGame game, Assets assets, String path){
        super(game, assets, path, Type.SETTINGS);
        this.pacman = new PacMan(game, assets, 2 * TILE_SIZE, 33 * TILE_SIZE, this, hud);
        this.pacman.setSpeed(8);
        this.ghosts.add(new Enemy(25 * TILE_SIZE, 3 * TILE_SIZE,  assets, this, assets.manager.get(assets.GHOST_1)));
        this.hud = new Hud(game, assets,this, false);
        this.FONT = new BitmapFont();
        FONT.getData().setScale(FONT.getScaleX()*2);
    }

    @Override
    public void update(float dt){
        boolean moving = false;
        if(handleInput()) moving = true;
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
            if(pacman.getYPosition() == 17 * TILE_SIZE){
                game.setScreen(new MenuScreen(game, assets, assets.MENU_MAP));
                this.dispose();
            }
            if(pacman.getYPosition() == 25 * TILE_SIZE){
                game.setScreen(new ScoreScreen(game, assets, assets.SCORE_MAP));
                this.dispose();
            }
            if(pacman.getYPosition() == 37 * TILE_SIZE && !controllerSet){
                PrefManager.setJoystick(true);
                PrefManager.savePrefs();
                controller.dispose();
                controller = new ControllerJoystick(assets,this);
                controllerSet = true;
            }
            if(pacman.getYPosition() == 29 * TILE_SIZE && !controllerSet){
                PrefManager.setJoystick(false);
                PrefManager.savePrefs();
                controller.dispose();
                controller = new ControllerButtons(assets, this);
                controllerSet = true;
            }
        }

        if(pacman.getYPosition() == 33 * TILE_SIZE) controllerSet = false;

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

        if(pacman.getYPosition() == 37 * TILE_SIZE){
            if(pacman.getXPosition() == 11 * TILE_SIZE) PrefManager.setSfxOn(true);
            if(pacman.getXPosition() == 15 * TILE_SIZE) PrefManager.setSfxOn(false);
            PrefManager.savePrefs();
        }
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

        ghosts.get(0).update(dt);
        ghosts.get(0).move();
        gameCam.update();
        map.renderer.setView(gameCam);
    }

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
                "RESUME",
                19 * TILE_SIZE,
                18 * TILE_SIZE
        );
        PacManGame.batch.end();
    }

}
