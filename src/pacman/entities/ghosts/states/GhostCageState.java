package pacman.entities.ghosts.states;

import pacman.core.Handler;
import pacman.entities.ghosts.Ghost;
import pacman.tiles.Coordinates;

public class GhostCageState extends GhostState {

	private Coordinates cageTarget;

	public GhostCageState(Ghost ghost, Handler handler) {
		super(ghost, handler);

		this.cageTarget = new Coordinates(this.ghost.getCageXTarget(), this.ghost.getCageYTarget());
	}

	@Override
	public void startImpl() {
		// Empty
	}

	@Override
	public void updateImpl() {
		if (this.ghost.getXTile() == this.cageTarget.x && this.ghost.getYTile() == this.cageTarget.y) {
			// this.ghost.setCanMoveThroughCageDoor(false); // not needed anymore - done in GhostState base class start method
			this.ghost.startScatterOrChaseState();
			return;
		}
	}

	@Override
	public Coordinates calculateTarget() {
		return this.cageTarget;
	}
}
