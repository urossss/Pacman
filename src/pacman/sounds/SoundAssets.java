package pacman.sounds;

public class SoundAssets {

	public static boolean SoundsLoaded = false;

	public static Sound
			sound_beginning, sound_eat, sound_pacman_died,
			sound_ghost_died, sound_intermission, sound_extra_life,
			sound_ghost_chase, sound_pacman_chase, sound_ghost_return;

	public static void init() {
		sound_beginning = new Sound("beginning");
		sound_pacman_died = new Sound("pacman_died");
		sound_ghost_died = new Sound("ghost_died");
		sound_intermission = new Sound("intermission");
		sound_extra_life = new Sound("extra_life");
		sound_eat = new Sound("pacman_eat");
		sound_ghost_chase = new Sound("ghost_chase");
		sound_ghost_return = new Sound("ghost_return");
		sound_pacman_chase = new Sound("pacman_chase");

		SoundsLoaded = true;
	}
}
