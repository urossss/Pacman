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

		int fps = 60;
		double timePerTick = 1000000000.0 / fps;
		double delta = 0;
		long now, timer = 0;
		int ticks = 0;
		long lastTime = System.nanoTime();

		// Game loop
		while (running) {
			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
			timer += now - lastTime;
			lastTime = now;

			if (delta >= 1) {
				update();
				render();

				ticks++;
				delta--;
			}

			if (timer >= 1000000000) {
				//System.out.println("Ticks and frames: " + ticks);
				ticks = 0;
				timer = 0;
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
