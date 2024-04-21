package pacman.states;

import pacman.core.Handler;

import java.awt.*;

public class GameState extends State {

	public GameState(Handler handler) {
		super(handler);
	}

	@Override
	public void startImpl() {

	}

	@Override
	public void update() {
		this.handler.getBoard().update();
	}

	@Override
	public void renderImpl(Graphics g) {
		this.handler.getBoard().render(g, true);
	}
}
