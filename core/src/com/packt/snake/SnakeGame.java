package com.packt.snake;

import com.badlogic.gdx.Game;

public class SnakeGame extends Game {

	public GameScreen gameScreen;
	public MenuScreen menuScreen;

	@Override
	public void create() {

			gameScreen = new GameScreen(this);
			menuScreen = new MenuScreen(this);
			setScreen(gameScreen);



	}
}
