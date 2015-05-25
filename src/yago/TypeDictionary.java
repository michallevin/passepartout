package yago;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.HashMap;

import db.JDBCConnection;

public class TypeDictionary {

	private static TypeDictionary instance = null;

	private HashMap<String, Integer> typeMap;

	protected TypeDictionary() {
		typeMap = new HashMap<String, Integer>();
		Connection conn;
		try {
			conn = JDBCConnection.getConnection();
			try (Statement statement = conn.createStatement();
					ResultSet rs = statement.executeQuery("SELECT * FROM question_type");) {
				while (rs.next() == true) {
					typeMap.put(rs.getString("name"), rs.getInt("id"));
				}
			} catch (SQLException e) {
				System.out.println("ERROR executeQuery - " + e.getMessage());
			}

		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}

	} 

	public static TypeDictionary getInstance() {
		if (instance == null) {
			instance = new TypeDictionary();
		}
		return instance;
	}

	public Integer getId(String typeName) {

		if (typeMap.containsKey(typeName)) {
			return typeMap.get(typeName);
		}
		else {
			try {
				Connection conn = JDBCConnection.getConnection();
				try (Statement statement = conn.createStatement()){

					int result = statement.executeUpdate(String.format(""
							+ "INSERT INTO question_type(name) "
							+ "VALUES('%s')", typeName));

					try (ResultSet genKeys = statement.getGeneratedKeys()) {
						if (genKeys.next()) {
							typeMap.put(typeName, (int) genKeys.getLong(1));
						}
					}

				} catch (SQLException e) {
					System.out.println("ERROR executeQuery - " + e.getMessage());
				}
			} catch (IOException | ParseException e1) {
				e1.printStackTrace();
			}
			return typeMap.get(typeName);
		}

	}
}

