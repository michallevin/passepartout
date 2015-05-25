package yago;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.HashMap;

import db.JDBCConnection;

public class CountryDictionary {

	private static CountryDictionary instance = null;
	
	private HashMap<String, Integer> countryMap;
	
	protected CountryDictionary() {
		countryMap = new HashMap<String, Integer>();
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (Statement statement = conn.createStatement();
				ResultSet rs = statement.executeQuery("SELECT * FROM country");) {
				while (rs.next() == true) {
					countryMap.put(rs.getString("name"), rs.getInt("id"));
				}
			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}
			
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
	
	} 
	
	public static Integer getCountryId(String countryName) {
		if (instance == null) {
			instance = new CountryDictionary();
		}
		
		if (instance.countryMap.containsKey(countryName)) {
			return instance.countryMap.get(countryName);
		}
		else {
			return null;
		}
	}
}
