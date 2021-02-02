package uas.lntv.pacmangame.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Scenes.Hud;
import uas.lntv.pacmangame.Screens.MapScreen;

public class SuperPacMan extends PacMan{

    private Sound killSound;
    private float supStatusTime;

    public SuperPacMan(PacManGame game, int initX, int initY, int speed, MapScreen screen, Hud hud, Direction now, Direction next, Direction prev) {
        super(game, initX, initY, screen, hud, now, next, prev);
//        this.setSpeed(speed*2);
        killSound = Gdx.audio.newSound(Gdx.files.internal("kill.wav"));
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        supStatusTime += Gdx.graphics.getDeltaTime();
        if(supStatusTime > 10){
            screen.switchMusicGame();
            screen.pacman = new PacMan(game, getXPosition(), getYPosition(), screen, hud, direction, nextdirection, prevdirection);
            for(Enemy ghost : screen.getGhosts()){
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
