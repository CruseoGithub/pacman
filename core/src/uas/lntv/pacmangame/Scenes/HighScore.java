package uas.lntv.pacmangame.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.util.ArrayList;

import uas.lntv.pacmangame.PacManGame;

public class HighScore {

    private static Preferences prefs;
    private static ArrayList<Integer> highScores = new ArrayList<Integer>();

    public HighScore(){
        prefs = Gdx.app.getPreferences("PacManPreferences");
        loadPrefs();
    }

    public static ArrayList<Integer> getHighScores(){
        return highScores;
    }

    public static boolean addScore(int newScore){
        boolean newHighScore=false;

        for(int i = 0; i< highScores.size(); i++){
            int x = highScores.get(i);
            if(newScore > x){
                highScores.add(i, newScore);
                if(highScores.size()>10){
                    highScores.remove(10);
                }
                newHighScore = true;
                savePrefs();
                break;
            }
        }
        return newHighScore;
    }

    public static void loadPrefs(){
        highScores.add(prefs.getInteger("high_score_1"));
        highScores.add(prefs.getInteger("high_score_2"));
        highScores.add(prefs.getInteger("high_score_3"));
        highScores.add(prefs.getInteger("high_score_4"));
        highScores.add(prefs.getInteger("high_score_5"));
        highScores.add(prefs.getInteger("high_score_6"));
        highScores.add(prefs.getInteger("high_score_7"));
        highScores.add(prefs.getInteger("high_score_8"));
        highScores.add(prefs.getInteger("high_score_9"));
        highScores.add(prefs.getInteger("high_score_10"));
    }

    public static void savePrefs(){
        prefs.putInteger("high_score_1", highScores.get(0));
        prefs.putInteger("high_score_2", highScores.get(1));
        prefs.putInteger("high_score_3", highScores.get(2));
        prefs.putInteger("high_score_4", highScores.get(3));
        prefs.putInteger("high_score_5", highScores.get(4));
        prefs.putInteger("high_score_6", highScores.get(5));
        prefs.putInteger("high_score_7", highScores.get(6));
        prefs.putInteger("high_score_8", highScores.get(7));
        prefs.putInteger("high_score_9", highScores.get(8));
        prefs.putInteger("high_score_10", highScores.get(9));

        prefs.flush();
    }
}
