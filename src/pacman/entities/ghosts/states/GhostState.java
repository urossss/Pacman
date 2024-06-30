package pacman.entities.ghosts.states;

import pacman.core.Handler;
import pacman.entities.Creature;
import pacman.entities.ghosts.Ghost;
import pacman.graphics.Animation;
import pacman.graphics.ImageAssets;
import pacman.tiles.Coordinates;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public abstract class GhostState {

	protected Ghost ghost;
	protected Handler handler;

	protected Map<Creature.Direction, Animation> animations = new HashMap<>();

	public GhostState(Ghost ghost, Handler handler) {
		this.ghost = ghost;
		this.handler = handler;

		this.setupAnimations();
	}

	protected void setupAnimations() {
		int id = this.ghost.getGhostId();
		this.animations.put(Creature.Direction.UP, new Animation(50, ImageAssets.ghost_up[id]));
		this.animations.put(Creature.Direction.DOWN, new Animation(50, ImageAssets.ghost_down[id]));
		this.animations.put(Creature.Direction.LEFT, new Animation(50, ImageAssets.ghost_left[id]));
		this.animations.put(Creature.Direction.RIGHT, new Animation(50, ImageAssets.ghost_right[id]));
	}

	public abstract void start();

	public abstract void updateImpl();

	public abstract Coordinates calculateTarget();

	public void update() {
		this.updateImpl();
		this.updateAnimations();
	}

	public void render(Graphics g) {
		g.drawImage(
				this.getCurrentAnimationFrame(),
				(int) this.ghost.getX() - this.ghost.getTileXOffset(),
				(int) this.ghost.getY() - this.ghost.getTileYOffset(),
				this.ghost.getWidth(),
				this.ghost.getHeight(),
				null);
	}

	private void updateAnimations() {
		this.animations.get(this.ghost.getCurrentDirection()).update();
	}

	private BufferedImage getCurrentAnimationFrame() {
		return this.animations.get(this.ghost.getCurrentDirection()).getCurrentFrame();
	}
}
