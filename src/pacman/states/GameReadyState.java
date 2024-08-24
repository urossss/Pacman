package pacman.states;

import pacman.core.Board;
import pacman.core.Handler;
import pacman.graphics.ImageAssets;
import pacman.sounds.SoundAssets;

import java.awt.*;

public class GameReadyState extends State {

	private static final int T1 = 4000; // for the first 4 seconds, just display Ready message and play intro theme
	private static final int T2 = 5000; // for the following 1 second, display the entities (still no movement)

	private long previousTime;
	private long timer;
	private boolean livesDecreased;

	public GameReadyState(Handler handler) {
		super(handler);
	}

	@Override
	protected void startImpl() {
		if (this.handler.getBoard().isCompleted()) {
			this.handler.setBoard(new Board(this.handler));
		} else {
			this.handler.getBoard().recreateEntities();
		}

		this.timer = 0;
		this.previousTime = System.currentTimeMillis();
		this.livesDecreased = false;

		SoundAssets.sound_beginning.play();
	}

	@Override
	protected void updateImpl() {
		long currentTime = System.currentTimeMillis();
		this.timer += currentTime - this.previousTime;
		this.previousTime = currentTime;

		if (this.timer >= T1 && !this.livesDecreased) {
			this.handler.getGame().decreaseLivesLeft();
			this.livesDecreased = true;
		}

		if (this.timer >= T2) {
			this.handler.getStateManager().startGamePlayState();
		}
	}

	@Override
	protected void renderImpl(Graphics g) {
		if (this.timer < T1) {
			this.handler.getBoard().render(g, false);
		} else {
			this.handler.getBoard().render(g, true);
		}

		g.drawImage(ImageAssets.ready, 185, 330, 80, 10, null);
	}
}
