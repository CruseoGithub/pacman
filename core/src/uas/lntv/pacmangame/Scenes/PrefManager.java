package uas.lntv.pacmangame.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;

import java.util.ArrayList;

/**
 * This class manages all the saved preferences, like high-scores and settings.
 */
public class PrefManager {

    private static Preferences prefs;
    private static final ArrayList<Integer> highScores = new ArrayList<>();
    private static final ArrayList<String> names = new ArrayList<>();
    private static final ArrayList<String> causeOfDeath = new ArrayList<>();
    private static final ArrayList<Integer> level = new ArrayList<>();
    private static String name;
    private static boolean nameSet;
    private static boolean musicOn;
    private static boolean sfxOn;

    /**
     * This constructor simply loads the saved high-scores and settings from the preferences.
     */
    public PrefManager(){
        prefs = Gdx.app.getPreferences("PacManPreferences");
        loadPrefs();
    }

    public static ArrayList<Integer> getHighScores(){ return highScores; }

    public static ArrayList<String> getNames(){ return names; }

    public static ArrayList<String> getCauseOfDeath(){ return causeOfDeath; }

    public static ArrayList<Integer> getLevel(){ return level; }



    /**
     * This method compares the new score to all values in the high-score list. If the new score is
     * higher than an existing score, it will be put on the right place. The lower scores will
     * increase their positions by one and the lowest score in the list will be deleted.
     * @param newScore Achieved score that tries to be put into the high-score list
     * @return boolean value, that tells you whether the score made it into the list or not
     */
    public boolean addScore(int newScore, String causeOfDeath, int level){
        boolean newHighScore = false;

        for(int i = 0; i< highScores.size(); i++){
            int x = highScores.get(i);
            if(newScore > x){
                highScores.add(i, newScore);
                names.add(i, name);
                PrefManager.causeOfDeath.add(i, causeOfDeath);
                PrefManager.level.add(i, level);
                if(highScores.size() > 10){
                    highScores.remove(10);
                    names.remove(10);
                    PrefManager.causeOfDeath.remove(10);
                    PrefManager.level.remove(10);
                }
                newHighScore = true;
                savePrefs();
                break;
            }
        }
        return newHighScore;
    }

    public static boolean noNameSet(){ return !nameSet; }

    public static void setNameSet(boolean nameSet){ PrefManager.nameSet = nameSet; }

    public static boolean isMusicOn() { return musicOn; }

    public static void setMusicOn(boolean musicOn) { PrefManager.musicOn = musicOn; }

    public static boolean isSfxOn() { return sfxOn; }

    public static void setSfxOn(boolean sfxOn) { PrefManager.sfxOn = sfxOn; }

    /**
     * Gives the player two chances to insert a name, if he doesn't he will be treated as
     * Anonymous Bastard.
     */
    public static void setName() {
        Gdx.input.getTextInput(
                new Input.TextInputListener() {
                    @Override
                    public void input(String text) {
                        if (text.isEmpty()) {
                            Gdx.input.getTextInput(
                                    new Input.TextInputListener() {
                                        @Override
                                        public void input(String secondChance) {
                                            if (secondChance.isEmpty()) anonymous();
                                            name = secondChance;
                                        }

                                        @Override
                                        public void canceled() {
                                            anonymous();
                                        }
                                    },
                                    "Are you sure?", "Anonymous Bastard", ""
                            );
                        } else name = text;
                    }

                    @Override
                    public void canceled() {
                        anonymous();
                    }
                },
                "Please enter your name", "", "Name"
        );
        nameSet = true;
    }

    /**
     * Gives the indecisive player the name "Anonymous Bastard".
     */
    private static void anonymous(){ name = "Anonymous Bastard"; }

