package uas.lntv.pacmangame.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import uas.lntv.pacmangame.Managers.Assets;
import uas.lntv.pacmangame.Managers.PrefManager;
import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Screens.GameScreen;
import uas.lntv.pacmangame.Screens.MapScreen;
import uas.lntv.pacmangame.Sprites.Actor;
import uas.lntv.pacmangame.Sprites.PacMan;

public class Hud {
    public Stage stage;
    private final Assets assets;
    private final MapScreen SCREEN;

    public float time;
    public Integer levelScore;
    public boolean visible;

    private final ArrayList<String> STAGES = new ArrayList<>();
    private final ArrayList<PacMan> LIVE_PAC_MEN = new ArrayList<>();

    private final Label TIME_TEXT_LABEL;
    private final Label TIME_LABEL;
    private final Label SCORE_LABEL;

    private boolean warned;
    private boolean red;
    private final int WARNING_TIME;
    private float timeStamp;

    @SuppressWarnings("DefaultLocale")
    public Hud(PacManGame game, Assets assets, MapScreen screen, boolean visible){
        this.assets = assets;
        this.SCREEN = screen;
        int mapWidth = screen.map.getMapWidth();
        int mapHeight = screen.map.getMapHeight();
        int tileSize = screen.map.getTileSize();
        time = 120;
        levelScore = 0;
        STAGES.add(assets.MAP_1);
        STAGES.add(assets.MAP_2);
        STAGES.add(assets.MAP_3);
        STAGES.add(assets.MAP_4);
        STAGES.add(assets.MAP_5);

        this.visible = visible;

        Viewport viewport = new FitViewport(mapWidth * tileSize, mapHeight * tileSize, (new OrthographicCamera()));
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

        SCORE_LABEL = new Label(String.format("%06d  ", PacManGame.getScore()), new Label.LabelStyle(new BitmapFont(), Color.YELLOW));
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

        LIVE_PAC_MEN.add(new PacMan(game, assets, 20 * tileSize, (45 * tileSize + tileSize /2), screen, this));
        LIVE_PAC_MEN.add(new PacMan(game, assets, 22 * tileSize, (45 * tileSize + tileSize /2), screen, this));
        LIVE_PAC_MEN.add(new PacMan(game, assets, 24 * tileSize, (45 * tileSize + tileSize /2), screen, this));

        warned = false;
        red = false;
        WARNING_TIME = 30;
        timeStamp = time;
    }

    public final String getMap(){ return STAGES.get(PacManGame.getLevel() % 5); }


    public void animateLives(float dt) {
        if (PacManGame.getLives() == 1) {
            animate(LIVE_PAC_MEN.get(1), dt/1.1f);
        }

        if (PacManGame.getLives() == 2) {
            animate(LIVE_PAC_MEN.get(2), dt/1.1f);
        }
    }

    private void animate(PacMan pacman, float dt){
        pacman.setState(Actor.State.DIEING);
        pacman.update(dt);
        pacman.drawLife();
    }

    @SuppressWarnings("DefaultLocale")
    public void update() {
        if (time < WARNING_TIME) {
            if (!warned) {
                if (PrefManager.isSfxOn()) assets.manager.get(assets.ALARM).play(0.4f);
                warned = true;
            }
            if (timeStamp - 0.5 > time) {
                if (!red) {
                    TIME_TEXT_LABEL.setColor(Color.RED);
                    TIME_LABEL.setColor(Color.RED);
                    red = true;
                } else {
                    TIME_TEXT_LABEL.setColor(Color.WHITE);
                    TIME_LABEL.setColor(Color.WHITE);
                    red = false;
                }
                timeStamp = time;
            }
        }


        if (visible) {
            SCORE_LABEL.setText(String.format("%06d", PacManGame.getScore()));
            TIME_LABEL.setText(String.format("%03d", (int) time));


            if(((GameScreen)SCREEN).isPacManSuper()){
                for(PacMan pacman : LIVE_PAC_MEN) {
                    pacman.setTexture(assets.manager.get(assets.SUPER_PAC));
                }
            } else if (SCREEN.pacman.getState() != Actor.State.DIEING){
                for(PacMan pacman : LIVE_PAC_MEN) {
                    pacman.setTexture(assets.manager.get(assets.PAC_MAN));
                }
            }

            if (PacManGame.getLives() >= 1) {
                LIVE_PAC_MEN.get(0).drawLife();
            }
            if (PacManGame.getLives() >= 2) {
                LIVE_PAC_MEN.get(1).drawLife();
            }
            if (PacManGame.getLives() >= 3) {
                LIVE_PAC_MEN.get(2).drawLife();
            }
        }
    }

    public void resetLives(){
        if (PacManGame.getLives() == 2) {
            reset(LIVE_PAC_MEN.get(1));
        }
        if (PacManGame.getLives() == 3) {
            reset(LIVE_PAC_MEN.get(2));
        }
    }

    private void reset(PacMan pacman){
        pacman.getAnimation().resetTmp();
        pacman.setState(Actor.State.RUNNING);
        pacman.resetTexturePosition();
        pacman.setXPosition(pacman.getHomeX());
        pacman.setYPosition(pacman.getHomeY());
    }


    public void dispose(){
        stage.dispose();
    }
}