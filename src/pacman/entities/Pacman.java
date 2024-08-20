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

	public static final double PACMAN_MAX_SPEED = 2.5;
	private static final int DEFAULT_PACMAN_STARTING_X = 216, DEFAULT_PACMAN_STARTING_Y = 368;

	private Map<Direction, Animation> animations;

	private long timeToClearPowerSpeed = -1;

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
	public boolean canMoveThroughCageDoor() {
		return false;
	}

	@Override
	public void update() {
		this.updateAnimations();

		long currentTime = System.currentTimeMillis();
		if (this.timeToClearPowerSpeed > 0 && currentTime > this.timeToClearPowerSpeed) {
			this.setSpeed(this.handler.getGame().getPacmanRegularSpeed());
		}

		this.move();
		this.handler.getBoard().eatTile(this.getXTile(), this.getYTile());
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(this.getCurrentAnimationFrame(), (int) this.x - this.tileXOffset, (int) this.y - this.tileYOffset, this.width, this.height, null);
	}

	public void setTimeToClearPowerSpeed(long timeToClearPowerSpeed) {
		this.timeToClearPowerSpeed = timeToClearPowerSpeed;
	}

	private void updateAnimations() {
		this.animations.get(this.currentDirection).update();
	}

	private BufferedImage getCurrentAnimationFrame() {
		return this.animations.get(this.currentDirection).getCurrentFrame();
	}
}
