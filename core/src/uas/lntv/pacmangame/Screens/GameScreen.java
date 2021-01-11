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
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        ghost.findNextDirection(map.matrix, pacman);
        ghost.move();

        //Animation alle 0.5 Sekunden
        if ((tmpTimerAnimation + 0.5f) <= hud.animationTime) {
            if (pacman.texturePositionX == 0) {
                pacman.texturePositionX = 96;
            } else {
                pacman.texturePositionX = 0;
            }
            tmpTimerAnimation = hud.animationTime;
        }

        map.renderer.setView(gamecam);
        map.renderer.render();

        game.batch.begin();

        //Neuer Draw Befehl, der die Rotation mit berechnet
        /*game.batch.draw(pacman.texture,pacman.getXPosition(),pacman.getYPosition(),pacman.sprite.getOriginX(), pacman.sprite.getOriginY(),
                map.tileSize,map.tileSize, pacman.sprite.getScaleX(), pacman.sprite.getScaleY(), pacman.rotation,
                pacman.texturePositionX,0,60,60,true,false);*/
        game.batch.draw(pacman.texture, pacman.getXPosition(), pacman.getYPosition(), pacman.sprite.getOriginX(), pacman.sprite.getOriginY(),
                map.tileSize, map.tileSize, pacman.sprite.getScaleX(), pacman.sprite.getScaleY(), pacman.rotation,
                pacman.texturePositionX, 0, 32, 32, false, false);

        game.batch.draw(ghost.sprite, ghost.xPosition, ghost.yPosition, map.tileSize, map.tileSize);
        game.batch.end();



        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.time -= Gdx.graphics.getDeltaTime();
        hud.animationTime += Gdx.graphics.getDeltaTime();
        hud.update();
        hud.stage.draw();
        if(hud.time == 0){
            game.setScreen(new MenuScreen(game, "menuMap.tmx"));
            game.resetLives();
            game.resetScore();
            game.resetLevel();
        }

        if(hud.levelScore == 150){
            game.levelUp();
            game.increaseScore((int)hud.time);
            game.setScreen(new GameScreen(game, hud.getStage()));
        }
    }

}