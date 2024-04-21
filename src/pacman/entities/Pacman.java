package pacman.entities;

import pacman.core.Handler;
import pacman.graphics.Animation;
import pacman.graphics.ImageAssets;
import pacman.tiles.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Pacman extends Creature {

	private static final int DEFAULT_PACMAN_STARTING_X = 216, DEFAULT_PACMAN_STARTING_Y = 368;

	private Map<Direction, Animation> animations;

	public Pacman(Handler handler) {
		this(handler, DEFAULT_PACMAN_STARTING_X, DEFAULT_PACMAN_STARTING_Y);
	}

	public Pacman(Handler handler, double x, double y) {
		super(handler, x, y);

		this.animations = new HashMap<>();
		this.animations.put(Direction.UP, new Animation(50, ImageAssets.pacman_up));
		this.animations.put(Direction.DOWN, new Animation(50, ImageAssets.pacman_down));
		this.animations.put(Direction.LEFT, new Animation(50, ImageAssets.pacman_left));
		this.animations.put(Direction.RIGHT, new Animation(50, ImageAssets.pacman_right));
	}

	@Override
	public Direction calculateDesiredDirection() {
		return this.handler.getKeyManager().direction;
	}

	@Override
	public void update() {
		this.updateAnimations();
		this.move();
		// todo: eat
		// todo: check for collision with other entities
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(this.getCurrentAnimationFrame(), (int) this.x - this.tileXOffset, (int) this.y - this.tileYOffset, this.width, this.height, null);
		g.setColor(Color.red);
		g.drawRect((int) this.x, (int) this.y, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
		g.setColor(Color.green);
		g.drawRect(this.getXTile() * Tile.TILE_WIDTH, this.getYTile() * Tile.TILE_HEIGHT, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
	}

	private void updateAnimations() {
		this.animations.get(this.currentDirection).update();
	}

	private BufferedImage getCurrentAnimationFrame() {
		return this.animations.get(this.currentDirection).getCurrentFrame();
	}
}
