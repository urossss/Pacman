package pacman.entities;

import pacman.core.Handler;

import java.awt.*;

public class EntityManager {

	private Pacman pacman;

	public EntityManager(Handler handler) {
		this.pacman = new Pacman(handler);
	}

	public void update() {
		this.pacman.update();
	}

	public void render(Graphics g) {
		this.pacman.render(g);
	}

	public Pacman getPacman() {
		return pacman;
	}
}
