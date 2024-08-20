package pacman.entities.ghosts.states;

import pacman.core.Handler;
import pacman.entities.Creature;
import pacman.entities.ghosts.Ghost;
import pacman.graphics.Animation;
import pacman.graphics.ImageAssets;
import pacman.tiles.Coordinates;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GhostVulnerableState extends GhostState {

	private Random random;

	protected Map<Creature.Direction, Animation> animations1;
	protected Map<Creature.Direction, Animation> animations2;

	public GhostVulnerableState(Ghost ghost, Handler handler) {
		super(ghost, handler);

		this.random = new Random();
	}

	@Override
	protected void setupAnimations() {
		this.animations1 = new HashMap<>();
		this.animations2 = new HashMap<>();

		this.animations1.put(Creature.Direction.UP, new Animation(50, ImageAssets.ghost_vulnerable_1));
		this.animations1.put(Creature.Direction.DOWN, new Animation(50, ImageAssets.ghost_vulnerable_1));
		this.animations1.put(Creature.Direction.LEFT, new Animation(50, ImageAssets.ghost_vulnerable_1));
		this.animations1.put(Creature.Direction.RIGHT, new Animation(50, ImageAssets.ghost_vulnerable_1));

		this.animations2.put(Creature.Direction.UP, new Animation(50, ImageAssets.ghost_vulnerable_2));
		this.animations2.put(Creature.Direction.DOWN, new Animation(50, ImageAssets.ghost_vulnerable_2));
		this.animations2.put(Creature.Direction.LEFT, new Animation(50, ImageAssets.ghost_vulnerable_2));
		this.animations2.put(Creature.Direction.RIGHT, new Animation(50, ImageAssets.ghost_vulnerable_2));

		this.animations = this.animations1;
	}

	@Override
	public void startImpl() {
		this.ghost.setCanMoveThroughCageDoor(true);
		this.ghost.setSpeed(Ghost.getBaseSpeed() * 0.9);
	}

	@Override
	public void updateImpl() {
		// todo: change animations between 1 and 2
	}

	@Override
	public Coordinates calculateTarget() {
		// if the ghost is inside the cage while vulnerable, just get out asap
		if (this.handler.getBoard().isTileInsideGhostCage(this.ghost.getXTile(), this.ghost.getYTile())) {
			return new Coordinates(14, 11);
		}

		int xTarget = random.nextInt(28);
		int yTarget = random.nextInt(31);
		return new Coordinates(xTarget, yTarget);
	}
}
