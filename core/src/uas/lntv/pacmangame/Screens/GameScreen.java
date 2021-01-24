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
import uas.lntv.pacmangame.Sprites.Actor.State;


public class GameScreen extends MapScreen {

    public GameScreen(PacManGame game, String mapPath) {
        super(game, mapPath, Type.GAME);

        this.hud = new Hud(game, this, true);
        this.pacman = new PacMan(game, map.tileSize, 17 * map.tileSize, this, hud);
        this.ghost = new Enemy(map.tileSize, 40 * map.tileSize, this, Enemy.Difficulty.EASY);
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        ghost.findNextDirection(pacman);

        map.renderer.setView(gamecam);
        map.renderer.render();

        game.batch.begin();

        game.batch.draw(pacman.texture, pacman.getXPosition(), pacman.getYPosition(), pacman.sprite.getOriginX(), pacman.sprite.getOriginY(),
                map.tileSize, map.tileSize, pacman.sprite.getScaleX(), pacman.sprite.getScaleY(), pacman.rotation,
                pacman.texturePositionX, 0, 32, 32, false, false);

        game.batch.draw(ghost.texture,ghost.xPosition, ghost.yPosition,ghost.sprite.getOriginX(), ghost.sprite.getOriginY(),
                map.tileSize,map.tileSize, ghost.sprite.getScaleX(), ghost.sprite.getScaleY(), ghost.rotation,
                ghost.texturePositionX, ghost.texturePositionY,32,32,false,false);

        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.time -= Gdx.graphics.getDeltaTime();
        hud.update();
        hud.stage.draw();


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
            game.setScreen(new GameScreen(game, hud.getStage()));
            this.dispose();
        }
    }

}