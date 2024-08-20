package pacman.entities.ghosts.states;

import pacman.core.Handler;
import pacman.entities.ghosts.Ghost;
import pacman.tiles.Coordinates;

public class GhostChaseState extends GhostState {

	public GhostChaseState(Ghost ghost, Handler handler) {
		super(ghost, handler);
	}

	@Override
	public void startImpl() {
		// Empty
	}

	@Override
	public void updateImpl() {
		if (this.handler.getGame().isGhostScatterModeActive()) {
			this.ghost.startScatterState();
		}
	}

	@Override
	public Coordinates calculateTarget() {
		return this.ghost.getChaseTarget();
	}
}
