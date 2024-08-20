package pacman.entities.ghosts.states;

import pacman.core.Handler;
import pacman.entities.ghosts.Ghost;
import pacman.tiles.Coordinates;

public class GhostDiedState extends GhostState {

	public GhostDiedState(Ghost ghost, Handler handler) {
		super(ghost, handler);
	}

	@Override
	public void startImpl() {

	}

	@Override
	public void updateImpl() {

	}

	@Override
	public Coordinates calculateTarget() {
		return null;
	}
}
