package pacman.entities.ghosts;

import pacman.core.Handler;
import pacman.entities.Creature;
import pacman.entities.ghosts.states.GhostScatterState;
import pacman.entities.ghosts.states.GhostState;
import pacman.entities.ghosts.states.GhostVulnerableState;
import pacman.tiles.Coordinates;

import java.awt.*;
import java.util.ArrayList;

public abstract class Ghost extends Creature {

	private static double baseSpeed = 2.0;

	protected final int scatterXTarget, scatterYTarget;
	protected final int cageXTarget = 13, cageYTarget = 11;

	private int nextTileX, nextTileY;
	private Direction nextDirection;

	protected GhostState currentState;

	protected GhostState scatterState;
	protected GhostState vulnerableState;

	public Ghost(Handler handler, double x, double y, int scatterXTarget, int scatterYTarget) {
		super(handler, x, y);

		this.scatterXTarget = scatterXTarget;
		this.scatterYTarget = scatterYTarget;

		this.scatterState = new GhostScatterState(this, handler);
		this.vulnerableState = new GhostVulnerableState(this, handler);
	}

	public abstract int getGhostId();

	public abstract Coordinates getChaseTarget();

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
		possibleMove[0] = this.handler.getBoard().getAdjacentTileCoordinates(xCurrent, yCurrent, Direction.UP) != null;
		possibleMove[1] = this.handler.getBoard().getAdjacentTileCoordinates(xCurrent, yCurrent, Direction.DOWN) != null;
		possibleMove[2] = this.handler.getBoard().getAdjacentTileCoordinates(xCurrent, yCurrent, Direction.LEFT) != null;
		possibleMove[3] = this.handler.getBoard().getAdjacentTileCoordinates(xCurrent, yCurrent, Direction.RIGHT) != null;

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

//		if (desiredDirection != this.currentDirection) {
//			System.out.println("Current: " + this.currentDirection + " -> desired: " + desiredDirection);
//		}

		this.setNextTileAndDirection(desiredDirection);

		return desiredDirection;
	}

	// public interface

	@Override
	public void update() {
		this.move();
		if (this.currentState != null) {
			this.currentState.update();
		}
	}

	@Override
	public void render(Graphics g) {
		if (this.currentState != null) {
			this.currentState.render(g);
		}
	}

	public void startScatterState() {
		this.currentState = this.scatterState;
		this.scatterState.start();
	}

	public void startVulnerableState() {
		this.currentState = this.vulnerableState;
		this.vulnerableState.start();
	}

	public int getScatterXTarget() {
		return scatterXTarget;
	}

	public int getScatterYTarget() {
		return scatterYTarget;
	}

	// static interface

	public static double getBaseSpeed() {
		return Ghost.baseSpeed;
	}

	public static void setBaseSpeed(double baseSpeed) {
		Ghost.baseSpeed = baseSpeed;
	}

	// helper methods

	private double distance(int x1, int y1, int x2, int y2) {
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

		Coordinates nextTile = this.handler.getBoard().getAdjacentTileCoordinates(currentTileX, currentTileY, direction);

		if (nextTile != null) {
			this.nextTileX = nextTile.x;
			this.nextTileY = nextTile.y;
		}
	}

}
