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
	public Coordinates getChaseTarget() {
		Creature pacman = this.handler.getEntityManager().getPacman();
		int pacmanXTile = pacman.getXTile();
		int pacmanYTile = pacman.getYTile();
		Creature blinky = this.handler.getEntityManager().getBlinky();
		int blinkyXTile = blinky.getXTile();
		int blinkyYTile = blinky.getYTile();

		int targetXTile = pacmanXTile + (pacmanXTile - blinkyXTile);
		int targetYTile = pacmanYTile + (pacmanYTile - blinkyYTile);

		return new Coordinates(targetXTile, targetYTile);
	}

	@Override
	public int getMaxCageTimeMillis() {
		return 2000;
	}
}
