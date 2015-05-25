package yago;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;

import db.JDBCConnection;

class YagoCountry {

	String name;

	public YagoCountry(String name) {

		this.name = name;
	}

	public void save() {
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (Statement statement = conn.createStatement()){

				int result = statement.executeUpdate(String.format(""
						+ "INSERT INTO country(name) "
						+ "VALUES('%s')", name));

			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}

	}

}
