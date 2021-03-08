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
    private PacManGame game;
    public Stage stage;
    private Viewport viewport;
    private MapScreen screen;

    public float time;
    public Integer levelScore;
    //public Integer lives;
    public boolean visible;

    private String[] stages;

    Label timeTextLabel;
    Label timeLabel;
    Label scoreTextLabel;
    Label scoreLabel;
    Label livesTextLabel;
    Label livesLabel;

    private Sound timeWarning;
    private boolean warned;
    private boolean red;
    private int warningTime;
    private float timeStamp;
    private final int MAP_WIDTH;
    private final int MAP_HEIGHT;
    private final int TILE_SIZE;

    private PacMan pacman;

    public Hud(PacManGame game, MapScreen screen, boolean visible){
        this.game = game;
        this.screen = screen;
        this.MAP_WIDTH = screen.map.getMapWidth();
        this.MAP_HEIGHT = screen.map.getMapHeight();
        this.TILE_SIZE = screen.map.getTileSize();
        time = 100;
        levelScore = 0;
        //lives = 3;
        stages = new String[5];
        stages[0] = "map.tmx";
        stages[1] = "map2.tmx";
        stages[2] = "map3.tmx";
        stages[3] = "map4.tmx";
        stages[4] = "map5.tmx";

        this.visible = visible;

        viewport = new FitViewport(MAP_WIDTH * TILE_SIZE, MAP_HEIGHT * TILE_SIZE, (new OrthographicCamera()));
        stage = new Stage(viewport, game.batch);

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

        pacman = new PacMan(
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

        timeWarning = Gdx.audio.newSound(Gdx.files.internal("ALARM.mp3"));
        warned = false;
        red = false;
        warningTime = 30;
        timeStamp = time;
    }

    public String getMap(){
        return stages[(game.getLevel() % 5)];
    }

    public void update(){
        if(time < warningTime){
            if(!warned){
                timeWarning.play(0.4f);
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
            scoreLabel.setText(String.format("%06d", game.getScore()));
            timeLabel.setText(String.format("%03d", (int)time));
            //livesLabel.setText(String.format("%01d", game.getLives()));
            game.batch.begin();
            if(game.getLives() >= 1) {
                game.batch.draw(pacman.texture,
                        pacman.getXPosition(), pacman.getYPosition(),
                        pacman.sprite.getOriginX(), pacman.sprite.getOriginY(),
                        2 * TILE_SIZE, 2 * TILE_SIZE,
                        pacman.sprite.getScaleX(), pacman.sprite.getScaleY(), pacman.rotation,
                        pacman.getTexturePositionX() + 96, 0, 32, 32, false, false);
            }
            if(game.getLives() >= 2) {
                game.batch.draw(pacman.texture,
                        pacman.getXPosition() + 2 * TILE_SIZE, pacman.getYPosition(),
                        pacman.sprite.getOriginX(), pacman.sprite.getOriginY(),
                        2 * TILE_SIZE, 2 * TILE_SIZE,
                        pacman.sprite.getScaleX(), pacman.sprite.getScaleY(), pacman.rotation,
                        pacman.getTexturePositionX() + 96, 0, 32, 32, false, false);
            }
            if(game.getLives() >= 3) {
                game.batch.draw(pacman.texture,
                        pacman.getXPosition() + 4 * TILE_SIZE, pacman.getYPosition(),
                        pacman.sprite.getOriginX(), pacman.sprite.getOriginY(),
                        2 * TILE_SIZE, 2 * TILE_SIZE,
                        pacman.sprite.getScaleX(), pacman.sprite.getScaleY(), pacman.rotation,
                        pacman.getTexturePositionX() + 96, 0, 32, 32, false, false);
            }
            game.batch.end();
        }
    }

    public void dispose(){
        stage.dispose();
    }
}