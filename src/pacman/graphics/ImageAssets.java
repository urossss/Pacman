package pacman.graphics;

import pacman.graphics.utils.ImageLoader;
import pacman.graphics.utils.SpriteSheet;

import java.awt.image.BufferedImage;

public class ImageAssets {

	private static final int DIGIT_SIZE = 9, LETTER_SIZE = 9, GHOST_SIZE = 16, PACMAN_SIZE = 16;

	public static BufferedImage title;
	public static BufferedImage[] digits, letters;
	public static BufferedImage[][] ghost_up, ghost_down, ghost_left, ghost_right;
	public static BufferedImage[] ghost_scared_1, ghost_scared_2, ghost_eaten;
	public static BufferedImage[] pacman_up, pacman_down, pacman_left, pacman_right, pacman_eaten;
	public static BufferedImage map1, map2, food, powerFood;
	public static BufferedImage underscore;

	public static void init() {
		SpriteSheet sheet = new SpriteSheet(ImageLoader.loadImage("/res/textures/sheet.png"));

		title = sheet.crop(0, 0, 182, 46);

		letters = loadArray(sheet, 0, 293, 26, LETTER_SIZE);
		digits = loadArray(sheet, 0, 302, 10, DIGIT_SIZE);
		underscore = sheet.crop(234, 294, 8, 8);

		ghost_right = new BufferedImage[4][2];
		for (int i = 0; i < 4; i++) {
			ghost_right[i] = loadArray(sheet, 0, 124 + i * GHOST_SIZE, 2, GHOST_SIZE);
		}

		ghost_left = new BufferedImage[4][2];
		for (int i = 0; i < 4; i++) {
			ghost_left[i] = loadArray(sheet, GHOST_SIZE * 2, 124 + i * GHOST_SIZE, 2, GHOST_SIZE);
		}

		ghost_up = new BufferedImage[4][2];
		for (int i = 0; i < 4; i++) {
			ghost_up[i] = loadArray(sheet, GHOST_SIZE * 4, 124 + i * GHOST_SIZE, 2, GHOST_SIZE);
		}

		ghost_down = new BufferedImage[4][2];
		for (int i = 0; i < 4; i++) {
			ghost_down[i] = loadArray(sheet, GHOST_SIZE * 6, 124 + i * GHOST_SIZE, 2, GHOST_SIZE);
		}

		ghost_scared_1 = loadArray(sheet, 0, 124 + GHOST_SIZE * 4, 2, GHOST_SIZE);
		ghost_scared_2 = loadArray(sheet, GHOST_SIZE * 2, 124 + GHOST_SIZE * 4, 2, GHOST_SIZE);
		ghost_eaten = loadArray(sheet, GHOST_SIZE * 4, 124 + GHOST_SIZE * 4, 4, GHOST_SIZE);

		pacman_eaten = loadArray(sheet, 0, 258, 16, PACMAN_SIZE);
		pacman_right = loadArray(sheet, 0, 89, 4, PACMAN_SIZE);
		pacman_left = loadArray(sheet, 0, 105, 4, PACMAN_SIZE);
		pacman_up = loadArray(sheet, 64, 89, 4, PACMAN_SIZE);
		pacman_down = loadArray(sheet, 64, 105, 4, PACMAN_SIZE);

		map1 = sheet.crop(202, 0, 224, 248);
		map2 = sheet.crop(432, 0, 224, 248);

		food = sheet.crop(0, 66, 8, 8);
		powerFood = sheet.crop(8, 66, 18, 18);
	}

	private static BufferedImage[] loadArray(SpriteSheet sheet, int x, int y, int n, int size) {
		BufferedImage[] arr = new BufferedImage[n];

		for (int i = 0; i < n; i++) {
			arr[i] = sheet.crop(x + i * size, y, size, size);
		}

		return arr;
	}
}
