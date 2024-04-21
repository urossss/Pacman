package pacman.states;

import pacman.core.Handler;
import pacman.graphics.Animation;
import pacman.graphics.ImageAssets;
import pacman.graphics.TextRenderer;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MenuState extends State {

	private boolean titleLoaded; // whether title animation is finished
	private int xTitle, yTitle; // title coordinates
	private long t0; // title animation start time
	private final int v0 = 10; // title animation start speed
	private final int vMin = 1; // title animation min speed
	private final double a = 0.01; // title animation acceleration (deceleration)

	private boolean pressSpaceVisible;
	private long currentTime, previousTime, delta;

	private Animation[] chaseAnimationLeft, chaseAnimationRight;
	private boolean chaseGoingRight;
	private final int chaseSpeed = 2;
	private int xAnim; // chase animation coordinates (top left corner)
	private final int yAnim = 380; // y coordinate is constant for this animation
	private final int xAnimMin = -300; // animation width is 200 pixels (5 images, 40 pixels each); the limit is 100 pixels off the edge
	private final int xAnimMax = 448 + 100; // the limit is 100 pixels off the edge on this side as well, 448 is display width

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

		this.chaseGoingRight = true;
		this.xAnim = -300;

		this.chaseAnimationLeft = new Animation[5];
		this.chaseAnimationLeft[0] = new Animation(50, ImageAssets.pacman_left);
		for (int i = 1; i <= 4; i++) {
			this.chaseAnimationLeft[i] = new Animation(50, ImageAssets.ghost_left[i - 1]);
		}

		this.chaseAnimationRight = new Animation[5];
		this.chaseAnimationRight[0] = new Animation(50, ImageAssets.pacman_right);
		for (int i = 1; i <= 4; i++) {
			this.chaseAnimationRight[i] = new Animation(50, ImageAssets.ghost_scared_1);
		}
	}

	@Override
	public void update() {
		if (this.titleLoaded && this.handler.getKeyManager().space) {
			this.handler.getStateManager().startGameState();
			return;
		}

		this.currentTime = System.currentTimeMillis();
		this.delta = (this.delta + this.currentTime - this.previousTime) % 400;
		this.previousTime = this.currentTime;

		if (!this.titleLoaded) {
			// title animation
			this.yTitle += Math.max(this.vMin, this.v0 - (this.yTitle >= 100 ? this.a * (System.currentTimeMillis() - this.t0) : 0));
			if (this.yTitle >= 195) {
				this.titleLoaded = true;
			}
		} else {
			// space visible animation
			this.pressSpaceVisible = this.delta < 300;

			// chase animations
			for (int i = 0; i < 5; i++) {
				this.chaseAnimationLeft[i].update();
				this.chaseAnimationRight[i].update();
			}

			if (this.xAnim > this.xAnimMax) {
				this.chaseGoingRight = false;
			}
			if (this.xAnim <= this.xAnimMin) {
				this.chaseGoingRight = true;
			}

			if (this.chaseGoingRight) {
				this.xAnim += this.chaseSpeed;
			} else {
				this.xAnim -= this.chaseSpeed;
			}
		}
	}

	@Override
	public void renderImpl(Graphics g) {
		g.drawImage(ImageAssets.title, xTitle, yTitle, 350, 90, null);

		if (this.pressSpaceVisible) {
			TextRenderer.drawText(g, "press space to start", 68, 310, 16);
			this.pressSpaceVisible = false;
		}

		for (int i = 0; i < 5; i++) {
			BufferedImage image;
			if (this.chaseGoingRight) {
				image = this.chaseAnimationRight[i].getCurrentFrame();
			} else {
				image = this.chaseAnimationLeft[i].getCurrentFrame();
			}
			g.drawImage(image, this.xAnim + i * 40, this.yAnim, 40, 40, null);
		}

		TextRenderer.drawText(g, "original game by namco 1980", 8, 490, 16);
		TextRenderer.drawText(g, "remastered by uros 2024", 40, 515, 16);
	}
}
