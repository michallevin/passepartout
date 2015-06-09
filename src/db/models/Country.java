package db.models;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import db.InputHelper;
import db.JDBCConnection;

public class Country {

	private String name;
	private int id;
	private String yagoId;
	private String label;
	private boolean dirty = false;

	public Country(int id, String yagoId, String name, String label) {

		this.id = id;
		this.name = name;
		this.label = label;
		this.yagoId = yagoId;
	}

	public Country(String yagoId, String name) {

		this.name = name;
		this.yagoId = yagoId;
	}


	public Country(String name) {
		this.name = name;
		this.yagoId = "";
	}
	
	public void save() {
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (Statement statement = conn.createStatement()){

				statement.executeUpdate(String.format(""
						+ "INSERT INTO country(yago_id, name) "
						+ "VALUES('%s', '%s')", yagoId, getName().replace("'", "''")), Statement.RETURN_GENERATED_KEYS);

				try (ResultSet genKeys = statement.getGeneratedKeys()) {
					if (genKeys.next()) {
						int id = (int) genKeys.getLong(1);
						this.setId(id);
					}
				}
				

			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
		dirty = false;


	}
	
	public static Country fetchById(int id) {
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (Statement statement = conn.createStatement();
					ResultSet rs = statement.executeQuery(String.format("SELECT * FROM country WHERE deleted = 0 AND id = %d", id));) {
				while (rs.next() == true) {
					return new Country(rs.getInt("id"), rs.getString("yago_id"), rs.getString("name"), rs.getString("label"));
				}
			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}

		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
		return null;
	}
	
	public static List<Country> fetchAll() {
		List<Country> result = new ArrayList<Country>();
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (Statement statement = conn.createStatement();
					ResultSet rs = statement.executeQuery("SELECT * FROM country WHERE deleted = 0");) {
				while (rs.next() == true) {
					result.add(new Country(rs.getInt("id"), rs.getString("yago_id"), rs.getString("name"), rs.getString("label")));
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
					ResultSet rs = statement.executeQuery("SELECT * FROM country JOIN country_order ON country_order.country_id = country.id WHERE route_order IS NOT NULL AND deleted = 0 ORDER BY route_order");) {
				while (rs.next() == true) {
					result.add(new Country(rs.getInt("id"), rs.getString("yago_id"), rs.getString("name"), rs.getString("label")));
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
		dirty = true;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		dirty = true;
		this.id = id;
	}



	public void update() {
		if (!dirty) return;

		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (Statement statement = conn.createStatement()){

				statement.executeUpdate(String.format(""
						+ "UPDATE country SET name = '%s', label = '%s', updated = 1 WHERE id = %d", InputHelper.santize(name), InputHelper.santize(label), id));

				
			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}		
		dirty = false;

	}

	public void updateFromImport() {
		if (!dirty) return;
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (Statement statement = conn.createStatement()){

				statement.executeUpdate(String.format(""
						+ "UPDATE country SET name = '%s', label = '%s' WHERE id = %d and updated = 0",
						InputHelper.santize(name), InputHelper.santize(label), id));

			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
		dirty = false;

	}


	
	
	public void delete() {
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (Statement statement = conn.createStatement()){

				statement.executeUpdate(String.format(""
						+ "UPDATE country SET deleted = 1, updated = 1 WHERE id = %d", InputHelper.santize(name), id));

				
			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}				
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		dirty = true;
		this.label = label;
	}


}
