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
import yago.CountryDictionary;
import yago.TypeDictionary;

public class Fact {

	private String subject;
	String infoType;
	private String yagoId;
	

	private int countryId;
	private int factTypeId;
	private String data;
	private String label;
	private boolean isLiteral;
	private int id;
	private int rank;
	
	private boolean dirty = false;



	public Fact(String yagoId, String subject, String infoType, String data, boolean isLiteral) {

		this.yagoId = yagoId;
		this.subject = subject;
		this.infoType = infoType;
		this.isLiteral = isLiteral;
		this.data = data;
	}



	public Fact(int id, String yagoId, int countryId, String data, int factTypeId, int rank) {
		this.id = id;
		this.yagoId = yagoId;
		this.countryId=countryId;
		this.data=data;
		this.factTypeId=factTypeId;
		this.rank = rank;

	}

	public Fact(String yagoId, int countryId, String data, int factTypeId, int rank) {
		this.yagoId = yagoId;
		this.countryId=countryId;
		this.data=data;
		this.factTypeId=factTypeId;
		this.rank = rank;

	}

	public Fact(int countryId, int factTypeId, String data) {
		this.countryId=countryId;
		this.data=data;
		this.factTypeId=factTypeId;
	}

	public void saveFromImport() {
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (Statement statement = conn.createStatement()){
				Integer countryId = CountryDictionary.getInstance().getCountryId(getSubject());
				String answer = getData();

				if (countryId == null) {
					countryId = CountryDictionary.getInstance().getCountryId(getData());
					if (countryId == null)
						return;
					answer = getSubject();
				}

				Integer typeId = TypeDictionary.getInstance().getId(infoType, isLiteral);


				statement.executeUpdate(String.format(""
						+ "INSERT INTO fact(yago_id, country_id, data, type_id) "
						+ "VALUES('%s', %d, '%s', %d)", getYagoId(), countryId, InputHelper.santize(answer), typeId));

			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
		this.dirty = false;
	}


	public void save() {
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (Statement statement = conn.createStatement()){

				statement.executeUpdate(String.format(""
						+"INSERT INTO fact(country_id,data,type_id,rank) "
						+ "VALUES(%d, '%s', %d, %d)", countryId, data, factTypeId, rank), Statement.RETURN_GENERATED_KEYS);

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
		dirty = false;

	}



	public static Fact getFact(int countryId, int factTypeId, int userId) {

		Connection conn;
		Fact fact = null;
		try {
			conn = JDBCConnection.getConnection();
			// Get the least viewed by user random fact
			try (Statement statement = conn.createStatement();
					ResultSet rs = statement.executeQuery(String.format("SELECT *, count(1) as appearance_count " +
							"FROM fact " +
							"LEFT JOIN  user_fact_history ON fact.id = user_fact_history.fact_id AND " +
							"user_fact_history.user_id = %d AND user_fact_history.deleted = 0 " +
							"WHERE country_id = %d and type_id = %d and fact.deleted = 0 " +
							"GROUP BY fact.id " +
							"ORDER BY appearance_count ASC, RAND() " +
							"LIMIT 1",
							userId, countryId, factTypeId))) {
				while (rs.next() == true) {
					fact = new Fact(rs.getInt("id"), rs.getString("yago_id"), rs.getInt("country_id"), 
							rs.getString("data"), rs.getInt("type_id"),  rs.getInt("rank"));
				}

			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}
			
			if (fact == null) 
				return null;
			// Mark the returned fact as viewed by the user
			try (Statement statement = conn.createStatement();){
				statement.executeUpdate(String.format("INSERT INTO user_fact_history(user_id, fact_id) " +
						"VALUES(%d, %d)", userId, fact.getId()));

			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}


		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
		return fact;
	}

	public static List<Fact> getWrongAnswers(int factTypeId, String correctAnswer) {

		List<Fact> result = new ArrayList<Fact>();
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (Statement statement = conn.createStatement();
					ResultSet rs = statement.executeQuery(String.format("SELECT * " +
							"FROM fact " +
							"WHERE type_id = %d and data <> '%s' " +
							"ORDER BY RAND() " +
							"LIMIT 3 ",
							factTypeId, correctAnswer))) {
				while (rs.next() == true) {
					result.add(new Fact(rs.getInt("country_id"), rs.getInt("type_id"), rs.getString("data")));
				}
			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}

		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
		return result;


	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public static List<Fact> fetchAll() {
		List<Fact> result = new ArrayList<Fact>();
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (Statement statement = conn.createStatement();
					ResultSet rs = statement.executeQuery("SELECT * FROM fact WHERE deleted = 0");) {
				while (rs.next() == true) {
					result.add(new Fact(rs.getInt("id"), rs.getString("yago_id"), rs.getInt("country_id"), rs.getString("data"), rs.getInt("type_id"),rs.getInt("rank")));
				}
			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}

		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
		return result;
	}

	public static Fact fetchById(int id) {
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (Statement statement = conn.createStatement();
					ResultSet rs = statement.executeQuery(String.format("SELECT * FROM fact WHERE deleted = 0 AND id = %d", id));) {
				while (rs.next() == true) {
					return new Fact(rs.getInt("id"), rs.getString("yago_id"), rs.getInt("country_id"), rs.getString("data"), rs.getInt("type_id"),rs.getInt("rank"));
				}
			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}

		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
		return null;
	}


	public void update() {
		if (!dirty) return;
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (Statement statement = conn.createStatement()){

				statement.executeUpdate(String.format(""
						+ "UPDATE fact SET country_id = %d, data = '%s', type_id = %d, rank = %d, updated = 1 WHERE id = %d",
						countryId, InputHelper.santize(data), factTypeId, rank, getId()));

			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
		this.dirty = false;

	}
	
	

	public void updateFromImport() {
		if (!dirty) return;
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (Statement statement = conn.createStatement()){

				statement.executeUpdate(String.format(""
						+ "UPDATE fact SET country_id = %d, data = '%s', type_id = %d, rank = %d WHERE id = %d and updated = 0",
						countryId, InputHelper.santize(data), factTypeId, rank, getId()));

			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
		this.dirty = false;

	}


	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		dirty = true;
		this.countryId = countryId;
	}

	public int getFactTypeId() {
		return factTypeId;
	}

	public void setFactTypeId(int factTypeId) {
		dirty = true;
		this.factTypeId = factTypeId;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		dirty = true;
		this.rank = rank;
	}

	public String getYagoId() {
		return yagoId;
	}

	public void setYagoId(String yagoId) {
		dirty = true;
		this.yagoId = yagoId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		dirty = true;
		this.subject = subject;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		dirty = true;
		this.id = id;
	}



	public String getLabel() {
		return label;
	}



	public void setLabel(String label) {
		this.label = label;
	}


//	public static void updateFactByData(String factData, Integer rank) {
//		Connection conn;
//		try {
//			conn = JDBCConnection.getConnection();
//			try (Statement statement = conn.createStatement()){
//
//				statement.executeUpdate(String.format(""
//						+ "UPDATE fact SET rank = %d WHERE data = '%s'", rank, InputHelper.santize(factData)));
//
//
//			} catch (SQLException e) {
//				System.out.println("ERROR executeQuery - " + e.getMessage());
//			}
//		} catch (IOException | ParseException e1) {
//			e1.printStackTrace();
//		}			
//	}



}