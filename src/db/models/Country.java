package db.models;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import db.JDBCConnection;

public class Country {

	private static final String DELETE_BY_ID = "UPDATE country SET deleted = 1, updated = 1 WHERE id = ?";
	private static final String UPDATE_AFTER_IMPORT = "UPDATE country SET name = ?, label = ? WHERE id = ? and updated = 0";
	private static final String UPDATE = "UPDATE country SET name = ?, label = ?, updated = 1 WHERE id = ?";
	private static final String SELECT_BY_ORDER = "SELECT country.id, country.yago_id, country.name, country.label, country.updated, country_order.poster_image, country_order.route_name " + 
						"FROM country JOIN country_order ON country_order.country_id = country.id WHERE route_order IS NOT NULL AND country.deleted = 0 AND country_order.deleted = 0 ORDER BY route_order";
	private static final String SELECT_ALL = "SELECT country.id, country.yago_id, country.name, country.label, country.updated " + 
						"FROM country WHERE deleted = 0";
	private static final String SELECT_BY_ID = "SELECT country.id, country.yago_id, country.name, country.label, country.updated " + 
						"FROM country WHERE deleted = 0 AND id = ?";
	private static final String INSERT = "INSERT INTO country(yago_id, name) VALUES(?, ?)";
	private String name;
	private int id;
	private String yagoId;
	private String label;
	private boolean dirty = false;
	private String posterImage;
	private boolean updated;
	private String routeLabel;

	public Country(int id, String yagoId, String name, String label, boolean updated) {

		this.id = id;
		this.name = name;
		this.label = label;
		this.yagoId = yagoId;
		this.updated = updated;
	}

	public static Country parseCountry(String yagoId, String name) {
		return new Country(-1, yagoId, name, null, false);
	}

	public void save() {
		Connection conn;
		try{
			conn = JDBCConnection.getConnection();
			try (PreparedStatement statement = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)){

				statement.setString(1, yagoId);
				statement.setString(2, getName().replace("'", "''"));
				statement.executeUpdate();
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
			try (PreparedStatement statement = conn.prepareStatement(SELECT_BY_ID)){
				statement.setInt(1, id);
				try (ResultSet rs = statement.executeQuery()) {
					while (rs.next() == true) {
						return new Country(rs.getInt("id"), rs.getString("yago_id"), rs.getString("name"), rs.getString("label"), rs.getBoolean("updated"));
					}
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
			try (PreparedStatement statement = conn.prepareStatement(SELECT_ALL)) {
				try (ResultSet rs = statement.executeQuery()) {
					while (rs.next() == true) {
						result.add(new Country(rs.getInt("id"), rs.getString("yago_id"), rs.getString("name"), rs.getString("label"), rs.getBoolean("updated")));
					}
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
			try (PreparedStatement statement = conn.prepareStatement(SELECT_BY_ORDER)) {
				try (ResultSet rs = statement.executeQuery()) {
					while (rs.next() == true) {
						Country country = new Country(rs.getInt("id"), rs.getString("yago_id"), rs.getString("name"), rs.getString("label"), rs.getBoolean("updated"));
						country.posterImage = rs.getString("poster_image");
						country.setRouteLabel(rs.getString("route_name"));
						result.add(country);
					}
				}
			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}

		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
		return result;
	}


	public void update() {
		if (!dirty) return;

		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (PreparedStatement statement = conn.prepareStatement(UPDATE)){
				statement.setString(1, name);
				statement.setString(2, label);
				statement.setInt(3, id);
				statement.executeUpdate();

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
			try (PreparedStatement statement = conn.prepareStatement(UPDATE_AFTER_IMPORT)){
				statement.setString(1, name);
				statement.setString(2, label);
				statement.setInt(3, id);
				statement.executeUpdate();

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
			try (PreparedStatement statement = conn.prepareStatement(DELETE_BY_ID)){
				statement.setInt(1, id);
				statement.executeUpdate();
			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}				
	}


	public void updateFields(Country other) {
		setName(other.getName());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name != null && this.name != null && name.equals(this.name)) return;
		dirty = true;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		if (id == this.id) return;
		dirty = true;
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		if (label != null && this.label != null && label.equals(this.label)) return;
		dirty = true;
		this.label = label;
	}

	public String getPosterImage() {
		return posterImage;
	}

	public boolean isUpdated() {
		return updated;
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}

	public String getRouteLabel() {
		return routeLabel;
	}

	public void setRouteLabel(String routeLabel) {
		this.routeLabel = routeLabel;
	}


}
