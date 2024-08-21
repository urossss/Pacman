package pacman.states;

import pacman.core.Handler;
import pacman.graphics.TextRenderer;

import java.awt.*;

public class GamePausedState extends State {

	public GamePausedState(Handler handler) {
		super(handler);
	}

	@Override
	protected void startImpl() {
		// Empty
	}

	@Override
	protected void updateImpl() {
		// Resume the game if space key was pressed, but:
		// - ignore presses in first 0.5 seconds after starting this state
		// - only react once to key holds
		if (this.handler.getKeyManager().space
				&& (this.handler.getKeyManager().lastSpacePressTime - this.stateStartTime > 500)
				&& (this.handler.getKeyManager().lastSpaceReleaseTime > this.stateStartTime)) {
			this.handler.getStateManager().resumeGamePlayState();
			return;
		}

		// todo: think about game timers (e.g. for ghost modes) when pausing/resuming the game
	}

	@Override
	protected void renderImpl(Graphics g) {
		this.handler.getBoard().render(g, true);
		TextRenderer.drawTextCenterAligned(g, "paused", 327, 15);
	}
}
