package pacman.states;

import pacman.core.Game;
import pacman.core.Handler;
import pacman.graphics.ImageAssets;
import pacman.graphics.TextRenderer;

import java.awt.*;

public abstract class State {

	protected Handler handler;

	private boolean shouldDrawScore = true;
	private boolean shouldDrawHighScore = true;
	private boolean shouldDrawLives = true;
	private boolean isHighScoreVisible = true;
	private long _basePreviousTime;
	private long _baseTimer;

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
		this._baseTimer = 0;
		this._basePreviousTime = System.currentTimeMillis();

		this.startImpl();

		this.handler.getStateManager().setCurrentState(this);
	}

	public void update() {
		long currentTime = System.currentTimeMillis();
		this._baseTimer += currentTime - this._basePreviousTime;
		this._basePreviousTime = currentTime;

		this.updateImpl();

		// high score animation update
		if (this.handler.getGame().isNewHighScore()) {
			if (this._baseTimer < 300) {
				this.isHighScoreVisible = true;
			} else if (this._baseTimer > 500) {
				this._baseTimer = 0;
			} else {
				this.isHighScoreVisible = false;
			}
		} else {
			this.isHighScoreVisible = true;
		}
	}

	public void render(Graphics g) {
		this.renderImpl(g);

		Game game = this.handler.getGame();

		if (this.shouldDrawScore) {
			TextRenderer.drawText(g, "score", 10, 5);
			TextRenderer.drawInteger(g, game.getScore(), 10, 30);
		}

		if (this.shouldDrawHighScore) {
			TextRenderer.drawText(g, "high", 245, 5);
			TextRenderer.drawText(g, "score", 340, 5);

			if (this.isHighScoreVisible) {
				int nameLength = game.getHighScorePlayer().length();
				int nameDisplayLength = nameLength == 0 || this.handler.getGame().isNewHighScore() ? 0 : nameLength * 16 + 10;
				TextRenderer.drawInteger(g, game.getHighScore(), 320 - nameDisplayLength, 30);
				if (nameDisplayLength > 0) {
					TextRenderer.drawText(g, game.getHighScorePlayer(), 440 - nameLength * 16, 34, 16);
				}
			}
		}

		if (this.shouldDrawLives) {
			g.translate(0, Game.GAME_BOARD_HEIGHT_PIXELS + Game.GAME_TOP_SECTION_HEIGHT_PIXELS);
			for (int i = 0; i < Math.min(handler.getGame().getLivesLeft(), 4); i++) {
				g.drawImage(ImageAssets.pacman_left[1], 30 * i, 0, 30, 30, null);
			}
		}
	}

	protected abstract void startImpl();

	protected abstract void updateImpl();

	protected abstract void renderImpl(Graphics g);

}
