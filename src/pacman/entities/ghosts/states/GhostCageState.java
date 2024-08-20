package pacman.entities.ghosts.states;

import pacman.core.Handler;
import pacman.entities.ghosts.Ghost;
import pacman.tiles.Coordinates;

public class GhostCageState extends GhostState {

	private Coordinates cageTarget;

	private long previousTime;
	private long timer;
	private long maxCageTime;

	public GhostCageState(Ghost ghost, Handler handler) {
		super(ghost, handler);

		this.cageTarget = new Coordinates(this.ghost.getCageXTarget(), this.ghost.getCageYTarget());
		this.maxCageTime = ghost.getMaxCageTimeMillis();
	}

	@Override
	public void startImpl() {
		this.previousTime = System.currentTimeMillis();
		this.timer = 0;
//		System.out.println("GhostCageState start " + this.ghost.getGhostId() + " " + this.previousTime);
	}

	@Override
	public void updateImpl() {
		if (this.ghost.getXTile() == this.cageTarget.x && this.ghost.getYTile() == this.cageTarget.y) {
			this.ghost.setCanMoveThroughCageDoor(false);
			this.ghost.startScatterOrChaseState();
			return;
		}

//		long currentTime = System.currentTimeMillis();
//		this.timer += currentTime - this.previousTime;
//		this.previousTime = currentTime;
//
//		if (this.timer >= this.maxCageTime) {
//			this.ghost.setCanMoveThroughCageDoor(true);
////			System.out.println("cage mode over " + this.ghost.getGhostId() + " " + this.previousTime);
//		}
	}

	@Override
	public Coordinates calculateTarget() {
		return this.cageTarget;
	}
}
