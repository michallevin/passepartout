package db.models;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;

import db.InputHelper;
import db.JDBCConnection;

public class Highscore {

	private String name;
	private Integer score;

	public Highscore(String name, Integer score) {
		this.name = name;
		this.score = score;
	}

	public void save() {
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (Statement statement = conn.createStatement()){
				
				statement.executeUpdate(String.format(""
						+ "INSERT INTO highscore(name, score) "
						+ "VALUES('%s', %s)", InputHelper.santize(name), score));

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

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

}
