package pacman.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Utils {

	public static String loadFileAsString(String path) {
		StringBuilder builder = new StringBuilder();

		try {
			InputStream inputStream = Utils.class.getResourceAsStream(path);
			if (inputStream == null) { // invalid file path
				return null;
			}

			InputStreamReader inputReader = new InputStreamReader(inputStream);
			BufferedReader br = new BufferedReader(inputReader);

			String line;
			while ((line = br.readLine()) != null) {
				builder.append(line).append('\n');
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return builder.toString();
	}

	public static int parseInt(String number) {
		try {
			return Integer.parseInt(number);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return 0;
		}
	}

}
