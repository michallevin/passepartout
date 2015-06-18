package yago.importers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import parsing.YagoImport;
import db.JDBCConnection;

public abstract class BaseImporter {

	private BufferedReader reader;
	protected static final String ENG = "@eng";

	public abstract String getFileName();

	public BaseImporter() throws FileNotFoundException {
		reader = new BufferedReader(new FileReader(getFileName()));
	}
	
	
	String cleanInput(String input, String postfix) {
		return input.substring(1, input.length() - postfix.length() - 1);

	}	
	 
	String cleanInput(String input) {
		return cleanInput(input, ENG);
	}

	public void importData() throws IOException {
		if (YagoImport.shouldCancel()) {
			return;
		}
		
		System.out.println(String.format("reading file: '%s'", getFileName()));

		try {
			JDBCConnection.getConnection().setAutoCommit(false);

			int i = 0;
			String str;
			while ((str = reader.readLine()) != null) {
				if (YagoImport.shouldCancel()) {
					break;
				}
				
				String[] attributes = str.split("\\t");
				if (attributes.length >= 4) {
					handleRow(attributes[0], attributes[1], attributes[2], attributes[3], str);
					++i;
				}
				if (i > 0 && i % 50000 == 0) {
					System.out.print(".");
					if (i % 5000000 == 0)
						System.out.println("");
					JDBCConnection.getConnection().commit();
				}
			}
			System.out.println("");


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
		System.out.println("finished reading file");
		
	}
}
