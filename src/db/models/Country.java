package db.models;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import db.JDBCConnection;

public class Country {

	private String name;
	private int id;

	public Country(int id, String name) {

		this.setId(id);
		this.setName(name);
	}

	public void save() {
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (Statement statement = conn.createStatement()){

				statement.executeUpdate(String.format(""
						+ "INSERT INTO country(name) "
						+ "VALUES('%s')", getName().replace("'", "''")));

			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}

	}
	
	public static List<Country> fetchAll() {
		List<Country> result = new ArrayList<Country>();
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (Statement statement = conn.createStatement();
					ResultSet rs = statement.executeQuery("SELECT * FROM country");) {
				while (rs.next() == true) {
					result.add(new Country(rs.getInt("id"), rs.getString("name")));
				}
			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}

		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
		return result;
	}

	public static List<Country> fetchByOrder() {
		List<Country> result = new ArrayList<Country>();
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (Statement statement = conn.createStatement();
					ResultSet rs = statement.executeQuery("SELECT * FROM country WHERE route_order IS NOT NULL ORDER BY route_order");) {
				while (rs.next() == true) {
					result.add(new Country(rs.getInt("id"), rs.getString("name")));
				}
			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}

		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
		return result;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
