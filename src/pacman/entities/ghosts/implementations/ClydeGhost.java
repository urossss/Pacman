package pacman.entities.ghosts.implementations;

import pacman.core.Handler;
import pacman.entities.Creature;
import pacman.entities.ghosts.Ghost;
import pacman.tiles.Coordinates;

public class ClydeGhost extends Ghost {

	public ClydeGhost(Handler handler, double x, double y) {
		super(handler, x, y, new Coordinates(0, 32));
	}

	@Override
	public int getGhostId() {
		return 3;
	}

	@Override
	public Coordinates getChaseTarget() {
		Creature pacman = this.handler.getEntityManager().getPacman();
		double distanceToPacman = this.distance(this.getXTile(), this.getYTile(), pacman.getXTile(), pacman.getYTile());
		if (distanceToPacman >= 8) {
			return new Coordinates(pacman.getXTile(), pacman.getYTile());
		} else {
			return this.scatterTarget;
		}
	}

	@Override
	public int getMaxCageTimeMillis() {
		return 3000;
	}
}
