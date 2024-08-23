package pacman.entities.ghosts;

import pacman.core.Handler;
import pacman.entities.Creature;
import pacman.entities.ghosts.states.*;
import pacman.tiles.Coordinates;

import java.awt.*;
import java.util.ArrayList;

public abstract class Ghost extends Creature {

	public static final double GHOST_MAX_SPEED = 2.5;

	protected final Coordinates scatterTarget;
	protected final Coordinates cageTarget = new Coordinates(14, 11);
	protected final Coordinates diedTarget = new Coordinates(14, 14);

	private int nextTileX, nextTileY;
	private Direction nextDirection;

	private boolean canMoveThroughCageDoor = false;
	private boolean isInTheTunnel = false;
	private double speedBeforeTunnel;
	private long cageStateEndTime, vulnerableStateEndTime;
	private int diedXTile, diedYTile, diedGhostIndex;

	protected GhostState currentState;

	protected GhostState cageState;
	protected GhostState scatterState;
	protected GhostState chaseState;
	protected GhostState vulnerableState;
	protected GhostState diedState;

	public Ghost(Handler handler, double x, double y, Coordinates scatterTarget) {
		super(handler, x, y);

		this.scatterTarget = scatterTarget;

		this.cageState = new GhostCageState(this, handler);
		this.chaseState = new GhostChaseState(this, handler);
		this.scatterState = new GhostScatterState(this, handler);
		this.vulnerableState = new GhostVulnerableState(this, handler);
		this.diedState = new GhostDiedState(this, handler);
	}

	public abstract int getGhostId();

	public abstract Coordinates getChaseTarget();

	public abstract int getMaxCageTimeMillis();

	@Override
	protected Direction calculateDesiredDirection() {
		if (this.currentState == null) {
			return this.currentDirection;
		}

		if (this.nextDirection == null) {
			this.setNextTileAndDirection(this.currentDirection);
		}

		int xCurrent = this.getXTile();
		int yCurrent = this.getYTile();

		// it's possible to change the direction only after previously set next tile has been reached
		if (!(xCurrent == this.nextTileX && yCurrent == this.nextTileY)) {
			return this.nextDirection;
		}

		Coordinates target = this.currentState.calculateTarget();
		int xTarget = target.x;
		int yTarget = target.y;

		boolean[] possibleMove = new boolean[4];
		possibleMove[0] = this.getAdjacentTileCoordinates(xCurrent, yCurrent, Direction.UP) != null;
		possibleMove[1] = this.getAdjacentTileCoordinates(xCurrent, yCurrent, Direction.DOWN) != null;
		possibleMove[2] = this.getAdjacentTileCoordinates(xCurrent, yCurrent, Direction.LEFT) != null;
		possibleMove[3] = this.getAdjacentTileCoordinates(xCurrent, yCurrent, Direction.RIGHT) != null;

		// a ghost can never reverse a direction
		switch (this.currentDirection) {
			case UP:
				possibleMove[1] = false;
				break;
			case DOWN:
				possibleMove[0] = false;
				break;
			case LEFT:
				possibleMove[3] = false;
				break;
			case RIGHT:
				possibleMove[2] = false;
				break;
		}

		double[] dist = new double[4];
		dist[0] = distance(xCurrent, yCurrent - 1, xTarget, yTarget);   // up
		dist[1] = distance(xCurrent, yCurrent + 1, xTarget, yTarget);   // down
		dist[2] = distance(xCurrent - 1, yCurrent, xTarget, yTarget);   // left
		dist[3] = distance(xCurrent + 1, yCurrent, xTarget, yTarget);   // right

		ArrayList<Integer> possibleIndexes = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			if (possibleMove[i]) {
				possibleIndexes.add(i);
			}
		}

		int index = -1;
		double min = 1000;
		for (int i : possibleIndexes) {
			if (dist[i] < min) {
				index = i;
				min = dist[i];
			}
		}

		Direction desiredDirection = this.currentDirection;
		switch (index) {
			case 0:
				desiredDirection = Direction.UP;
				break;
			case 1:
				desiredDirection = Direction.DOWN;
				break;
			case 2:
				desiredDirection = Direction.LEFT;
				break;
			case 3:
				desiredDirection = Direction.RIGHT;
				break;
			default: // index == -1 means no change in direction
		}

		this.setNextTileAndDirection(desiredDirection);

