package pacman.sounds;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {

	private Clip clip;

	public Sound(String fileName) {
		fileName = "/sounds/" + fileName + ".wav";
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(Sound.class.getResource(fileName));
			clip = AudioSystem.getClip();
			clip.open(ais);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void play() {
		try {
			if (clip != null) {
				new Thread(() -> {
					synchronized (clip) {
						clip.stop();
						clip.setFramePosition(0);
						clip.start();
					}
				}).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		if (clip == null) {
			return;
		}
		clip.stop();
	}

	public void loop() {
		try {
			if (clip != null) {
				new Thread(() -> {
					synchronized (clip) {
						clip.stop();
						clip.setFramePosition(0);
						clip.loop(Clip.LOOP_CONTINUOUSLY);
					}
				}).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isActive() {
		return clip.isActive();
	}

	public void setVolume(float volume) {
		if (clip == null) {
			return;
		}
		if (volume < 0 || volume > 1) {
			return;
		}

		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		float gain = 20f * (float) Math.log10(volume);
		gainControl.setValue(gain);
	}
}
