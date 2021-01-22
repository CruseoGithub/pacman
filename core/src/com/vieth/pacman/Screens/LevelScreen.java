package com.vieth.pacman.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.vieth.pacman.PacMan;


public class LevelScreen implements Screen{



        private Stage stage;
        private Game game;
        boolean backPressed;

        public LevelScreen(Game aGame) {
            game = aGame;
            stage = new Stage(new ScreenViewport());

            Label title = new Label("Pick your Level ", PacMan.gameSkin,"big-black");
            title.setAlignment(Align.center);
            title.setY(Gdx.graphics.getHeight()*2/3);
            title.setWidth(Gdx.graphics.getWidth());
            stage.addActor(title);
            Table table2 = new Table();
            table2.left().bottom();


            TextButton playButton = new TextButton("Level 1",PacMan.gameSkin);
            playButton.setWidth(Gdx.graphics.getWidth()/2);
            playButton.setPosition(Gdx.graphics.getWidth()/2-playButton.getWidth()/2,Gdx.graphics.getHeight()/2-playButton.getHeight()/2);
            playButton.addListener(new InputListener(){
                @Override
                public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                    game.setScreen(new GameScreen((PacMan) game));
                }
                @Override
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                    return true;
                }
            });
            stage.addActor(playButton);



            //Creating return Image
            Image backArrow = new Image(new Texture("return.png"));
            backArrow.setSize(70, 70 );
            backArrow.addListener(new InputListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    backPressed = true;
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    game.setScreen(new MenuScreen(game));
                    backPressed = false;
                }
            });

            table2.add(backArrow).size(backArrow.getWidth(), backArrow.getHeight());
            stage.addActor(table2);



        }

        @Override
        public void show() {
            Gdx.input.setInputProcessor(stage);
        }

        @Override
        public void render(float delta) {
            Gdx.gl.glClearColor(1, 1, 1, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            stage.act();
            stage.draw();
        }

        @Override
        public void resize(int width, int height) {

        }

        @Override
        public void pause() {

        }

        @Override
        public void resume() {

        }

        @Override
        public void hide() {

        }

        @Override
        public void dispose() {
            stage.dispose();
        }
}


