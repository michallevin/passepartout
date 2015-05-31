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

public class FactType {

	private String typeName;
	private String questionWording;
	private int id;

	public FactType(int id, String typeName) {
		this.setId(id);
		this.setTypeName(typeName);
	}

	public FactType(int id, String typeName, String questionWording) {
		this.setId(id);
		this.setTypeName(typeName);
		this.setQuestionWording(questionWording);
	}

	
	public int save() {
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (Statement statement = conn.createStatement()){
				statement.executeUpdate(String.format(""
						+ "INSERT INTO fact_type(name) "
						+ "VALUES('%s')", getTypeName()), Statement.RETURN_GENERATED_KEYS);

				try (ResultSet genKeys = statement.getGeneratedKeys()) {
					if (genKeys.next()) {
						int id = (int) genKeys.getLong(1);
						this.setId(id);
						return id;
					}
				}

			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
		return -1;

	}
	

	public static FactType getRandom() {
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (Statement statement = conn.createStatement();
					ResultSet rs = statement.executeQuery("SELECT * FROM fact_type WHERE question_wording IS NOT NULL ORDER BY RAND() LIMIT 0,1");) {
				while (rs.next() == true) {
					return new FactType(rs.getInt("id"),
							rs.getString("name"),
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


	public static List<FactType> fetchAll() {
		List<FactType> result = new ArrayList<FactType>();
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (Statement statement = conn.createStatement();
					ResultSet rs = statement.executeQuery("SELECT * FROM fact_type");) {
				while (rs.next() == true) {
					result.add(new FactType(rs.getInt("id"), rs.getString("name")));
				}
			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}

		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
		return result;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getQuestionWording() {
		return questionWording;
	}

	public void setQuestionWording(String questionWording) {
		this.questionWording = questionWording;
	}
	
}
