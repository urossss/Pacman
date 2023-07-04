package pacman.tiles;

public class WallTile extends Tile {

	public WallTile(int type) {
		super(null, type);
	}

	@Override
	public boolean isSolid() {
		return true;
	}

	@Override
	public boolean isEatable() {
		return false;
	}

}
