package db.models;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.List;
import java.util.ArrayList;

import db.JDBCConnection;

public class Highscore {
	private static final String DELETE_BY_ID = "UPDATE highscore SET deleted = 1, updated = 1 WHERE id = ?";
	private static final String UPDATE_BY_ID = "UPDATE highscore SET user_id = ?, score = ?, updated = 1 WHERE id = ?";
	private static final String SELECT_ALL = "SELECT id, user_id, score FROM highscore WHERE deleted = 0 LIMIT ?, ?";
	private static final String SELECT_TOP = "SELECT highscore.id, highscore.user_id, highscore.score, user.name FROM highscore"
			+ " JOIN user on highscore.user_id = user.id"
			+ " WHERE user.deleted = 0 AND highscore.deleted = 0 ORDER BY score	DESC LIMIT ?";
	private static final String SELECT_BY_ID = "SELECT id, user_id, score FROM highscore WHERE deleted = 0 and id = ?";
	private static final String INSERT = "INSERT INTO highscore (user_id, score) VALUES (?, ?)";
	private static final String SELECT_COUNT = "SELECT count(1) as row_count FROM highscore";

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
			try (PreparedStatement statement = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)){

				statement.setInt(1, userId);
				statement.setInt(2, score);
				statement.executeUpdate();

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
			try (PreparedStatement statement = conn.prepareStatement(SELECT_BY_ID)){
				statement.setInt(1, id);
				try (ResultSet rs = statement.executeQuery()) {
					while (rs.next() == true) {
						Highscore highscore = new Highscore(rs.getInt("id"), rs.getInt("user_id"), rs.getInt("score"));
						return highscore;
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
	
		public static List<Highscore> fetchTop(int scoresCount) {
		List<Highscore> result = new ArrayList<Highscore>();
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (PreparedStatement statement = conn.prepareStatement(SELECT_TOP)){
				statement.setInt(1, scoresCount);
				try (ResultSet rs = statement.executeQuery()) {
					while (rs.next() == true) {
						Highscore highscore = new Highscore(rs.getInt("id"), rs.getInt("user_id"), rs.getInt("score"));
						highscore.setName(rs.getString("name"));
						result.add(highscore);
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

	public static List<Highscore> fetchAll(int start, int end) {
		List<Highscore> result = new ArrayList<Highscore>();
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (PreparedStatement statement = conn.prepareStatement(SELECT_ALL)){
				statement.setInt(1, start);
				statement.setInt(2, end - start);
				try (ResultSet rs = statement.executeQuery()) {
					while (rs.next() == true) {
						result.add(new Highscore(rs.getInt("id"), rs.getInt("user_id"), rs.getInt("score")));
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
	
	public static Integer getCount() {
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (PreparedStatement statement = conn
					.prepareStatement(SELECT_COUNT)) {
				try (ResultSet rs = statement.executeQuery()) {
					while (rs.next() == true) {
						return rs.getInt("row_count");
					}
				}
			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}

		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
		return 0;	
	}

	public void update() {
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (PreparedStatement statement = conn.prepareStatement(UPDATE_BY_ID)){
				statement.setInt(1, userId);
				statement.setInt(2, score);
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
	
	
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
