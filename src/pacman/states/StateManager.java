package pacman.states;

import pacman.core.Handler;

import java.awt.*;

public class StateManager {

	private State currentState = null;
	private Handler handler;

	protected State loadingState;
	protected State menuState;
	protected State gameState;

	public StateManager(Handler handler) {
		this.handler = handler;

		this.loadingState = new LoadingState(this.handler);
		this.menuState = new MenuState(this.handler);
		this.gameState = new GameState(this.handler);
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

	public void startGameState() {
		this.gameState.start();
	}

	// Implementation

	public State getCurrentState() {
		return currentState;
	}

	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}
}
