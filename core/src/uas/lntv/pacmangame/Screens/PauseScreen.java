package uas.lntv.pacmangame.Screens;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

import uas.lntv.pacmangame.Assets;
import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Scenes.Hud;
import uas.lntv.pacmangame.Scenes.PrefManager;
import uas.lntv.pacmangame.Sprites.Enemy;
import uas.lntv.pacmangame.Sprites.PacMan;

public class PauseScreen extends MapScreen {
    private GameScreen screen;
    private BitmapFont font;


    public PauseScreen(PacManGame game, Assets assets, String mapPath, GameScreen screen, Hud hud){
        super(game, assets, mapPath, Type.MENU);
        this.hud = hud;
        this.pacman = new PacMan(game, assets, 25*TILE_SIZE, 43*TILE_SIZE, this, hud);
        this.pacman.setSpeed(8);
        ghosts.add(new Enemy(25*TILE_SIZE, 3*TILE_SIZE, assets, this, assets.manager.get(assets.BLUE_DEAD)));
        this.font = new BitmapFont();
        font.getData().setScale(font.getScaleX()*2);
        this.screen = screen;
        music.pause();

    }
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
            if(pacman.getXPosition() <= 1 * TILE_SIZE) {
                game.setScreen(screen);
                this.dispose();
            }
        }
        //TO MENU
        if(pacman.getYPosition() == 16 * TILE_SIZE){
            if(pacman.getXPosition() <= 2 * TILE_SIZE) {
                game.setScreen(new MenuScreen(game, assets, "maps/mainMenu.tmx"));
                PacManGame.resetLives();
                PacManGame.resetScore();
                PacManGame.resetLevel();
                this.dispose();
            }
        }
        //MUSIC
        if(pacman.getYPosition() == 34*TILE_SIZE){
            if(pacman.getXPosition() == 2*TILE_SIZE) {
                PrefManager.setMusicOn(true);
                if(!music.isPlaying()) music.play();
                System.out.println("MUSIC LALALA");
            }
            if(pacman.getXPosition() == 7*TILE_SIZE) {
                PrefManager.setMusicOn(false);
                music.stop();
                System.out.println("SOUND OF SILENCE");
            }
            PrefManager.savePrefs();
        }
        //SOUND SFX
        if(pacman.getYPosition() == 29*TILE_SIZE){
            if(pacman.getXPosition() == 12*TILE_SIZE) {
                PrefManager.setSfxOn(true);
                System.out.println("SFX ON");
            }
            if(pacman.getXPosition() == 17*TILE_SIZE) {
                PrefManager.setSfxOn(false);
                System.out.println("SFX OFFFFFFF");
            }
            PrefManager.savePrefs();
        }
    }

    @Override
    public void render(float delta) {

        super.render(delta);
        update(delta);
        hud.stage.draw();
        hud.update();

        PacManGame.batch.begin();
            font.draw(PacManGame.batch,
                    "CONTINUE",
                    1 * TILE_SIZE +15,
                    44* TILE_SIZE -6);

            font.draw(PacManGame.batch,
                    "RETURN TO MENU",
                    2 * TILE_SIZE,
                    17* TILE_SIZE);

            font.draw(PacManGame.batch,
                    "MUSIC",
                    3 * TILE_SIZE + 20,
                    40* TILE_SIZE);

            font.draw(PacManGame.batch,
                    "ON",
                    2 * TILE_SIZE,
                    35* TILE_SIZE -10);

            font.draw(PacManGame.batch,
                    "OFF",
                    7 * TILE_SIZE,
                    35* TILE_SIZE -10);

            font.draw(PacManGame.batch,
                    "SOUND",
                    13 * TILE_SIZE +13 ,
                    35* TILE_SIZE);

            font.draw(PacManGame.batch,
                    "ON",
                    12 * TILE_SIZE,
                    30* TILE_SIZE -10);

            font.draw(PacManGame.batch,
                    "OFF",
                    17 * TILE_SIZE,
                    30* TILE_SIZE -10);

            font.draw(PacManGame.batch,
                    "CONTROLLER",
                    6 * TILE_SIZE,
                    24* TILE_SIZE );

            font.draw(PacManGame.batch,
                    "TOUCH",
                    4 * TILE_SIZE,
                    27* TILE_SIZE );

            font.draw(PacManGame.batch,
                    "BUTTONS",
                    4 * TILE_SIZE,
                    21* TILE_SIZE );

        PacManGame.batch.end();



    }

}