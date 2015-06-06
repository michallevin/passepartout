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

public class UserFactHistory {

	private int id;
  private int userId;
  private int factId;

	public UserFactHistory(int id, int userId, int factId) {

		this.setId(id);
		this.userId = userId;
    this.factId = factId;
	}

	public UserFactHistory(int userId, int factId) {

		this.userId = userId;
    this.factId = factId;
	}

	
	
	public void save() {
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (Statement statement = conn.createStatement()){

				statement.executeUpdate(String.format(""
						+ "INSERT INTO user_fact_history(user_id, fact_id) "
						+ "VALUES(%d, %d)", getUserId(), getFactId()), Statement.RETURN_GENERATED_KEYS);

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
			try (Statement statement = conn.createStatement();
					ResultSet rs = statement.executeQuery(
					  String.format("SELECT * FROM user_fact_history WHERE deleted = 0 AND id = %d", id));) {
				while (rs.next() == true) {
					return new UserFactHistory(rs.getInt("id"), rs.getInt("user_id"), rs.getInt("factid"));
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
			try (Statement statement = conn.createStatement();
					ResultSet rs = statement.executeQuery("SELECT * FROM user_fact_history WHERE deleted = 0");) {
				while (rs.next() == true) {
					result.add(new UserFactHistory(rs.getInt("id"), rs.getInt("userId"), rs.getInt("factId")));
				}
			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}

		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
		return result;
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



	public void update() {
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (Statement statement = conn.createStatement()){

				statement.executeUpdate(String.format(""
						+ "UPDATE user_fact_history SET user_id = %d, fact_id = %d, updated = 1 WHERE id = %d", userId, factId, id));

				
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
						+ "UPDATE user_fact_history SET deleted = 1, updated = 1 WHERE id = %d", id));

				
			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
	}


}
