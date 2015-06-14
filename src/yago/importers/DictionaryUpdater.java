package yago.importers;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Collection;

import parsing.CountryDictionary;
import parsing.FactDictionary;
import parsing.YagoImport;
import db.JDBCConnection;
import db.models.Country;
import db.models.Fact;

public class DictionaryUpdater {

	public static void updateFactDictionary() {
		if (YagoImport.shouldCancel()) return;
		
		try {
			JDBCConnection.getConnection().setAutoCommit(false);

			PreparedStatement insertStatment = Fact.prepareInsertStatment();
			PreparedStatement updateStatment = Fact.prepareUpdateStatment();
			int updated = 0;
			int inserted = 0;
			int deleted = 0;
			
			//update labels and ranks (if we calculated them) for all facts
			int i = 0;
			for (String factData : FactDictionary.getInstance().getFactByDataMap().keySet()) {
				Collection<Fact> factList = FactDictionary.getInstance().getFactByDataMap().get(factData);
				for (Fact fact : factList) {
					if (YagoImport.shouldCancel()) break;

					
					if (fact.isNew()) {
						fact.saveFromImport(insertStatment);
						inserted += 1;
					}
					else if (!fact.isUpdated()) {
						if (fact.shouldDelete()) {
							fact.delete();
							deleted += 1;
						}
						else {
							fact.updateFromImport(updateStatment);
							updated += 1;
						}
					}
				
					if (i > 0 && i % 10000 == 0) {
						if (i % 1000000 == 0)
							System.out.println("");
						System.out.print(".");
						
						insertStatment.executeBatch();
						updateStatment.executeBatch();
						
						JDBCConnection.getConnection().commit();
					}
					++i;
				}
			}


			
			insertStatment.executeBatch();
			updateStatment.executeBatch();
			
			JDBCConnection.getConnection().commit();
			JDBCConnection.getConnection().setAutoCommit(true);
			
			System.out.println(String.format("inserted %d, updated %d, delted %d", inserted, updated, deleted));
			System.out.println("finished");
			
		} catch (SQLException | IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void updateCountryDictionary() {
		if (YagoImport.shouldCancel()) return;

		
		try {
			JDBCConnection.getConnection().setAutoCommit(false);
			

			for (String countryName : CountryDictionary.getInstance().getCountryMap().keySet()) {
				if (YagoImport.shouldCancel()) break;

				Country country = CountryDictionary.getInstance().getCountryMap().get(countryName);
				country.updateFromImport();
			}


			System.out.println("commiting");
			JDBCConnection.getConnection().commit();
			JDBCConnection.getConnection().setAutoCommit(true);
			System.out.println("finished");


		} catch (SQLException | IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
