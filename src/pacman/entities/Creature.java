package pacman.entities;

import pacman.core.Handler;
import pacman.tiles.Tile;

public abstract class Creature extends Entity {

	public enum Direction {
		UP, DOWN, LEFT, RIGHT
	}

	protected Direction currentDirection = Direction.RIGHT;

	private double speed = 2.0;
	private double xMove = 0.0, yMove = 0.0;

	private int xTileCurrent, yTileCurrent; // current tile
	private int xTileNext, yTileNext; // next tile (tile where the creature is moving to, e.g. if the direction is UP, this will be the tile above the current one)

	public Creature(Handler handler, double x, double y) {
		super(handler, x, y);
	}

	public Creature(Handler handler, double x, double y, int width, int height) {
		super(handler, x, y, width, height);
	}

	public abstract Direction calculateDesiredDirection();

	public void move() {
		// change the direction to the target (desired) direction if possible
		Direction desiredDirection = this.calculateDesiredDirection();
		this.changeDirectionIfPossible(desiredDirection);

		// check if movement is possible
		if (!this.canMoveWithoutTileCollision()) {
			// since further movement is not possible, align the coordinates to the current tile borders
			this.x = this.getXTile() * Tile.TILE_WIDTH;
			this.y = this.getYTile() * Tile.TILE_HEIGHT;

			// direction change is possible since there is no movement
			this.currentDirection = desiredDirection;

			return;
		}

		// actual movement (not that there will be non-zero movement only along 1 axis, not both)
		this.x += this.xMove;
		this.y += this.yMove;

		// align the non-movement axis to the current tile borders
		if (this.getDirectionAxis(this.currentDirection) == 'x') {
			this.y = getYTile() * Tile.TILE_HEIGHT;
		} else {
			this.x = getXTile() * Tile.TILE_WIDTH;
		}

		// todo: check bounds on that map tunnel on yTile = 14
	}

	private void setMovesAndTiles(Direction direction) {
		this.xTileCurrent = this.getXTile();
		this.yTileCurrent = this.getYTile();

		this.xMove = 0;
		this.yMove = 0;
		this.xTileNext = xTileCurrent;
		this.yTileNext = yTileCurrent;

		if (direction == null) {
			return;
		}

		switch (direction) {
			case UP:
				this.yMove = -this.speed;
				this.yTileNext = this.yTileCurrent - 1;
				break;
			case DOWN:
				this.yMove = this.speed;
				this.yTileNext = this.yTileCurrent + 1;
				break;
			case LEFT:
				this.xMove = -this.speed;
				this.xTileNext = this.xTileCurrent - 1;
				break;
			case RIGHT:
				this.xMove = this.speed;
				this.xTileNext = this.xTileCurrent + 1;
				break;
		}
	}

	private void changeDirectionIfPossible(Direction desiredDirection) {
		if (desiredDirection == null) {
			return;
		}

		this.setMovesAndTiles(desiredDirection);

		if (!this.canMoveToTile(this.xTileNext, this.yTileNext)) { // movement to the next tile is not possible
			return;
		}

		// same axis direction change is always possible
		if (this.getDirectionAxis(this.currentDirection) == this.getDirectionAxis(desiredDirection)) {
			this.currentDirection = desiredDirection;
			return;
		}

		// different axis direction change is only possible if the creature's top left corner (x,y) is close to the tile's top left corner
		if (Math.abs(this.x - this.xTileCurrent * Tile.TILE_WIDTH) + Math.abs(this.y - this.yTileCurrent * Tile.TILE_HEIGHT) <= speed) {
			this.currentDirection = desiredDirection;
		}
	}

	private boolean canMoveWithoutTileCollision() {
		if (this.currentDirection == null) { // no movement means no collision, plus this should never be null
			return true;
		}

		this.setMovesAndTiles(this.currentDirection);

		if (this.canMoveToTile(this.xTileNext, this.yTileNext)) { // movement to the next tile is possible
			return true;
		}

		int currentTileMinX = this.xTileCurrent * Tile.TILE_WIDTH;
		int currentTileMinY = this.yTileCurrent * Tile.TILE_HEIGHT;
		double xNext = this.x + this.xMove;
		double yNext = this.y + this.yMove;

		// Movement within the current tile is possible, outside it is not possible.
		// If the creature's top left corner (x,y) is not too close to the tile's top left corner (distance is larger
		// than speed), then the movement is possible because it means that the movement within this tile has already
		// started and it can continue until the creature's corner is aligned with the tile's corner.
		// If the creatures's corner is aligned with the tile's corner (distance is less than or equal to speed),
		// that means that the creature is perfectly aligned with current tile, and any new movement would move it
		// towards the next tile, and movement to the next tile is not possible as checked above.
		return Math.abs(xNext - currentTileMinX) + Math.abs(yNext - currentTileMinY) > speed;
	}

	private boolean canMoveToTile(int xTile, int yTile) {
		return this.handler.getBoard().canMoveToTile(xTile, yTile);
	}

	private char getDirectionAxis(Direction direction) {
		if (direction == Direction.UP || direction == Direction.DOWN) {
			return 'y';
		}
		if (direction == Direction.LEFT || direction == Direction.RIGHT) {
			return 'x';
		}
		return '?';
	}
}