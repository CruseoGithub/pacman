package uas.lntv.pacmangame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Scenes.Hud;
import uas.lntv.pacmangame.Sprites.Enemy;
import uas.lntv.pacmangame.Sprites.PacMan;

public class PauseScreen extends MapScreen {

    private BitmapFont font;

    public PauseScreen(PacManGame game, String mapPath){
        super(game, mapPath, Type.MENU);
        this.pacman = new PacMan(game, 25*map.tileSize, 47*map.tileSize, this, hud);
        this.pacman.setSpeed(16);

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
                    map.tileSize * 3,
                    (map.tileSize * 44)-3*map.tileSize*i
            );
        }
        game.batch.end();

        if(pacman.getXPosition() <= 2*map.tileSize) {
            //game.setScreen(GameScreen(game, "map.tmx"));
            //game.setScreen(new GameScreen(game, "map.tmx"));
            this.dispose();
        }
    }

}