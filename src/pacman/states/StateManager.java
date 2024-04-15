package pacman.states;

import pacman.core.Handler;

import java.awt.*;

public class StateManager {

	private State currentState = null;
	private Handler handler;

	protected State gameState;
	protected State loadingState;

	public StateManager(Handler handler) {
		this.handler = handler;

		this.gameState = new GameState(this.handler);
		this.loadingState = new LoadingState(this.handler);
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
