package pacman.core;

import pacman.display.Display;
import pacman.graphics.ImageAssets;
import pacman.states.StateManager;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game implements Runnable {

	// Display
	private Display display;
	private String title;
	private int width, height;

	// Game loop
	private boolean running = false;
	private Thread thread;

	// Rendering
	private BufferStrategy bs;
	private Graphics g;

	// Game handler
	private Handler handler;

	private StateManager stateManager;

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

		this.stateManager.startLoadingState();

		// Input manager
//		display.getFrame().addKeyListener(keyManager);

		// Asynchronously load assets and start the game
		new Thread(() -> {
			// Assets
			ImageAssets.init();

			// The board can be created now since the tile image assets are ready
			this.handler.setBoard(new Board());

			this.stateManager.startGameState();
		}).start();


//		menuState = new MenuState(handler);
//		readyState = new ReadyState(handler);
//		gameState = new GameState(handler);
//		levelCompletedState = new LevelCompletedState(handler);
//		pacmanDiedState = new PacmanDiedState(handler);
//		gameOverState = new GameOverState(handler);
//		newRecordState = new NewRecordState(handler);
	}

	private void update() {
		this.stateManager.update();
	}

	private void render() {
		// Get Graphics object
		this.bs = this.display.getCanvas().getBufferStrategy();
		if (this.bs == null) { // calling this method for the first time
			this.display.getCanvas().createBufferStrategy(3);
			return;
		}
		this.g = this.bs.getDrawGraphics();

		// Clear the screen
		this.g.setColor(Color.black);
		this.g.fillRect(0, 0, this.width, this.height);

		// Draw game here
//		this.board.render(g);
		this.stateManager.render(g);

		// End drawing
		this.bs.show();
		this.g.dispose();
	}

}
