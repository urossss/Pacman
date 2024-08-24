package pacman.entities;

import pacman.core.Game;
import pacman.core.Handler;
import pacman.tiles.Coordinates;
import pacman.tiles.Tile;

public abstract class Creature extends Entity {

	public enum Direction {
		UP, DOWN, LEFT, RIGHT
	}

	protected Direction currentDirection = Direction.RIGHT;

	private double speed = 2.0;
	private double xMove = 0.0, yMove = 0.0;

	private int xTileCurrent, yTileCurrent; // current tile

	public Creature(Handler handler, double x, double y) {
		super(handler, x, y);
	}

	public Creature(Handler handler, double x, double y, int width, int height) {
		super(handler, x, y, width, height);
	}

	protected abstract Direction calculateDesiredDirection();

	protected abstract boolean canMoveThroughCageDoor();

	public boolean isInCollisionWith(Creature c) {
		return Math.abs(this.x - c.x) + Math.abs(this.y - c.y) <= 20;
	}

	public void move() {
		// change the direction to the target (desired) direction if possible
		Direction desiredDirection = this.calculateDesiredDirection();
		this.changeDirectionIfPossible(desiredDirection);

		// check if movement is possible
		if (!this.canMoveWithoutTileCollision()) {
			// since further movement is not possible, align the coordinates with the current tile's borders
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

		// If the map (board) has tiles with coordinates from 0 to N-1, it is possible for the creature to go from
		// 0 to N-1 directly (if both tiles are empty, i.e. not solid). In that case, the transition would go through
		// one imaginary tile at coordinate -1 or N.
		// The checks below make sure that the alignment is done properly on both axes in this case.
		if (this.x <= -Tile.TILE_WIDTH) {
			this.x = Game.GAME_BOARD_WIDTH_PIXELS;
		} else if (this.x >= Game.GAME_BOARD_WIDTH_PIXELS) {
			this.x = -Tile.TILE_WIDTH;
		}
		if (this.y <= -Tile.TILE_HEIGHT) {
			this.y = Game.GAME_BOARD_HEIGHT_PIXELS;
		} else if (this.y >= Game.GAME_BOARD_HEIGHT_PIXELS) {
			this.y = -Tile.TILE_HEIGHT;
		}
	}

	public double getSpeed() {
		return this.speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public Direction getCurrentDirection() {
		return this.currentDirection;
	}

	// movement implementation

	private void setMovesAndTiles(Direction direction) {
		this.xTileCurrent = this.getXTile();
		this.yTileCurrent = this.getYTile();

		this.xMove = 0;
		this.yMove = 0;

		if (direction == null) {
			return;
		}

		switch (direction) {
			case UP:
				this.yMove = -this.speed;
				break;
			case DOWN:
				this.yMove = this.speed;
				break;
			case LEFT:
				this.xMove = -this.speed;
				break;
			case RIGHT:
				this.xMove = this.speed;
				break;
		}
	}

	private void changeDirectionIfPossible(Direction desiredDirection) {
		if (desiredDirection == null) {
			return;
		}

		this.setMovesAndTiles(desiredDirection);

		Coordinates c = this.getAdjacentTileCoordinates(this.xTileCurrent, this.yTileCurrent, desiredDirection);
		if (c == null) { // movement to the next tile is not possible
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

		Coordinates c = this.getAdjacentTileCoordinates(this.xTileCurrent, this.yTileCurrent, this.currentDirection);
		if (c != null) { // movement to the next tile is possible
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

	protected Coordinates getAdjacentTileCoordinates(int xTile, int yTile, Direction direction) {
		return this.handler.getBoard().getAdjacentTileCoordinates(xTile, yTile, direction, this.canMoveThroughCageDoor());
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
