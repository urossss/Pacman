package pacman.core;

import pacman.display.Display;
import pacman.graphics.ImageAssets;
import pacman.input.KeyManager;
import pacman.states.StateManager;
import pacman.utils.Utils;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Game implements Runnable {

	private final String SCORE_FILE_PATH = "./score.txt";

	// Display
	private Display display;
	private String title;
	private int width, height;

	// Game loop
	private boolean running = false;
	private Thread thread;

	// Game handler
	private Handler handler;
	private StateManager stateManager;
	private KeyManager keyManager;

	// Game variables
	private int score;
	private int highScore;
	private String highScorePlayer;
	private int livesLeft;
	private boolean isNewHighScore = false;

	public Game(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
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
				currentTickCount++;
				totalTickCount++;

				this.update();
				this.render();

				nextGameTick += TIME_PER_TICK;

				if (PERF_TRACKING_ENABLED) {
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
		this.display = new Display(title, width, height);

		// Handler
		this.handler = new Handler(this);

		this.stateManager = new StateManager(this.handler);
		this.handler.setStateManager(this.stateManager);

		this.keyManager = new KeyManager();
		this.handler.setKeyManager(this.keyManager);
		this.display.getFrame().addKeyListener(this.keyManager);

		this.stateManager.startLoadingState();

		// Asynchronously load assets and start the game
		new Thread(() -> {
			ImageAssets.init();
			this.stateManager.startMenuState();
		}).start();
	}

	private void update() {
		this.keyManager.update();
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
		g.fillRect(0, 0, this.width, this.height);

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

		this.readHighScore();

		this.handler.setBoard(new Board(this.handler));
	}

	public void scorePoints(int points) {
		if (this.score / 10000 != (this.score + points) / 10000) { // extra life for every 10,000 points scored
			this.livesLeft++;
		}
		this.score += points;

		if (this.score > this.highScore) {
			this.isNewHighScore = true;
			this.highScore = this.score;
		}
	}

	public void increaseLivesLeft() {
		livesLeft++;
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

	// Private implementation

	// Reads high score and high score player name from score.txt if the file exists and if it has the right format
	private void readHighScore() {
		try {
			String file = Utils.loadFileAsString(this.SCORE_FILE_PATH);
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
