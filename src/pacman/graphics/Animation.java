package pacman.graphics;

import java.awt.image.BufferedImage;

public class Animation {

	private int speed; // animation speed - number of milliseconds between consecutive animation frames
	private int index; // index of current animation frame
	private long previousTime, timer;
	private BufferedImage[] frames;

	public Animation(int speed, BufferedImage[] frames) {
		this.speed = speed;
		this.frames = frames;
		this.index = 0;
		this.timer = 0;
		this.previousTime = System.currentTimeMillis();
	}

	public void update() {
		this.timer += System.currentTimeMillis() - this.previousTime;
		this.previousTime = System.currentTimeMillis();

		if (this.timer > this.speed) {
			this.index = (this.index + 1) % this.frames.length;
			this.timer = 0;
		}
	}

	public BufferedImage getCurrentFrame() {
		return this.frames[this.index];
	}
}
