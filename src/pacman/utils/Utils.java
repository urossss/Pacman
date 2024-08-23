package pacman.utils;

import java.io.*;

public class Utils {

	public static String loadFileAsString(String path) {
		StringBuilder builder = new StringBuilder();

		try {
			InputStream inputStream = Utils.class.getResourceAsStream(path);
			Reader reader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(reader);

			String line;
			while ((line = bufferedReader.readLine()) != null) {
				builder.append(line).append('\n');
			}
			bufferedReader.close();
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
