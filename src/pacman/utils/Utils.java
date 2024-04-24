package pacman.utils;

import java.io.*;

public class Utils {

	public static String loadFileAsString(String path) {
		StringBuilder builder = new StringBuilder();

		try {
			BufferedReader br = new BufferedReader(new FileReader(path));

			String line;
			while ((line = br.readLine()) != null) {
				builder.append(line).append('\n');
			}
			br.close();
		} catch (Exception e) {
//			e.printStackTrace();
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
