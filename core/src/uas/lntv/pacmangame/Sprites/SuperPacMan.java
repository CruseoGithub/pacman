package uas.lntv.pacmangame.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import uas.lntv.pacmangame.Assets;
import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Scenes.Hud;
import uas.lntv.pacmangame.Screens.GameScreen;

/**
 * SuperPacMan is the stat that PacMan reaches after eating a hunter item.
 * He is faster, looks different and is able to eat ghosts.
 */
public class SuperPacMan extends PacMan {

    private float supStatusTime;

    /**
     * Let PacMan go Super!
     * @param game the running PacManGame
     * @param initX exact x-Position of PacMan
     * @param initY exact y-Position of PacMan
     * @param speed velocity of PacMan
     * @param screen should be the GameScreen
     * @param hud HUD of the screen
     * @param now the momentarily direction in which PacMan is moving
     * @param next the direction which is earmarked for PacMan
     * @param prev the last direction PacMan was moving
     * @see PacMan
     */
    public SuperPacMan(PacManGame game, Assets assets, int initX, int initY, int speed, GameScreen screen, Hud hud, Direction now, Direction next, Direction prev) {
        super(game, assets, initX, initY, speed, screen, hud, now, next, prev);
        correctPosition(now);                                       //avoids rushing through walls
        setSpeed(speed * 2);
    }

    /**
     * This method is supposed to be used, when PacMan is already SuperPacMan and eats
     * another hunting item.
     * The only thing that happens here is, that the duration of the Super-status is reset.
     */
    public void resetSupStatusTime() {
        supStatusTime = 0;
    }

    /**
     * Updates the time spent in Super-status and turns PacMan back to normal after
     * a certain amount of time.
     * @param dt delta time
     * @see PacMan
     */
    @Override
    public void update(float dt) {
        super.update(dt);
        supStatusTime += Gdx.graphics.getDeltaTime();
        if (supStatusTime > 10) {
            screen.shrinkPacMan();
            screen.switchMusicGame();
            screen.pacman = new PacMan(game, assets, getXPosition(), getYPosition(), (getSpeed() / 2), screen, hud, direction, nextDirection, prevDirection);
            for (Enemy ghost : screen.getGhosts()) {
                ghost.resetDifficulty();
            }
        }
    }

    /**
     * If SuperPacMan collides with a ghost, he will kill it.
     * It will cause a sound effect and increase the score.
     */
    @Override
    public void collide() {
        assets.manager.get(assets.KILL).play(0.15f);
        game.increaseScore(50);
    }

}