    /**
     * Loads the scores and settings from the preferences folder into the game.
     */
    public static void loadPrefs(){
        highScores.add(prefs.getInteger("high_score_1"));
        names.add(prefs.getString("names_1"));
        causeOfDeath.add(prefs.getString("cause_of_death_1"));
        level.add(prefs.getInteger("level_1"));
        highScores.add(prefs.getInteger("high_score_2"));
        names.add(prefs.getString("names_2"));
        causeOfDeath.add(prefs.getString("cause_of_death_2"));
        level.add(prefs.getInteger("level_2"));
        highScores.add(prefs.getInteger("high_score_3"));
        names.add(prefs.getString("names_3"));
        causeOfDeath.add(prefs.getString("cause_of_death_3"));
        level.add(prefs.getInteger("level_3"));
        highScores.add(prefs.getInteger("high_score_4"));
        names.add(prefs.getString("names_4"));
        causeOfDeath.add(prefs.getString("cause_of_death_4"));
        level.add(prefs.getInteger("level_4"));
        highScores.add(prefs.getInteger("high_score_5"));
        names.add(prefs.getString("names_5"));
        causeOfDeath.add(prefs.getString("cause_of_death_5"));
        level.add(prefs.getInteger("level_5"));
        highScores.add(prefs.getInteger("high_score_6"));
        names.add(prefs.getString("names_6"));
        causeOfDeath.add(prefs.getString("cause_of_death_6"));
        level.add(prefs.getInteger("level_6"));
        highScores.add(prefs.getInteger("high_score_7"));
        names.add(prefs.getString("names_7"));
        causeOfDeath.add(prefs.getString("cause_of_death_7"));
        level.add(prefs.getInteger("level_7"));
        highScores.add(prefs.getInteger("high_score_8"));
        names.add(prefs.getString("names_8"));
        causeOfDeath.add(prefs.getString("cause_of_death_8"));
        level.add(prefs.getInteger("level_8"));
        highScores.add(prefs.getInteger("high_score_9"));
        names.add(prefs.getString("names_9"));
        causeOfDeath.add(prefs.getString("cause_of_death_9"));
        level.add(prefs.getInteger("level_9"));
        highScores.add(prefs.getInteger("high_score_10"));
        names.add(prefs.getString("names_10"));
        causeOfDeath.add(prefs.getString("cause_of_death_10"));
        level.add(prefs.getInteger("level_10"));
        name = prefs.getString("player");
        nameSet = prefs.getBoolean("NameSet");
        musicOn = prefs.getBoolean("Music");
        sfxOn = prefs.getBoolean("SFX");
    }

    /**
     * Safes the updated scores and settings into the preferences folder.
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
        prefs.putString("cause_of_death_1", causeOfDeath.get(0));
        prefs.putString("cause_of_death_2", causeOfDeath.get(1));
        prefs.putString("cause_of_death_3", causeOfDeath.get(2));
        prefs.putString("cause_of_death_4", causeOfDeath.get(3));
        prefs.putString("cause_of_death_5", causeOfDeath.get(4));
        prefs.putString("cause_of_death_6", causeOfDeath.get(5));
        prefs.putString("cause_of_death_7", causeOfDeath.get(6));
        prefs.putString("cause_of_death_8", causeOfDeath.get(7));
        prefs.putString("cause_of_death_9", causeOfDeath.get(8));
        prefs.putString("cause_of_death_10", causeOfDeath.get(9));
        prefs.putInteger("level_1", level.get(0));
        prefs.putInteger("level_2", level.get(1));
        prefs.putInteger("level_3", level.get(2));
        prefs.putInteger("level_4", level.get(3));
        prefs.putInteger("level_5", level.get(4));
        prefs.putInteger("level_6", level.get(5));
        prefs.putInteger("level_7", level.get(6));
        prefs.putInteger("level_8", level.get(7));
        prefs.putInteger("level_9", level.get(8));
        prefs.putInteger("level_10", level.get(9));
        prefs.putString("player", name);
        prefs.putBoolean("NameSet", nameSet);
        prefs.putBoolean("Music", musicOn);
        prefs.putBoolean("SFX", sfxOn);

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
        prefs.putString("cause_of_death_1", "Nothing happened");
        prefs.putString("cause_of_death_2", "Nothing happened");
        prefs.putString("cause_of_death_3", "Nothing happened");
        prefs.putString("cause_of_death_4", "Nothing happened");
        prefs.putString("cause_of_death_5", "Nothing happened");
        prefs.putString("cause_of_death_6", "Nothing happened");
        prefs.putString("cause_of_death_7", "Nothing happened");
        prefs.putString("cause_of_death_8", "Nothing happened");
        prefs.putString("cause_of_death_9", "Nothing happened");
        prefs.putString("cause_of_death_10", "Nothing happened");
        prefs.putInteger("level_1", 0);
        prefs.putInteger("level_2", 0);
        prefs.putInteger("level_3", 0);
        prefs.putInteger("level_4", 0);
        prefs.putInteger("level_5", 0);
        prefs.putInteger("level_6", 0);
        prefs.putInteger("level_7", 0);
        prefs.putInteger("level_8", 0);
        prefs.putInteger("level_9", 0);
        prefs.putInteger("level_10", 0);
        prefs.putString("player", "");
*/


        prefs.flush();
    }
}
