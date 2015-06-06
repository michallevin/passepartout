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
	private boolean isLiteral;
	private int id;
	private int rank;
	


	public Fact(String yagoId, String subject, String infoType, String data, boolean isLiteral) {

		this.yagoId = yagoId;
		this.subject = subject;
		this.infoType = infoType;
		this.isLiteral = isLiteral;
		this.setData(data);
	}



	public Fact(int id, String yagoId, int countryId, String data, int factTypeId, int rank) {
			this.setId(id);
			this.setYagoId(yagoId);
			this.countryId=countryId;
			this.data=data;
			this.factTypeId=factTypeId;
			this.setRank(rank);

	}
	
	public Fact(String yagoId, int countryId, String data, int factTypeId, int rank) {
		this.setYagoId(yagoId);
		this.countryId=countryId;
		this.data=data;
		this.factTypeId=factTypeId;
		this.setRank(rank);

}

	public Fact(int countryId, int factTypeId, String data) {

		this.setCountryId(countryId);
		this.setFactTypeId(factTypeId);
		this.setData(data);
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
						+ "VALUES('%s', %d, '%s', %d)", getYagoId(), countryId, answer.replace("'", "''"), typeId));

			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}

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
					                                                    "JOIN user_fact_history ON fact.id = user_fact_history.fact_id " +
					                                                    "WHERE country_id = %d and type_id = %d and deleted = 0 and user_id = %d" +
					                                                    "GROUP BY fact.id " +
					                                                    "ORDER BY appearance_count, RAND() ASC" +
					                                                    "LIMIT 1",
					                                                    countryId, factTypeId, userId))) {
				while (rs.next() == true) {
					 fact = new Fact(rs.getInt("country_id"), rs.getInt("type_id"), rs.getString("data"));;
				}
				
			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}
			// Mark the returned fact as viewed by the user
			try (Statement statement = conn.createStatement();){
				statement.executeUpdate(String.format("INSERT INTO user_fact_history(user_id, fact_id) " +
                        "VALUES(%d, %d)", userId, fact.getId()), Statement.RETURN_GENERATED_KEYS);

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
					                                                    "LIMIT 0,3 ",
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
					result.add(new Fact(rs.getInt("id"), rs.getString("yagoId"), rs.getInt("countryId"), rs.getString("data"), rs.getInt("factTypeId"),rs.getInt("rank")));
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
					return new Fact(rs.getInt("id"), rs.getString("yagoId"), rs.getInt("countryId"), rs.getString("data"), rs.getInt("factTypeId"),rs.getInt("rank"));
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
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (Statement statement = conn.createStatement()){

				statement.executeUpdate(String.format(""
						+ "UPDATE fact SET CountryId = %d, data ='%s', factTypeId=%d, rank=%d, updated = 1 WHERE id = %d",
						countryId, data, factTypeId, rank, getId()));
			
			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public int getFactTypeId() {
		return factTypeId;
	}

	public void setFactTypeId(int factTypeId) {
		this.factTypeId = factTypeId;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getYagoId() {
		return yagoId;
	}

	public void setYagoId(String yagoId) {
		this.yagoId = yagoId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


		public static void updateFactByData(String factData, Integer rank) {
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (Statement statement = conn.createStatement()){

				statement.executeUpdate(String.format(""
						+ "UPDATE fact SET rank = %d WHERE data = '%s'", rank, InputHelper.santize(factData)));

				
			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}			
	}
	
	
	
}