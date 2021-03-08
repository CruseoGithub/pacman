package uas.lntv.pacmangame.Screens;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Scenes.Hud;
import uas.lntv.pacmangame.Sprites.Enemy;
import uas.lntv.pacmangame.Sprites.PacMan;

public class ScoreScreen extends MapScreen {

    private BitmapFont font;

    public ScoreScreen(PacManGame game, String mapPath){
        super(game, mapPath, Type.SCORE);
        this.pacman = new PacMan(game, 25 * TILE_SIZE, 47 * TILE_SIZE, this, hud);
        this.pacman.setSpeed(16);
        this.ghosts.add(new Enemy(25 * TILE_SIZE, 3 * TILE_SIZE, this, "white.png"));
        this.hud = new Hud(game, this, false);
        this.font = new BitmapFont();
        font.getData().setScale(font.getScaleX()*2);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        game.batch.begin();
        for(int i = 0; i < 10; i++) {
            font.draw(
                    game.batch,
                    game.highScore.getHighScores().get(i).toString(),
                    15* TILE_SIZE,
                    (44 * TILE_SIZE) - (3 * i) * TILE_SIZE
            );
            font.draw(
                    game.batch,
                    game.highScore.getNames().get(i),
                    3 * TILE_SIZE,
                    (44 * TILE_SIZE) - (3 * i) * TILE_SIZE
            );
        }
        game.batch.end();

        if(pacman.getXPosition() <= 2* TILE_SIZE) {
            game.setScreen(new MenuScreen(game, "mainMenu.tmx"));
            this.dispose();
        }
    }

}
