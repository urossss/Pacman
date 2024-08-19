package pacman.states;

import pacman.core.Handler;

import java.awt.*;

public class LoadingState extends State {

	private long previousTime;
	private long timer;

	public LoadingState(Handler handler) {
		super(handler, false, false, false);
	}

	@Override
	public void startImpl() {
		this.timer = 0;
		this.previousTime = System.currentTimeMillis();
	}

	@Override
	public void updateImpl() {
		long currentTime = System.currentTimeMillis();
		this.timer += currentTime - this.previousTime;
		this.previousTime = currentTime;
	}

	@Override
	public void renderImpl(Graphics g) {
		g.setColor(Color.white);
		int dotCount = ((int) this.timer / 350) % 4;
		for (int i = 0; i < dotCount; i++) {
			g.fillRect(220 + i * 10, 280, 4, 4);
		}
	}
}
