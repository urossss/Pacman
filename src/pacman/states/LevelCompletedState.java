package pacman.states;

import pacman.core.Game;
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
		g.translate(0, Game.GAME_TOP_SECTION_HEIGHT_PIXELS);

		g.drawImage(this.mapAnimation.getCurrentFrame(), 0, 0, Game.GAME_BOARD_WIDTH_PIXELS, Game.GAME_BOARD_HEIGHT_PIXELS, null);

		if (!this.livesIncreased) {
			handler.getEntityManager().getPacman().render(g);
		}

		g.translate(0, -Game.GAME_TOP_SECTION_HEIGHT_PIXELS);
	}
}
