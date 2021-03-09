package uas.lntv.pacmangame.Screens;

import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Scenes.Hud;
import uas.lntv.pacmangame.Sprites.Enemy;
import uas.lntv.pacmangame.Sprites.PacMan;
import uas.lntv.pacmangame.Sprites.SuperPacMan;


public class GameScreen extends MapScreen {

    private boolean pacManSuper;

    public boolean isPacManSuper() { return pacManSuper; }

    public GameScreen(PacManGame game, String mapPath) {
        super(game, mapPath, Type.GAME);

        this.hud = new Hud(game, this, true);
        this.pacman = new PacMan(game, TILE_SIZE, 17* TILE_SIZE, this, hud);
        this.ghosts.add(new Enemy(13* TILE_SIZE, 33* TILE_SIZE, this, "redghost.png"));
        if(game.getLevel() >= 2) {
            this.ghosts.add(new Enemy(14* TILE_SIZE, 33* TILE_SIZE, this, "orange.png"));
        }
        if(game.getLevel() >= 4) {
            this.ghosts.add(new Enemy(12* TILE_SIZE, 33* TILE_SIZE, this, "pinky.png"));
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

    public void evolvePacMan(){
        if(!isPacManSuper()){
            this.switchMusicHunting();
            pacman.getSound().dispose();
            this.pacman = new SuperPacMan(
                    game,
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