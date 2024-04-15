package pacman.states;

import pacman.core.Handler;

import java.awt.*;

public class MenuState extends State {

	public MenuState(Handler handler) {
		super(handler, false, true, false);
	}

	@Override
	public void startImpl() {
		this.handler.getGame().restartGame();
	}

	@Override
	public void update() {
		if (this.handler.getKeyManager().space) {
			this.handler.getStateManager().startGameState();
		}
	}

	@Override
	public void renderImpl(Graphics g) {

	}
}
