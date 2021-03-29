package uas.lntv.pacmangame.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import uas.lntv.pacmangame.Managers.Assets;
import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Managers.PrefManager;
import uas.lntv.pacmangame.Screens.GameScreen;
import uas.lntv.pacmangame.Screens.MapScreen;
import uas.lntv.pacmangame.Screens.MenuScreen;
import uas.lntv.pacmangame.Screens.ScoreScreen;

/**
 * PacMan is our protagonist. He is a yellow ball with a mouth with the size of half of his body.
 * PacMan is able to eat dots and collect items to get buffed.
 * He is very afraid of ghosts and should avoid contact with them unless he eats a cherrylicious
 * hunter item and becomes SuperPacMan, who is also able to eat ghosts.
 * PacMan is also representing himself in the HUD and displays the amount of remaining lives with
 * his own body.
 */
public class PacMan extends Actor {

    /* Fields */

    private final PacManGame GAME;

    /* Constructor */

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

        this.GAME = game;

        this.texture = assets.manager.get(assets.PAC_MAN);
        region = new TextureRegion(texture);
        region.setRegionX(0);
        region.setRegionY(0);
        texturePositionX = 0;
        animationSpeed = 0.01f;
        mouthOpen = true;

        animation = new Animation(this, assets, animationSpeed, this.SCREEN,6);

        region.flip(true, false);
        this.sprite = new Sprite(region);
        this.sprite.setOrigin(TILE_SIZE/2f, TILE_SIZE/2f);
    }

    /* Methods */

    /**
     * If PacMan as a hunter collides with a ghost, he will kill it.
     * It will cause a sound effect and increase the score.
     * Otherwise he will be killed by the ghost, lose a life and be set to his starting position.
     */
    @Override
    public void collide() {
        if(((GameScreen) SCREEN).isPacManSuper()){
            if(PrefManager.isSfxOn()) ASSETS.manager.get(ASSETS.KILL).play(0.15f);
            PacManGame.increaseScore(50);
        } else {
            super.collide();
            if (PrefManager.isSfxOn()) ASSETS.manager.get(ASSETS.DIE).play(0.35f);
            if (PacManGame.getLives() > 1) PacManGame.die();
            else {
                PacManGame.die();
                super.SCREEN.dispose();
                if (PacManGame.prefManager.addScore(PacManGame.getScore(), "Killed", PacManGame.getLevel() + 1)) {
                    GAME.setScreen(new ScoreScreen(GAME, ASSETS, ASSETS.SCORE_MAP));
                } else {
                    GAME.setScreen(new MenuScreen(GAME, ASSETS, ASSETS.MENU_MAP));
                }
                PacManGame.resetScore();
                PacManGame.resetLives();
                PacManGame.resetLevel();
            }
        }
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
     * Moves like an Actor, but additionally collects dots and items while walking.
     * @see Actor
     */
    @Override
    public void move(){
        super.move();
        MAP.collect(MAP.getTile(xPosition, yPosition)); //collect Dots
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

}