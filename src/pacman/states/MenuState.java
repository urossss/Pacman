package pacman.states;

import pacman.core.Handler;
import pacman.graphics.ImageAssets;
import pacman.graphics.TextRenderer;

import java.awt.*;

public class MenuState extends State {

	private boolean titleLoaded; // whether title animation is finished
	private int xTitle, yTitle; // title coordinates
	private long t0; // title animation start time
	private final int v0 = 10; // title animation start speed
	private final int vMin = 1; // title animation min speed
	private final double a = 0.01; // title animation acceleration (deceleration)

	private boolean pressSpaceVisible;
	private long currentTime, previousTime, delta;

	public MenuState(Handler handler) {
		super(handler, false, true, false);
	}

	@Override
	public void startImpl() {
		this.handler.getGame().restartGame();

		this.titleLoaded = false;
		this.pressSpaceVisible = false;

		this.xTitle = 50;
		this.yTitle = -150;

		this.t0 = this.previousTime = System.currentTimeMillis();
		this.delta = 0;
	}

	@Override
	public void update() {
		if (this.titleLoaded && this.handler.getKeyManager().space) {
			this.handler.getStateManager().startMenuState(); // todo: startGameState
			return;
		}

		this.currentTime = System.currentTimeMillis();
		this.delta = (this.delta + this.currentTime - this.previousTime) % 400;
		this.previousTime = this.currentTime;

		if (!this.titleLoaded) {
			this.yTitle += Math.max(this.vMin, this.v0 - (this.yTitle >= 100 ? this.a * (System.currentTimeMillis() - this.t0) : 0));

			if (this.yTitle >= 200) {
				this.titleLoaded = true;
			}
		}

		this.pressSpaceVisible = this.titleLoaded && this.delta < 300;
	}

	@Override
	public void renderImpl(Graphics g) {
		g.drawImage(ImageAssets.title, xTitle, yTitle, 350, 90, null);

		if (this.pressSpaceVisible) {
			TextRenderer.drawText(g, "press space to start", 68, 320, 16);
			this.pressSpaceVisible = false;
		}

		TextRenderer.drawText(g, "original game by namco 1980", 8, 490, 16);
		TextRenderer.drawText(g, "remastered by uros 2024", 40, 515, 16);
	}
}
