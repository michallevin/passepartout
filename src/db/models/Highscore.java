package db.models;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.List;
import java.util.ArrayList;

import db.JDBCConnection;

public class Highscore {
	private Integer id;
	private Integer userId;
	private Integer score;
	private String name;

	public Highscore(Integer userId, Integer score) {
		this.setUserId(userId);
		this.setScore(score);
	}

	public Highscore(Integer id, Integer userId, Integer score) {
		this.setId(id);
		this.setUserId(userId);
		this.setScore(score);
	}

	public void save() {
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (Statement statement = conn.createStatement()) {

				statement.executeUpdate(String.format(""
						+ "INSERT INTO highscore(user_id, score) "
						+ "VALUES(%d, %d)", userId, score),
						Statement.RETURN_GENERATED_KEYS);

				try (ResultSet genKeys = statement.getGeneratedKeys()) {
					if (genKeys.next()) {
						int id = (int) genKeys.getLong(1);
						this.id = id;
					}
				}

			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}

	}

	public static Highscore fetchById(int id) {
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (Statement statement = conn.createStatement();
					ResultSet rs = statement
							.executeQuery(String
									.format("SELECT * FROM highscore JOIN user ON user.id = highscore.user_id WHERE deleted = 0 AND id = %d",
											id));) {
				while (rs.next() == true) {
					Highscore highscore = new Highscore(rs.getInt("user_id"), rs.getInt("score"));
					highscore.setName(rs.getString("name"));
					return highscore;
				}
			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}

		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	public static List<Highscore> fetchAll() {
		List<Highscore> result = new ArrayList<Highscore>();
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (Statement statement = conn.createStatement();
					ResultSet rs = statement
							.executeQuery("SELECT user_id, score FROM highscore WHERE deleted = 0");) {
				while (rs.next() == true) {
					result.add(new Highscore(rs.getInt("user_id"), rs
							.getInt("score")));
				}
			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}

		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
		return result;
	}

	public Integer getUser_id() {
		return userId;
	}

	public void setUserId(Integer user_id) {
		this.userId = user_id;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
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
						+ "UPDATE highscore SET user_id = %d,score = %d updated = 1 WHERE id = %d", userId, score, id));


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
						+ "UPDATE highscore SET deleted = 1, updated = 1 WHERE id = %d", id));


			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}				
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
