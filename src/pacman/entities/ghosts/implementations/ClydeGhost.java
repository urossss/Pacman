package pacman.entities.ghosts.implementations;

import pacman.core.Handler;
import pacman.entities.Creature;
import pacman.entities.ghosts.Ghost;
import pacman.tiles.Coordinates;

public class ClydeGhost extends Ghost {

	public ClydeGhost(Handler handler, double x, double y, int scatterXTarget, int scatterYTarget) {
		super(handler, x, y, scatterXTarget, scatterYTarget);
	}

	@Override
	public int getGhostId() {
		return 3;
	}

	@Override
	public Coordinates getChaseTarget() { // TODO: fix the algorithm
		Creature pacman = this.handler.getEntityManager().getPacman();
		return new Coordinates(pacman.getXTile(), pacman.getYTile());
	}

	@Override
	public int getMaxCageTimeMillis() {
		return 4000;
	}
}
