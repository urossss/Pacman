package pacman.core;

import pacman.entities.EntityManager;
import pacman.graphics.ImageAssets;
import pacman.tiles.PowerFoodTile;
import pacman.tiles.Tile;
import pacman.tiles.TileManager;
import pacman.utils.Utils;

import java.awt.*;

public class Board {

	private static final String DefaultMapPath = "/res/maps/map.txt";

	private int width, height;
	private Tile[][] tiles;

	private TileManager tileManager = new TileManager();

	private Handler handler;
	private EntityManager entityManager;

	private int foodLeft;

	public Board(Handler handler) {
		this(handler, DefaultMapPath);
	}

	public Board(Handler handler, String mapPath) {
		this.handler = handler;
		this.loadMap(mapPath);

		this.recreateEntities();
	}

	// Game loop interface

	public void update() {
		this.entityManager.update();
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

		// Entities
		this.entityManager.render(g);
	}

	// Public interface

	public boolean canMoveToTile(int x, int y) {
		if (x < 0 || y < 0 || x >= this.width || y >= this.height) {
//			if (y == 14) {
//				return Tile.emptyTile;
//			}
			return false;
		}

		return !this.tiles[x][y].isSolid();
	}

	public void recreateEntities() {
		this.entityManager = new EntityManager(this.handler);
		this.handler.setEntityManager(this.entityManager);
	}

	public void eatTile(int x, int y) {
		if (x < 0 || y < 0 || x >= this.width || y >= this.height) {
			return;
		}

		if (this.tiles[x][y].isEatable()) {
			// increase game score
			int score = this.tiles[x][y].getScore();
			this.handler.getGame().scorePoints(score);

			// start ghosts vulnerable mode when power food is eaten
			if (this.tiles[x][y] instanceof PowerFoodTile) {
				// todo
			}

			// clear the tile
			this.tiles[x][y] = this.tileManager.getTileByType(0); // empty tile

			 // reduce food counter and create the Fruit object if needed
			this.foodLeft--;
			// todo: create Fruits if enough food has been eaten
		}
	}

	// Board implementation

	private void loadMap(String path) {
		String file = Utils.loadFileAsString(path);
		String[] tokens = file.split("\\s+");
		this.width = Utils.parseInt(tokens[0]);
		this.height = Utils.parseInt(tokens[1]);

		this.tiles = new Tile[this.width][this.height];
		this.foodLeft = 0;

		int tileType;
		for (int y = 0; y < this.height; y++) {
			for (int x = 0; x < this.width; x++) {
				tileType = Utils.parseInt(tokens[2 + (y * this.width + x)]);
				this.tiles[x][y] = this.tileManager.getTileByType(tileType);
				if (this.tiles[x][y].isEatable()) {
					this.foodLeft++;
				}
			}
		}
	}

}
