package pacman.states;

import pacman.core.Game;
import pacman.core.Handler;
import pacman.graphics.ImageAssets;
import pacman.graphics.TextRenderer;

import java.awt.*;

public abstract class State {

	protected Handler handler;
	protected boolean shouldDrawScore = true;
	protected boolean shouldDrawHighScore = true;
	protected boolean shouldDrawLives = true;

	protected long stateStartTime;

	public State(Handler handler) {
		this.handler = handler;
	}

	public State(Handler handler, boolean shouldDrawScore, boolean shouldDrawHighScore, boolean shouldDrawLives) {
		this.handler = handler;
		this.shouldDrawScore = shouldDrawScore;
		this.shouldDrawHighScore = shouldDrawHighScore;
		this.shouldDrawLives = shouldDrawLives;
	}

	public void start() {
		this.stateStartTime = System.currentTimeMillis();
		startImpl();
		this.handler.getStateManager().setCurrentState(this);
	}

	public void render(Graphics g) {
		renderImpl(g);

		Game game = this.handler.getGame();

		if (this.shouldDrawScore) {
			TextRenderer.drawText(g, "score", 10, 5);
			TextRenderer.drawInteger(g, game.getScore(), 10, 30);
		}

		if (this.shouldDrawHighScore) {
			TextRenderer.drawText(g, "high", 245, 5);
			TextRenderer.drawText(g, "score", 340, 5);

			int nameLength = game.getHighScorePlayer().length();
			int nameDisplayLength = nameLength == 0 ? 0 : nameLength * 16 + 10; // todo: make it 0 if the record is broken
			TextRenderer.drawInteger(g, game.getHighScore(), 320 - nameDisplayLength, 30);
			if (nameDisplayLength > 0) {
				TextRenderer.drawText(g, game.getHighScorePlayer(), 440 - nameLength * 16, 34, 16);
			}
		}

		if (this.shouldDrawLives) {
			g.translate(0, 496 + 55);

//			TextRenderer.drawText(g, "lives", 5, 5);
			for (int i = 0; i < handler.getGame().getLivesLeft(); i++) {
				g.drawImage(ImageAssets.pacman_left[1], 30 * i, 0, 30, 30, null);
			}
		}
	}

	public abstract void startImpl();

	public abstract void update();

	public abstract void renderImpl(Graphics g);

}
