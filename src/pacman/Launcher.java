package pacman;

import pacman.core.Game;

public class Launcher {

	public static void main(String[] args) {
		new Game("Pacman", 448, 496 + 55 + 30).start();
	}

}
