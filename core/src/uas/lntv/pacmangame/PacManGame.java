package uas.lntv.pacmangame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import uas.lntv.pacmangame.Scenes.HighScore;
import uas.lntv.pacmangame.Screens.SplashScreen;
import uas.lntv.pacmangame.Screens.LoadingScreen;

public class PacManGame extends Game {
	private int level;
	private int score;
	private int lives;
	public static SpriteBatch batch;
	public HighScore highScore;
	private final Assets ASSETS = new Assets();

	@Override
	public void create () {
		batch = new SpriteBatch();
		level = 0;
		score = 0;
		lives = 3;
		highScore = new HighScore();
		ASSETS.loadSetup();
		ASSETS.manager.finishLoading();
		setScreen(new LoadingScreen(this, ASSETS));
	}

	public int getLevel(){
		return level;
	}

	public void levelUp(){
		level++;
	}

	public void resetLevel(){ level = 0; }

	public int getScore(){ return score; }

	public void increaseScore(int value){ score += value; }

	public void resetScore(){ score = 0; }

	public int getLives(){ return lives; }

	public void die(){ lives--; }

	public void resetLives(){ lives = 3; }

	@Override
	public void render () { super.render(); }
	
	@Override
	public void dispose () {
		super.dispose();
		ASSETS.dispose();
		batch.dispose();
		System.exit(0);
		Gdx.app.exit();
	}
}
