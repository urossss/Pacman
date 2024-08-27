package pacman.core;

import pacman.display.Display;
import pacman.entities.Pacman;
import pacman.entities.ghosts.Ghost;
import pacman.graphics.ImageAssets;
import pacman.input.KeyManager;
import pacman.input.SoundControlManager;
import pacman.sounds.SoundAssets;
import pacman.states.StateManager;
import pacman.utils.Utils;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Game implements Runnable {

	public static final String GAME_TITLE = "Pacman";
	public static final int GAME_BOARD_WIDTH_PIXELS = 448;
	public static final int GAME_BOARD_HEIGHT_PIXELS = 496;
	public static final int GAME_TOP_SECTION_HEIGHT_PIXELS = 55;
	public static final int GAME_BOTTOM_SECTION_HEIGHT_PIXELS = 30;
	public static final int GAME_FULL_HEIGHT_PIXELS = GAME_BOARD_HEIGHT_PIXELS + GAME_TOP_SECTION_HEIGHT_PIXELS + GAME_BOTTOM_SECTION_HEIGHT_PIXELS;

	private final String SCORE_FILE_PATH = "./score.txt";

	// Display
	private Display display;

	// Game loop
	private boolean running = false;
	private Thread thread;

	// Game handler
	private Handler handler;
	private StateManager stateManager;
	private KeyManager keyManager;

	private SoundControlManager soundControlManager;
	private int currentSoundVolume = 3;

	// Game variables
	private int score;
	private int highScore;
	private String highScorePlayer;
	private int livesLeft;
	private boolean isNewHighScore = false;
	private int currentLevel = 1;
	private int ghostsEatenCount;

	private boolean ghostScatterModeActive = true;

	public Game() {

	}

	// Game loop skeleton

	@Override
	public void run() {
		init();

		final int FPS = 60;
		final double TIME_PER_TICK = 1000000000.0 / FPS; // time per tick in nanoseconds

		long nextGameTick = System.nanoTime();
		int sleepTime; // sleep time in milliseconds

		long currentNanoTime, previousNanoTime = System.nanoTime();

		// tracking average fps and tick length
		final boolean PERF_TRACKING_ENABLED = false;
		int currentTickCount = 0;
		long timer = 0;
		long totalTickCount = 0, totalTickLength = 0, lastTickLength, maxTickLength = 0;

		// Game loop
		while (this.running) {
			currentNanoTime = System.nanoTime();

			// Tick the game if it's time for it
			if (currentNanoTime >= nextGameTick) {
				try {
					this.update();
					this.render();
				} catch (Exception e) {
					e.printStackTrace();
				}

				nextGameTick += TIME_PER_TICK;

				if (PERF_TRACKING_ENABLED) {
					currentTickCount++;
					totalTickCount++;

					lastTickLength = System.nanoTime() - currentNanoTime;
					totalTickLength += lastTickLength;
					maxTickLength = Math.max(maxTickLength, lastTickLength);
					if (lastTickLength > TIME_PER_TICK) {
						System.out.println("Last tick length: " + lastTickLength / 1000000.0 + " ms");
					}
				}
			}

			// Perf metrics
			if (PERF_TRACKING_ENABLED) {
				timer += currentNanoTime - previousNanoTime;
				previousNanoTime = currentNanoTime;

				if (timer >= 1000000000) {
					System.out.println("fps: " + currentTickCount);
					System.out.printf("Average tick length: %.6fms\n", (1.0 * totalTickLength / totalTickCount) / 1000000.0);
					System.out.printf("Max tick length: %.6fms\n", maxTickLength / 1000000.0);
					currentTickCount = 0;
					timer = 0;
				}
			}

			// Sleep until next tick to save CPU time
			sleepTime = (int) ((nextGameTick - currentNanoTime) / 1000000); // sleep time is in milliseconds

			if (sleepTime > 0) {
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		stop();
	}

	// Thread manipulation

	public synchronized void start() {
		if (this.running) {
			return;
		}

		this.running = true;
		this.thread = new Thread(this);
		this.thread.start();
	}

	public synchronized void stop() {
		if (!running) {
			return;
		}

		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// Game loop implementation

	private void init() {
		// Display
		this.display = new Display(GAME_TITLE, GAME_BOARD_WIDTH_PIXELS, GAME_FULL_HEIGHT_PIXELS);

		// Handler
		this.handler = new Handler(this);

		this.stateManager = new StateManager(this.handler);
		this.handler.setStateManager(this.stateManager);

		this.keyManager = new KeyManager();
		this.soundControlManager = new SoundControlManager();
		this.handler.setKeyManager(this.keyManager);
		this.display.getFrame().addKeyListener(this.keyManager);
		this.display.getCanvas().addMouseListener(this.soundControlManager);

		this.stateManager.startLoadingState();

		// Asynchronously load assets and start the game
		new Thread(() -> {
			ImageAssets.init();
			SoundAssets.init();
			this.stateManager.startMenuState();
		}).start();
	}

	private void update() {
		this.keyManager.update();

		int newSoundVolume = this.soundControlManager.getSoundVolume();
		if (newSoundVolume != this.currentSoundVolume) {
			SoundAssets.setSoundsVolume(newSoundVolume);
			this.currentSoundVolume = newSoundVolume;
		}

		this.stateManager.update();
	}

	private void render() {
		// Get Graphics object
		// Rendering
		BufferStrategy bs = this.display.getCanvas().getBufferStrategy();
		if (bs == null) { // calling this method for the first time
			this.display.getCanvas().createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();

		// Clear the screen
		g.setColor(Color.black);
		g.fillRect(0, 0, GAME_BOARD_WIDTH_PIXELS, GAME_FULL_HEIGHT_PIXELS);

		// Draw game here
//		this.board.render(g);
		this.stateManager.render(g);

		// End drawing
		bs.show();
		g.dispose();
	}

	// Game logic

	public void restartGame() {
		this.score = 0;
		this.livesLeft = 3;
		this.isNewHighScore = false;
		this.currentLevel = 1;
		this.ghostScatterModeActive = true;

		this.readHighScore();

		this.handler.setBoard(new Board(this.handler));
	}

	public void scorePoints(int points) {
		if (this.score / 10000 != (this.score + points) / 10000) { // extra life for every 10,000 points scored
			this.increaseLivesLeft();
			SoundAssets.sound_extra_life.play();
		}
		this.score += points;

		if (this.score > this.highScore) {
			this.isNewHighScore = true;
			this.highScore = this.score;
		}
	}

	public void increaseLivesLeft() {
		this.livesLeft = Math.min(this.livesLeft + 1, 5);
	}

	public void decreaseLivesLeft() {
		livesLeft--;
	}

	public boolean isNewHighScore() {
		return this.isNewHighScore;
	}

	public void saveNewHighScore(String playerName) {
		if (this.score < this.highScore) {
			return;
		}

		this.highScore = this.score;
		this.highScorePlayer = playerName;

		this.writeHighScore();
	}

	public void setGhostScatterModeActive(boolean ghostScatterModeActive) {
		this.ghostScatterModeActive = ghostScatterModeActive;
	}

	public void increaseCurrentLevel() {
		this.currentLevel++;
	}

	public int getGhostVulnerableStateDurationMillis() {
		switch (this.currentLevel) {
			case 1: return 8000;
			case 2: return 7000;
			case 3: return 6000;
			case 4: return 5000;
			default: return 4000;
		}
	}

	public void powerPelletEaten() {
		this.ghostsEatenCount = 0;
		for (Ghost g : this.handler.getEntityManager().getGhosts()) {
			g.startVulnerableState();
		}
		Ghost.setVulnerableGhostsCount(4);

		Pacman pacman = this.handler.getEntityManager().getPacman();
		pacman.setSpeed(this.getPacmanPowerSpeed());
		pacman.setTimeToClearPowerSpeed(System.currentTimeMillis() + this.getGhostVulnerableStateDurationMillis());
	}

	public void ghostEaten(Ghost g) {
		g.startDiedState();
		this.scorePoints((int) (200 * Math.pow(2, this.ghostsEatenCount++)));
	}

	public double getPacmanRegularSpeed() {
		switch (this.currentLevel) {
			case 1: return 0.8 * Pacman.PACMAN_MAX_SPEED;
			case 2:
			case 3:
			case 4: return 0.9 * Pacman.PACMAN_MAX_SPEED;
			default: return 1.0 * Pacman.PACMAN_MAX_SPEED;
		}
	}

	public double getPacmanPowerSpeed() {
		switch (this.currentLevel) {
			case 1: return 0.9 * Pacman.PACMAN_MAX_SPEED;
			case 2:
			case 3:
			case 4: return 0.95 * Pacman.PACMAN_MAX_SPEED;
			default: return 1.0 * Pacman.PACMAN_MAX_SPEED;
		}
	}

	public double getGhostRegularSpeed() {
		switch (this.currentLevel) {
			case 1: return 0.8 * Ghost.GHOST_MAX_SPEED;
			case 2:
			case 3:
			case 4: return 0.85 * Ghost.GHOST_MAX_SPEED;
			default: return 0.95 * Ghost.GHOST_MAX_SPEED;
		}
	}

	public double getGhostVulnerableSpeed() {
		switch (this.currentLevel) {
			case 1: return 0.5 * Ghost.GHOST_MAX_SPEED;
			case 2:
			case 3:
			case 4: return 0.55 * Ghost.GHOST_MAX_SPEED;
			default: return 0.6 * Ghost.GHOST_MAX_SPEED;
		}
	}

	public double getGhostTunnelSpeed() {
		switch (this.currentLevel) {
			case 1: return 0.4 * Ghost.GHOST_MAX_SPEED;
			case 2:
			case 3:
			case 4: return 0.45 * Ghost.GHOST_MAX_SPEED;
			default: return 0.5 * Ghost.GHOST_MAX_SPEED;
		}
	}

	public double getGhostDiedSpeed() {
		return 3 * Ghost.GHOST_MAX_SPEED;
	}

	public int[] getScatterChaseSwitchTimesInSeconds() {
		switch (this.currentLevel) {
			case 1: return new int[]{ 7, 20, 7, 20, 5, 20, 5, Integer.MAX_VALUE };
			case 2:
			case 3:
			case 4: return new int[]{ 7, 20, 7, 20, 5, 100, 3, Integer.MAX_VALUE };
			default: return new int[]{ 5, 20, 5, 20, 5, 200, 2, Integer.MAX_VALUE };
		}
	}

	// Getters

	public int getScore() {
		return score;
	}

	public int getHighScore() {
		return highScore;
	}

	public String getHighScorePlayer() {
		return highScorePlayer;
	}

	public int getLivesLeft() {
		return livesLeft;
	}

	public boolean isGhostScatterModeActive() {
		return ghostScatterModeActive;
	}

	public int getGhostsEatenCount() {
		return ghostsEatenCount;
	}

	public int getCurrentSoundVolume() {
		return currentSoundVolume;
	}

	// Private implementation

	// Reads high score and high score player name from score.txt if the file exists and if it has the right format
	private void readHighScore() {
		try {
			String file = Utils.loadFileFromDiskAsString(this.SCORE_FILE_PATH);
			String[] tokens = file.split("\\s+");
			this.highScore = Utils.parseInt(tokens[0]);
			this.highScorePlayer = tokens[1];
		} catch (Exception e) {
			this.highScore = 0;
			this.highScorePlayer = "";
		}
	}

	// Writes new high score to score.txt (overwrites it if needed, only 1 score is remembered)
	private void writeHighScore() {
		File file = new File(this.SCORE_FILE_PATH);
		try {
			FileWriter f = new FileWriter(file, false); // not appending to the file means overwriting if it exists
			f.write(this.highScore + " " + this.highScorePlayer);
			f.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
