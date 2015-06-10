package yago;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import yago.importers.AttributesImporter;
import yago.importers.CountriesImporter;
import yago.importers.DictionaryUpdater;
import yago.importers.FactsImporter;
import yago.importers.LabelsImporter;
import yago.importers.LinksImporter;
import yago.importers.LiteralFactsImporter;

public class YagoImport {

	private static boolean isImporting = false;

	public static void startImport(String[] imports) {
		
		isImporting = true;
		List<String> importsList = Arrays.asList(imports);

		try {
	
			if (importsList.contains("countries")) {
				CountriesImporter countriesImporter = new CountriesImporter();
				countriesImporter.importData();
			}
			
			if (importsList.contains("attributes")) {
				AttributesImporter attributesImporter = new AttributesImporter();
				attributesImporter.importData();
			}
			
			if (importsList.contains("facts")) {
				FactsImporter factsImporter = new FactsImporter();
				factsImporter.importData();
			}
			
			if (importsList.contains("literalFacts")) {
				LiteralFactsImporter literalFactsImporter = new LiteralFactsImporter();
				literalFactsImporter.importData();
			}
			
			if (importsList.contains("links")) {
				LinksImporter linksImporter = new LinksImporter();
				linksImporter.importData();
			}
			
			if (importsList.contains("labels")) {
				LabelsImporter labelsImporter = new LabelsImporter();
				labelsImporter.importData();
			}
			

			if (importsList.contains("labels")) {
				DictionaryUpdater.updateFactDictionary();
				DictionaryUpdater.updateCountryDictionary();
			}
			else if (importsList.contains("links")) {
				DictionaryUpdater.updateFactDictionary();
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
