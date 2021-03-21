package uas.lntv.pacmangame.Screens;

import com.badlogic.gdx.Gdx;

import uas.lntv.pacmangame.Managers.Assets;
import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Scenes.ControllerButtons;
import uas.lntv.pacmangame.Scenes.ControllerJoystick;
import uas.lntv.pacmangame.Scenes.Hud;
import uas.lntv.pacmangame.Managers.PrefManager;
import uas.lntv.pacmangame.Sprites.Actor;
import uas.lntv.pacmangame.Sprites.Enemy;
import uas.lntv.pacmangame.Sprites.PacMan;
import uas.lntv.pacmangame.Sprites.SuperPacMan;


public class GameScreen extends MapScreen {


    public boolean PauseActive = false;
    private boolean paused = false;
    public void setPauseActive(boolean bool) {
        PauseActive = bool;
    }

    private boolean pacManSuper;
    private float supStatusTime = 0;

    public boolean isPacManSuper() { return pacManSuper; }


    public GameScreen(PacManGame game, Assets assets, String path) {
        super(game, assets, path, Type.GAME);

        this.hud = new Hud(game, assets, this, true);
        this.pacman = new PacMan(game, assets, 14 * TILE_SIZE, 21 * TILE_SIZE, this, hud);
        this.ghosts.add(new Enemy(13 * TILE_SIZE, 33 * TILE_SIZE, assets,this, assets.manager.get(assets.GHOST_1)));
        if(PacManGame.getLevel() >= 2) {
            this.ghosts.add(new Enemy(15 * TILE_SIZE, 30 * TILE_SIZE, assets,this, assets.manager.get(assets.GHOST_2)));
            this.ghosts.get(1).setState(Actor.State.BOXED);
            this.ghosts.get(1).setBoxTimer(5);
            map.getTile(15 * TILE_SIZE, 30 * TILE_SIZE).enter(this.ghosts.get(1));
        }
        if(PacManGame.getLevel() >= 4) {
            this.ghosts.add(new Enemy(12 * TILE_SIZE, 30 * TILE_SIZE, assets,this, assets.manager.get(assets.GHOST_3)));
            this.ghosts.get(2).setState(Actor.State.BOXED);
            this.ghosts.get(2).setBoxTimer(10);
            map.getTile(12 * TILE_SIZE, 30 * TILE_SIZE).enter(this.ghosts.get(2));
            ghosts.get(0).setDifficulty(Enemy.Difficulty.MEDIUM);
        }
        if(PacManGame.getLevel() >= 6){
            ghosts.get(1).setDifficulty(Enemy.Difficulty.MEDIUM);
            pacman.setSpeed(pacman.getSpeed()*2);
            for(Enemy ghost : ghosts) ghost.setSpeed(pacman.getSpeed());
        }
        if(PacManGame.getLevel() >= 8){
            ghosts.get(2).setDifficulty(Enemy.Difficulty.MEDIUM);
        }
        if(PacManGame.getLevel() >= 12){
            ghosts.get(0).setDifficulty(Enemy.Difficulty.HARD);
        }
        if(PacManGame.getLevel() >= 19){
            ghosts.get(1).setDifficulty(Enemy.Difficulty.HARD);
        }
        if(PacManGame.getLevel() >= 24){
            pacman.setSpeed(pacman.getSpeed()*2);
            for(Enemy ghost : ghosts) ghost.setSpeed(pacman.getSpeed());
        }
        if(PacManGame.getLevel() >= 30){
            ghosts.get(2).setDifficulty(Enemy.Difficulty.HARD);
        }
        this.pacManSuper = false;
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        if(ready) hud.time -= Gdx.graphics.getDeltaTime();

        if(pacManSuper) {
            supStatusTime += Gdx.graphics.getDeltaTime();
            if (supStatusTime > 10) {
                pacManSuper = false;
                switchMusicGame();
                pacman = new PacMan(
                        game,
                        assets,
                        pacman.getXPosition(),
                        pacman.getYPosition(),
                        (pacman.getSpeed() / 2),
                        this,
                        hud,
                        pacman.getDirection(),
                        pacman.getNextDirection(),
                        pacman.getPrevDirection()
                );
                for (Enemy ghost : ghosts) {
                    ghost.resetDifficulty();
                }
            }
        }

        if(hud.time < 0){
            this.dispose();
            if(PacManGame.prefManager.addScore(PacManGame.getScore(), "Time elapsed", PacManGame.getLevel() + 1)){
                game.setScreen(new ScoreScreen(game, assets, assets.SCORE_MAP));
            } else {
                game.setScreen(new MenuScreen(game, assets, assets.MENU_MAP));
            }
            PacManGame.resetLives();
            PacManGame.resetScore();
            PacManGame.resetLevel();
        }

        if(hud.levelScore == 150){
            PacManGame.levelUp();
            PacManGame.increaseScore((int)hud.time);
            this.dispose();
            game.setScreen(new GameScreen(game, assets, hud.getMap()));
        }

        if(paused) {
            if(PrefManager.isJoystick()) this.controller = new ControllerJoystick(assets, this);
            else this.controller = new ControllerButtons(assets,this);
            if(PrefManager.isMusicOn()){
                if(pacManSuper) assets.manager.get(assets.HUNTING_MUSIC).play();
                else music.play();
            }
            paused = false;
            PauseActive = false;
        }

        if(PauseActive){
            if(music.isPlaying()) music.pause();
            if(assets.manager.get(assets.HUNTING_MUSIC).isPlaying()) assets.manager.get(assets.HUNTING_MUSIC).pause();
            game.setScreen(new PauseScreen(game, assets, assets.PAUSE, this, hud));
            paused = true;
        }
    }

    @Override
    public void render(float delta) {
        update(delta);
        super.render(delta);
        hud.update();
        if (pacman.getState() == Actor.State.DIEING) {
            hud.animateLives(delta/1.001f);
        }
        hud.stage.draw();
    }

    public void evolvePacMan(){
        supStatusTime = 0;
        if(!pacManSuper) {
            this.switchMusicHunting();
            this.pacman = new SuperPacMan(
                    game,
                    assets,
                    this.pacman.getXPosition(),
                    this.pacman.getYPosition(),
                    this.pacman.getSpeed(),
                    this,
                    this.hud,
                    this.pacman.getDirection(),
                    this.pacman.getNextDirection(),
                    this.pacman.getPrevDirection()
            );
            this.pacManSuper = true;
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        hud.dispose();
    }
}