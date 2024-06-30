package pacman.entities.ghosts.states;

import pacman.core.Handler;
import pacman.entities.Creature;
import pacman.entities.ghosts.Ghost;
import pacman.graphics.Animation;
import pacman.tiles.Coordinates;

import java.awt.*;
import java.util.Map;

public class GhostScatterState extends GhostState {

	private Coordinates scatterTarget;

	public GhostScatterState(Ghost ghost, Handler handler) {
		super(ghost, handler);

		this.scatterTarget = new Coordinates(this.ghost.getScatterXTarget(), this.ghost.getScatterYTarget());
	}

	@Override
	public void start() {
		this.ghost.setSpeed(Ghost.getBaseSpeed());
	}

	@Override
	public void updateImpl() {
		// empty
	}

	@Override
	public Coordinates calculateTarget() {
		return this.scatterTarget;
	}
}