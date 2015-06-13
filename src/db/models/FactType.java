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

public class FactType {

	private static final String SELECT_BY_ID = "SELECT * FROM fact WHERE deleted = 0 AND id = ?";
	private static final String SELECT_ALL = "SELECT * FROM fact_type LEFT JOIN fact_type_question_wording on fact_type_question_wording.fact_id = fact_type.id";
	private static final String SELECT_RANDOM = "SELECT * FROM fact_type "
			+ " JOIN fact_type_question_wording on fact_type_question_wording.fact_id = fact_type.id"
			+ " WHERE question_wording IS NOT NULL"
			+ " and is_literal = ?"
			+ " ORDER BY RAND() LIMIT 0,1";
	private static final String DELETE = "UPDATE fact_type SET deleted = 1, updated = 1 WHERE id = ?";
	private static final String UPDATE_BY_ID = "UPDATE fact_type SET name = ?, updated = 1 WHERE id = ?";
	private static final String INSERT = "INSERT INTO fact_type(name, is_literal) VALUES(?, ?)";
	private String typeName;
	private String questionWording;
	private int id;
	private boolean isLiteral;

	public FactType(int id, String typeName, boolean isLiteral) {
		this.setId(id);
		this.setTypeName(typeName);
		this.setLiteral(isLiteral);
	}

	public FactType(int id, String typeName, boolean isLiteral, String questionWording) {
		this.setId(id);
		this.setTypeName(typeName);
		this.setQuestionWording(questionWording);
		this.setLiteral(isLiteral);

	}

	public FactType(String typeName, boolean isLiteral) {
		this.setTypeName(typeName);
		this.setLiteral(isLiteral);
	}
	
	public int save() {
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (PreparedStatement statement = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)){
				statement.setString(1, getTypeName());
				statement.setBoolean(2, isLiteral());
				statement.executeUpdate();
	
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
	

	public void update() {
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (PreparedStatement statement = conn.prepareStatement(UPDATE_BY_ID)){
				statement.setString(1, typeName);
				statement.setInt(2, id);
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
	
	public static FactType getRandom(boolean isLiteral) {
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (PreparedStatement statement = conn.prepareStatement(SElECT_RANDOM)) {
				statement.setBoolean(1, isLiteral);
			
				try (ResultSet rs = statement.executeQuery()) {
	
				while (rs.next() == true) {
					return new FactType(rs.getInt("id"),
							rs.getString("name"),
							rs.getBoolean("is_literal"),
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


	public static List<FactType> fetchAll() {
		List<FactType> result = new ArrayList<FactType>();
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (PreparedStatement statement = conn.prepareStatement(SELECT_ALL)) {
				try (ResultSet rs = statement.executeQuery()) {
				while (rs.next() == true) {
					result.add(new FactType(rs.getInt("id"), rs.getString("name"), rs.getBoolean("is_literal"), rs.getString("question_wording")));
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

	public static FactType fetchById(Integer id) {
		// TODO Auto-generated method stub
			Connection conn;
			try {
				conn = JDBCConnection.getConnection();
				try (PreparedStatement statement = conn.prepareStatement(SELECT_BY_ID)) {
				statement.setInt(1, id);
				try (ResultSet rs = statement.executeQuery()) {
					while (rs.next() == true) {
						return new FactType(rs.getInt("id"), rs.getString("name"), rs.getBoolean("is_literal"), rs.getString("question_wording"));
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

	public boolean isLiteral() {
		return isLiteral;
	}

	public void setLiteral(boolean isLiteral) {
		this.isLiteral = isLiteral;
	}

	
}
