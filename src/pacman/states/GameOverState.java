package pacman.states;

import pacman.core.Handler;
import pacman.graphics.ImageAssets;

import java.awt.*;

public class GameOverState extends State {

	private static final int T = 3000;

	private long previousTime;
	private long timer;

	public GameOverState(Handler handler) {
		super(handler);
	}

	@Override
	protected void startImpl() {
		this.timer = 0;
		this.previousTime = System.currentTimeMillis();
	}

	@Override
	protected void updateImpl() {
		long currentTime = System.currentTimeMillis();
		this.timer += currentTime - this.previousTime;
		this.previousTime = currentTime;

		if (this.timer >= T) {
			if (this.handler.getGame().isNewHighScore()) {
				this.handler.getStateManager().startNewRecordState();
			} else {
				this.handler.getStateManager().startMenuState();
			}
		}
	}

	@Override
	protected void renderImpl(Graphics g) {
		this.handler.getBoard().render(g, false);
		g.drawImage(ImageAssets.game_over, 175, 330, 100, 10, null);
	}
}
