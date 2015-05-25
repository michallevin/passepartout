package yago;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;

import db.JDBCConnection;

class YagoInfoboxAttribute {
	
	String subject;
	String infoType;
	String data;
	
	public YagoInfoboxAttribute(String subject, String infoType, String data) {
	
		this.subject=subject;
		this.infoType=infoType;
		this.data=data;
	}
	
	public void save() {
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (Statement statement = conn.createStatement()){
				Integer countryId= CountryDictionary.getCountryId(subject);
				String answer = data;
				
				if (countryId != null) {
					countryId= CountryDictionary.getCountryId(data);
					if (countryId == null) 
						return;
					answer = subject;
				}
				
				Integer typeId = TypeDictionary.getInstance().getId(infoType);
				
				
				int result = statement.executeUpdate(String.format(""
						+ "INSERT INTO question(country_id, answer, type_id) "
						+ "VALUES(%d, '%s', %d)", countryId, answer, typeId));
				
		} catch (SQLException e) {
			System.out.println("ERROR executeQuery - " + e.getMessage());
		}
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
	
		}
	
}
