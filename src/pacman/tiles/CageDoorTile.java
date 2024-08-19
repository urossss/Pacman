package pacman.tiles;

public class CageDoorTile extends Tile {

	public CageDoorTile(int type) {
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

	@Override
	public boolean isCageDoor() {
		return true;
	}
}
