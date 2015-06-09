package yago.importers;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import yago.CountryDictionary;
import yago.FactDictionary;
import db.JDBCConnection;
import db.models.Country;
import db.models.Fact;

public class DictionaryUpdater {

	public static void updateFactDictionary() {
		try {
			JDBCConnection.getConnection().setAutoCommit(false);

			System.out.println("start label update for facts");

			//update labels and ranks (if we calculated them) for all facts
			for (String factData : FactDictionary.getInstance().getFactMap().keySet()) {
				List<Fact> factList = FactDictionary.getInstance().getFactMap().get(factData);
				for (Fact fact : factList)
					fact.updateFromImport();
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
		
		
		try {
			JDBCConnection.getConnection().setAutoCommit(false);
			
			System.out.println("start label update for countries");


			for (String countryName : CountryDictionary.getInstance().getCountryMap().keySet()) {
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
