package db.models;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;

import db.InputHelper;
import db.JDBCConnection;

public class Link {

	private String entity1;
	private String entity2;

	public Link(String entity1, String entity2) {
		this.entity1 = entity1;
		this.entity2 = entity2;
	}

	public void save() {
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (Statement statement = conn.createStatement()){
				
				statement.executeUpdate(String.format(""
						+ "INSERT INTO link(entity1, entity2) "
						+ "VALUES('%s', '%s')", InputHelper.santize(entity1), InputHelper.santize(entity2)));

			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}

	}

}
