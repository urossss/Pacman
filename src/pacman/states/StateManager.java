package pacman.states;

import pacman.core.Handler;

import java.awt.*;

public class StateManager {

	private State currentState = null;
	private Handler handler;

	protected State gameState;

	public StateManager(Handler handler) {
		this.handler = handler;
	}

	// Game loop interface

	public void init() {
		this.gameState = new GameState(this.handler);

		this.gameState.start();
	}

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

	// Implementation

	public State getCurrentState() {
		return currentState;
	}

	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}
}
