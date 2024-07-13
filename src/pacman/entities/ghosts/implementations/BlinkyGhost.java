package pacman.entities.ghosts.implementations;

import pacman.core.Handler;
import pacman.entities.Creature;
import pacman.entities.ghosts.Ghost;
import pacman.tiles.Coordinates;

public class BlinkyGhost extends Ghost {

	public BlinkyGhost(Handler handler, double x, double y, int scatterXTarget, int scatterYTarget) {
		super(handler, x, y, scatterXTarget, scatterYTarget);
	}

	@Override
	public int getGhostId() {
		return 0;
	}

	@Override
	public Coordinates getChaseTarget() {
		Creature pacman = this.handler.getEntityManager().getPacman();
		return new Coordinates(pacman.getXTile(), pacman.getYTile());
	}
}
