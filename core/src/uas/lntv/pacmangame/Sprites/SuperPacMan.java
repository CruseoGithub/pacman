package uas.lntv.pacmangame.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Scenes.Hud;
import uas.lntv.pacmangame.Screens.GameScreen;
import uas.lntv.pacmangame.Screens.MapScreen;

public class SuperPacMan extends PacMan {

    private Sound killSound;
    private float supStatusTime;

    public SuperPacMan(PacManGame game, int initX, int initY, int speed, GameScreen screen, Hud hud, Direction now, Direction next, Direction prev) {
        super(game, initX, initY, speed, screen, hud, now, next, prev);
        correctPosition(now);
        setSpeed(speed * 2);
        killSound = Gdx.audio.newSound(Gdx.files.internal("kill.wav"));
    }

    public void resetSupStatusTime() {
        supStatusTime = 0;
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        supStatusTime += Gdx.graphics.getDeltaTime();
        if (supStatusTime > 10) {
            screen.shrinkPacMan();
            screen.switchMusicGame();
            screen.pacman = new PacMan(game, getXPosition(), getYPosition(), (getSpeed() / 2), screen, hud, direction, nextdirection, prevdirection);
            for (Enemy ghost : screen.getGhosts()) {
                ghost.resetDifficulty();
            }
        }
    }

    @Override
    public void collide() {
        killSound.play(0.15f);
        game.increaseScore(50);
    }

}