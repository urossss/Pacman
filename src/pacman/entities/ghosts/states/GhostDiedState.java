package pacman.entities.ghosts.states;

import pacman.core.Handler;
import pacman.entities.Creature;
import pacman.entities.ghosts.Ghost;
import pacman.graphics.Animation;
import pacman.graphics.ImageAssets;
import pacman.tiles.Coordinates;
import pacman.tiles.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GhostDiedState extends GhostState {

	private Coordinates diedTarget;

	public GhostDiedState(Ghost ghost, Handler handler) {
		super(ghost, handler);

		this.diedTarget = ghost.getDiedTarget();
	}

	@Override
	protected void setupAnimations() {
		this.animations.put(Creature.Direction.UP, new Animation(50, new BufferedImage[]{ ImageAssets.ghost_died[2] }));
		this.animations.put(Creature.Direction.DOWN, new Animation(50, new BufferedImage[]{ ImageAssets.ghost_died[3] }));
		this.animations.put(Creature.Direction.LEFT, new Animation(50, new BufferedImage[]{ ImageAssets.ghost_died[1] }));
		this.animations.put(Creature.Direction.RIGHT, new Animation(50, new BufferedImage[]{ ImageAssets.ghost_died[0] }));
	}

	@Override
	public void startImpl() {
		this.ghost.setSpeed(this.handler.getGame().getGhostDiedSpeed());
	}

	@Override
	public void updateImpl() {
		if (this.ghost.getXTile() == this.diedTarget.x && this.ghost.getYTile() == this.diedTarget.y) {
			this.ghost.startCageState(0);
			return;
		}
	}

	@Override
	public Coordinates calculateTarget() {
		return this.diedTarget;
	}

	@Override
	protected void renderImpl(Graphics g) {
		g.drawImage(
				ImageAssets.ghost_eaten_points[this.ghost.getDiedGhostIndex()],
				this.ghost.getDiedXTile() * Tile.TILE_WIDTH - 8,
				this.ghost.getDiedYTile()* Tile.TILE_HEIGHT - 8,
				32,
				32,
				null);
	}
}
