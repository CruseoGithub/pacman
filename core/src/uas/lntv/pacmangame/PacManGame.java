package uas.lntv.pacmangame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import uas.lntv.pacmangame.Scenes.PrefManager;
import uas.lntv.pacmangame.Screens.LoadingScreen;
import uas.lntv.pacmangame.Screens.SplashScreen;

public class PacManGame extends Game {
	private static int level;
	private static int score;
	private static int lives;
	public static SpriteBatch batch;
	public static PrefManager prefManager;
	private Assets assets;

	@Override
	public void create () {
		batch = new SpriteBatch();
		prefManager = new PrefManager();
		assets = new Assets();
		assets.loadSetup();
		level = 0;
		score = 0;
		lives = 3;
		setScreen(new LoadingScreen(this, assets));
	}

	public static int getLevel(){
		return level;
	}

	public static void levelUp(){
		level++;
	}

	public static void resetLevel(){ level = 0; }

	public static int getScore(){ return score; }

	public static void increaseScore(int value){ score += value; }

	public static void resetScore(){ score = 0; }

	public static int getLives(){ return lives; }

	public static void die(){ lives--; }

	public static void resetLives(){ lives = 3; }

	@Override
	public void render () { super.render(); }
	
	@Override
	public void dispose () {
		super.dispose();
		assets.dispose();
		batch.dispose();
		System.exit(0);
		Gdx.app.exit();
	}
}
