package com.vieth.pacman;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;

import com.vieth.pacman.Screens.GameScreen;


public class PacMan extends Game {

	public static final int V_WIDTH = 8 * 28;
	public static final int V_HEIGHT = 8 * 52;

	public static SpriteBatch batch;
	@Override
	public void create() {
		batch = new SpriteBatch();
		setScreen(new GameScreen(this));
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
