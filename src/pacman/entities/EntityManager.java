package pacman.entities;

import pacman.core.Handler;
import pacman.entities.ghosts.*;
import pacman.entities.ghosts.implementations.*;

import java.awt.Graphics;
import java.util.*;

public class EntityManager {

	private Handler handler;

	private Pacman pacman;

	private Ghost blinky;
	private Ghost pinky;
	private Ghost inky;
	private Ghost clyde;

	private List<Entity> entities;
	private List<Ghost> ghosts;

	public EntityManager(Handler handler) {
		this.handler = handler;

		this.pacman = new Pacman(handler);

		this.blinky = new BlinkyGhost(handler, 216, 176);
		this.pinky = new PinkyGhost(handler, 186, 232);
		this.inky = new InkyGhost(handler, 210, 210);
		this.clyde = new ClydeGhost(handler, 246, 220);

		this.entities = new ArrayList<>();
		this.entities.add(this.pacman);
		this.entities.add(this.blinky);
		this.entities.add(this.pinky);
		this.entities.add(this.inky);
		this.entities.add(this.clyde);

		this.ghosts = new ArrayList<>();
		this.ghosts.add(this.blinky);
		this.ghosts.add(this.pinky);
		this.ghosts.add(this.inky);
		this.ghosts.add(this.clyde);
	}

	public void update() {
		for (Entity e : this.entities) {
			e.update();
		}

		for (Ghost g : this.ghosts) {
			if (pacman.isInCollisionWith(g)) {
				if (g.canEat()) { // the ghost eats pacman
					this.handler.getStateManager().startPacmanDiedState();
				}
				if (g.canBeEaten()) { // pacman easts the ghost
					this.handler.getGame().ghostEaten(g);
				}
			}
		}
	}

	public void render(Graphics g) {
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

	public List<Ghost> getGhosts() {
		return ghosts;
	}
}
