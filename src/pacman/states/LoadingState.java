package pacman.states;

import pacman.core.Handler;

import java.awt.*;

public class LoadingState extends State {

	public LoadingState(Handler handler) {
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
		g.fillRect(0, 0, 100, 100);
	}
}
