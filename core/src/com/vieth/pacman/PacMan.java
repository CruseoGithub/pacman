package com.vieth.pacman;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.vieth.pacman.Screens.GameScreen;
import com.vieth.pacman.Screens.MenuScreen;


public class PacMan extends Game {

	public static final int V_WIDTH = 8 * 28;
	public static final int V_HEIGHT = 8 * 52;
	static public Skin gameSkin;
	public static SpriteBatch batch;


	@Override
	public void create() {
		batch = new SpriteBatch();
		gameSkin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));
		//setScreen(new GameScreen(this));
		setScreen(new MenuScreen(this));
	}

	@Override
	public void dispose() {

	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void resize(int width, int height) {

	}
}
