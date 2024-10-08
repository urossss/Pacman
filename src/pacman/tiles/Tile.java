package pacman.tiles;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Tile {

	public static final int TILE_WIDTH = 16, TILE_HEIGHT = 16;
	protected static final int FOOD_SCORE = 10, POWER_FOOD_SCORE = 50;

	protected BufferedImage texture;
	protected final int type;

	public Tile(BufferedImage texture, int type) {
		this.texture = texture;
		this.type = type;
	}

	public void render(Graphics g, int x, int y) {
		g.drawImage(this.texture, x, y, TILE_WIDTH, TILE_HEIGHT, null);
	}

	public abstract boolean isSolid();

	public abstract boolean isEatable();

	public int getScore() {
		return 0;
	}

	public boolean isCageDoor() {
		return false;
	}

}
