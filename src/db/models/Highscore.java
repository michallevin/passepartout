package db.models;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.List;
import java.util.ArrayList;

import db.InputHelper;
import db.JDBCConnection;

public class Highscore {
	private Integer id;
	private Integer user_id;
	private Integer score;

	public Highscore(Integer user_id, Integer score) {
		this.user_id = user_id;
		this.score = score;
	}
	
	public void save() {
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (Statement statement = conn.createStatement()){

				statement.executeUpdate(String.format(""
						+ "INSERT INTO highscore(user_id, score) "
						+ "VALUES(%d, %d)", user_id, score), Statement.RETURN_GENERATED_KEYS);

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

	public static List<Highscore> fetchAll() {
		List<Highscore> result = new ArrayList<Highscore>();
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (Statement statement = conn.createStatement();
					ResultSet rs = statement.executeQuery("SELECT name, score FROM highscore WHERE deleted = 0");) {
				while (rs.next() == true) {
					result.add(new Highscore(rs.getInt("user_id"), rs.getInt("score")));
				}
			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}

		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
		return result;	}
	

	public Integer getUser_id() {
		return user_id;
	}

	public void setName(Integer user_id) {
		this.user_id = user_id;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

}
