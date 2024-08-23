package pacman.core;

import pacman.entities.Creature.Direction;
import pacman.entities.EntityManager;
import pacman.graphics.ImageAssets;
import pacman.tiles.*;
import pacman.utils.Utils;

import java.awt.*;

public class Board {

	private static final String DefaultMapPath = "/maps/map.txt";

	private int width, height;
	private Tile[][] tiles;

	private TileManager tileManager = new TileManager();

	private Handler handler;
	private EntityManager entityManager;

	private int foodLeft, foodEaten;

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

	public void render(Graphics g, boolean shouldRenderEntities) {
		g.translate(0, Game.GAME_TOP_SECTION_HEIGHT_PIXELS);

		// Background
		g.drawImage(ImageAssets.map1, 0, 0, width * Tile.TILE_WIDTH, height * Tile.TILE_HEIGHT, null);

		// Map
		for (int y = 0; y < this.height; y++) {
			for (int x = 0; x < this.width; x++) {
				this.tiles[x][y].render(g, x * Tile.TILE_WIDTH, y * Tile.TILE_HEIGHT);
			}
		}

		// Entities
		if (shouldRenderEntities) {
			this.entityManager.render(g);
		}

		g.translate(0, -Game.GAME_TOP_SECTION_HEIGHT_PIXELS);
	}

	// Public interface

	public Coordinates getAdjacentTileCoordinates(int x, int y, Direction direction, boolean canMoveThroughCageDoor) {
		int xTarget = x;
		int yTarget = y;

		if (direction != null) {
			switch (direction) {
				case UP:
					yTarget--;
					break;
				case DOWN:
					yTarget++;
					break;
				case LEFT:
					xTarget--;
					break;
				case RIGHT:
					xTarget++;
					break;
			}
		}

		if (xTarget < 0 || yTarget < 0 || xTarget >= this.width || yTarget >= this.height) {
			while (xTarget < 0) {
				xTarget += this.width;
			}
			while (xTarget >= this.width) {
				xTarget -= this.width;
			}
			while (yTarget < 0) {
				yTarget += this.height;
			}
			while (yTarget >= this.height) {
				yTarget -= this.height;
			}
		}

		if (this.tiles[xTarget][yTarget].isCageDoor() && canMoveThroughCageDoor) {
			return new Coordinates(xTarget, yTarget);
		}

		if (this.tiles[xTarget][yTarget].isSolid()) {
			return null;
		}

		return new Coordinates(xTarget, yTarget);
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
				this.handler.getGame().powerPelletEaten();
			}

			// clear the tile
			this.tiles[x][y] = this.tileManager.getTileByType(0); // empty tile

			 // reduce food counter and create the Fruit object if needed
			this.foodLeft--;
			this.foodEaten++;
		}
	}

	public boolean isTileInsideGhostCage(int x, int y) {
		if (x < 0 || y < 0 || x >= this.width || y >= this.height) {
			return false;
		}
		return this.tiles[x][y] instanceof CageInsideTile;
	}

	public boolean isTileInsideTunnel(int x, int y) {
		return y == 14 && (x <= 5 || x >= 22);
	}

	public boolean isCompleted() {
		return this.foodLeft == 0;
	}

	// Board implementation

	private void loadMap(String path) {
		String file = Utils.loadFileAsString(path);
		String[] tokens = file.split("\\s+");
		this.width = Utils.parseInt(tokens[0]);
		this.height = Utils.parseInt(tokens[1]);

		this.tiles = new Tile[this.width][this.height];
		this.foodLeft = 0;
		this.foodEaten = 0;

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
