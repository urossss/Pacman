package pacman.entities;

import pacman.core.Handler;
import pacman.entities.ghosts.BlinkyGhost;
import pacman.entities.ghosts.Ghost;
import pacman.entities.ghosts.PinkyGhost;

import java.awt.Graphics;
import java.util.*;

public class EntityManager {

	private Pacman pacman;

	private Ghost blinky;
	private Ghost pinky;

	private List<Entity> entities;

	public EntityManager(Handler handler) {
		this.pacman = new Pacman(handler);

		this.blinky = new BlinkyGhost(handler, 216, 176, 25, -3);
		this.blinky.startScatterState();

		this.pinky = new PinkyGhost(handler, 186, 232, 2, -3);
		this.pinky.startScatterState();

		this.entities = new ArrayList<>();
		this.entities.add(this.pacman);
		this.entities.add(this.blinky);
//		this.entities.add(this.pinky);
	}

	public void update() {
//		this.pacman.update();
		for (Entity e : this.entities) {
			e.update();
		}
	}

	public void render(Graphics g) {
//		this.pacman.render(g);
		for (Entity e : this.entities) {
			e.render(g);
		}
	}

	public Pacman getPacman() {
		return pacman;
	}
}
