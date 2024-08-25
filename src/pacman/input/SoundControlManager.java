package pacman.input;

import pacman.core.Game;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SoundControlManager implements MouseListener {

	private int soundVolume = 3;

	public int getSoundVolume() {
		return this.soundVolume;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getX() >= Game.GAME_BOARD_WIDTH_PIXELS - 28
				&& e.getX() < Game.GAME_BOARD_WIDTH_PIXELS - 4
				&& e.getY() >= Game.GAME_BOARD_HEIGHT_PIXELS + Game.GAME_TOP_SECTION_HEIGHT_PIXELS + 4
				&& e.getY() < Game.GAME_BOARD_HEIGHT_PIXELS + Game.GAME_TOP_SECTION_HEIGHT_PIXELS + 28) {
			this.soundVolume = (this.soundVolume + 1) % 4;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// Empty
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// Empty
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// Empty
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// Empty
	}
}
