package uas.lntv.pacmangame.Screens;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

import uas.lntv.pacmangame.Assets;
import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Scenes.Hud;
import uas.lntv.pacmangame.Sprites.Enemy;
import uas.lntv.pacmangame.Sprites.PacMan;

public class PauseScreen extends MapScreen {
    private GameScreen screen;
    private BitmapFont font;


    public PauseScreen(PacManGame game, Assets assets, String mapPath, GameScreen screen, Hud hud){
        super(game, assets, mapPath, Type.MENU);
        this.hud = hud;
        this.pacman = new PacMan(game, assets, 25*TILE_SIZE, 40*TILE_SIZE, this, hud);
        this.pacman.setSpeed(8);
        ghosts.add(new Enemy(25*TILE_SIZE, 3*TILE_SIZE, assets, this, assets.manager.get(assets.BLUE_DEAD)));

        this.font = new BitmapFont();
        font.getData().setScale(font.getScaleX()*2);
        this.screen = screen;
        music.pause();

    }

    @Override
    public void render(float delta) {
        update(delta);
        super.render(delta);

        hud.stage.draw();
        hud.update();

        PacManGame.batch.begin();
            font.draw(PacManGame.batch, "CONTINUE", 2 * TILE_SIZE,40* TILE_SIZE);
            font.draw(PacManGame.batch, "RETURN TO MENU", 2 * TILE_SIZE,17* TILE_SIZE);
            font.draw(PacManGame.batch, "MUSIC", 11 * TILE_SIZE + 20,43* TILE_SIZE);
            font.draw(PacManGame.batch, "ON", 10 * TILE_SIZE,38* TILE_SIZE);
            font.draw(PacManGame.batch, "OFF", 15 * TILE_SIZE,38* TILE_SIZE);
        PacManGame.batch.end();

        //RETURN
        if(pacman.getYPosition() == 47*TILE_SIZE){
            if(pacman.getXPosition() <= 2*TILE_SIZE) {
                game.setScreen(screen);
                this.dispose();
                music.dispose();
            }
        }
        //TO MENU
        if(pacman.getYPosition() == 16*TILE_SIZE){
            if(pacman.getXPosition() <= 2*TILE_SIZE) {
                game.setScreen(new MenuScreen(game, assets, "maps/mainMenu.tmx"));
                PacManGame.resetLives();
                PacManGame.resetScore();
                PacManGame.resetLevel();
                this.dispose();
            }

        }
        //MUSIC
        if(pacman.getYPosition() == 37*TILE_SIZE){
            if(pacman.getXPosition() == 10*TILE_SIZE) {
                // TO BE MADE
                
            }
            if(pacman.getXPosition() == 15*TILE_SIZE) {
                // TO BE MADE

            }
        }

    }

}