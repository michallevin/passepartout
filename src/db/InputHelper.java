package db;

public class InputHelper {
	public static String santize(String str) {
		if (str == null) return null;
		return str.replace("'", "''");
	}
}
