package uas.lntv.pacmangame.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Screens.MapScreen;
import uas.lntv.pacmangame.Sprites.Actor;
import uas.lntv.pacmangame.Sprites.PacMan;

public class Hud {
    private final PacManGame GAME;
    public Stage stage;

    public float time;
    public Integer levelScore;
    public boolean visible;

    private final String[] STAGES;

    Label timeTextLabel;
    Label timeLabel;
    Label scoreTextLabel;
    Label scoreLabel;
    Label livesTextLabel;
    Label livesLabel;

    private final Sound TIME_WARNING;
    private boolean warned;
    private boolean red;
    private final int WARNING_TIME;
    private float timeStamp;
    private final int TILE_SIZE;

    private final PacMan PACMAN;

    @SuppressWarnings("DefaultLocale")
    public Hud(PacManGame game, MapScreen screen, boolean visible){
        this.GAME = game;
        int mapWidth = screen.map.getMapWidth();
        int mapHeight = screen.map.getMapHeight();
        this.TILE_SIZE = screen.map.getTileSize();
        time = 100;
        levelScore = 0;
        STAGES = new String[5];
        STAGES[0] = "map.tmx";
        STAGES[1] = "map2.tmx";
        STAGES[2] = "map3.tmx";
        STAGES[3] = "map4.tmx";
        STAGES[4] = "map5.tmx";

        this.visible = visible;

        Viewport viewport = new FitViewport(mapWidth * TILE_SIZE, mapHeight * TILE_SIZE, (new OrthographicCamera()));
        stage = new Stage(viewport, PacManGame.batch);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        scoreTextLabel = new Label("SCORE:", new Label.LabelStyle(new BitmapFont(), Color.YELLOW));
        timeTextLabel = new Label("TIME:", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        livesTextLabel = new Label("LIVES:", new Label.LabelStyle(new BitmapFont(), Color.FIREBRICK));

        scoreTextLabel.setFontScale(4);
        timeTextLabel.setFontScale(4);
        livesTextLabel.setFontScale(4);

        scoreLabel = new Label(String.format("%06d  ", game.getScore()), new Label.LabelStyle(new BitmapFont(), Color.YELLOW));
        timeLabel = new Label(String.format("%03d  ", (int)time), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        livesLabel = new Label(String.format("%01d  ", game.getLives()), new Label.LabelStyle(new BitmapFont(), Color.FIREBRICK));

        scoreLabel.setFontScale(4);
        timeLabel.setFontScale(4);
        livesLabel.setFontScale(4);

        table.add(scoreTextLabel).expandX().padTop(0);
        table.add(timeTextLabel).expandX().padTop(0);
        table.add(livesTextLabel).expandX().padTop(0);
        table.row();
        table.add(scoreLabel).expandX().padTop(0);
        table.add(timeLabel).expandX().padTop(0);
        //table.add(livesLabel).expandX().padTop(0);
        if(visible) stage.addActor(table);

        PACMAN = new PacMan(
                game,
                20 * TILE_SIZE,
                (45 * TILE_SIZE + TILE_SIZE /2),
                0,
                screen,
                this,
                Actor.Direction.RIGHT,
                Actor.Direction.RIGHT,
                Actor.Direction.RIGHT
        );

        TIME_WARNING = Gdx.audio.newSound(Gdx.files.internal("ALARM.mp3"));
        warned = false;
        red = false;
        WARNING_TIME = 30;
        timeStamp = time;
    }

    public String getMap(){
        return STAGES[(GAME.getLevel() % 5)];
    }

    @SuppressWarnings("DefaultLocale")
    public void update(){
        if(time < WARNING_TIME){
            if(!warned){
                TIME_WARNING.play(0.4f);
                warned = true;
            }
            if(timeStamp - 0.5 > time) {
                if(!red) {
                    timeTextLabel.setColor(Color.RED);
                    timeLabel.setColor(Color.RED);
                    red = true;
                } else{
                    timeTextLabel.setColor(Color.WHITE);
                    timeLabel.setColor(Color.WHITE);
                    red = false;
                }
                timeStamp = time;
            }
        }

        if(visible){
            scoreLabel.setText(String.format("%06d", GAME.getScore()));
            timeLabel.setText(String.format("%03d", (int)time));
            //livesLabel.setText(String.format("%01d", game.getLives()));
            PacManGame.batch.begin();
            if(GAME.getLives() >= 1) {
                PacManGame.batch.draw(PACMAN.texture,
                        PACMAN.getXPosition(), PACMAN.getYPosition(),
                        PACMAN.sprite.getOriginX(), PACMAN.sprite.getOriginY(),
                        2 * TILE_SIZE, 2 * TILE_SIZE,
                        PACMAN.sprite.getScaleX(), PACMAN.sprite.getScaleY(), PACMAN.rotation,
                        PACMAN.getTexturePositionX() + 96, 0, 32, 32, false, false);
            }
            if(GAME.getLives() >= 2) {
                PacManGame.batch.draw(PACMAN.texture,
                        PACMAN.getXPosition() + 2 * TILE_SIZE, PACMAN.getYPosition(),
                        PACMAN.sprite.getOriginX(), PACMAN.sprite.getOriginY(),
                        2 * TILE_SIZE, 2 * TILE_SIZE,
                        PACMAN.sprite.getScaleX(), PACMAN.sprite.getScaleY(), PACMAN.rotation,
                        PACMAN.getTexturePositionX() + 96, 0, 32, 32, false, false);
            }
            if(GAME.getLives() >= 3) {
                PacManGame.batch.draw(PACMAN.texture,
                        PACMAN.getXPosition() + 4 * TILE_SIZE, PACMAN.getYPosition(),
                        PACMAN.sprite.getOriginX(), PACMAN.sprite.getOriginY(),
                        2 * TILE_SIZE, 2 * TILE_SIZE,
                        PACMAN.sprite.getScaleX(), PACMAN.sprite.getScaleY(), PACMAN.rotation,
                        PACMAN.getTexturePositionX() + 96, 0, 32, 32, false, false);
            }
            PacManGame.batch.end();
        }
    }

    public void dispose(){
        stage.dispose();
    }
}