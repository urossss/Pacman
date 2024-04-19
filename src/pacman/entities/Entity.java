package pacman.entities;

import pacman.core.Handler;
import pacman.tiles.Tile;

import java.awt.*;

public abstract class Entity {

	private static final int DEFAULT_WIDTH = 32, DEFAULT_HEIGHT = 32;

	protected Handler handler;
	protected double x, y;
	protected int width, height;

	public Entity(Handler handler, double x, double y) {
		this(handler, x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}

	public Entity(Handler handler, double x, double y, int width, int height) {
		this.handler = handler;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public abstract void update();

	public abstract void render(Graphics g);

	public int getXTile() {
		return (int) Math.round(this.x / Tile.TILE_WIDTH);
	}

	public int getYTile() {
		return (int) Math.round(this.y / Tile.TILE_HEIGHT);
	}
}
