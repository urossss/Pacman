package pacman.entities;

import pacman.core.Handler;
import pacman.entities.ghosts.*;
import pacman.entities.ghosts.implementations.*;

import java.awt.Graphics;
import java.util.*;

public class EntityManager {

	private Pacman pacman;

	private Ghost blinky;
	private Ghost pinky;
	private Ghost inky;
	private Ghost clyde;

	private List<Entity> entities;

	public EntityManager(Handler handler) {
		this.pacman = new Pacman(handler);

		this.blinky = new BlinkyGhost(handler, 216, 176, 25, -3);
//		this.blinky.startScatterState();

		this.pinky = new PinkyGhost(handler, 186, 232, 2, -3);
//		this.pinky = new PinkyGhost(handler, 216, 176, 4, 14);
//		this.pinky.startCageState();
//		this.pinky.setCanMoveThroughCageDoor(true);

		this.inky = new InkyGhost(handler, 210, 210, 27, 32);
//		this.inky.startCageState();

		this.clyde = new ClydeGhost(handler, 246, 220, 0, 32);
//		this.clyde.startCageState();

		this.entities = new ArrayList<>();
		this.entities.add(this.pacman);
		this.entities.add(this.blinky);
		this.entities.add(this.pinky);
		this.entities.add(this.inky);
		this.entities.add(this.clyde);
	}

	public void update() {
//		this.pacman.update(); // updated below
		for (Entity e : this.entities) {
			e.update();
		}
	}

	public void render(Graphics g) {
//		this.pacman.render(g); // rendered below
		for (Entity e : this.entities) {
			e.render(g);
		}
	}

	public Pacman getPacman() {
		return pacman;
	}

	public Ghost getBlinky() {
		return blinky;
	}

	public Ghost getPinky() {
		return pinky;
	}

	public Ghost getInky() {
		return inky;
	}

	public Ghost getClyde() {
		return clyde;
	}

}
