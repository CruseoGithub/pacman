package uas.lntv.pacmangame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Scenes.Hud;
import uas.lntv.pacmangame.Sprites.Enemy;
import uas.lntv.pacmangame.Sprites.PacMan;

public class PauseScreen extends MapScreen {
    private GameScreen screen;
    private BitmapFont font;


    public PauseScreen(PacManGame game, String mapPath, GameScreen screen){
        super(game, mapPath, Type.MENU);
        this.hud = new Hud(game, this, true);
        this.pacman = new PacMan(game, 25*map.tileSize, 40*map.tileSize, this, hud);
        this.pacman.setSpeed(8);
        this.ghost = new Enemy(25*map.tileSize, 3*map.tileSize, this, Enemy.Difficulty.EASY);

        this.font = new BitmapFont();
        font.getData().setScale(font.getScaleX()*2);
        this.screen = screen;
        music.pause();

    }

    @Override
    public void render(float delta) {
        super.render(delta);

        hud.stage.draw();
        hud.animationTime += Gdx.graphics.getDeltaTime();
        hud.update();

        game.batch.begin();
            font.draw(game.batch, "CONTINUE", 2 * map.tileSize,40* map.tileSize);
            font.draw(game.batch, "RETURN TO MENU", 2 * map.tileSize,17* map.tileSize);
            font.draw(game.batch, "MUSIC", 11 * map.tileSize + 20,43* map.tileSize);
            font.draw(game.batch, "ON", 10 * map.tileSize,38* map.tileSize);
            font.draw(game.batch, "OFF", 15 * map.tileSize,38* map.tileSize);
        game.batch.end();

        //RETURN
        if(pacman.getYPosition() == 47*map.tileSize){
            if(pacman.getXPosition() <= 2*map.tileSize) {
                game.setScreen(screen);
                this.dispose();
            }
        }
        //TO MENU
        if(pacman.getYPosition() == 16*map.tileSize){
            if(pacman.getXPosition() <= 2*map.tileSize) {
                game.setScreen(new MenuScreen(game, "mainMenu.tmx"));
                game.resetLives();
                game.resetScore();
                game.resetLevel();
                this.dispose();
            }

        }
        //MUSIC
        if(pacman.getYPosition() == 37*map.tileSize){
            if(pacman.getXPosition() == 10*map.tileSize) {
                // TO BE MADE
                
            }
            if(pacman.getXPosition() == 15*map.tileSize) {
                // TO BE MADE

            }
        }

    }

}