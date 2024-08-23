package pacman.entities.ghosts.implementations;

import pacman.core.Handler;
import pacman.entities.Creature;
import pacman.entities.ghosts.Ghost;
import pacman.tiles.Coordinates;

public class PinkyGhost extends Ghost {

	public PinkyGhost(Handler handler, double x, double y) {
		super(handler, x, y, new Coordinates(2, -3));
	}

	@Override
	public int getGhostId() {
		return 1;
	}

	@Override
	public Coordinates getChaseTarget() {
		Creature pacman = this.handler.getEntityManager().getPacman();
		int pacmanXTile = pacman.getXTile();
		int pacmanYTile = pacman.getYTile();

		int xTarget = pacmanXTile, yTarget = pacmanYTile;
		switch (pacman.getCurrentDirection()) {
			case UP:
				yTarget = pacmanYTile - 4;
				break;
			case DOWN:
				yTarget = pacmanYTile + 4;
				break;
			case LEFT:
				xTarget = pacmanXTile - 4;
				break;
			case RIGHT:
				xTarget = pacmanXTile + 4;
				break;
		}

		return new Coordinates(xTarget, yTarget);
	}

	@Override
	public int getMaxCageTimeMillis() {
		return 1000;
	}
}
