package yago.importers;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Collection;

import yago.CountryDictionary;
import yago.FactDictionary;
import yago.YagoImport;
import db.JDBCConnection;
import db.models.Country;
import db.models.Fact;

public class DictionaryUpdater {

	public static void updateFactDictionary() {
		if (YagoImport.shouldCancel()) return;
		
		try {
			JDBCConnection.getConnection().setAutoCommit(false);

			System.out.println("start label update for facts");

			//update labels and ranks (if we calculated them) for all facts
			int i = 0;
			for (String factData : FactDictionary.getInstance().getFactMap().keySet()) {
				Collection<Fact> factList = FactDictionary.getInstance().getFactMap().get(factData).values();
				for (Fact fact : factList) {
					if (YagoImport.shouldCancel()) break;

					if (fact.isNew())
						fact.save();
					else
						fact.updateFromImport();
				
					if (i > 0 && i % 10000 == 0) {
						if (i % 1000000 == 0)
							System.out.println("");
						System.out.print(".");
						JDBCConnection.getConnection().commit();
					}
					++i;
				}
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

	public static void updateCountryDictionary() {
		if (YagoImport.shouldCancel()) return;

		
		try {
			JDBCConnection.getConnection().setAutoCommit(false);
			
			System.out.println("start label update for countries");


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
