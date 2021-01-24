package uas.lntv.pacmangame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Json;

import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Scenes.Hud;
import uas.lntv.pacmangame.Sprites.Enemy;
import uas.lntv.pacmangame.Sprites.PacMan;

public class MenuScreen extends MapScreen {

    public MenuScreen(PacManGame game, String mapPath){
        super(game, mapPath, Type.MENU);

        this.pacman = new PacMan(game, 2*map.tileSize, 26*map.tileSize, this, hud);
        this.pacman.setSpeed(8);
        this.ghost = new Enemy(16*map.tileSize,23*map.tileSize,this, Enemy.Difficulty.EASY);
        this.hud = new Hud(game, this, false); //Not Visible
    }

    @Override
    public void render(float delta) {

        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        ghost.findNextDirection(pacman);

        map.renderer.setView(gamecam);
        map.renderer.render();

        game.batch.begin();
        game.batch.draw(pacman.texture,pacman.getXPosition(),pacman.getYPosition(),pacman.sprite.getOriginX(), pacman.sprite.getOriginY(),
                map.tileSize,map.tileSize, pacman.sprite.getScaleX(), pacman.sprite.getScaleY(), pacman.rotation,
                pacman.texturePositionX,0,32,32,false,false);

        //game.batch.draw(ghost.sprite, ghost.xPosition , ghost.yPosition , map.tileSize, map.tileSize);

        game.batch.draw(ghost.texture,ghost.xPosition, ghost.yPosition,ghost.sprite.getOriginX(), ghost.sprite.getOriginY(),
                map.tileSize,map.tileSize, ghost.sprite.getScaleX(), ghost.sprite.getScaleY(), ghost.rotation,
                ghost.texturePositionX,  ghost.texturePositionY,32,32,false,false);

        game.batch.end();


        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.time +=Gdx.graphics.getDeltaTime();

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

        if(pacman.getXPosition() == 12*map.tileSize){
            if(pacman.getYPosition() >= 19*map.tileSize && pacman.getYPosition() <=23* map.tileSize ){
                // PLAY
                //game.setScreen(game.gameScreen);

                game.setScreen(new GameScreen(game, "map.tmx"));
                this.dispose();
            }
            else if(pacman.getYPosition() >= 29*map.tileSize && pacman.getYPosition() <=33* map.tileSize ){
                //SETTINGS (Momentan auch GameScreen)
                //game.setScreen(game.gameScreen);
                game.setScreen(new ScoreScreen(game, "HighScoreList.tmx"));
                this.dispose();
            }
        }

    }

}
