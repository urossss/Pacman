package pacman.states;

import pacman.core.Handler;
import pacman.entities.ghosts.Ghost;
import pacman.sounds.SoundAssets;

import java.awt.*;

public class GamePlayState extends State {

	private boolean isGameResumed;

	private long previousTime;
	private long timer = 0;

	private int[] scatterChaseSwitchTimes;
	private int scatterChaseSwitchIndex;
	private long scatterChaseNextSwitchTime;
	private boolean scatterModeActive;

	public GamePlayState(Handler handler) {
		super(handler);
	}

	@Override
	protected void startImpl() {
		this.isGameResumed = false;
		this.previousTime = System.currentTimeMillis();

		this.resetGamePlay();
	}

	public void resume() {
		this.isGameResumed = true;
		this.previousTime = System.currentTimeMillis();
		this.handler.getStateManager().setCurrentState(this);
	}

	@Override
	protected void updateImpl() {
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
		this.updateBackgroundSounds(); // needs to happen before board update so that these sounds don't get started after they got stopped somewhere else
		this.handler.getBoard().update();

		if (this.handler.getBoard().isCompleted()) {
			this.handler.getStateManager().startLevelCompletedState();
			this.handler.getGame().increaseCurrentLevel();
			this.resetGamePlay();
			return;
		}
	}

	@Override
	protected void renderImpl(Graphics g) {
		this.handler.getBoard().render(g, true);
	}

	// private implementation

	private void resetGamePlay() {
		this.scatterModeActive = true;
		this.handler.getGame().setGhostScatterModeActive(true);

		this.handler.getEntityManager().getBlinky().startScatterState();
		this.handler.getEntityManager().getPinky().startCageState();
		this.handler.getEntityManager().getInky().startCageState();
		this.handler.getEntityManager().getClyde().startCageState();

		this.timer = 0;
		this.scatterChaseSwitchTimes = this.handler.getGame().getScatterChaseSwitchTimesInSeconds();
		this.scatterChaseSwitchIndex = 0;
		this.scatterModeActive = true;
		if (this.scatterChaseSwitchTimes.length > 0) {
			this.scatterChaseNextSwitchTime = this.scatterChaseSwitchTimes[0] * 1000;
		}

		Ghost.setDiedGhostsCount(0);
		Ghost.setVulnerableGhostsCount(0);
	}

	private void timerUpdates() {
		long currentTime = System.currentTimeMillis();
		this.timer += currentTime - this.previousTime;
		this.previousTime = currentTime;

		if (this.timer >= this.scatterChaseNextSwitchTime) {
			this.scatterModeActive = !this.scatterModeActive;
			this.handler.getGame().setGhostScatterModeActive(this.scatterModeActive);

			this.scatterChaseSwitchIndex++;
			if (this.scatterChaseSwitchTimes.length > this.scatterChaseSwitchIndex) {
				this.scatterChaseNextSwitchTime += this.scatterChaseSwitchTimes[this.scatterChaseSwitchIndex] * 1000;
			} else {
				this.scatterChaseNextSwitchTime = Long.MAX_VALUE;
			}
		}
	}

	private void updateBackgroundSounds() {
		if (Ghost.getDiedGhostsCount() > 0) { // there is a ghost returning to cage
			SoundAssets.sound_pacman_chase.stop();
			SoundAssets.sound_ghost_chase.stop();
			if (!SoundAssets.sound_ghost_return.isActive()) {
				SoundAssets.sound_ghost_return.loop();
			}
		} else if (Ghost.getVulnerableGhostsCount() > 0) { // there is a vulnerable ghost, pacman is chasing
			SoundAssets.sound_ghost_chase.stop();
			SoundAssets.sound_ghost_return.stop();
			if (!SoundAssets.sound_pacman_chase.isActive()) {
				SoundAssets.sound_pacman_chase.loop();
			}
		} else { // ghosts are chasing
			SoundAssets.sound_pacman_chase.stop();
			SoundAssets.sound_ghost_return.stop();
			if (!SoundAssets.sound_ghost_chase.isActive()) {
				SoundAssets.sound_ghost_chase.loop();
			}
		}
	}
}
