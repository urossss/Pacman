package pacman.graphics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class TextRenderer {

	private static final int
			DEFAULT_CHAR_WIDTH = 20,
			DEFAULT_CHAR_HEIGHT = 20,
			DEFAULT_DIGIT_WIDTH = 20,
			DEFAULT_DIGIT_HEIGHT = 20,
			DEFAULT_HORIZONTAL_SPACING = 0,
			DEFAULT_VERTICAL_SPACING = 0;

	public static void drawText(Graphics g, String text, int x, int y) {
		drawText(g, text, x, y, DEFAULT_CHAR_WIDTH, DEFAULT_CHAR_HEIGHT, DEFAULT_HORIZONTAL_SPACING, DEFAULT_VERTICAL_SPACING);
	}

	public static void drawText(Graphics g, String text, int x, int y, int charSize) {
		drawText(g, text, x, y, charSize, charSize, DEFAULT_HORIZONTAL_SPACING, DEFAULT_VERTICAL_SPACING);
	}

	public static void drawText(
			Graphics g,
			String text,
			int x,
			int y,
			int charWidth,
			int charHeight,
			int horizontalSpacing,
			int verticalSpacing) {
		text = text.toUpperCase();
		int dx = 0, dy = 0;

		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);

			if (c == '\n') {
				dx = 0;
				dy += charHeight + verticalSpacing;
				continue;
			} else if (c == ' ') {
				dx += charWidth + horizontalSpacing;
				continue;
			}

			BufferedImage image = null;

			if (c >= 'A' && c <= 'Z') { // letters
				image = ImageAssets.letters[c - 'A'];
			} else if (c >= '0' && c <= '9') { // digits
				image = ImageAssets.digits[c - '0'];
			} else { // special characters
				if (c == '_') {
					image = ImageAssets.underscore;
				}
			}

			g.drawImage(image, dx + x, dy + y, charWidth, charHeight, null);
			dx += charWidth + horizontalSpacing;
		}
	}

	public static void drawInteger(Graphics g, int num, int x, int y) {
		drawInteger(g, num, x, y, DEFAULT_DIGIT_WIDTH, DEFAULT_DIGIT_HEIGHT, DEFAULT_HORIZONTAL_SPACING);
	}

	public static void drawInteger(Graphics g, int num, int x, int y, int digitSize) {
		drawInteger(g, num, x, y, digitSize, digitSize, DEFAULT_HORIZONTAL_SPACING);
	}

	// Draws correctly numbers from 0 to 999999
	public static void drawInteger(Graphics g, int num, int x, int y, int digitWidth, int digitHeight, int horizontalSpacing) {
		String text = Integer.toString(num);
		if (text.length() < 6) {
			text = "000000" + text;
		}
		if (text.length() > 6) {
			text = text.substring(text.length() - 6);
		}

		for (int i = 0, dx = 0; i < text.length(); i++, dx += digitWidth + horizontalSpacing) {
			g.drawImage(ImageAssets.digits[text.charAt(i) - '0'], dx + x, y, digitWidth, digitHeight, null);
		}
	}
}
