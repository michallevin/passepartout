package yago.importers;

import java.io.FileNotFoundException;

import parsing.FactDictionary;
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

	public void handleRow(String id, String attr1, String attr2, String attr3,
			String line) {
		
		if (attr3.length() > 255) return;
		Fact fact = Fact.parseFact(id, attr1, attr2, attr3);
		if (fact != null) {
			Fact existing = FactDictionary.getInstance().getFactByYagoId(id);
			if (existing == null)  {
				FactDictionary.getInstance().addFact(fact);
			}
			else if (!existing.isUpdated()) {//make sure fact what not edited by user
				existing.updateFields(fact);
			}
		}
	}
}






