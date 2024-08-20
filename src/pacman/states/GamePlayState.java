package pacman.states;

import pacman.core.Handler;

import java.awt.*;

public class GamePlayState extends State {

	private boolean isGameResumed;

	private long previousTime;
	private long timer = 0;

	public GamePlayState(Handler handler) {
		super(handler);

		this.resetGamePlay();
	}

	@Override
	public void startImpl() {
		this.isGameResumed = false;
		this.previousTime = System.currentTimeMillis();

		this.handler.getEntityManager().getBlinky().startScatterState();
		this.handler.getEntityManager().getPinky().startCageState();
		this.handler.getEntityManager().getInky().startCageState();
		this.handler.getEntityManager().getClyde().startCageState();
	}

	@Override
	public void updateImpl() {
		// Pause the game if space key was pressed, but:
		// - ignore presses in first 0.5 seconds after starting this state
		// - only react once to key holds
		if (this.handler.getKeyManager().space
				&& (this.handler.getKeyManager().lastSpacePressTime - this.stateStartTime > 500)
				&& ((this.handler.getKeyManager().lastSpaceReleaseTime > this.stateStartTime) || !this.isGameResumed)) {
			this.handler.getStateManager().startGamePausedState();
			return;
		}

		this.timerUpdates();

		this.handler.getBoard().update();

		if (this.handler.getBoard().isCompleted()) {
			this.handler.getStateManager().startLevelCompletedState();
			this.resetGamePlay();
			return;
		}

		if (this.handler.getEntityManager().getPacman().getYTile() == 5) { // todo: fix this test condition
			this.handler.getStateManager().startPacmanDiedState();
			this.resetGamePlay();
			return;
		}
	}

	@Override
	public void renderImpl(Graphics g) {
		this.handler.getBoard().render(g, true);
	}

	public void setResumeStatus(boolean isResumed) {
		this.isGameResumed = isResumed;
	}

	// private implementation

	private void resetGamePlay() {
		this.timer = 0;
	}

	private void timerUpdates() {
		long currentTime = System.currentTimeMillis();
		this.timer += currentTime - this.previousTime;
		this.previousTime = currentTime;

		int level = this.handler.getGame().getCurrentLevel();

		// TODO: scatter/chase mode switching on timer based on level

		if (this.timer >= 10000) {
			this.handler.getGame().setGhostScatterModeActive(true);
		}
	}

}
