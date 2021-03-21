package uas.lntv.pacmangame.Sprites;

import uas.lntv.pacmangame.Managers.Assets;
import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Scenes.Hud;
import uas.lntv.pacmangame.Managers.PrefManager;
import uas.lntv.pacmangame.Screens.GameScreen;

/**
 * SuperPacMan is the stat that PacMan reaches after eating a hunter item.
 * He is faster, looks different and is able to eat ghosts.
 */
public class SuperPacMan extends PacMan {

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
     * If SuperPacMan collides with a ghost, he will kill it.
     * It will cause a sound effect and increase the score.
     */
    @Override
    public void collide() {
        if(PrefManager.isSfxOn()) assets.manager.get(assets.KILL).play(0.15f);
        PacManGame.increaseScore(50);
    }

}