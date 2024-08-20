package pacman.tiles;

public class CageInsideTile extends Tile {

	public CageInsideTile(int type) {
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
