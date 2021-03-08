package uas.lntv.pacmangame.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.util.ArrayList;

import uas.lntv.pacmangame.PacManGame;

public class HighScore {

    private static Preferences prefs;
    private static ArrayList<Integer> highScores = new ArrayList<Integer>();
    private static ArrayList<String> names = new ArrayList<String>();
    private String gamer;
    private boolean nameSet;

    /**
     * This constructor simply loads the saved high-scores from the preferences.
     * It also checks, if the player's name has already been set before.
     */
    public HighScore(){
        prefs = Gdx.app.getPreferences("PacManPreferences");
        loadPrefs();
        nameSet = false;
    }

    public static ArrayList<Integer> getHighScores(){ return highScores; }

    public static ArrayList<String> getNames(){ return names; }

    /**
     * This method compares the new score to all values in the high-score list. If the new score is
     * higher than an existing score, it will be put on the right place. The lower scores will
     * increase their positions by one and the lowest score in the list will be deleted.
     * @param newScore Achieved score that tries to be put into the high-score list
     * @return boolean value, that tells you whether the score made it into the list or not
     */
    public boolean addScore(int newScore){
        boolean newHighScore = false;

        for(int i = 0; i< highScores.size(); i++){
            int x = highScores.get(i);
            if(newScore > x){
                names.add(i, gamer);
                highScores.add(i, newScore);
                if(highScores.size() > 10){
                    highScores.remove(10);
                    names.remove(10);
                }
                newHighScore = true;
                savePrefs();
                break;
            }
        }
        return newHighScore;
    }

    public void nameIsSet(){ nameSet = true; }

    public boolean isNameSet(){ return nameSet; }

    public void setGamer(String name){ this.gamer = name;  }

    /**
     * Loads the scores from the preferences into the array list.
     */
    public static void loadPrefs(){
        highScores.add(prefs.getInteger("high_score_1"));
        names.add(prefs.getString("names_1"));
        highScores.add(prefs.getInteger("high_score_2"));
        names.add(prefs.getString("names_2"));
        highScores.add(prefs.getInteger("high_score_3"));
        names.add(prefs.getString("names_3"));
        highScores.add(prefs.getInteger("high_score_4"));
        names.add(prefs.getString("names_4"));
        highScores.add(prefs.getInteger("high_score_5"));
        names.add(prefs.getString("names_5"));
        highScores.add(prefs.getInteger("high_score_6"));
        names.add(prefs.getString("names_6"));
        highScores.add(prefs.getInteger("high_score_7"));
        names.add(prefs.getString("names_7"));
        highScores.add(prefs.getInteger("high_score_8"));
        names.add(prefs.getString("names_8"));
        highScores.add(prefs.getInteger("high_score_9"));
        names.add(prefs.getString("names_9"));
        highScores.add(prefs.getInteger("high_score_10"));
        names.add(prefs.getString("names_10"));
    }

    /**
     * Safes the updated scores into the preferences.
     */
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
        prefs.putString("names_1", names.get(0));
        prefs.putString("names_2", names.get(1));
        prefs.putString("names_3", names.get(2));
        prefs.putString("names_4", names.get(3));
        prefs.putString("names_5", names.get(4));
        prefs.putString("names_6", names.get(5));
        prefs.putString("names_7", names.get(6));
        prefs.putString("names_8", names.get(7));
        prefs.putString("names_9", names.get(8));
        prefs.putString("names_10", names.get(9));

/* This resets the list
        prefs.putInteger("high_score_1", 0);
        prefs.putInteger("high_score_2", 0);
        prefs.putInteger("high_score_3", 0);
        prefs.putInteger("high_score_4", 0);
        prefs.putInteger("high_score_5", 0);
        prefs.putInteger("high_score_6", 0);
        prefs.putInteger("high_score_7", 0);
        prefs.putInteger("high_score_8", 0);
        prefs.putInteger("high_score_9", 0);
        prefs.putInteger("high_score_10", 0);
        prefs.putString("names_1", "nobody");
        prefs.putString("names_2", "nobody");
        prefs.putString("names_3", "nobody");
        prefs.putString("names_4", "nobody");
        prefs.putString("names_5", "nobody");
        prefs.putString("names_6", "nobody");
        prefs.putString("names_7", "nobody");
        prefs.putString("names_8", "nobody");
        prefs.putString("names_9", "nobody");
        prefs.putString("names_10", "nobody");
*/

        prefs.flush();
    }
}