		return desiredDirection;
	}

	@Override
	public boolean canMoveThroughCageDoor() {
		// when can a ghost move through cage door
		// - if it is outside the cage
		//   - only if it is in GhostDiedState
		// - if it is inside the cage
		//   - this can be in GhostCageState, or in GhostVulnerableState if the Pacman eats power pellet
		//     before the ghost left the Cage mode
		//   - only if canMoveThroughCageDoor is set to true

		boolean isInsideTheCage = this.handler.getBoard().isTileInsideGhostCage(this.getXTile(), this.getYTile());

		if (isInsideTheCage) {
			return this.canMoveThroughCageDoor;
		} else {
			return this.currentState instanceof GhostDiedState;
		}
	}

	// public interface

	@Override
	public void update() {
		timerUpdates();
		if (this.currentState != null) {
			this.currentState.update();
		}
		this.move();

		if (this.handler.getBoard().isTileInsideTunnel(this.getXTile(), this.getYTile())) {
			if (!this.isInTheTunnel && !(this.currentState instanceof GhostDiedState)) {
				this.isInTheTunnel = true;
				this.speedBeforeTunnel = this.getSpeed();
				this.setSpeed(this.handler.getGame().getGhostTunnelSpeed());
			}
		} else {
			if (this.isInTheTunnel) {
				this.isInTheTunnel = false;
				this.setSpeed(this.speedBeforeTunnel);
			}
		}
	}

	@Override
	public void render(Graphics g) {
		if (this.currentState != null) {
			this.currentState.render(g);
		}
	}

	// ghost state management

	public void startCageState(int duration) {
		this.currentState = this.cageState;
		this.cageState.start();
		this.cageStateEndTime = System.currentTimeMillis() + duration;
	}

	public void startCageState() {
		this.startCageState(this.getMaxCageTimeMillis());
	}

	public void startScatterState() {
		this.currentState = this.scatterState;
		this.scatterState.start();
	}

	public void startChaseState() {
		this.currentState = this.chaseState;
		this.chaseState.start();
	}

	public void startVulnerableState() {
		this.currentState = this.vulnerableState;
		this.vulnerableState.start();
		this.vulnerableStateEndTime = System.currentTimeMillis() + this.handler.getGame().getGhostVulnerableStateDurationMillis();
	}

	public void startDiedState() {
		this.diedXTile = this.getXTile();
		this.diedYTile = this.getYTile();
		this.diedGhostIndex = this.handler.getGame().getGhostsEatenCount();

		this.vulnerableStateEndTime = -1;

		this.currentState = this.diedState;
		this.diedState.start();
	}

	public void startScatterOrChaseState() {
		if (this.handler.getGame().isGhostScatterModeActive()) {
			this.startScatterState();
		} else {
			this.startChaseState();
		}
	}

	// ghost movement

	public Coordinates getScatterTarget() {
		return scatterTarget;
	}

	public Coordinates getCageTarget() {
		return cageTarget;
	}

	public Coordinates getDiedTarget() {
		return diedTarget;
	}

	public void setCanMoveThroughCageDoor(boolean canMoveThroughCageDoor) {
		this.canMoveThroughCageDoor = canMoveThroughCageDoor;
	}

	public int getDiedXTile() {
		return diedXTile;
	}

	public int getDiedYTile() {
		return diedYTile;
	}

	public int getDiedGhostIndex() {
		return diedGhostIndex;
	}

	public boolean canBeEaten() {
		return this.currentState instanceof GhostVulnerableState;
	}

	public boolean canEat() {
		return this.currentState instanceof GhostScatterState || this.currentState instanceof GhostChaseState;
	}

	// helper methods

	protected double distance(int x1, int y1, int x2, int y2) {
		return Math.hypot(x1 - x2, y1 - y2);
	}

	private void setNextTileAndDirection(Direction direction) {
		int currentTileX = this.getXTile();
		int currentTileY = this.getYTile();

		this.nextTileX = currentTileX;
		this.nextTileY = currentTileY;
		this.nextDirection = direction;

		if (direction == null) {
			return;
		}

		Coordinates nextTile = this.getAdjacentTileCoordinates(currentTileX, currentTileY, direction);

		if (nextTile != null) {
			this.nextTileX = nextTile.x;
			this.nextTileY = nextTile.y;
		}
	}

	private void timerUpdates() {
		long currentTime = System.currentTimeMillis();

		// get out of cage state
		if (this.cageStateEndTime > 0 && currentTime > this.cageStateEndTime) {
			this.cageStateEndTime = -1;
			this.setCanMoveThroughCageDoor(true);
		}

		// get out of vulnerable state
		if (this.vulnerableStateEndTime > 0 && currentTime > this.vulnerableStateEndTime) {
			this.vulnerableStateEndTime = -1;
			this.startScatterOrChaseState();
		}
	}
}
