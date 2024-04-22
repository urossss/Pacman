package pacman.states;

import pacman.core.Handler;

import java.awt.*;

public class GamePlayState extends State {

	private int startCounter = 0;

	public GamePlayState(Handler handler) {
		super(handler);
	}

	@Override
	public void startImpl() {
		this.startCounter++;
	}

	@Override
	public void updateImpl() {
		// Pause the game if space key was pressed, but:
		// - ignore presses in first 0.5 seconds after starting this state
		// - only react once to key holds
		if (this.handler.getKeyManager().space
				&& (this.handler.getKeyManager().lastSpacePressTime - this.stateStartTime > 500)
				&& ((this.handler.getKeyManager().lastSpaceReleaseTime > this.stateStartTime) || startCounter == 1)) {
			this.handler.getStateManager().startGamePausedState();
			return;
		}

		this.handler.getBoard().update();

		if (this.handler.getBoard().isCompleted()) {
			this.handler.getStateManager().startLevelCompletedState();
		}
	}

	@Override
	public void renderImpl(Graphics g) {
		this.handler.getBoard().render(g, true);
	}
}
