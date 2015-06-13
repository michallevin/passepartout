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
	private static boolean cancelImport = false;

	public static void startImport(String[] imports) {
		
		isImporting = true;
		cancelImport = false;
		List<String> importsList = Arrays.asList(imports);

		try {
	
			if (importsList.contains("countries")) {
				System.out.println("starting countries import");
				CountriesImporter countriesImporter = new CountriesImporter();
				countriesImporter.importData();
			}
			else {
				System.out.println("skipping countries import");
			}
			
			if (importsList.contains("attributes")) {
				System.out.println("starting attributes import");
				AttributesImporter attributesImporter = new AttributesImporter();
				attributesImporter.importData();
			}
			else {
				System.out.println("skipping attributes import");
			}
			
			if (importsList.contains("facts")) {
				System.out.println("starting facts import");

				FactsImporter factsImporter = new FactsImporter();
				factsImporter.importData();
			}
			else {
				System.out.println("skipping facts import");
			}
			
			if (importsList.contains("literalFacts")) {
				System.out.println("starting literal facts import");

				LiteralFactsImporter literalFactsImporter = new LiteralFactsImporter();
				literalFactsImporter.importData();
			}
			else {
				System.out.println("skipping literal facts import");
			}
			
			if (importsList.contains("links")) {
				System.out.println("starting ranking");
				LinksImporter linksImporter = new LinksImporter();
				linksImporter.importData();
			}			
			else {
				System.out.println("skipping labeling");
			}
			
			if (importsList.contains("labels")) {
				System.out.println("starting labeling");
				LabelsImporter labelsImporter = new LabelsImporter();
				labelsImporter.importData();
			}
			else {
				System.out.println("skipping labeling");
			}
			
			System.out.println("updating all facts");
			DictionaryUpdater.updateFactDictionary();
			System.out.println("updating all countries");
			DictionaryUpdater.updateCountryDictionary();
			
			
		
			System.out.println("import process finished");

			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		isImporting = false;

	}

	public static boolean isImporting() {
		return isImporting;
	}
	
	public static boolean shouldCancel() {
		return cancelImport;
	}

	public static void cancelImport() {
		cancelImport = true;
	}


}
