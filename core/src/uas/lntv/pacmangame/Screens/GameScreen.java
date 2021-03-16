package uas.lntv.pacmangame.Screens;

import com.badlogic.gdx.Gdx;

import uas.lntv.pacmangame.Assets;
import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Scenes.ControllerJoystick;
import uas.lntv.pacmangame.Scenes.Hud;
import uas.lntv.pacmangame.Scenes.PrefManager;
import uas.lntv.pacmangame.Sprites.Enemy;
import uas.lntv.pacmangame.Sprites.Joystick;
import uas.lntv.pacmangame.Sprites.PacMan;
import uas.lntv.pacmangame.Sprites.SuperPacMan;


public class GameScreen extends MapScreen {


    public boolean PauseActive = false;
    private boolean paused= false;
    public void setPauseActive(boolean bool) {
        PauseActive = bool;
    }

    private boolean pacManSuper;

    public boolean isPacManSuper() { return pacManSuper; }


    public GameScreen(PacManGame game, Assets assets, String path) {
        super(game, assets, path, Type.GAME);

        this.hud = new Hud(game, assets, this, true);
        this.pacman = new PacMan(game, assets, 14 * TILE_SIZE, 21 * TILE_SIZE, this, hud);
        this.ghosts.add(new Enemy(13* TILE_SIZE, 33* TILE_SIZE, assets,this, assets.manager.get(assets.GHOST_1)));
        if(PacManGame.getLevel() >= 2) {
            this.ghosts.add(new Enemy(14* TILE_SIZE, 33* TILE_SIZE, assets,this, assets.manager.get(assets.GHOST_2)));
        }
        if(PacManGame.getLevel() >= 4) {
            this.ghosts.add(new Enemy(12* TILE_SIZE, 33* TILE_SIZE, assets,this, assets.manager.get(assets.GHOST_3)));
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

        if(hud.time < 0){
            if(PacManGame.prefManager.addScore(PacManGame.getScore(), "Time elapsed", PacManGame.getLevel() + 1)){
                game.setScreen(new ScoreScreen(game, assets, assets.SCORE_MAP));
            } else {
                game.setScreen(new MenuScreen(game, assets, assets.MENU_MAP));
            }
            PacManGame.resetLives();
            PacManGame.resetScore();
            PacManGame.resetLevel();
            this.dispose();
        }

        if(hud.levelScore == 150){
            PacManGame.levelUp();
            PacManGame.increaseScore((int)hud.time);
            music.stop();
            game.setScreen(new GameScreen(game, assets, hud.getMap()));
            this.dispose();

        }
        if(paused) {
            controller = new ControllerJoystick(new Joystick(assets, this), assets, this);
            if(PrefManager.isMusicOn()== true) music.play();
            paused = false;
            PauseActive = false;
        }
        if(PauseActive){
            music.pause();
            game.setScreen(new PauseScreen(game, assets,"maps/PauseMap.tmx", this, hud));
            paused = true;
        }

    }

    @Override
    public void render(float delta) {
        update(delta);
        super.render(delta);


        if(ready) hud.time -= Gdx.graphics.getDeltaTime();
        hud.update();
        hud.stage.draw();
    }

    public void evolvePacMan(){
        if(!isPacManSuper()){
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
        } else{
            pacman.resetSupStatusTime();
        }
    }

    public void shrinkPacMan(){ this.pacManSuper = false; }

    @Override
    public void dispose() {
        super.dispose();
        hud.dispose();
    }
}