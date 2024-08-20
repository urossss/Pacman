package pacman.entities.ghosts.states;

import pacman.core.Handler;
import pacman.entities.ghosts.Ghost;
import pacman.tiles.Coordinates;

public class GhostScatterState extends GhostState {

	private Coordinates scatterTarget;

	public GhostScatterState(Ghost ghost, Handler handler) {
		super(ghost, handler);

		this.scatterTarget = new Coordinates(this.ghost.getScatterXTarget(), this.ghost.getScatterYTarget());
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
		return this.scatterTarget;
	}
}
