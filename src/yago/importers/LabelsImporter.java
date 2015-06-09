package yago.importers;

import java.io.FileNotFoundException;

import yago.CountryDictionary;
import yago.FactDictionary;

public class LabelsImporter extends BaseImporter {

	private static final String FILENAME = "res//yagoLabels.tsv";
	private static final String LABEL = "rdfs:label";
	private static final String ENG = "@eng";

	public LabelsImporter() throws FileNotFoundException {
		super();
	}

	@Override
	public String getFileName() {
		return FILENAME;
	}

	@Override
	public void handleRow(String id, String attr1, String attr2, String attr3,
			String line) {
		if (attr2.equals(LABEL) && attr3.endsWith(ENG)) {
			String label = attr3.substring(1, attr3.length() - ENG.length() - 1);
			FactDictionary.getInstance().setLabel(attr1, label);
			CountryDictionary.getInstance().setLabel(attr1, label);
		}
	}
	
}






