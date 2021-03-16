package uas.lntv.pacmangame.Screens;

import uas.lntv.pacmangame.Assets;
import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Scenes.Hud;
import uas.lntv.pacmangame.Sprites.Actor;
import uas.lntv.pacmangame.Sprites.Enemy;
import uas.lntv.pacmangame.Sprites.PacMan;
import uas.lntv.pacmangame.Sprites.SuperPacMan;


public class GameScreen extends MapScreen {

    private boolean pacManSuper;

    public boolean isPacManSuper() { return pacManSuper; }

    public GameScreen(PacManGame game, Assets assets, String path) {
        super(game, assets, path, Type.GAME);

        this.hud = new Hud(game, assets, this, true);
        this.pacman = new PacMan(game, assets, TILE_SIZE, 17* TILE_SIZE, this, hud);
        this.ghosts.add(new Enemy(13* TILE_SIZE, 33* TILE_SIZE, assets,this, assets.manager.get(assets.GHOST_1)));
        if(game.getLevel() >= 2) {
            this.ghosts.add(new Enemy(14* TILE_SIZE, 33* TILE_SIZE, assets,this, assets.manager.get(assets.GHOST_2)));
        }
        if(game.getLevel() >= 4) {
            this.ghosts.add(new Enemy(12* TILE_SIZE, 33* TILE_SIZE, assets,this, assets.manager.get(assets.GHOST_3)));
            ghosts.get(0).setDifficulty(Enemy.Difficulty.MEDIUM);
        }
        if(game.getLevel() >= 6){
            ghosts.get(1).setDifficulty(Enemy.Difficulty.MEDIUM);
            pacman.setSpeed(pacman.getSpeed()*2);
            for(Enemy ghost : ghosts) ghost.setSpeed(pacman.getSpeed());
        }
        if(game.getLevel() >= 8){
            ghosts.get(2).setDifficulty(Enemy.Difficulty.MEDIUM);
        }
        if(game.getLevel() >= 12){
            ghosts.get(0).setDifficulty(Enemy.Difficulty.HARD);
        }
        if(game.getLevel() >= 19){
            ghosts.get(1).setDifficulty(Enemy.Difficulty.HARD);
        }
        if(game.getLevel() >= 24){
            pacman.setSpeed(pacman.getSpeed()*2);
            for(Enemy ghost : ghosts) ghost.setSpeed(pacman.getSpeed());
        }
        if(game.getLevel() >= 30){
            ghosts.get(2).setDifficulty(Enemy.Difficulty.HARD);
        }
        this.pacManSuper = false;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        hud.update();
        if (pacman.getState() == Actor.State.DIEING) {
        hud.animateLifes(delta/2f);
        }

        hud.stage.draw();
        if(hud.time < 0){
            if(game.highScore.addScore(game.getScore())){
                game.setScreen(new ScoreScreen(game, assets, assets.SCORE_MAP));
            } else {
                game.setScreen(new MenuScreen(game, assets, assets.MENU_MAP));
            }
            game.resetLives();
            game.resetScore();
            game.resetLevel();
            this.dispose();
        }

        if(hud.levelScore == 150){
            game.levelUp();
            game.increaseScore((int)hud.time);
            music.stop();
            game.setScreen(new GameScreen(game, assets, hud.getMap()));
            this.dispose();
        }
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