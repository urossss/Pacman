package pacman.tiles;

import pacman.graphics.ImageAssets;

public class PowerFoodTile extends Tile {

	public PowerFoodTile(int type) {
		super(ImageAssets.powerFood, type);
	}

	@Override
	public boolean isSolid() {
		return false;
	}

	@Override
	public boolean isEatable() {
		return true;
	}

}
