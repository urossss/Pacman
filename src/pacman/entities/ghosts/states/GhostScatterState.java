package pacman.entities.ghosts.states;

import pacman.core.Handler;
import pacman.entities.ghosts.Ghost;
import pacman.tiles.Coordinates;

public class GhostScatterState extends GhostState {

	public GhostScatterState(Ghost ghost, Handler handler) {
		super(ghost, handler);
	}

	@Override
	public void startImpl() {
		// Empty
	}

	@Override
	public void updateImpl() {
		if (!this.handler.getGame().isGhostScatterModeActive()) {
			this.ghost.startChaseState();
		}
	}

	@Override
	public Coordinates calculateTarget() {
		return this.ghost.getScatterTarget();
	}
}
