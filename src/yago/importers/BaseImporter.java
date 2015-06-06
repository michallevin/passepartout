package yago.importers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import db.JDBCConnection;

public abstract class BaseImporter {

	private BufferedReader reader;

	public abstract String getFileName();

	public BaseImporter() throws FileNotFoundException {
		reader = new BufferedReader(new FileReader(getFileName()));
	}

	public void importData() throws IOException {
		System.out.println(String.format("Starting import from file: '%s'", getFileName()));

		try {
			JDBCConnection.getConnection().setAutoCommit(false);

			int i = 0;
			String str;
			while ((str = reader.readLine()) != null) {
				//if (i == 10000) break;
				String[] attributes = str.split("\\t");
				if (attributes.length >= 4) {
					handleRow(attributes[0], attributes[1], attributes[2], attributes[3], str);
					++i;
				}
				if (i % 10000 == 0) {
					System.out.println("Commiting 10000 records");
					JDBCConnection.getConnection().commit();
				}
			}

			System.out.println("Commiting last records");

			this.finished();

			JDBCConnection.getConnection().commit();
			JDBCConnection.getConnection().setAutoCommit(true);
			
			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	public abstract void handleRow(String id, String attr1, String attr2,
			String attr3, String line);
	
	
	public void finished() {
		System.out.println("importing finished");
		
	}
}
