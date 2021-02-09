package uas.lntv.pacmangame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;

import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Scenes.Hud;
import uas.lntv.pacmangame.Sprites.Actor;
import uas.lntv.pacmangame.Sprites.Enemy;
import uas.lntv.pacmangame.Sprites.PacMan;

public class MenuScreen extends MapScreen {

    public MenuScreen(PacManGame game, String mapPath){
        super(game, mapPath, Type.MENU);

        this.pacman = new PacMan(game, 2*tileSize, 26*tileSize, this, hud);
        this.pacman.setSpeed(8);
        this.ghosts.add(new Enemy(16*tileSize,23*tileSize,this, "blue.png"));
        this.hud = new Hud(game, this, false); //Not Visible
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if(!game.highScore.isNameSet()) {
            Gdx.input.getTextInput(
                    new Input.TextInputListener() {
                        @Override
                        public void input(String text) {
                            game.highScore.setGamer(text);
                        }

                        @Override
                        public void canceled() {
                            game.highScore.setGamer("Anonymous Bastard");
                        }
                    },
                    "Please enter your name", "", "Name"
            );
            game.highScore.nameIsSet();
        }

        if(pacman.getXPosition() == 12*tileSize){
            if(pacman.getYPosition() >= 19*tileSize && pacman.getYPosition() <= 23*tileSize ){
                // PLAY
                //game.setScreen(game.gameScreen);

                game.setScreen(new GameScreen(game, "map.tmx"));
                this.dispose();
            }
            else if(pacman.getYPosition() >= 29*tileSize && pacman.getYPosition() <= 33*tileSize ){
                //SETTINGS (Momentan auch GameScreen)
                //game.setScreen(game.gameScreen);
                game.setScreen(new ScoreScreen(game, "HighScoreList.tmx"));
                this.dispose();
            }
        }

    }

}