package yago.importers;

import java.io.FileNotFoundException;

import db.models.Fact;

public class LiteralFactsImporter extends BaseImporter {

	private static final String FILENAME = "res//yagoLiteralFacts.tsv";

	public LiteralFactsImporter() throws FileNotFoundException {
		super();
	}

	@Override
	public String getFileName() {
		return FILENAME;
	}

	@Override
	public void handleRow(String id, String attr1, String attr2, String attr3,
			String line) {
		Fact yagoFact = new Fact(id, attr1, attr2, attr3, true);
		yagoFact.saveFromImport();
		
	}
}






