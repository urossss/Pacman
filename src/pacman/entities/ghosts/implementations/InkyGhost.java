package pacman.entities.ghosts.implementations;

import pacman.core.Handler;
import pacman.entities.Creature;
import pacman.entities.ghosts.Ghost;
import pacman.tiles.Coordinates;

public class InkyGhost extends Ghost {

	public InkyGhost(Handler handler, double x, double y) {
		super(handler, x, y, new Coordinates(27, 32));
	}

	@Override
	public int getGhostId() {
		return 2;
	}

	@Override
	public Coordinates getChaseTarget() { // TODO: fix the algorithm
		Creature pacman = this.handler.getEntityManager().getPacman();
		return new Coordinates(pacman.getXTile(), pacman.getYTile());
	}

	@Override
	public int getMaxCageTimeMillis() {
		return 2000;
	}
}
