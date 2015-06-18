package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configuration {

	public static String hostName = "localhost";
	public static String port = "3306";
	public static String dbName = "passepartout";
	public static String protocol = "jdbc:mysql";
	private static String userName = "root";
	private static String password = "";

	public static String getConnectionURL(){
		return String.format("%s://%s:%s/%s", protocol, hostName, port, dbName);
	}


	public static void load(String fileName) {
		try (FileInputStream input = new FileInputStream(fileName)) {

			// load a properties file
			Properties prop = new Properties();
			prop.load(input);

			// get the property value and print it out
			Configuration.protocol = prop.getProperty("protocol", "jdbc:mysql");
			Configuration.hostName = prop.getProperty("hostname", "localhost");
			Configuration.port = prop.getProperty("port", "3306");
			Configuration.dbName = prop.getProperty("database", "passepartout");
			Configuration.userName = prop.getProperty("user", "root");
			Configuration.password = prop.getProperty("password", "");


		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}


	public static String getUserName() {
		return userName;
	}

	public static String getPassword() {
		return password;
	}

}
