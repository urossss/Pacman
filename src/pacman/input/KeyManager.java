package pacman.input;

import pacman.entities.Creature.Direction;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

public class KeyManager implements KeyListener {

	private boolean[] keys;

	public Direction direction = Direction.RIGHT;

	public boolean space = false, enter = false, backspace = false;
	public boolean underscore = false, shift = false, minus = false; // works for US keyboard
	public boolean[] letters;
	public boolean[] digits;

	public KeyManager() {
		keys = new boolean[256];
		letters = new boolean[26];
		digits = new boolean[10];
	}

	public void update() {
		if (keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP]) {
			direction = Direction.UP;
		} else if (keys[KeyEvent.VK_S] || keys[KeyEvent.VK_DOWN]) {
			direction = Direction.DOWN;
		} else if (keys[KeyEvent.VK_A] || keys[KeyEvent.VK_LEFT]) {
			direction = Direction.LEFT;
		} else if (keys[KeyEvent.VK_D] || keys[KeyEvent.VK_RIGHT]) {
			direction = Direction.RIGHT;
		}

		space = keys[KeyEvent.VK_SPACE];
		enter = keys[KeyEvent.VK_ENTER];
		backspace = keys[KeyEvent.VK_BACK_SPACE];
		shift = keys[KeyEvent.VK_SHIFT];
		minus = keys[KeyEvent.VK_MINUS];
		underscore = shift && minus;

		for (int i = KeyEvent.VK_0; i <= KeyEvent.VK_9; i++) {
			digits[i - KeyEvent.VK_0] = keys[i];
		}

		letters[0] = keys[KeyEvent.VK_A];
		letters[1] = keys[KeyEvent.VK_B];
		letters[2] = keys[KeyEvent.VK_C];
		letters[3] = keys[KeyEvent.VK_D];
		letters[4] = keys[KeyEvent.VK_E];
		letters[5] = keys[KeyEvent.VK_F];
		letters[6] = keys[KeyEvent.VK_G];
		letters[7] = keys[KeyEvent.VK_H];
		letters[8] = keys[KeyEvent.VK_I];
		letters[9] = keys[KeyEvent.VK_J];
		letters[10] = keys[KeyEvent.VK_K];
		letters[11] = keys[KeyEvent.VK_L];
		letters[12] = keys[KeyEvent.VK_M];
		letters[13] = keys[KeyEvent.VK_N];
		letters[14] = keys[KeyEvent.VK_O];
		letters[15] = keys[KeyEvent.VK_P];
		letters[16] = keys[KeyEvent.VK_Q];
		letters[17] = keys[KeyEvent.VK_R];
		letters[18] = keys[KeyEvent.VK_S];
		letters[19] = keys[KeyEvent.VK_T];
		letters[20] = keys[KeyEvent.VK_U];
		letters[21] = keys[KeyEvent.VK_V];
		letters[22] = keys[KeyEvent.VK_W];
		letters[23] = keys[KeyEvent.VK_X];
		letters[24] = keys[KeyEvent.VK_Y];
		letters[25] = keys[KeyEvent.VK_Z];

		Arrays.fill(keys, false);
		keys[KeyEvent.VK_SHIFT] = shift;
		//keys[KeyEvent.VK_MINUS] = minus;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode >= 0 && keyCode < 256) {
			keys[e.getKeyCode()] = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode >= 0 && keyCode < 256) {
			keys[e.getKeyCode()] = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}
}
