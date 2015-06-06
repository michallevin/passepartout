package yago;

import java.io.FileNotFoundException;
import java.io.IOException;

import yago.importers.AttributesImporter;
import yago.importers.CountriesImporter;
import yago.importers.FactsImporter;
import yago.importers.LinksImporter;
import yago.importers.LiteralFactsImporter;

public class YagoImport {

	private static boolean isImporting = false;

	public static void startImport(Boolean links, Boolean countries, Boolean attributes, Boolean facts, Boolean literalFacts) {
		isImporting = true;
				
		try {
	
			if (countries) {
				CountriesImporter countriesImporter = new CountriesImporter();
				countriesImporter.importData();
			}
			
			if (attributes) {
				AttributesImporter attributesImporter = new AttributesImporter();
				attributesImporter.importData();
			}
			
			if (facts) {
				FactsImporter factsImporter = new FactsImporter();
				factsImporter.importData();
			}
			
			if (literalFacts) {
				LiteralFactsImporter literalFactsImporter = new LiteralFactsImporter();
				literalFactsImporter.importData();
			}
			
			if (links) {
				LinksImporter linksImporter = new LinksImporter();
				linksImporter.importData();
			}
			
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		isImporting = false;

	}

	public static boolean isImporting() {
		return isImporting;
	}


}
