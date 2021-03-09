package uas.lntv.pacmangame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import uas.lntv.pacmangame.Assets;
import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Scenes.HighScore;
import uas.lntv.pacmangame.Scenes.Hud;
import uas.lntv.pacmangame.Sprites.Enemy;
import uas.lntv.pacmangame.Sprites.PacMan;

public class MenuScreen extends MapScreen {

    public MenuScreen(PacManGame game, Assets assets, String path){
        super(game, assets, path, Type.MENU);
        this.pacman = new PacMan(game, assets, 2* TILE_SIZE, 26* TILE_SIZE, this, hud);
        this.pacman.setSpeed(8);
        this.ghosts.add(new Enemy(16* TILE_SIZE,23* TILE_SIZE,this, "blue.png"));
        this.hud = new Hud(game, assets,this, false); //Not Visible
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if(!game.highScore.isNameSet()) {
            Gdx.input.getTextInput(
                    new Input.TextInputListener() {
                        @Override
                        public void input(String text) {
                            if(text.isEmpty()) {
                                Gdx.input.getTextInput(
                                    new Input.TextInputListener() {
                                        @Override
                                        public void input(String s) {
                                            if(s.isEmpty()) anonymous();
                                            HighScore.setPlayer(s);
                                        }
                                        @Override
                                        public void canceled() {
                                            anonymous();
                                        }
                                    },
                                        "Are you sure?", "Anonymous Bastard", ""
                                );
                            } else HighScore.setPlayer(text);
                        }

                        @Override
                        public void canceled() {
                            anonymous();
                        }
                    },
                    "Please enter your name", "", "Name"
            );
            game.highScore.nameIsSet();
        }

        if(pacman.getXPosition() == 12* TILE_SIZE){
            if(pacman.getYPosition() >= 19* TILE_SIZE && pacman.getYPosition() <= 23* TILE_SIZE){
                game.setScreen(new GameScreen(game, assets, "map.tmx"));
                this.dispose();
            }
            else if(pacman.getYPosition() >= 29* TILE_SIZE && pacman.getYPosition() <= 33* TILE_SIZE){
                game.setScreen(new ScoreScreen(game, assets, "HighScoreList.tmx"));
                this.dispose();
            }
        }
    }

    private void anonymous(){ HighScore.setPlayer("Anonymous Bastard"); }

    @Override
    public void dispose() {
        super.dispose();
        hud.dispose();
    }
}