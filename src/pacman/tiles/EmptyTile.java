package pacman.tiles;

public class EmptyTile extends Tile {

	public EmptyTile(int type) {
		super(null, type);
	}

	@Override
	public boolean isSolid() {
		return false;
	}

	@Override
	public boolean isEatable() {
		return false;
	}

}
