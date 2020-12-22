package com.vieth.pacman.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import com.vieth.pacman.PacMan;

public class Hud {
    public Stage stage;
    private Viewport viewport;

    private Integer time;
    private Integer score;
    private Integer lives;

    Label timeTextLabel;
    Label timeLabel;
    Label scoreTextLabel;
    Label scoreLabel;
    Label livesTextLabel;
    Label livesLabel;

    public Hud(SpriteBatch sb){
        time = 400;
        score = 0;
        lives = 3;

        viewport = new FitViewport(PacMan.V_WIDTH, PacMan.V_HEIGHT, (new OrthographicCamera()));
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        scoreTextLabel = new Label("SCORE:", new Label.LabelStyle(new BitmapFont(), Color.YELLOW));
        timeTextLabel = new Label("TIME:", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        livesTextLabel = new Label("LIVES:", new Label.LabelStyle(new BitmapFont(), Color.FIREBRICK));

        scoreLabel = new Label(String.format("%06d  ", score), new Label.LabelStyle(new BitmapFont(), Color.YELLOW));
        timeLabel = new Label(String.format("%03d  ", time), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        livesLabel = new Label(String.format("%01d  ", lives), new Label.LabelStyle(new BitmapFont(), Color.FIREBRICK));

        table.add(scoreTextLabel).expandX().padTop(0);
        table.add(timeTextLabel).expandX().padTop(0);
        table.add(livesTextLabel).expandX().padTop(0);
        table.row();
        table.add(scoreLabel).expandX().padTop(0);
        table.add(timeLabel).expandX().padTop(0);
        table.add(livesLabel).expandX().padTop(0);
        //

        stage.addActor(table);
    }
}
