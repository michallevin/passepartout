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
import yago.CountryDictionary;
import yago.TypeDictionary;

public class Fact {

	String subject;
	String infoType;

	int countryId;
	int factTypeId;
	private String data;



	public Fact(String subject, String infoType, String data) {

		this.subject = subject;
		this.infoType = infoType;
		this.setData(data);
	}

	public Fact(int countryId, int factTypeId, String data) {

		this.countryId = countryId;
		this.factTypeId = factTypeId;
		this.setData(data);
	}

	public void save() {
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (Statement statement = conn.createStatement()){
				Integer countryId = CountryDictionary.getInstance().getCountryId(subject);
				String answer = getData();

				if (countryId == null) {
					countryId = CountryDictionary.getInstance().getCountryId(getData());
					if (countryId == null) 
						return;
					answer = subject;
				}

				Integer typeId = TypeDictionary.getInstance().getId(infoType);


				statement.executeUpdate(String.format(""
						+ "INSERT INTO fact(country_id, data, type_id) "
						+ "VALUES(%d, '%s', %d)", countryId, answer.replace("'", "''"), typeId));

			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}

	}

	public static Fact getFact(int countryId, int factTypeId) {

		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (Statement statement = conn.createStatement();
					ResultSet rs = statement.executeQuery(String.format("SELECT * from fact where country_id = %d "
							+ "and type_id = %d LIMIT 1", countryId, factTypeId))) {
				while (rs.next() == true) {
					return new Fact(rs.getInt("country_id"), rs.getInt("type_id"), rs.getString("data"));
				}
			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}

		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	public static List<Fact> getWrongAnswers(int factTypeId, String correctAnswer) {

		List<Fact> result = new ArrayList<Fact>();
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (Statement statement = conn.createStatement();
					ResultSet rs = statement.executeQuery(String.format("SELECT * from fact where type_id = %d and "
							+ " data <> '%s' "
							+ " ORDER BY RAND() " 
							+ " LIMIT 0,3 ", factTypeId, correctAnswer))) {
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
}
