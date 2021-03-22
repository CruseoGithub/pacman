package uas.lntv.pacmangame.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import uas.lntv.pacmangame.Managers.Assets;
import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Scenes.Hud;
import uas.lntv.pacmangame.Managers.PrefManager;
import uas.lntv.pacmangame.Screens.GameScreen;
import uas.lntv.pacmangame.Screens.MapScreen;
import uas.lntv.pacmangame.Screens.MenuScreen;
import uas.lntv.pacmangame.Screens.ScoreScreen;

/**
 * PacMan is out protagonist. He is a yellow ball with a mouth with the size of half of his body.
 * PacMan is able to eat dots and collect items to get buffed.
 * He is very afraid of ghosts and should avoid contact with them unless he ate a hunter item and
 * became SuperPacMan, who is also able to eat ghosts.
 * PacMan is also representing himself in the HUD and displays the amount of remaining lives with
 * his own body.
 */
public class PacMan extends Actor {
    protected PacManGame game;

    /**
     * Create a new PacMan
     * @param game The running game
     * @param assets The used assets-manager
     * @param initX Starting x-coordinate
     * @param initY Starting y-coordinate
     * @param screen The screen in which PacMan will be created
     */
    public PacMan(PacManGame game, Assets assets, int initX, int initY, MapScreen screen){
        super(assets, initX, initY, screen);
        this.direction = Direction.RIGHT;
        this.nextDirection = Direction.RIGHT;
        this.prevDirection = Direction.RIGHT;

        homeX = initX;
        homeY = initY;

        this.game = game;

        this.texture = assets.manager.get(assets.PAC_MAN);
        region = new TextureRegion(texture);
        region.setRegionX(0);
        region.setRegionY(0);
        texturePositionX = 0;
        animationSpeed = 0.01f;
        mouthOpen = true;

        animation = new Animation(this, assets, animationSpeed, this.screen,6);

        region.flip(true, false);
        this.sprite = new Sprite(region);
        this.sprite.setOrigin(TILE_SIZE/2f, TILE_SIZE/2f);
    }

    /**
     * Draws PacMan at the top of the screen in super-size.
     * This method should only be called in the HUD to display the lives in a beautiful way.
     */
    public void drawLife(){
        int shift = 0;
        if(state != State.DIEING) shift = 96;
        PacManGame.batch.begin();
        PacManGame.batch.draw(
                texture,
                xPosition, yPosition,
                sprite.getOriginX(), sprite.getOriginY(),
                2 * TILE_SIZE, 2 * TILE_SIZE,
                sprite.getScaleX(), sprite.getScaleY(), rotation,
                texturePositionX + shift,
                0, 32, 32, false, false
        );
        PacManGame.batch.end();
    }

    /**
     * If PacMan as a hunter collides with a ghost, he will kill it.
     * It will cause a sound effect and increase the score.
     * Otherwise he will be killed by the ghost, lose a life and be set to his starting position.
     */
    @Override
    public void collide() {
        if(((GameScreen)screen).isPacManSuper()){
            if(PrefManager.isSfxOn()) assets.manager.get(assets.KILL).play(0.15f);
            PacManGame.increaseScore(50);
        } else {
            super.collide();
            if (PrefManager.isSfxOn()) assets.manager.get(assets.DIE).play(0.35f);
            if (PacManGame.getLives() > 1) PacManGame.die();
            else {
                PacManGame.die();
                super.screen.dispose();
                if (PacManGame.prefManager.addScore(PacManGame.getScore(), "Killed", PacManGame.getLevel() + 1)) {
                    game.setScreen(new ScoreScreen(game, assets, assets.SCORE_MAP));
                } else {
                    game.setScreen(new MenuScreen(game, assets, assets.MENU_MAP));
                }
                PacManGame.resetScore();
                PacManGame.resetLives();
                PacManGame.resetLevel();
            }
        }
    }

    /**
     * Checks if PacMan is dieing right now and moves if he's not.
     * @param dt time parameter used by libGDX
     * @see Actor
     */
    @Override
    public void update(float dt) {
        super.update(dt);
        if(getState() != State.DIEING) move();
    }

    /**
     * Moves like an Actor, but additionally collects dots and items while walking.
     * @see Actor
     */
    @Override
    public void move(){
        super.move();
        screen.map.collect(screen.map.getTile(xPosition, yPosition)); //collect Dots
    }

}