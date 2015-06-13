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

import org.apache.commons.lang3.StringEscapeUtils;

import yago.CountryDictionary;
import yago.TypeDictionary;
import db.JDBCConnection;

public class Fact {

	private static final String UPDATE_BY_ID_FROM_IMPORT = "UPDATE fact SET country_id = ?, data = ?, type_id = ?, rank = ?, label= ? WHERE id = ? and updated = 0";
	private static final String UPDATE_BY_ID = "UPDATE fact SET country_id = ?, data = ?, type_id = ?, rank = ?, label= ? updated = 1 WHERE id = ?";
	private static final String SELECT_BY_ID = "SELECT fact.id, fact.yago_id, fact.country_id " +
						   "fact.data, fact.type_id, fact.label, fact.rank, fact.updated " +
						   "FROM fact WHERE deleted = 0 AND id = ?";
	private static final String SELECT_ALL = "SELECT fact.id, fact.yago_id, fact.country_id " +
						   "fact.data, fact.type_id, fact.label, fact.rank, fact.updated " +
						   "FROM fact WHERE deleted = 0";
	private static final String SELECT_ALL_PAGED = "SELECT fact.id, fact.yago_id, fact.country_id " +
						   "fact.data, fact.type_id, fact.label, fact.rank, fact.updated " +
						   "FROM fact WHERE deleted = 0 LIMIT ?, ?";
	private static final String INSERT_QUERY = "INSERT INTO fact (yago_id, country_id, data, type_id, rank) VALUES (?, ?, ?, ?, ?)";
	private static final String DELETE_BY_ID = "UPDATE fact SET deleted = 1, updated = 1 WHERE id = ?";

	private static final String SELECT_WRONG_ANSWERS = "SELECT fact.id, fact.yago_id, fact.country_id " +
						   "fact.data, fact.type_id, fact.label, fact.rank, fact.updated " +
						   "FROM fact WHERE type_id = ? AND country_id <> ? "
			//+ "AND data NOT IN (SELECT data FROM fact WHERE type_id = ? AND country_id = ?) "
			+ "ORDER BY RAND() "
			+ "LIMIT 3";

	private static final String SElECT_RANDOM_LITERAL_FACT = "SELECT fact.id, fact.yago_id, fact.country_id " +
						   "fact.data, fact.type_id, fact.label, fact.rank, fact.updated " +
						   ", count(1) as appearance_count FROM fact " +
			+ "LEFT JOIN user_fact_history "
			+ "ON fact.id = user_fact_history.fact_id AND user_fact_history.user_id = ? AND user_fact_history.deleted = 0 "
			+ "WHERE country_id = ? AND type_id = ? AND fact.deleted = 0 "
			+ "GROUP BY fact.id "
			+ "ORDER BY appearance_count ASC, RAND() LIMIT 1";

	private static final String SElECT_FACT_BY_DIFFICULTY = "SELECT fact.id, fact.yago_id, fact.country_id " +
						   "fact.data, fact.type_id, fact.label, fact.rank, fact.updated " +
						   ", count(1) as appearance_count FROM fact " +
			+ "LEFT JOIN user_fact_history "
			+ "ON fact.id = user_fact_history.fact_id AND user_fact_history.user_id = ? AND user_fact_history.deleted = 0 "
			+ "WHERE country_id = ? AND type_id = ? AND fact.deleted = 0 "
			+ "GROUP BY fact.id "
			+ "ORDER BY appearance_count ASC, rank DESC";

	private String yagoId;
	private int countryId;
	private int factTypeId;
	private String data;
	private String label;
	private int id;
	private int rank;
	private boolean updated;

	private boolean dirty = false;

	public static Fact parseFact(String yagoId, String attr1, String type, String attr2) {
		Country country = CountryDictionary.getInstance().getCountry(attr1);
		String data = attr2;
		boolean isLiteral = true;

		if (country == null) {
			country = CountryDictionary.getInstance().getCountry(attr2);
			data = attr1;
			if (country == null)
				return null;
			isLiteral = false;
		}

		data = StringEscapeUtils.escapeJava(data);
		if (data.length() >= 255) return null;

		Integer typeId = TypeDictionary.getInstance().getId(type, isLiteral);
		return new Fact(-1, yagoId, country.getId(), data, typeId, null, 0, false);
	}


	public Fact(int id, String yagoId, int countryId, String data, int factTypeId, String label, int rank, boolean updated) {
		this.id = id;
		this.yagoId = yagoId;
		this.countryId=countryId;
		this.data=data;
		this.factTypeId=factTypeId;
		this.rank = rank;
		this.label = label;
		this.updated = updated;
	}

	public void save() {
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (PreparedStatement statement = conn.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)){
				statement.setString(1, yagoId);
				statement.setInt(2, countryId);
				statement.setString(3, data);
				statement.setInt(4, factTypeId);
				statement.setInt(5, rank);
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
		dirty = false;

	}

