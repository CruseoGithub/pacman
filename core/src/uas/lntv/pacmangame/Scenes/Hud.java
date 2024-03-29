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
import uas.lntv.pacmangame.Maps.Map;
import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Screens.GameScreen;
import uas.lntv.pacmangame.Screens.MapScreen;
import uas.lntv.pacmangame.Sprites.Actor;
import uas.lntv.pacmangame.Sprites.PacMan;

/**
 * (Jabba)The HUD shows: Score, Time, Lives and functions as a Pause-button.
 */
public class Hud {

    /* Fields */

    private boolean warned = false;
    private boolean red = false;
    private final Assets assets;
    private final ArrayList<PacMan> LIVE_PAC_MEN = new ArrayList<>();
    private final ArrayList<String> STAGES = new ArrayList<>();
    private final boolean VISIBLE;
    private final int WARNING_TIME = 30;
    private final Label TIME_LABEL;
    private final Label TIME_TEXT_LABEL;
    private final Label SCORE_LABEL;
    private final MapScreen SCREEN;
    private final Stage STAGE;
    private float time;
    private float timeStamp;

    /* Constructor */

    /**
     * (Jabba)The HUD
     * Arranges the HUD with SCORE, TIME and LIVES
     *
     * @param game gets handed over
     * @param assets asset management
     * @param screen gets handed over
     * @param visible HUD only visible when true
     */
    @SuppressWarnings("DefaultLocale")
    public Hud(PacManGame game, Assets assets, MapScreen screen, boolean visible){
        this.assets = assets;
        this.SCREEN = screen;
        int tileSize = Map.getTileSize();
        time = 120;
        STAGES.add(assets.MAP_1);
        STAGES.add(assets.MAP_2);
        STAGES.add(assets.MAP_3);
        STAGES.add(assets.MAP_4);
        STAGES.add(assets.MAP_5);

        this.VISIBLE = visible;

        Viewport viewport = new FitViewport(
                Map.getMapWidth() * tileSize,
                Map.getMapHeight() * tileSize,
                new OrthographicCamera()
        );
        STAGE = new Stage(viewport, PacManGame.batch);

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
        if(visible) STAGE.addActor(table);

        for(int i = 0; i < 3; i++) {
            LIVE_PAC_MEN.add(new PacMan(game, assets, (20 + (2 * i)) * tileSize, (45 * tileSize + tileSize / 2), screen));
        }

        timeStamp = time;
    }

    /* Accessors */

    public float getTime(){ return this.time; }

    public Stage getStage(){ return this.STAGE; }

    /* Mutators */

    public void updateTime(float delta){ this.time -= delta; }

    /* Methods */

    /**
     * Animates the images according to the lives of PacMan.
     * @param dt time parameter used by libGDX
     */
    public void animateLives(float dt) {
        if (PacManGame.getLives() == 1) {
            animate(LIVE_PAC_MEN.get(1), dt/1.1f);
        }

        if (PacManGame.getLives() == 2) {
            animate(LIVE_PAC_MEN.get(2), dt/1.1f);
        }
    }

    /**
     * Animation of dying.
     * @param pacman one of the drawn lives
     * @param dt time parameter used by libGDX
     */
    private void animate(PacMan pacman, float dt){
        pacman.setState(Actor.State.DIEING);
        pacman.update(dt);
        pacman.drawLife();
    }

    /**
     * Ends the dying animation and sets PacMan back to the displayable state he used to have.
     * @param pacman one of the drawn lives
     */
    private void reset(PacMan pacman){
        pacman.getAnimation().resetTmp();
        pacman.setState(Actor.State.RUNNING);
        pacman.resetTexturePosition();
        pacman.setXPosition(pacman.getHomeX());
        pacman.setYPosition(pacman.getHomeY());
    }

    /**
     * Checks the level of the game and returns the according path to the map.
     * @return path of map as a String
     */
    public String getMap(){ return STAGES.get(PacManGame.getLevel() % 5); }

    /**
     * Disposes the created stage.
     */
    public void dispose(){
        STAGE.dispose();
    }

    /**
     * Redraws the PacMans in the HUD when when you eat a LifeUp item.
     */
    public void resetLives(){
        if (PacManGame.getLives() == 2) {
            reset(LIVE_PAC_MEN.get(1));
        }
        if (PacManGame.getLives() == 3) {
            reset(LIVE_PAC_MEN.get(2));
        }
    }

    public void resetTimeStamp() {
        if (time > WARNING_TIME){
            TIME_TEXT_LABEL.setColor(Color.WHITE);
            TIME_LABEL.setColor(Color.WHITE);
            red = false;
        }
        timeStamp = time;
    }

    /**
     * Changes the Color of the displayed TIME according to time left
     * Number of PacMans in HUD change depending on lives left
     */
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


        if (VISIBLE) {
            SCORE_LABEL.setText(String.format("%06d", PacManGame.getScore()));
            TIME_LABEL.setText(String.format("%03d", (int) time));


            if(((GameScreen)SCREEN).isPacManSuper()){
                for(PacMan pacman : LIVE_PAC_MEN) {
                    pacman.setTexture(assets.manager.get(assets.SUPER_PAC));
                }
            } else if (SCREEN.getPacman().getState() != Actor.State.DIEING){
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

}