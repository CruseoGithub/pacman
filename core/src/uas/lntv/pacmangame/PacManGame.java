package uas.lntv.pacmangame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import uas.lntv.pacmangame.Managers.Assets;
import uas.lntv.pacmangame.Managers.PrefManager;
import uas.lntv.pacmangame.Screens.SplashScreen;

/**
 * This game is inspired by the original arcade pac-man game. The protagonist is Pac-Man a yellow
 * ball with a mouth. Pac-Man finds himself in a maze that he can not escape from. Only eating all
 * the dots which are scattered around the map will get pac-man to the next level. But beware!
 * There are enemy ghosts on the map too and they are deadly! At first, they might not seem too
 * smart but they will get more and more intelligent.
 */
public class PacManGame extends Game {

	/* Fields */

	private Assets assets;
	private static int level;
	private static int lives;
	private static int score;
	public static PrefManager prefManager;
	public static SpriteBatch batch;

	/* Accessors */

	public static int getLevel(){ return level; }

	public static int getLives(){ return lives; }

	public static int getScore(){ return score; }

	/* Mutators */

	public static void levelUp(){ level++; }

	public static void resetLevel(){ level = 0; }

	public static void increaseScore(int value){ score += value; }

	public static void resetScore(){ score = 0; }

	public static void die(){ lives--; }

	public static void addLive(){ lives++; }

	public static void resetLives(){ lives = 3; }

	/* Methods */

	/**
	 * The origin of all life. This method creates the foundation of the game and turns on the
	 * first screen.
	 */
	@Override
	public void create () {
		batch = new SpriteBatch();
		prefManager = new PrefManager();
		assets = new Assets();
		assets.loadSetup();
		level = 0;
		score = 0;
		lives = 3;
		setScreen(new SplashScreen(this, assets));
	}

	/**
	 * Apocalypse incoming!
	 * This method is called, when the game is closed. It will clean up and close the game safely.
	 */
	@Override
	public void dispose () {
		super.dispose();
		assets.dispose();
		batch.dispose();
		Gdx.app.exit();
		System.exit(0);
	}

	/**
	 * Renders the screen. Method by libGDX.
	 */
	@Override
	public void render () { super.render(); }

}
