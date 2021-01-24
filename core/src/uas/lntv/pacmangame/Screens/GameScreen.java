package uas.lntv.pacmangame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Scenes.Hud;
import uas.lntv.pacmangame.Sprites.Enemy;
import uas.lntv.pacmangame.Sprites.PacMan;


public class GameScreen extends MapScreen {

    public GameScreen(PacManGame game, String mapPath) {
        super(game, mapPath, Type.GAME);

        this.hud = new Hud(game, this, true);
        this.pacman = new PacMan(game, map.tileSize, 17 * map.tileSize, this, hud);
        this.pacman.setSpeed(4);
        this.ghosts.add(new Enemy(map.tileSize, 40 * map.tileSize, this, Enemy.Difficulty.EASY, "redghost.png"));
        this.ghosts.add(new Enemy(map.tileSize, 39 * map.tileSize, this, Enemy.Difficulty.EASY, "orange.png"));
        this.ghosts.add(new Enemy(map.tileSize, 41 * map.tileSize, this, Enemy.Difficulty.EASY, "pinky.png"));
    }

    @Override
    public void render(float delta) {
        super.render(delta);
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

        if(hud.levelScore == 150){
            game.levelUp();
            game.increaseScore((int)hud.time);
            game.setScreen(new GameScreen(game, hud.getMap()));
            this.dispose();
        }
    }

}