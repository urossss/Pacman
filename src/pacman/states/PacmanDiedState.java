package pacman.states;

import pacman.core.Game;
import pacman.core.Handler;
import pacman.graphics.Animation;
import pacman.graphics.ImageAssets;
import pacman.sounds.SoundAssets;

import java.awt.*;

public class PacmanDiedState extends State {

	private final int ANIMATION_SPEED = 100;

	private Animation pacmanDiedAnimation;
	private int animationDuration;
	private long previousTime;
	private long timer;

	public PacmanDiedState(Handler handler) {
		super(handler);
	}

	@Override
	protected void startImpl() {
		this.pacmanDiedAnimation = new Animation(ANIMATION_SPEED, ImageAssets.pacman_eaten);
		this.animationDuration = ANIMATION_SPEED * ImageAssets.pacman_eaten.length;

		this.timer = 0;
		this.previousTime = System.currentTimeMillis();

		SoundAssets.sound_pacman_died.play();
	}

	@Override
	protected void updateImpl() {
		long currentTime = System.currentTimeMillis();
		this.timer += currentTime - this.previousTime;
		this.previousTime = currentTime;

		if (this.timer < this.animationDuration) {
			this.pacmanDiedAnimation.update();
		}

		if (this.timer >= this.animationDuration + 500) {
			if (this.handler.getGame().getLivesLeft() == 0) {
				this.handler.getStateManager().startGameOverState();
			} else {
				// no need to decrease number of lives left because that is done in GameReadyState
				this.handler.getStateManager().startGameReadyState();
			}
		}
	}

	@Override
	protected void renderImpl(Graphics g) {
		this.handler.getBoard().render(g, false);

		g.translate(0, Game.GAME_TOP_SECTION_HEIGHT_PIXELS);
		g.drawImage(this.pacmanDiedAnimation.getCurrentFrame(),
				(int) this.handler.getEntityManager().getPacman().getX() - 8,
				(int) this.handler.getEntityManager().getPacman().getY() - 8,
				32, 32, null);
		g.translate(0, -Game.GAME_TOP_SECTION_HEIGHT_PIXELS);
	}
}
