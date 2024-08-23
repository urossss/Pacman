package pacman.utils;

import java.io.*;

public class Utils {

	public static String loadFileFromClasspathAsString(String path) {
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
			return null;
		}

		return builder.toString();
	}

	public static String loadFileFromDiskAsString(String path) {
		StringBuilder builder = new StringBuilder();

		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(path));

			String line;
			while ((line = bufferedReader.readLine()) != null) {
				builder.append(line).append('\n');
			}
			bufferedReader.close();
		} catch (Exception e) {
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
