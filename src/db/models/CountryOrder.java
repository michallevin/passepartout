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

public class CountryOrder {

	private static final String DELETE = "UPDATE country_order SET deleted = 1, updated = 1 WHERE id = ?";
	private static final String UPDATE_BY_ID = "UPDATE country_order SET route_name = ?, country_id = ?, route_order = ?, updated = 1 WHERE id = ?";
	private static final String SELECT_ALL = "SELECT country_order.id, country_order.country_id "
			+ "country_order.route_order, country_order.poster_image, "
			+ "country_order.route_name "
			+ "FROM country_order WHERE deleted = 0";
	private static final String SELECT_BY_ID = "SELECT country_order.id, country_order.country_id "
			+ "country_order.route_order, country_order.poster_image, "
			+ "country_order.route_name "
			+ "FROM country_order WHERE deleted = 0 AND id = ?";
	private static final String INSERT = "INSERT INTO country_order(country_id, route_order, poster_image, route_name) VALUES(?, ?, ?, ?)";
	private int id;
	private int countryId;
	private int routeOrder;
	private String routeName;
	private String posterImage;

	public CountryOrder(int id, int countryId, int routeOrder,
			String posterImage, String routeName) {

		this.setId(id);
		this.countryId = countryId;
		this.routeOrder = routeOrder;
		this.setPosterImage(posterImage);
		this.setName(routeName);
	}

	public CountryOrder(int countryId, int routeOrder, String posterImage,
			String routeName) {

		this.countryId = countryId;
		this.routeOrder = routeOrder;
		this.setPosterImage(posterImage);
		this.setName(routeName);

	}

	public void save() {
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (PreparedStatement statement = conn.prepareStatement(INSERT,
					Statement.RETURN_GENERATED_KEYS)) {

				statement.setInt(1, countryId);
				statement.setInt(2, routeOrder);
				statement.setString(3, posterImage);
				statement.setString(4, getRouteName());
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

	}

	public static CountryOrder fetchById(int id) {
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (PreparedStatement statement = conn
					.prepareStatement(SELECT_BY_ID)) {
				statement.setInt(1, id);
				try (ResultSet rs = statement.executeQuery()) {
					while (rs.next() == true) {
						return new CountryOrder(rs.getInt("id"),
								rs.getInt("country_id"),
								rs.getInt("route_order"),
								rs.getString("poster_image"),
								rs.getString("route_name"));
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

	public static List<CountryOrder> fetchAll() {
		List<CountryOrder> result = new ArrayList<CountryOrder>();
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (PreparedStatement statement = conn
					.prepareStatement(SELECT_ALL)) {
				try (ResultSet rs = statement.executeQuery()) {
					while (rs.next() == true) {
						result.add(new CountryOrder(rs.getInt("id"), rs
								.getInt("country_id"),
								rs.getInt("route_order"), rs
										.getString("poster_image"), rs
										.getString("route_name")));
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

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public int getRouteOrder() {
		return routeOrder;
	}

	public void setRouteOrder(int routeOrder) {
		this.routeOrder = routeOrder;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void update() {
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (PreparedStatement statement = conn
					.prepareStatement(UPDATE_BY_ID)) {
				statement.setInt(1, countryId);
				statement.setInt(2, routeOrder);
				statement.setInt(3, id);
				statement.executeUpdate();

			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
	}

	public void delete() {
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (PreparedStatement statement = conn.prepareStatement(DELETE)) {
				statement.setInt(1, id);
				statement.executeUpdate();
			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
	}

	public String getPosterImage() {
		return posterImage;
	}

	public void setPosterImage(String posterImage) {
		this.posterImage = posterImage;
	}

	public void replace() {
		// TODO Auto-generated method stub

	}

	public String getRouteName() {
		return routeName;
	}

	public void setName(String name) {
		this.routeName = name;
	}

}
