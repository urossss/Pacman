package pacman.sounds;

public class SoundAssets {

	public static boolean SoundsLoaded = false;

	public static Sound
			sound_beginning, sound_eat, sound_pacman_died,
			sound_ghost_died, sound_intermission, sound_extra_life,
			sound_ghost_chase, sound_pacman_chase, sound_ghost_return;
	private static Sound[] allSounds;

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

		allSounds = new Sound[]{
				sound_beginning,
				sound_pacman_died,
				sound_ghost_died,
				sound_intermission,
				sound_extra_life,
				sound_eat,
				sound_ghost_chase,
				sound_ghost_return,
				sound_pacman_chase
		};

		SoundsLoaded = true;
	}

	public static void setSoundsVolume(int volumeLevel) { // accepts volume level 0, 1, 2 and 3
		if (!SoundsLoaded) {
			return;
		}
		if (volumeLevel < 0 || volumeLevel > 3) {
			return;
		}

		float volume = volumeLevel / 3.0f; // convert volume to float in range from 0 to 1
		for (Sound sound : allSounds) {
			new Thread(() -> {
				sound.setVolume(volume);
			}).start();
		}
	}
}