	public static Fact getFact(int countryId, int factTypeId, int userId, boolean isLiteral, int difficulty) {

		Connection conn;
		Fact fact = null;
		try {
			conn = JDBCConnection.getConnection();
			// Get the least viewed by user random fact
			String query = isLiteral ? SElECT_RANDOM_LITERAL_FACT : SElECT_FACT_BY_DIFFICULTY;
			try (PreparedStatement statement = conn.prepareStatement(query)) {
				statement.setInt(1, userId);
				statement.setInt(2, countryId);
				statement.setInt(3, factTypeId);

				try (ResultSet rs = statement.executeQuery()) {
					while (rs.next() == true) {
						fact = new Fact(
								rs.getInt("id"), rs.getString("yago_id"), rs.getInt("country_id"), rs.getString("data"),
								rs.getInt("type_id"), rs.getString("label"), rs.getInt("rank"), rs.getBoolean("updated")
								);
					}
				}
			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}

			if (fact == null) 
				return null;

			// Mark the returned fact as viewed by the user
			UserFactHistory userFactHistory = new UserFactHistory(userId, fact.getId());
			userFactHistory.save();

		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
		return fact;
	}

	public static List<Fact> getWrongAnswers(int factTypeId, int countryId) {

		List<Fact> result = new ArrayList<Fact>();
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (PreparedStatement statement = conn.prepareStatement(SELECT_WRONG_ANSWERS)) {
				statement.setInt(1, factTypeId);
				statement.setInt(2, countryId);
				//statement.setInt(3, factTypeId);
				//statement.setInt(4, countryId);
				try (ResultSet rs = statement.executeQuery()) {
					while (rs.next() == true) {
						result.add(new Fact(
								rs.getInt("id"), rs.getString("yago_id"), rs.getInt("country_id"), rs.getString("data"),
								rs.getInt("type_id"), rs.getString("label"), rs.getInt("rank"), rs.getBoolean("updated"))
								);
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

	public static List<Fact> fetchAll(int start, int end) {
		List<Fact> result = new ArrayList<Fact>();
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (PreparedStatement statement = conn.prepareStatement(SELECT_ALL_PAGED)) {
				statement.setInt(1, start);
				statement.setInt(2, end-start);
				try (ResultSet rs = statement.executeQuery()) {
					while (rs.next() == true) {
						result.add(new Fact(rs.getInt("id"),
								    rs.getString("yago_id"),
								    rs.getInt("country_id"),
								    rs.getString("data"),
								    rs.getInt("type_id"),
								    rs.getString("label"),
								    rs.getInt("rank"),
								    rs.getBoolean("updated")));
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

	public static List<Fact> fetchAll() {
		List<Fact> result = new ArrayList<Fact>();
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (PreparedStatement statement = conn.prepareStatement(SELECT_ALL)) {
				try (ResultSet rs = statement.executeQuery()) {
					while (rs.next() == true) {
						result.add(new Fact(rs.getInt("id"), rs.getString("yago_id"), rs.getInt("country_id"), rs.getString("data"), rs.getInt("type_id"),rs.getString("label"), rs.getInt("rank"), rs.getBoolean("updated")));
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

	public static Fact fetchById(int id) {
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (PreparedStatement statement = conn.prepareStatement(SELECT_BY_ID)) {
				statement.setInt(1, id);
				try (ResultSet rs = statement.executeQuery()) {
					while (rs.next() == true) {
						return new Fact(
								rs.getInt("id"),
								rs.getString("yago_id"),
								rs.getInt("country_id"),
								rs.getString("data"),
								rs.getInt("type_id"),
								rs.getString("label"),
								rs.getInt("rank"),
								rs.getBoolean("updated")
								);
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

	public String getReadableData(String countryName) {
		 
		if (label != null && label.length() > 0)
			return StringEscapeUtils.unescapeJava(label.replace(countryName, "").replaceAll("\\(.*?\\)", ""));
		else 
			return StringEscapeUtils.unescapeJava(
					data.replace("<", "").replace(">", "").replace("_", " ").replace("[", "").replace("]", "")
				.replace("}", "").replace("{", "").replace("^^xsd:integer", "").replace("\"", "")
				.replaceFirst("[a-zA-Z][a-zA-Z]/", "").replace(countryName, "").replaceAll("\\(.*?\\)", ""));
	}
	
	public void update() {
		if (!dirty) return;
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (PreparedStatement statement = conn.prepareStatement(UPDATE_BY_ID)){
				statement.setInt(1, countryId);
				statement.setString(2, data);
				statement.setInt(3, factTypeId);
				statement.setInt(4, rank);
				statement.setString(5, label);
				statement.setInt(6, id);
				statement.executeUpdate();
				
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
			try (PreparedStatement statement = conn.prepareStatement(UPDATE_BY_ID_FROM_IMPORT)){
				statement.setInt(1, countryId);
				statement.setString(2, data);
				statement.setInt(3, factTypeId);
				statement.setInt(4, rank);
				statement.setString(5, label);
				statement.setInt(6, id);
				statement.executeUpdate();
			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
		this.dirty = false;
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

	public void updateFields(Fact fact) {
		setData(fact.getData());
		setFactTypeId(fact.getFactTypeId());
		setYagoId(fact.getYagoId());
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		if (countryId == this.countryId) return;
		dirty = true;
		this.countryId = countryId;
	}

	public int getFactTypeId() {
		return factTypeId;
	}

	public void setFactTypeId(int factTypeId) {
		if (factTypeId == this.factTypeId) return;
		dirty = true;
		this.factTypeId = factTypeId;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		if (rank == this.rank) return;
		dirty = true;
		this.rank = rank;
		
	}

	public String getYagoId() {
		return yagoId;
	}

	public void setYagoId(String yagoId) {
		if (yagoId != null && this.yagoId != null && yagoId.equals(this.yagoId)) return;
		dirty = true;
		this.yagoId = yagoId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		if (id == this.id) return;
		dirty = true;
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		if (label != null && this.label != null && label.equals(this.label)) return;
		dirty = true;
		this.label = label;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		if (data != null && this.data != null && data.equals(this.data)) return;
		dirty = true;
		this.data = data;
	}


	public boolean isUpdated() {
		return updated;
	}


	public void setUpdated(boolean updated) {
		this.updated = updated;
	}

	public boolean isNew() {
		return id == -1;
	}

}
