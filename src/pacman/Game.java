package pacman;

import pacman.display.Display;

public class Game implements Runnable {

	// Display
	private Display display;
	private String title;
	private int width, height;

	// Game loop
	private boolean running = false;
	private Thread thread;

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
		display = new Display(title, width, height);
//		display.getFrame().addKeyListener(keyManager);
//
//		// Assets
//		Assets.init();
//
//		// Handler
//		handler = new Handler(this);
//
//		// States
//		menuState = new MenuState(handler);
//		readyState = new ReadyState(handler);
//		gameState = new GameState(handler);
//		levelCompletedState = new LevelCompletedState(handler);
//		pacmanDiedState = new PacmanDiedState(handler);
//		gameOverState = new GameOverState(handler);
//		newRecordState = new NewRecordState(handler);
//
//		menuState.start();
	}

	private void update() {

	}

	private void render() {

	}

}
