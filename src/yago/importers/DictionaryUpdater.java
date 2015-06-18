package yago.importers;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
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

			//insert/update/delete facts from dictionary
			int i = 0;
			for (Fact fact : FactDictionary.getInstance().getFactsByYagoIdMap().values()) {

				if (YagoImport.shouldCancel()) break;

				//rank and label were calculated in a temp vars, move final result to real vars (for dirty checking)
				fact.setRank(); 
				fact.setLabel();

				if (fact.isNew()) {
					fact.saveFromImport(insertStatment);
					inserted += 1;
				}
				else if (!fact.isUpdated()) { // make sure user hasn't updated this fact
					if (fact.shouldDelete()) {
						fact.delete();
						deleted += 1;
					}
					else {
						fact.setData(); //copy final data from temp
						fact.setFactTypeId(); //copy final type from temp

						fact.updateFromImport(updateStatment);
						updated += 1;
					}
				}

				if (i > 0 && i % 100 == 0) {
					if (i % 10000 == 0)
						System.out.println("");
					System.out.print(".");

					insertStatment.executeBatch();
					updateStatment.executeBatch();

					JDBCConnection.getConnection().commit();
				}
				++i;
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
