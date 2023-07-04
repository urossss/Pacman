package pacman.graphics.utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageLoader {

	public static BufferedImage loadImage(String path) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(ImageLoader.class.getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return image;
	}

}
