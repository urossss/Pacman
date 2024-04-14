package pacman.core;

import pacman.graphics.ImageAssets;
import pacman.tiles.Tile;
import pacman.tiles.TileManager;
import pacman.utils.Utils;

import java.awt.*;

public class Board {

	private static final String DefaultMapPath = "/res/maps/map.txt";

	private int width, height;
	private Tile[][] tiles;

	private TileManager tileManager = new TileManager();

	public Board() {
		this(DefaultMapPath);
	}

	public Board(String mapPath) {
		this.loadMap(mapPath);
	}

	// Game loop interface

	public void update() {

	}

	public void render(Graphics g) {
		// Background
		g.drawImage(ImageAssets.map1, 0, 0, width * Tile.TILE_WIDTH, height * Tile.TILE_HEIGHT, null);

		// Map
		for (int y = 0; y < this.height; y++) {
			for (int x = 0; x < this.width; x++) {
				this.tiles[x][y].render(g, x * Tile.TILE_WIDTH, y * Tile.TILE_HEIGHT);
			}
		}
	}

	// Board implementation

	private void loadMap(String path) {
		String file = Utils.loadFileAsString(path);
		String[] tokens = file.split("\\s+");
		this.width = Utils.parseInt(tokens[0]);
		this.height = Utils.parseInt(tokens[1]);

		this.tiles = new Tile[this.width][this.height];

		int tileType;
		for (int y = 0; y < this.height; y++) {
			for (int x = 0; x < this.width; x++) {
				tileType = Utils.parseInt(tokens[2 + (y * this.width + x)]);
				this.tiles[x][y] = this.tileManager.getTileByType(tileType);
			}
		}
	}

}
