package uas.lntv.pacmangame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import uas.lntv.pacmangame.Managers.Assets;
import uas.lntv.pacmangame.Maps.GameMap;
import uas.lntv.pacmangame.Maps.Tile;
import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Scenes.ControllerButtons;
import uas.lntv.pacmangame.Scenes.ControllerJoystick;
import uas.lntv.pacmangame.Scenes.Hud;
import uas.lntv.pacmangame.Managers.PrefManager;
import uas.lntv.pacmangame.Sprites.Actor;
import uas.lntv.pacmangame.Sprites.Enemy;
import uas.lntv.pacmangame.Sprites.PacMan;

/**
 * 
 */
public class GameScreen extends MapScreen {

    /* Fields */

    private boolean enemiesSlow = false;
    private boolean itemTaken = false;
    private boolean pacManSuper = false;
    private boolean PauseActive = false;
    private boolean paused = false;
    private float itemCoolDown = 0;
    private float slowDownTime = 10;
    private float supStatusTime = 10;

    /* Constructor */

    /**
     * Creates a screen with(out) HUD, PacMan, Ghost(s).
     * According to current level, there are more (difficult) Ghosts.
     * @param game running game
     * @param assets asset management
     * @param path the path where the map is stored
     */
    public GameScreen(PacManGame game, Assets assets, String path) {
        super(game, assets, path, Type.GAME);

        this.hud = new Hud(game, assets, this, true);
        this.pacman = new PacMan(game, assets, 14 * TILE_SIZE, 21 * TILE_SIZE, this);
        this.GHOSTS.add(new Enemy(13 * TILE_SIZE, 33 * TILE_SIZE, assets,this, assets.manager.get(assets.GHOST_1)));

        if(PacManGame.getLevel() >= 2) {
            this.GHOSTS.add(new Enemy(15 * TILE_SIZE, 30 * TILE_SIZE, assets,this, assets.manager.get(assets.GHOST_2)));
            this.GHOSTS.get(1).setState(Actor.State.BOXED);
            this.GHOSTS.get(1).setBoxTimer(5);
            map.getTile(15 * TILE_SIZE, 30 * TILE_SIZE).enter(this.GHOSTS.get(1));
        }

        if(PacManGame.getLevel() >= 4) {
            this.GHOSTS.add(new Enemy(12 * TILE_SIZE, 30 * TILE_SIZE, assets,this, assets.manager.get(assets.GHOST_3)));
            this.GHOSTS.get(2).setState(Actor.State.BOXED);
            this.GHOSTS.get(2).setBoxTimer(10);
            map.getTile(12 * TILE_SIZE, 30 * TILE_SIZE).enter(this.GHOSTS.get(2));
            GHOSTS.get(0).setDifficulty(Enemy.Difficulty.MEDIUM);
        }

        if(PacManGame.getLevel() >= 6){
            GHOSTS.get(1).setDifficulty(Enemy.Difficulty.MEDIUM);
            pacman.setSpeed(pacman.getSpeed()*2);
            for(Enemy ghost : GHOSTS) ghost.setSpeed(pacman.getSpeed());
        }

        if(PacManGame.getLevel() >= 8){
            GHOSTS.get(2).setDifficulty(Enemy.Difficulty.MEDIUM);
        }
        if(PacManGame.getLevel() >= 12){
            GHOSTS.get(0).setDifficulty(Enemy.Difficulty.HARD);
        }
        if(PacManGame.getLevel() >= 19){
            GHOSTS.get(1).setDifficulty(Enemy.Difficulty.HARD);
        }
        if(PacManGame.getLevel() >= 24){
            pacman.setSpeed(pacman.getSpeed()*2);
            for(Enemy ghost : GHOSTS) ghost.setSpeed(pacman.getSpeed());
        }
        if(PacManGame.getLevel() >= 30){
            GHOSTS.get(2).setDifficulty(Enemy.Difficulty.HARD);
        }
    }

    /* Accessors */

    public boolean isPacManSuper() { return pacManSuper; }

    /* Mutators */


    public void setPauseActive(boolean bool) { PauseActive = bool; }


    /* Methods */

    /**
     * Allows to generate new items when the cooldown is over and there are less than 4 items on the map.
     */
    private void updateCoolDown(){
        if(itemTaken){
            itemCoolDown -= Gdx.graphics.getDeltaTime();
            switch(map.countItems()){
                case 4:
                    itemTaken = false;
                    break;
                case 3:
                    if(itemCoolDown < 0) this.map.generateSpecialItem();
                    break;
                case 2:
                    if(itemCoolDown < 10) this.map.generateSpecialItem();
                    break;
                case 1:
                    if(itemCoolDown < 20) this.map.generateSpecialItem();
                    break;
                case 0:
                    if(itemCoolDown < 30) this.map.generateSpecialItem();
                    break;
            }
        }
    }

