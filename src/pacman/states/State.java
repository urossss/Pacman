package pacman.states;

import pacman.core.Handler;

import java.awt.*;

public abstract class State {

	protected Handler handler;

	public State(Handler handler) {
		this.handler = handler;
	}

	public void start() {
		startImpl();
		this.handler.getStateManager().setCurrentState(this);
	}

	public abstract void startImpl();

	public abstract void update();

	public abstract void render(Graphics g);

}
