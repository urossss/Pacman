package pacman.states;

import pacman.core.Handler;

import java.awt.*;

public class LoadingState extends State {

	public LoadingState(Handler handler) {
		super(handler, false, false, false);
	}

	@Override
	public void startImpl() {

	}

	@Override
	public void updateImpl() {

	}

	@Override
	public void renderImpl(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(0, 0, 100, 100);
	}
}
