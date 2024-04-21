package pacman.states;

import pacman.core.Handler;

import java.awt.*;

public class StateManager {

	private State currentState = null;
	private Handler handler;

	protected State loadingState;
	protected State menuState;
	protected State gameReadyState;
	protected State gamePlayState;
	protected State gamePausedState;
	protected State levelCompletedState;

	public StateManager(Handler handler) {
		this.handler = handler;

		this.loadingState = new LoadingState(this.handler);
		this.menuState = new MenuState(this.handler);
		this.gameReadyState = new GameReadyState(this.handler);
		this.gamePlayState = new GamePlayState(this.handler);
		this.gamePausedState = new GamePausedState(this.handler);
		this.levelCompletedState = new LevelCompletedState(this.handler);
	}

	// Game loop interface

	public void update() {
		if (this.currentState != null) {
			this.currentState.update();
		}
	}

	public void render(Graphics g) {
		if (this.currentState != null) {
			this.currentState.render(g);
		}
	}

	public void startLoadingState() {
		this.loadingState.start();
	}

	public void startMenuState() {
		this.menuState.start();
	}

	public void startGameReadyState() {
		this.gameReadyState.start();
	}

	public void startGamePlayState() {
		this.gamePlayState.start();
	}

	public void startGamePausedState() {
		this.gamePausedState.start();
	}

	public void startLevelCompletedState() {
		this.levelCompletedState.start();
	}

	// Implementation

	public State getCurrentState() {
		return currentState;
	}

	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}
}
