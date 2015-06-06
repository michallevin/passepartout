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

public class CountryOrder {

	private int id;
	private int countryId;
	private int routeOrder;

	public CountryOrder(int id, int countryId, int routeOrder) {

		this.setId(id);
		this.countryId = countryId;
		this.routeOrder = routeOrder;
	}
	
	public CountryOrder(int countryId, int routeOrder) {

		this.countryId = countryId;
		this.routeOrder = routeOrder;
	}

	
	
	public void save() {
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (Statement statement = conn.createStatement()){

				statement.executeUpdate(String.format(""
						+ "INSERT INTO country_order(country_id, route_order) "
						+ "VALUES(%d, %d)", countryId, routeOrder), Statement.RETURN_GENERATED_KEYS);

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
			try (Statement statement = conn.createStatement();
					ResultSet rs = statement.executeQuery(String.format("SELECT * FROM country_order WHERE deleted = 0 AND id = %d", id));) {
				while (rs.next() == true) {
					return new CountryOrder(rs.getInt("id"), rs.getInt("country_id"), rs.getInt("route_order"));
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
			try (Statement statement = conn.createStatement();
					ResultSet rs = statement.executeQuery("SELECT * FROM country_order WHERE deleted = 0");) {
				while (rs.next() == true) {
					result.add(new CountryOrder(rs.getInt("id"), rs.getInt("country_id"), rs.getInt("route_order")));
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
			try (Statement statement = conn.createStatement()){

				statement.executeUpdate(String.format(""
						+ "UPDATE country_order SET country_id = %d, route_order = %d, updated = 1 WHERE id = %d",
						countryId, routeOrder, id));

				
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
			try (Statement statement = conn.createStatement()){

				statement.executeUpdate(String.format(""
						+ "UPDATE country_order SET deleted = 1, updated = 1 WHERE id = %d", id));

				
			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
	}


}
