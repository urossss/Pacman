package pacman.tiles;

import pacman.graphics.ImageAssets;

public class FoodTile extends Tile {

	public FoodTile(int type) {
		super(ImageAssets.food, type);
	}

	@Override
	public boolean isSolid() {
		return false;
	}

	@Override
	public boolean isEatable() {
		return true;
	}

	@Override
	public int getScore() {
		return FOOD_SCORE;
	}

}
