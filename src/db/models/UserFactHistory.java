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

public class UserFactHistory {

	private static final String INSERT = "INSERT INTO user_fact_history (user_id, fact_id) VALUES (?, ?)";
	private static final String DELETE_BY_ID = "UPDATE user_fact_history SET deleted = 1, updated = 1 WHERE id = ?";
	private static final String UPDATE_BY_ID = "UPDATE user_fact_history SET user_id = ?, fact_id = ?, updated = 1 WHERE id = ?";
	private static final String SELECT_ALL = "SELECT * FROM user_fact_history WHERE deleted = 0";
	private static final String SELECT_BY_ID = "SELECT * FROM user_fact_history WHERE deleted = 0 AND id = ?";

	private int id;
	private int userId;
	private int factId;

	public UserFactHistory(int id, int userId, int factId) {

		this.setId(id);
		this.userId = userId;
		this.factId = factId;
	}

	public UserFactHistory(int userId, int factId) {

		this.id = -1;
		this.userId = userId;
		this.factId = factId;
	}


	public void save() {
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (PreparedStatement statement = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)){

				statement.setInt(1, getUserId());
				statement.setInt(2, getFactId());
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

	public static UserFactHistory fetchById(int id) {
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (PreparedStatement statement = conn.prepareStatement(SELECT_BY_ID)){
				statement.setInt(1, id);
				try (ResultSet rs = statement.executeQuery()) {
					while (rs.next() == true) {
						return new UserFactHistory(rs.getInt("id"), rs.getInt("user_id"), rs.getInt("factid"));
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

	public static List<UserFactHistory> fetchAll() {
		List<UserFactHistory> result = new ArrayList<UserFactHistory>();
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (PreparedStatement statement = conn.prepareStatement(SELECT_ALL)){
				try (ResultSet rs = statement.executeQuery()) {
					while (rs.next() == true) {
						result.add(new UserFactHistory(rs.getInt("id"), rs.getInt("userId"), rs.getInt("factId")));
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
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (PreparedStatement statement = conn.prepareStatement(UPDATE_BY_ID)){
				statement.setInt(1, userId);
				statement.setInt(2, factId);
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

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getFactId() {
		return factId;
	}

	public void setFactId(int factId) {
		this.factId = factId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


}
