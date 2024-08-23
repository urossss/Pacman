package pacman.entities.ghosts.implementations;

import pacman.core.Handler;
import pacman.entities.Creature;
import pacman.entities.ghosts.Ghost;
import pacman.tiles.Coordinates;

public class BlinkyGhost extends Ghost {

	public BlinkyGhost(Handler handler, double x, double y) {
		super(handler, x, y, new Coordinates(25, -3));
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

	@Override
	public int getMaxCageTimeMillis() {
		return 0;
	}
}
