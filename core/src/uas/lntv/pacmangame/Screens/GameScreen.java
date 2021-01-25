package uas.lntv.pacmangame.Screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Scenes.Controller;
import uas.lntv.pacmangame.Maps.GameMap;
import uas.lntv.pacmangame.Scenes.Hud;
import uas.lntv.pacmangame.Maps.Map;
import uas.lntv.pacmangame.Sprites.Actor;
import uas.lntv.pacmangame.Sprites.Enemy;
import uas.lntv.pacmangame.Sprites.PacMan;

public class GameScreen extends MapScreen {

    public GameScreen(PacManGame game, String mapPath) {
        super(game, mapPath, Type.GAME);

        this.hud = new Hud(game, this, true);
        this.pacman = new PacMan(game, map.tileSize, 17 * map.tileSize, this, hud);
        this.ghost = new Enemy(map.tileSize, 40 * map.tileSize, this, Enemy.Difficulty.EASY);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        hud.animationTime += Gdx.graphics.getDeltaTime();
        hud.update();
        hud.stage.draw();
        if(hud.time < 0){
            if(game.highScore.addScore(game.getScore())){
                game.setScreen(new ScoreScreen(game, "HighScoreList.tmx"));
            } else {
                game.setScreen(new MenuScreen(game, "mainMenu.tmx"));
            }
            game.resetLives();
            game.resetScore();
            game.resetLevel();
            this.dispose();
        }

        if(hud.levelScore == 15){
            //game.levelUp();
            //game.increaseScore((int)hud.time);
            //game.setScreen(new GameScreen(game, hud.getStage()));
            //this.dispose();
            music.pause();
            //game.setScreen(new PauseScreen(game, "PauseMap.tmx"));
            game.setScreen(new ScoreScreen(game, "Pausecopy.tmx"));

        }
    }

}