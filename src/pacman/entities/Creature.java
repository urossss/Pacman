package pacman.entities;

import pacman.core.Handler;
import pacman.tiles.Tile;

public abstract class Creature extends Entity {

	public static enum Direction {
		UP, DOWN, LEFT, RIGHT
	}

	protected Direction currentDirection = null;

	private double speed = 2.0;
	private double xMove = 0.0, yMove = 0.0;

	private int xTileCurrent, yTileCurrent; // current tile
	private int xTileNext, yTileNext; // next tile (tile where the creature is moving to, e.g. if the direction is UP, this will be the tile above the current one)

	public Creature(Handler handler, double x, double y) {
		super(handler, x, y);
	}

	public abstract Direction calculateDesiredDirection();

	public void move() {
		Direction desiredDirection = this.calculateDesiredDirection();
		this.changeDirectionIfPossible(desiredDirection);

		if (!this.canMoveWithoutTileCollision()) {
			// todo: adjust to the edge of the tile
			return;
		}

		this.x += this.xMove;
		this.y += this.yMove;

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

		// direction change is only possible if the creature top left corner (x,y) is close to the tile top left corner
		if (Math.abs(this.x - this.xTileCurrent * Tile.TILE_WIDTH) + Math.abs(this.y - this.yTileCurrent * Tile.TILE_HEIGHT) <= speed) {
			this.currentDirection = desiredDirection;
		}
	}

	private boolean canMoveWithoutTileCollision() { // todo: think about renaming this
		if (this.currentDirection == null) { // no movement, no collision
			return true; // todo: rethink this
		}

		this.setMovesAndTiles(this.currentDirection);

		if (this.canMoveToTile(this.xTileNext, this.yTileNext)) { // movement to the next tile is possible
			return true;
		}

		int currentTileMinX = this.xTileCurrent * Tile.TILE_WIDTH;
		int currentTileMinY = this.yTileCurrent * Tile.TILE_HEIGHT;
		double xNext = this.x + this.xMove;
		double yNext = this.y + this.yMove;

		// movement within the current tile is possible, outside it is not possible
		return xNext >= currentTileMinX
				&& xNext < currentTileMinX + Tile.TILE_WIDTH
				&& yNext >= currentTileMinY
				&& yNext < currentTileMinY + Tile.TILE_HEIGHT;
	}

	private boolean canMoveToTile(int xTile, int yTile) {
		return this.handler.getBoard().canMoveToTile(xTile, yTile);
	}
}
