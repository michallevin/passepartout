package db;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;

public class JDBCConnection {
	private JDBCConnection() {
		conn = null;
	}

	private static Connection conn; // DB connection
	
	public static Connection getConnection() throws FileNotFoundException, IOException, ParseException{
		if (conn != null){
			return conn;
		}
		else if (JDBCConnection.openConnection())
			return conn;
		
		return null;
	}
	
	private static boolean openConnection() throws FileNotFoundException, IOException, ParseException {

		// loading the driver
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Unable to load the MySQL JDBC driver..");
			return false;
		}


		// creating the connection
		//System.out.print("Trying to connect... ");
		
		try {
			
			Configuration dbConfig = Configuration.get();
			conn = DriverManager.getConnection(
					dbConfig.getConnectionURL(), dbConfig.getUserName(), dbConfig.getPassword()
					);
			
		} catch (SQLException e) {
			System.out.println("Unable to connect - " + e.getMessage());
			conn = null;
			return false;
		}
		System.out.println("created database connection");
		return true;
	}
	
	public static void closeConnection() {
		// closing the connection
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println("Unable to close the connection - "
					+ e.getMessage());
		}

	}
	
}