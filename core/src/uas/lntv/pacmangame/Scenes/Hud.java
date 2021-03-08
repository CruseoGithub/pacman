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

    private final Label TIME_TEXT_LABEL;
    private final Label TIME_LABEL;
    private final Label SCORE_LABEL;

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
        time = 120;
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

        Label SCORE_TEXT_LABEL = new Label("SCORE:", new Label.LabelStyle(new BitmapFont(), Color.YELLOW));
        TIME_TEXT_LABEL = new Label("TIME:", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Label LIVES_TEXT_LABEL = new Label("LIVES:", new Label.LabelStyle(new BitmapFont(), Color.FIREBRICK));

        SCORE_TEXT_LABEL.setFontScale(4);
        TIME_TEXT_LABEL.setFontScale(4);
        LIVES_TEXT_LABEL.setFontScale(4);

        SCORE_LABEL = new Label(String.format("%06d  ", game.getScore()), new Label.LabelStyle(new BitmapFont(), Color.YELLOW));
        TIME_LABEL = new Label(String.format("%03d  ", (int)time), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        SCORE_LABEL.setFontScale(4);
        TIME_LABEL.setFontScale(4);

        table.add(SCORE_TEXT_LABEL).expandX().padTop(0);
        table.add(TIME_TEXT_LABEL).expandX().padTop(0);
        table.add(LIVES_TEXT_LABEL).expandX().padTop(0);
        table.row();
        table.add(SCORE_LABEL).expandX().padTop(0);
        table.add(TIME_LABEL).expandX().padTop(0);
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
                    TIME_TEXT_LABEL.setColor(Color.RED);
                    TIME_LABEL.setColor(Color.RED);
                    red = true;
                } else{
                    TIME_TEXT_LABEL.setColor(Color.WHITE);
                    TIME_LABEL.setColor(Color.WHITE);
                    red = false;
                }
                timeStamp = time;
            }
        }

        if(visible){
            SCORE_LABEL.setText(String.format("%06d", GAME.getScore()));
            TIME_LABEL.setText(String.format("%03d", (int)time));
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