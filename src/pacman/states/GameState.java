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

	}

	@Override
	public void render(Graphics g) {
		this.handler.getBoard().render(g);
	}
}
