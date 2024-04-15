package pacman.graphics;

import pacman.graphics.utils.ImageLoader;
import pacman.graphics.utils.SpriteSheet;

import java.awt.image.BufferedImage;

public class ImageAssets {

	private static final int WIDTH = 8, HEIGHT = 8;

	private static final int DIGIT_SIZE = 9, LETTER_SIZE = 9, GHOST_SIZE = 16;

	public static BufferedImage title;
	public static BufferedImage[] digits, letters;
	public static BufferedImage[] ghost_red, ghost_pink, ghost_blue, ghost_orange, ghost_scared, ghost_eaten;
	public static BufferedImage map1, map2, food, powerFood;
	public static BufferedImage underscore;

	public static void init() {
		SpriteSheet sheet = new SpriteSheet(ImageLoader.loadImage("/res/textures/sheet.png"));

		title = sheet.crop(0, 0, 182, 46);

		letters = loadArray(sheet, 0, 293, 26, LETTER_SIZE);
		digits = loadArray(sheet, 0, 302, 10, DIGIT_SIZE);
		underscore = sheet.crop(234, 294, 8, 8);

		ghost_red = loadArray(sheet, 0, 124, 8, GHOST_SIZE);
		ghost_pink = loadArray(sheet, 0, 124 + GHOST_SIZE, 8, GHOST_SIZE);
		ghost_blue = loadArray(sheet, 0, 124 + GHOST_SIZE * 2, 8, GHOST_SIZE);
		ghost_orange = loadArray(sheet, 0, 124 + GHOST_SIZE * 3, 8, GHOST_SIZE);
		ghost_scared = loadArray(sheet, 0, 124 + GHOST_SIZE * 4, 4, GHOST_SIZE);
		ghost_eaten = loadArray(sheet, GHOST_SIZE * 4, 124 + GHOST_SIZE * 4, 4, GHOST_SIZE);

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
