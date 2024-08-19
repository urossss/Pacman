package pacman.tiles;

public class TileManager {

	// Flyweight tiles implementation
	private Tile[] tiles;

	public TileManager() {
		tiles = new Tile[]{
			new EmptyTile(0),
			new WallTile(1),
			new FoodTile(2),
			new PowerFoodTile(3),
			new CageDoorTile(4)
		};
	}

	public Tile getTileByType(int type) {
		if (type < 0 || type > tiles.length) {
			return tiles[0];
		}

		return tiles[type];
	}

}