    /**
     * Changes back to normal PacMan after the Hunter-Item-Time is over.
     * Ghosts also return to the difficulty they previously had and stop running away.
     */
    private void updateHunter(){
        if(pacManSuper) {
            supStatusTime -= Gdx.graphics.getDeltaTime();
            if (supStatusTime < 0) {
                pacManSuper = false;
                switchMusicGame();
                pacman.setTexture(ASSETS.manager.get(ASSETS.PAC_MAN));
                pacman.setSpeed(pacman.getSpeed()/2);
                for (Enemy ghost : GHOSTS) {
                    ghost.resetDifficulty();
                }
            }
        }
    }

    /**
     * After the SloMo buff ends, the Ghosts return to their normal speed.
     */
    private void updateSloMo(){
        if(enemiesSlow){
            slowDownTime -= Gdx.graphics.getDeltaTime();
            if(slowDownTime < 0){
                for (Enemy ghost : GHOSTS) {
                    ghost.correctPosition(ghost.getDirection());
                    ghost.setSpeed(ghost.getSpeed()*2);
                }
                enemiesSlow = false;
            }
        }
    }

    /**
     * Pause activation via spacebar.
     */
    @Override
    protected boolean handleInput(){
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) PauseActive = true;
        return super.handleInput();
    }

    /**
     * Buffs of the different items that can be picked up.
     */
    public void activateBuff(Tile.Item buffType){
        switch (buffType) {
            case HUNTER:
                this.itemTaken = true;
                this.itemCoolDown += 10;
                this.supStatusTime = 10;
                if (!pacManSuper) {
                    this.switchMusicHunting();
                    this.pacman.setTexture(ASSETS.manager.get(ASSETS.SUPER_PAC));
                    this.pacman.correctPosition(pacman.getDirection());
                    this.pacman.setSpeed(this.pacman.getSpeed() * 2);
                    this.pacManSuper = true;
                }
                break;
            case SLOWMO:
                this.itemTaken = true;
                this.itemCoolDown += 10;
                this.slowDownTime = 10;
                if(!enemiesSlow) {
                    for (Enemy ghost : GHOSTS) ghost.setSpeed(ghost.getSpeed() / 2);
                    this.enemiesSlow = true;
                }
                break;
            case TIME:
                this.itemTaken = true;
                this.itemCoolDown += 10;
                this.hud.time += 10;
                this.hud.resetTimeStamp();
                break;
            case LIFE:
                this.itemTaken = true;
                this.itemCoolDown += 10;
                if(PacManGame.getLives() < 3) {
                    PacManGame.addLive();
                    hud.resetLives();
                } else{
                    PacManGame.increaseScore(75);
                }
                break;
        }
    }

    @Override
    public void render(float delta) {
        update(delta);
        super.render(delta);
        hud.update();
        if (pacman.getState() == Actor.State.DIEING) {
            hud.animateLives(delta);
        }
        hud.stage.draw();
    }
    /**
     *  Game over when time hits 0, Level up when all dots are eaten.
     *  When Pause is active the PauseScreen is opened.
     */
    @Override
    public void update(float dt) {
        super.update(dt);
        if(ready) hud.time -= Gdx.graphics.getDeltaTime();

        updateCoolDown();
        updateHunter();
        updateSloMo();

        if(hud.time < 0){
            this.dispose();
            if(PacManGame.prefManager.addScore(PacManGame.getScore(), "Time elapsed", PacManGame.getLevel() + 1)){
                GAME.setScreen(new ScoreScreen(GAME, ASSETS, ASSETS.SCORE_MAP));
            } else {
                GAME.setScreen(new MenuScreen(GAME, ASSETS, ASSETS.MENU_MAP));
            }
            PacManGame.resetLives();
            PacManGame.resetScore();
            PacManGame.resetLevel();
        }

        if(GameMap.getCollectedDots() == GameMap.TOTAL_DOTS){
            PacManGame.levelUp();
            PacManGame.increaseScore((int)hud.time);
            this.dispose();
            GAME.setScreen(new GameScreen(GAME, ASSETS, hud.getMap()));
        }

        if(paused) {
            if(PrefManager.isJoystick()) this.controller = new ControllerJoystick(ASSETS, this);
            else this.controller = new ControllerButtons(ASSETS,this);
            if(PrefManager.isMusicOn()){
                if(pacManSuper) ASSETS.manager.get(ASSETS.HUNTING_MUSIC).play();
                else music.play();
            }
            paused = false;
            PauseActive = false;
        }

        if(PauseActive){
            if(music.isPlaying()) music.pause();
            if(ASSETS.manager.get(ASSETS.HUNTING_MUSIC).isPlaying()) ASSETS.manager.get(ASSETS.HUNTING_MUSIC).pause();
            GAME.setScreen(new PauseScreen(GAME, ASSETS, ASSETS.PAUSE, this, hud));
            paused = true;
        }

    }
}