package uas.lntv.pacmangame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import uas.lntv.pacmangame.Screens.GameScreen;
import uas.lntv.pacmangame.Screens.MenuScreen;

public class PacManGame extends Game {

	public static SpriteBatch batch;
	public GameScreen gameScreen;
	public MenuScreen menuScreen;
	@Override
	public void create () {
		batch = new SpriteBatch();
		gameScreen = new GameScreen(this, "map5.tmx");
		menuScreen = new MenuScreen(this, "mainMenu.tmx");
		setScreen(menuScreen);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {

		//gameScreen.dispose();
		batch.dispose();
	}
}
