package pacman.states;

import pacman.core.Handler;
import pacman.graphics.Animation;
import pacman.graphics.ImageAssets;
import pacman.tiles.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LevelCompletedState extends State {

	private static final int T1 = 2000, T2 = 3000;

	private long previousTime;
	private long timer;
	private boolean livesIncreased;

	private Animation mapAnimation;

	public LevelCompletedState(Handler handler) {
		super(handler);
	}

	@Override
	public void startImpl() {
		this.mapAnimation = new Animation(250, new BufferedImage[]{ ImageAssets.map1, ImageAssets.map2 });

		this.timer = 0;
		this.previousTime = System.currentTimeMillis();
		this.livesIncreased = false;
	}

	@Override
	public void updateImpl() {
		this.mapAnimation.update();

		long currentTime = System.currentTimeMillis();
		this.timer += currentTime - this.previousTime;
		this.previousTime = currentTime;

		if (this.timer >= T1 && !this.livesIncreased) {
			this.handler.getGame().increaseLivesLeft();
			this.livesIncreased = true;
		}

		if (this.timer >= T2) {
			this.handler.getStateManager().startGameReadyState();
		}
	}

	@Override
	public void renderImpl(Graphics g) {
		g.translate(0, 55);

		g.drawImage(this.mapAnimation.getCurrentFrame(), 0, 0, 448, 496, null);

		if (!this.livesIncreased) {
			handler.getEntityManager().getPacman().render(g);
		}

		g.translate(0, -55);
	}
}
