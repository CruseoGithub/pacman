package uas.lntv.pacmangame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import uas.lntv.pacmangame.Managers.Assets;
import uas.lntv.pacmangame.Managers.PrefManager;
import uas.lntv.pacmangame.Screens.SplashScreen;

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

	@Override
	public void dispose () {
		super.dispose();
		assets.dispose();
		batch.dispose();
		Gdx.app.exit();
		System.exit(0);
	}

	@Override
	public void render () { super.render(); }

}
