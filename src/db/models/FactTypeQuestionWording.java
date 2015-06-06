package db.models;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import db.InputHelper;
import db.JDBCConnection;

public class FactTypeQuestionWording {

	private int id;
	private int question_id;
	private String question_wording;

	public FactTypeQuestionWording(int question_id, String question_wording) {
		this.setQuestion_id(question_id);
		this.setQuestion_wording(question_wording);
	}

	public FactTypeQuestionWording(int id, int question_id,
			String question_wording) {
		this.setId(id);
		this.setQuestion_id(question_id);
		this.setQuestion_wording(question_wording);
	}

	public void save() {
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (Statement statement = conn.createStatement()) {

				statement
						.executeUpdate(
								String.format(
										""
												+ "INSERT INTO fact_type_question_wording(question_id, question_wording) "
												+ "VALUES('%s', '%s')",
										question_id, getQuestion_wording()
												.replace("'", "''")),
								Statement.RETURN_GENERATED_KEYS);

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
			try (Statement statement = conn.createStatement();
					ResultSet rs = statement
							.executeQuery(String
									.format("SELECT * FROM fact_type_question_wording WHERE deleted = 0 AND id = %d",
											id));) {
				while (rs.next() == true) {
					return new FactTypeQuestionWording(rs.getInt("id"),
							rs.getInt("question_id"),
							rs.getString("question_wording"));
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
			try (Statement statement = conn.createStatement();
					ResultSet rs = statement
							.executeQuery("SELECT * FROM fact_type_question_wording WHERE deleted = 0");) {
				while (rs.next() == true) {
					result.add(new FactTypeQuestionWording(rs.getInt("id"), rs
							.getInt("question_id"), rs
							.getString("question_wording")));
				}
			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}

		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
		return result;
	}

	public String getQuestion_wording() {
		return question_wording;
	}

	public void setQuestion_wording(String question_wording) {
		this.question_wording = question_wording;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(int question_id) {
		this.question_id = question_id;
	}

	public void update() {
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (Statement statement = conn.createStatement()) {

				statement
						.executeUpdate(String
								.format("UPDATE fact_type_question_wording SET question_wording = '%s', question_id = %d updated = 1 WHERE id = %d",
										InputHelper.santize(question_wording),
										question_id, id));

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
			try (Statement statement = conn.createStatement()) {

				statement
						.executeUpdate(String
								.format("UPDATE fact_type_question_wording SET deleted = 1, updated = 1 WHERE id = %d",
										id));

			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
	}

}
