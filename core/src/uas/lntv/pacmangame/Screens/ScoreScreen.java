package uas.lntv.pacmangame.Screens;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Scenes.HighScore;
import uas.lntv.pacmangame.Scenes.Hud;
import uas.lntv.pacmangame.Sprites.Enemy;
import uas.lntv.pacmangame.Sprites.PacMan;

public class ScoreScreen extends MapScreen {

    private final BitmapFont FONT;

    public ScoreScreen(PacManGame game, String mapPath){
        super(game, mapPath, Type.SCORE);
        this.pacman = new PacMan(game, 25 * TILE_SIZE, 47 * TILE_SIZE, this, hud);
        this.pacman.setSpeed(16);
        this.ghosts.add(new Enemy(25 * TILE_SIZE, 3 * TILE_SIZE, this, "white.png"));
        this.hud = new Hud(game, this, false);
        this.FONT = new BitmapFont();
        FONT.getData().setScale(FONT.getScaleX()*2);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        PacManGame.batch.begin();
        for(int i = 0; i < 10; i++) {
            int yPos = (44 * TILE_SIZE) - (3 * i) * TILE_SIZE;
            FONT.draw(
                    PacManGame.batch,
                    HighScore.getHighScores().get(i).toString(),
                    15* TILE_SIZE,
                    yPos
            );
            FONT.draw(
                    PacManGame.batch,
                    HighScore.getNames().get(i),
                    3 * TILE_SIZE,
                    yPos
            );
        }
        PacManGame.batch.end();

        if(pacman.getXPosition() <= 2* TILE_SIZE) {
            game.setScreen(new MenuScreen(game, "mainMenu.tmx"));
            this.dispose();
        }
    }

}
