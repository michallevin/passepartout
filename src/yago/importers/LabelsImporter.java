package yago.importers;

import java.io.FileNotFoundException;

import org.apache.commons.lang3.StringEscapeUtils;

import parsing.CountryDictionary;
import parsing.FactDictionary;

public class LabelsImporter extends BaseImporter {

	private static final String FILENAME = "res//yagoLabels.tsv";
	private static final String LABEL = "skos:prefLabel";

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
			String label = StringEscapeUtils.escapeJava(cleanInput(attr3));
			if (label.length() >= 255) return;
			FactDictionary.getInstance().setLabel(StringEscapeUtils.escapeJava(attr1), label);
			CountryDictionary.getInstance().setLabel(attr1, label);
		}
	}
	
}






