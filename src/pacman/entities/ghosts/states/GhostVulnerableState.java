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

	private long previousTime;
	private long timer;
	private int animationSwitchStartTime;

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
		this.ghost.setSpeed(this.handler.getGame().getGhostVulnerableSpeed());

		this.previousTime = System.currentTimeMillis();
		this.timer = 0;
		this.animationSwitchStartTime = this.handler.getGame().getGhostVulnerableStateDurationMillis() - 3000;

		this.animations = this.animations1;

		Ghost.increaseVulnerableGhostsCount();
	}

	@Override
	public void updateImpl() {
		long currentTime = System.currentTimeMillis();
		this.timer += currentTime - this.previousTime;
		this.previousTime = currentTime;

		if (this.timer >= this.animationSwitchStartTime) {
			this.animations = this.timer % 200 < 100 ? this.animations2 : this.animations1;
		}
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
