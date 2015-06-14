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

public class FactTypeQuestionWording {

	private static final String DELETE = "UPDATE fact_type_question_wording SET deleted = 1, updated = 1 WHERE id = ?";
	private static final String UPDATE_BY_ID = "UPDATE fact_type_question_wording SET question_wording = ?, fact_id = ?, updated = 1 WHERE id = ?";
	private static final String SELECT_ALL = "SELECT id, "
			+ "fact_id, question_wording "
			+ "FROM fact_type_question_wording WHERE deleted = 0";
	private static final String SELECT_BY_ID = "SELECT id, "
			+ "fact_id, question_wording "
			+ "FROM fact_type_question_wording WHERE deleted = 0 AND id = ?";
	private static final String INSERT = "INSERT INTO fact_type_question_wording (fact_id, question_wording) VALUES(?, ?)";
	private int id;
	private int factId;
	private String questionWording;

	public FactTypeQuestionWording(int factId, String questionWording) {
		this.setFactId(factId);
		this.setQuestionWording(questionWording);
	}

	public FactTypeQuestionWording(int id, int factId, String questionWording) {
		this.setId(id);
		this.setFactId(factId);
		this.setQuestionWording(questionWording);
	}

	public void save() {
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (PreparedStatement statement = conn.prepareStatement(INSERT,
					Statement.RETURN_GENERATED_KEYS)) {
				statement.setInt(1, factId);
				statement.setString(2, getQuestion_wording());
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

	public static FactTypeQuestionWording fetchById(int id) {
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (PreparedStatement statement = conn
					.prepareStatement(SELECT_BY_ID)) {
				statement.setInt(1, id);
				try (ResultSet rs = statement.executeQuery()) {
					while (rs.next() == true) {
						return new FactTypeQuestionWording(rs.getInt("id"),
								rs.getInt("fact_id"),
								rs.getString("question_wording"));
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

	public static List<FactTypeQuestionWording> fetchAll() {
		List<FactTypeQuestionWording> result = new ArrayList<FactTypeQuestionWording>();
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (PreparedStatement statement = conn
					.prepareStatement(SELECT_ALL)) {
				try (ResultSet rs = statement.executeQuery()) {
					while (rs.next() == true) {
						result.add(new FactTypeQuestionWording(rs.getInt("id"),
								rs.getInt("fact_id"), rs
										.getString("question_wording")));
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
			try (PreparedStatement statement = conn
					.prepareStatement(UPDATE_BY_ID)) {
				statement.setString(1, questionWording);
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

	public String getQuestion_wording() {
		return questionWording;
	}

	public void setQuestionWording(String questionWording) {
		this.questionWording = questionWording;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFactId() {
		return factId;
	}

	public void setFactId(int factId) {
		this.factId = factId;
	}

	public void replace() {

	}

}
